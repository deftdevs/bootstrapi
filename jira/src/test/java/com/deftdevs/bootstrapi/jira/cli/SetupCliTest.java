package com.deftdevs.bootstrapi.jira.cli;

import com.deftdevs.bootstrapi.commons.cli.SetupHttpSession;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SetupCliTest {

    private HttpServer server;
    private String baseUrl;
    private final Map<String, Map<String, String>> capturedForms = new LinkedHashMap<>();
    private final List<String> capturedPaths = new ArrayList<>();

    @BeforeEach
    void setup() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/jira";

        System.setProperty("BOOTSTRAPI_SETUP_BASE_URL", baseUrl);
        System.setProperty("BOOTSTRAPI_SETUP_TIMEOUT_SECONDS", "5");
        System.setProperty("BOOTSTRAPI_SETUP_POLL_SECONDS", "1");
        System.setProperty("BOOTSTRAPI_SETUP_RETRY_SECONDS", "0");
        System.setProperty("BOOTSTRAPI_SETUP_TITLE", "Test Jira");
        System.setProperty("BOOTSTRAPI_SETUP_LICENSE", "LICENSE-KEY");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_FULL_NAME", "Admin Admin");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_EMAIL", "admin@example.com");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_USERNAME", "admin");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_PASSWORD", "secret");
    }

    @AfterEach
    void teardown() {
        server.stop(0);
        System.getProperties().keySet().removeIf(key -> key.toString().startsWith("BOOTSTRAPI_SETUP_"));
    }

    @Test
    void testFullSetupRunWithLicenseRetry() {
        final AtomicInteger licenseAttempts = new AtomicInteger();
        final java.util.concurrent.atomic.AtomicBoolean databaseInitialized = new java.util.concurrent.atomic.AtomicBoolean();
        final java.util.concurrent.atomic.AtomicBoolean wizardComplete = new java.util.concurrent.atomic.AtomicBoolean();

        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_HOSTNAME", "postgres");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_NAME", "jira");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_USERNAME", "jira");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_PASSWORD", "jira");

        server.createContext("/jira", exchange -> {
            final String path = exchange.getRequestURI().getPath();
            switch (path) {
                case "/jira/status":
                    respond(exchange, 200, wizardComplete.get()
                            ? "{\"state\":\"RUNNING\"}"
                            : "{\"state\":\"FIRST_RUN\"}");
                    break;
                case "/jira/":
                case "/jira":
                    // an empty database puts the wizard on the database step first
                    respond(exchange, 200, databaseInitialized.get()
                            ? tokenPage("TOKEN-0")
                            : "<form action=\"SetupDatabase.jspa\">" + tokenPage("DB-TOKEN") + "</form>");
                    break;
                case "/jira/secure/SetupDatabase.jspa":
                    capture(exchange, path);
                    databaseInitialized.set(true);
                    respond(exchange, 200, "<html>database ready</html>");
                    break;
                case "/jira/secure/SetupApplicationProperties.jspa":
                    capture(exchange, path);
                    respond(exchange, 200, tokenPage("TOKEN-1"));
                    break;
                case "/jira/secure/SetupLicense.jspa":
                    capture(exchange, path);
                    if (licenseAttempts.incrementAndGet() == 1) {
                        // a freshly started instance may answer with an error once
                        respond(exchange, 500, "not ready yet");
                    } else {
                        respond(exchange, 200, tokenPage("TOKEN-2"));
                    }
                    break;
                case "/jira/secure/SetupAdminAccount.jspa":
                    capture(exchange, path);
                    respond(exchange, 200, tokenPage("TOKEN-3"));
                    break;
                case "/jira/secure/SetupMailNotifications.jspa":
                    capture(exchange, path);
                    wizardComplete.set(true);
                    respond(exchange, 200, "<html>done</html>");
                    break;
                default:
                    respond(exchange, 404, "unknown " + path);
            }
        });
        server.start();

        final int exitCode = new SetupCli(new SetupHttpSession(baseUrl)).run();

        assertEquals(0, exitCode);
        assertEquals(2, licenseAttempts.get());
        assertEquals("DB-TOKEN", capturedForms.get("/jira/secure/SetupDatabase.jspa").get("atl_token"));
        assertEquals("postgres", capturedForms.get("/jira/secure/SetupDatabase.jspa").get("jdbcHostname"));
        assertEquals("postgres72", capturedForms.get("/jira/secure/SetupDatabase.jspa").get("databaseType"));
        assertEquals("TOKEN-0", capturedForms.get("/jira/secure/SetupApplicationProperties.jspa").get("atl_token"));
        assertEquals("Test Jira", capturedForms.get("/jira/secure/SetupApplicationProperties.jspa").get("title"));
        assertEquals("TOKEN-1", capturedForms.get("/jira/secure/SetupLicense.jspa").get("atl_token"));
        assertEquals("LICENSE-KEY", capturedForms.get("/jira/secure/SetupLicense.jspa").get("setupLicenseKey"));
        assertEquals("TOKEN-2", capturedForms.get("/jira/secure/SetupAdminAccount.jspa").get("atl_token"));
        assertEquals("Admin Admin", capturedForms.get("/jira/secure/SetupAdminAccount.jspa").get("fullname"));
        assertEquals("TOKEN-3", capturedForms.get("/jira/secure/SetupMailNotifications.jspa").get("atl_token"));
        assertEquals("true", capturedForms.get("/jira/secure/SetupMailNotifications.jspa").get("noemail"));
    }

    @Test
    void testAlreadySetUpDoesNothing() {
        server.createContext("/jira", exchange -> {
            if (exchange.getRequestURI().getPath().equals("/jira/status")) {
                respond(exchange, 200, "{\"state\":\"RUNNING\"}");
            } else {
                capturedPaths.add(exchange.getRequestURI().getPath());
                respond(exchange, 404, "unexpected");
            }
        });
        server.start();

        final int exitCode = new SetupCli(new SetupHttpSession(baseUrl)).run();

        assertEquals(0, exitCode);
        assertTrue(capturedForms.isEmpty());
        assertTrue(capturedPaths.isEmpty());
    }

    private static String tokenPage(
            final String token) {

        return "<input name=\"atl_token\" type=\"hidden\" value=\"" + token + "\">";
    }

    private void capture(
            final HttpExchange exchange,
            final String path) throws IOException {

        final String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        final Map<String, String> form = new LinkedHashMap<>();
        for (final String pair : body.split("&")) {
            final String[] parts = pair.split("=", 2);
            form.put(URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                    parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "");
        }
        capturedForms.put(path, form);
    }

    private static void respond(
            final HttpExchange exchange,
            final int status,
            final String body) throws IOException {

        final byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }
}
