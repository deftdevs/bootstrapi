package com.deftdevs.bootstrapi.commons.plugins;

import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.PluginModel;
import com.deftdevs.bootstrapi.commons.model.PluginResolverModel;
import com.deftdevs.bootstrapi.commons.model.UpmModel;
import com.deftdevs.bootstrapi.commons.model.type.PluginResolverType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginArtifactDownloaderTest {

    private static final String PLUGIN_KEY = "com.example.plugin";
    private static final byte[] JAR_BYTES = "fake jar content".getBytes(StandardCharsets.UTF_8);

    private final PluginArtifactDownloader downloader = new PluginArtifactDownloader();
    private final List<String> requestedPaths = new ArrayList<>();

    private HttpServer server;
    private String baseUrl;

    @BeforeEach
    void setup() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/repository";
    }

    @AfterEach
    void teardown() {
        server.stop(0);
    }

    @Test
    void testMarketplaceDownloadRebasesTheBinaryLink() throws IOException {
        // the binary link is absolute to marketplace.atlassian.com, like in the real API
        stub("/repository/rest/2/addons/com.example.plugin/versions/name/1.2.3", 200,
                ("{\"_embedded\":{\"artifact\":{\"_links\":{\"binary\":"
                        + "{\"href\":\"https://marketplace.atlassian.com/download/apps/123/version/456\"}}}}}")
                        .getBytes(StandardCharsets.UTF_8));
        stub("/repository/download/apps/123/version/456", 200, JAR_BYTES);
        server.start();

        final Path artifact = downloader.download(upmModel(), PLUGIN_KEY, marketplacePlugin());

        assertArrayEquals(JAR_BYTES, Files.readAllBytes(artifact));
        assertTrue(requestedPaths.contains("/repository/download/apps/123/version/456"),
                "the binary must be fetched through the resolver's base URL, not the absolute link");

        PluginArtifactDownloader.deleteArtifact(artifact);
        assertFalse(Files.exists(artifact));
        assertFalse(Files.exists(artifact.getParent()), "the private staging directory must be deleted as well");
    }

    @Test
    void testMarketplaceUnknownVersionFailsWithBadRequest() {
        stub("/repository/rest/2/addons/com.example.plugin/versions/name/1.2.3", 404, new byte[0]);
        server.start();

        assertThrows(BadRequestException.class,
                () -> downloader.download(upmModel(), PLUGIN_KEY, marketplacePlugin()));
    }

    @Test
    void testMarketplaceVersionWithoutBinaryLinkFailsWithBadRequest() {
        stub("/repository/rest/2/addons/com.example.plugin/versions/name/1.2.3", 200,
                "{\"_embedded\":{}}".getBytes(StandardCharsets.UTF_8));
        server.start();

        assertThrows(BadRequestException.class,
                () -> downloader.download(upmModel(), PLUGIN_KEY, marketplacePlugin()));
    }

    @Test
    void testMarketplaceDownloadSendsBasicAuthWhenChallenged() throws IOException {
        // a marketplace resolver may point at a protected mirror, e.g. an
        // Artifactory generic remote, so it authenticates like any endpoint
        final List<String> authorizationHeaders = new ArrayList<>();
        server.createContext("/repository", exchange -> {
            final String authorization = exchange.getRequestHeaders().getFirst("Authorization");
            if (authorization == null) {
                exchange.getResponseHeaders().add("WWW-Authenticate", "Basic realm=\"repository\"");
                respond(exchange, 401, new byte[0]);
                return;
            }
            authorizationHeaders.add(authorization);
            if (exchange.getRequestURI().getPath().startsWith("/repository/rest/2/addons/")) {
                respond(exchange, 200, ("{\"_embedded\":{\"artifact\":{\"_links\":{\"binary\":"
                        + "{\"href\":\"https://marketplace.atlassian.com/download/apps/123/version/456\"}}}}}")
                        .getBytes(StandardCharsets.UTF_8));
            } else {
                respond(exchange, 200, JAR_BYTES);
            }
        });
        server.start();

        final UpmModel upmModel = upmModel();
        upmModel.getResolvers().get("central").setUsername("reader");
        upmModel.getResolvers().get("central").setPassword("secret");

        final Path artifact = downloader.download(upmModel, PLUGIN_KEY, marketplacePlugin());

        assertArrayEquals(JAR_BYTES, Files.readAllBytes(artifact));
        assertTrue(authorizationHeaders.stream().anyMatch(header -> header.startsWith("Basic ")));
        PluginArtifactDownloader.deleteArtifact(artifact);
    }

    @Test
    void testUndeclaredResolverFailsWithBadRequest() {
        server.start();

        final PluginModel pluginModel = marketplacePlugin();
        pluginModel.setResolver("unknown");
        assertThrows(BadRequestException.class,
                () -> downloader.download(upmModel(), PLUGIN_KEY, pluginModel));
    }

    @Test
    void testResolverWithoutTypeFailsWithBadRequest() {
        server.start();

        final UpmModel upmModel = UpmModel.builder()
                .resolvers(Map.of("central", PluginResolverModel.builder().baseUrl(baseUrl).build()))
                .build();
        assertThrows(BadRequestException.class,
                () -> downloader.download(upmModel, PLUGIN_KEY, marketplacePlugin()));
    }

    @Test
    void testMavenDownloadUsesTheRepositoryLayout() throws IOException {
        stub("/repository/com/example/example-plugin/1.2.3/example-plugin-1.2.3.jar", 200, JAR_BYTES);
        server.start();

        final Path artifact = downloader.download(upmModel(), PLUGIN_KEY, mavenPlugin());

        assertArrayEquals(JAR_BYTES, Files.readAllBytes(artifact));
        PluginArtifactDownloader.deleteArtifact(artifact);
    }

    @Test
    void testMavenDownloadSendsBasicAuthWhenChallenged() throws IOException {
        final List<String> authorizationHeaders = new ArrayList<>();
        server.createContext("/repository", exchange -> {
            final String authorization = exchange.getRequestHeaders().getFirst("Authorization");
            if (authorization == null) {
                exchange.getResponseHeaders().add("WWW-Authenticate", "Basic realm=\"repository\"");
                respond(exchange, 401, new byte[0]);
            } else {
                authorizationHeaders.add(authorization);
                respond(exchange, 200, JAR_BYTES);
            }
        });
        server.start();

        final UpmModel upmModel = upmModel();
        upmModel.getResolvers().get("corp").setUsername("deployer");
        upmModel.getResolvers().get("corp").setPassword("secret");

        final Path artifact = downloader.download(upmModel, PLUGIN_KEY, mavenPlugin());

        assertArrayEquals(JAR_BYTES, Files.readAllBytes(artifact));
        assertTrue(authorizationHeaders.stream().anyMatch(header -> header.startsWith("Basic ")));
        PluginArtifactDownloader.deleteArtifact(artifact);
    }

    @Test
    void testMavenWithoutCoordinatesFailsWithBadRequest() {
        server.start();

        final PluginModel pluginModel = mavenPlugin();
        pluginModel.setGroupId(null);
        assertThrows(BadRequestException.class,
                () -> downloader.download(upmModel(), PLUGIN_KEY, pluginModel));
    }

    @Test
    void testFailedDownloadFailsWithServerError() {
        stub("/repository/com/example/example-plugin/1.2.3/example-plugin-1.2.3.jar", 500, new byte[0]);
        server.start();

        assertThrows(InternalServerErrorException.class,
                () -> downloader.download(upmModel(), PLUGIN_KEY, mavenPlugin()));
    }

    private UpmModel upmModel() {
        return UpmModel.builder()
                .resolvers(Map.of(
                        "central", PluginResolverModel.builder()
                                .type(PluginResolverType.MARKETPLACE)
                                .baseUrl(baseUrl)
                                .build(),
                        "corp", PluginResolverModel.builder()
                                .type(PluginResolverType.MAVEN)
                                .baseUrl(baseUrl + "/")
                                .build()))
                .build();
    }

    private static PluginModel marketplacePlugin() {
        return PluginModel.builder()
                .version("1.2.3")
                .resolver("central")
                .build();
    }

    private static PluginModel mavenPlugin() {
        return PluginModel.builder()
                .version("1.2.3")
                .resolver("corp")
                .groupId("com.example")
                .artifactId("example-plugin")
                .build();
    }

    private void stub(
            final String path,
            final int status,
            final byte[] body) {

        server.createContext(path, exchange -> {
            respond(exchange, status, body);
        });
    }

    private void respond(
            final HttpExchange exchange,
            final int status,
            final byte[] body) throws IOException {

        requestedPaths.add(exchange.getRequestURI().getPath());
        if (body.length == 0) {
            exchange.sendResponseHeaders(status, -1);
            exchange.close();
            return;
        }
        exchange.sendResponseHeaders(status, body.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(body);
        }
    }
}
