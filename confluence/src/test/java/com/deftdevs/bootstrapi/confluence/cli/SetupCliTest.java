package com.deftdevs.bootstrapi.confluence.cli;

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
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SetupCliTest {

    private HttpServer server;
    private String baseUrl;
    private final Map<String, Map<String, String>> capturedForms = new LinkedHashMap<>();
    private final AtomicBoolean finishCalled = new AtomicBoolean();

    @BeforeEach
    void setup() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/confluence";

        System.setProperty("BOOTSTRAPI_SETUP_BASE_URL", baseUrl);
        System.setProperty("BOOTSTRAPI_SETUP_TIMEOUT_SECONDS", "5");
        System.setProperty("BOOTSTRAPI_SETUP_POLL_SECONDS", "1");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_USERNAME", "admin");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_PASSWORD", "secret");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_FULL_NAME", "Admin Admin");
        System.setProperty("BOOTSTRAPI_SETUP_ADMIN_EMAIL", "admin@example.com");
    }

    @AfterEach
    void teardown() {
        server.stop(0);
        System.getProperties().keySet().removeIf(key -> key.toString().startsWith("BOOTSTRAPI_SETUP_"));
    }

    @Test
    void testFullSetupRun() {
        stubWizard("/setup/setupcluster-start.action");
        server.start();

        final int exitCode = new SetupCli(new SetupHttpSession(baseUrl)).run();

        assertEquals(0, exitCode);
        assertEquals("skipCluster", capturedForms.get("/confluence/setup/setupcluster.action").get("newCluster"));
        assertEquals("Empty Site", capturedForms.get("/confluence/setup/setupdata.action").get("dbchoiceSelect"));
        assertEquals("internal", capturedForms.get("/confluence/setup/setupusermanagementchoice.action").get("userManagementChoice"));
        assertEquals("admin", capturedForms.get("/confluence/setup/setupadministrator.action").get("username"));
        assertEquals("secret", capturedForms.get("/confluence/setup/setupadministrator.action").get("confirm"));
        assertEquals("BASE-TOKEN", capturedForms.get("/confluence/setup/setupadministrator.action").get("atl_token"));
        assertTrue(finishCalled.get());
    }

    @Test
    void testResumesFromAdministratorStep() {
        stubWizard("/setup/setupadministrator-start.action");
        server.start();

        final int exitCode = new SetupCli(new SetupHttpSession(baseUrl)).run();

        assertEquals(0, exitCode);
        assertFalse(capturedForms.containsKey("/confluence/setup/setupdata.action"));
        assertFalse(capturedForms.containsKey("/confluence/setup/setupusermanagementchoice.action"));
        assertTrue(capturedForms.containsKey("/confluence/setup/setupadministrator.action"));
        assertTrue(finishCalled.get());
    }

    @Test
    void testAlreadySetUpDoesNothing() {
        server.createContext("/confluence", exchange -> {
            if (exchange.getRequestURI().getPath().equals("/confluence/status")) {
                respond(exchange, 200, "{\"state\":\"RUNNING\"}");
            } else {
                respond(exchange, 404, "unexpected");
            }
        });
        server.start();

        final int exitCode = new SetupCli(new SetupHttpSession(baseUrl)).run();

        assertEquals(0, exitCode);
        assertTrue(capturedForms.isEmpty());
        assertFalse(finishCalled.get());
    }

    private void stubWizard(
            final String currentStep) {

        server.createContext("/confluence", exchange -> {
            final String path = exchange.getRequestURI().getPath();
            if (path.equals("/confluence/status")) {
                // finishing the setup flips the application state to RUNNING
                respond(exchange, 200, finishCalled.get()
                        ? "{\"state\":\"RUNNING\"}"
                        : "{\"state\":\"FIRST_RUN\"}");
            } else if (path.equals("/confluence/bootstrap/selectsetupstep.action")) {
                exchange.getResponseHeaders().add("Location", "/confluence" + currentStep);
                exchange.sendResponseHeaders(302, -1);
                exchange.close();
            } else if (path.equals("/confluence/setup/finishsetup.action")) {
                finishCalled.set(true);
                respond(exchange, 200, "<html>done</html>");
            } else if (path.startsWith("/confluence/setup/")) {
                capture(exchange, path);
                respond(exchange, 200, "<html>ok</html>");
            } else {
                // the base page carries the session cookie and the XSRF token
                respond(exchange, 200, "<input type=\"hidden\" name=\"atl_token\" value=\"BASE-TOKEN\">");
            }
        });
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
