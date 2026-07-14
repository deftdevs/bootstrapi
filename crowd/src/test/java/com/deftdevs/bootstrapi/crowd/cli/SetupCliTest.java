package com.deftdevs.bootstrapi.crowd.cli;

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
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SetupCliTest {

    private HttpServer server;
    private String baseUrl;
    private final Map<String, Map<String, String>> capturedForms = new LinkedHashMap<>();

    @BeforeEach
    void setup() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/crowd";

        System.setProperty("BOOTSTRAPI_SETUP_BASE_URL", baseUrl);
        System.setProperty("BOOTSTRAPI_SETUP_TIMEOUT_SECONDS", "5");
        System.setProperty("BOOTSTRAPI_SETUP_POLL_SECONDS", "1");
        System.setProperty("BOOTSTRAPI_SETUP_LICENSE", "LICENSE-KEY");
        System.setProperty("BOOTSTRAPI_SETUP_DATABASE_OPTION", "embedded");
        System.setProperty("BOOTSTRAPI_SETUP_TITLE", "Test Crowd");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_USERNAME", "admin");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_PASSWORD", "secret");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_EMAIL", "admin@example.com");
    }

    @AfterEach
    void teardown() {
        server.stop(0);
        System.getProperties().keySet().removeIf(key -> key.toString().startsWith("BOOTSTRAPI_SETUP_"));
    }

    @Test
    void testFullSetupRun() {
        stubWizard("/crowd/console/setup/setuplicense.action");
        server.start();

        final int exitCode = new SetupCli(new SetupHttpSession(baseUrl)).run();

        assertEquals(0, exitCode);
        assertEquals("LICENSE-KEY", capturedForms.get("/crowd/console/setup/setuplicense!update.action").get("key"));
        assertEquals("SERVER-ID", capturedForms.get("/crowd/console/setup/setuplicense!update.action").get("sid"));
        assertEquals("install.new", capturedForms.get("/crowd/console/setup/installtype!update.action").get("installOption"));
        assertEquals("db.embedded", capturedForms.get("/crowd/console/setup/setupdatabase!update.action").get("databaseOption"));
        assertEquals("Test Crowd", capturedForms.get("/crowd/console/setup/setupoptions!update.action").get("title"));
        assertEquals("Internal directory", capturedForms.get("/crowd/console/setup/directoryinternalsetup!update.action").get("name"));
        assertEquals("admin", capturedForms.get("/crowd/console/setup/defaultadministrator!update.action").get("name"));
        assertEquals("secret", capturedForms.get("/crowd/console/setup/defaultadministrator!update.action").get("passwordConfirm"));
        assertEquals("STEP-TOKEN", capturedForms.get("/crowd/console/setup/integration!update.action").get("atl_token"));
    }

    @Test
    void testResumesFromLaterStep() {
        stubWizard("/crowd/console/setup/defaultadministrator.action");
        server.start();

        final int exitCode = new SetupCli(new SetupHttpSession(baseUrl)).run();

        assertEquals(0, exitCode);
        assertFalse(capturedForms.containsKey("/crowd/console/setup/setuplicense!update.action"));
        assertFalse(capturedForms.containsKey("/crowd/console/setup/setupdatabase!update.action"));
        assertTrue(capturedForms.containsKey("/crowd/console/setup/defaultadministrator!update.action"));
        assertTrue(capturedForms.containsKey("/crowd/console/setup/integration!update.action"));
    }

    @Test
    void testAlreadySetUpDoesNothing() throws IOException {
        // a set up instance serves the login page without redirecting to the wizard
        server.createContext("/crowd", exchange -> respond(exchange, 200, "<html>login</html>"));
        server.start();

        final int exitCode = new SetupCli(new SetupHttpSession(baseUrl)).run();

        assertEquals(0, exitCode);
        assertTrue(capturedForms.isEmpty());
    }

    private void stubWizard(
            final String currentStep) {

        final java.util.concurrent.atomic.AtomicBoolean wizardComplete = new java.util.concurrent.atomic.AtomicBoolean();
        server.createContext("/crowd", exchange -> {
            final String path = exchange.getRequestURI().getPath();
            if (path.equals("/crowd/console/login.action") || path.equals("/crowd/console/setup/selectsetupstep.action")) {
                // once the wizard has completed, the login page no longer redirects to it
                if (wizardComplete.get()) {
                    respond(exchange, 200, "<html>login</html>");
                } else {
                    redirect(exchange, currentStep);
                }
            } else if (path.endsWith("!update.action")) {
                capturedForms.put(path, parseForm(exchange));
                if (path.endsWith("/integration!update.action")) {
                    wizardComplete.set(true);
                }
                respond(exchange, 200, "<html>ok</html>");
            } else if (path.startsWith("/crowd/console/setup/")) {
                respond(exchange, 200, "<input name=\"atl_token\" value=\"STEP-TOKEN\">"
                        + "<input name=\"sid\" value=\"SERVER-ID\">");
            } else {
                respond(exchange, 200, "<html>base</html>");
            }
        });
    }

    private static void redirect(
            final HttpExchange exchange,
            final String location) throws IOException {

        exchange.getResponseHeaders().add("Location", location);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
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

    private static Map<String, String> parseForm(
            final HttpExchange exchange) throws IOException {

        final String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        final Map<String, String> form = new LinkedHashMap<>();
        for (final String pair : body.split("&")) {
            final String[] parts = pair.split("=", 2);
            form.put(URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                    parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "");
        }
        return form;
    }
}
