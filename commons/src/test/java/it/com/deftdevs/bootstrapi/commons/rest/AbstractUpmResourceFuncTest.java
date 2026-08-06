package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.PluginModel;
import com.deftdevs.bootstrapi.commons.model.PluginResolverModel;
import com.deftdevs.bootstrapi.commons.model.UpmModel;
import com.deftdevs.bootstrapi.commons.model.type.PluginResolverType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.HttpMethod;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Drives the UPM resource of a running product against a stub repository
 * served by the test itself: a Maven layout and a Marketplace API answering
 * with absolute links, plus a minimal throwaway plugin generated on the
 * fly. This exercises the real download, link rebasing, installation and
 * enablement path through the product's plugin framework.
 */
public abstract class AbstractUpmResourceFuncTest {

    private static final String PLUGIN_KEY = "it.com.deftdevs.bootstrapi.it-plugin";
    private static final String MAVEN_GROUP_PATH = "it/com/deftdevs/bootstrapi";
    private static final String MAVEN_ARTIFACT_ID = "bootstrapi-it-plugin";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<String> requestedPaths = new CopyOnWriteArrayList<>();

    private HttpServer server;
    private String repositoryUrl;

    @BeforeEach
    void setup() throws IOException {
        // the products run on this host too, so the stub repository is
        // reachable for the in-product downloader via the loopback address
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        repositoryUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/repository";

        stubMavenArtifact("1.0.0");
        stubMarketplaceVersion("2.0.0");
        server.start();
    }

    @AfterEach
    void teardown() {
        server.stop(0);
    }

    @Test
    void testInstallUpgradeAndDisableLifecycle() throws Exception {
        // install via the maven resolver
        final HttpResponse<String> installResponse = putUpm(pluginModel("1.0.0", "maven-repo", true));
        assertEquals(200, installResponse.statusCode(), installResponse.body());

        final UpmModel installResult = objectMapper.readValue(installResponse.body(), UpmModel.class);
        assertEquals(200, installResult.getStatus().get(PLUGIN_KEY).getStatus());
        assertEquals("1.0.0", installResult.getPlugins().get(PLUGIN_KEY).getVersion());
        assertTrue(installResult.getPlugins().get(PLUGIN_KEY).getEnabled());

        // the installed plugin shows up in the plugins map
        final HttpResponse<String> getResponse = HttpRequestHelper.builder(BootstrAPI.UPM).request();
        assertEquals(200, getResponse.statusCode());
        final Map<String, PluginModel> installedPlugins = objectMapper.readValue(getResponse.body(),
                new TypeReference<Map<String, PluginModel>>() {});
        assertTrue(installedPlugins.containsKey(PLUGIN_KEY));
        assertEquals("1.0.0", installedPlugins.get(PLUGIN_KEY).getVersion());

        // re-applying the same version must not download or install again
        final long downloadsBefore = countArtifactDownloads();
        final HttpResponse<String> rerunResponse = putUpm(pluginModel("1.0.0", "maven-repo", true));
        assertEquals(200, rerunResponse.statusCode(), rerunResponse.body());
        assertEquals(downloadsBefore, countArtifactDownloads(),
                "an already installed version must not be downloaded again");

        // upgrade via the marketplace resolver, proving the API lookup and
        // the rebased binary download inside the product
        final HttpResponse<String> upgradeResponse = putUpm(pluginModel("2.0.0", "marketplace-repo", true));
        assertEquals(200, upgradeResponse.statusCode(), upgradeResponse.body());

        final UpmModel upgradeResult = objectMapper.readValue(upgradeResponse.body(), UpmModel.class);
        assertEquals("2.0.0", upgradeResult.getPlugins().get(PLUGIN_KEY).getVersion());
        assertTrue(requestedPaths.contains("/repository/rest/2/addons/" + PLUGIN_KEY + "/versions/name/2.0.0"),
                "the marketplace version lookup must go through the resolver's base URL");
        assertTrue(requestedPaths.contains("/repository/download/apps/42/version/4200"),
                "the binary download must be rebased onto the resolver's base URL");

        // disable the plugin declaratively
        final HttpResponse<String> disableResponse = putUpm(pluginModel("2.0.0", "marketplace-repo", false));
        assertEquals(200, disableResponse.statusCode(), disableResponse.body());

        final UpmModel disableResult = objectMapper.readValue(disableResponse.body(), UpmModel.class);
        assertFalse(disableResult.getPlugins().get(PLUGIN_KEY).getEnabled());
    }

    @Test
    void testSetUpmUnknownResolverReportsFailedPlugin() throws Exception {
        final UpmModel upmModel = UpmModel.builder()
                .plugins(Map.of(PLUGIN_KEY, PluginModel.builder()
                        .version("1.0.0")
                        .resolver("unknown")
                        .build()))
                .build();

        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.UPM)
                .request(HttpMethod.PUT, upmModel);
        assertEquals(400, response.statusCode());

        final UpmModel result = objectMapper.readValue(response.body(), UpmModel.class);
        assertEquals(400, result.getStatus().get(PLUGIN_KEY).getStatus());
    }

    @Test
    void testGetUpmUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.UPM)
                .username("wrong")
                .password("password")
                .request();
        assertEquals(401, response.statusCode());
    }

    @Test
    void testSetUpmUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.UPM)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, new UpmModel());
        assertEquals(401, response.statusCode());
    }

    @Test
    void testGetUpmUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.UPM)
                .username("user")
                .password("user")
                .request();
        assertEquals(403, response.statusCode());
    }

    @Test
    void testSetUpmUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.UPM)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, new UpmModel());
        assertEquals(403, response.statusCode());
    }

    private HttpResponse<String> putUpm(
            final PluginModel pluginModel) throws IOException, InterruptedException {

        final UpmModel upmModel = UpmModel.builder()
                .resolvers(Map.of(
                        "maven-repo", PluginResolverModel.builder()
                                .type(PluginResolverType.MAVEN)
                                .baseUrl(repositoryUrl)
                                .build(),
                        "marketplace-repo", PluginResolverModel.builder()
                                .type(PluginResolverType.MARKETPLACE)
                                .baseUrl(repositoryUrl)
                                .build()))
                .plugins(Map.of(PLUGIN_KEY, pluginModel))
                .build();

        return HttpRequestHelper.builder(BootstrAPI.UPM).request(HttpMethod.PUT, upmModel);
    }

    private static PluginModel pluginModel(
            final String version,
            final String resolver,
            final boolean enabled) {

        return PluginModel.builder()
                .version(version)
                .resolver(resolver)
                .groupId(MAVEN_GROUP_PATH.replace('/', '.'))
                .artifactId(MAVEN_ARTIFACT_ID)
                .enabled(enabled)
                .build();
    }

    private void stubMavenArtifact(
            final String version) throws IOException {

        final byte[] jar = pluginJar(version);
        stub("/repository/" + MAVEN_GROUP_PATH + "/" + MAVEN_ARTIFACT_ID + "/" + version
                + "/" + MAVEN_ARTIFACT_ID + "-" + version + ".jar", jar);
    }

    private void stubMarketplaceVersion(
            final String version) throws IOException {

        // the binary link is absolute like in the real Marketplace API, so a
        // download succeeds only when the product rebases it onto the base URL
        stub("/repository/rest/2/addons/" + PLUGIN_KEY + "/versions/name/" + version,
                ("{\"_embedded\":{\"artifact\":{\"_links\":{\"binary\":"
                        + "{\"href\":\"https://marketplace.atlassian.com/download/apps/42/version/4200\"}}}}}")
                        .getBytes(StandardCharsets.UTF_8));
        stub("/repository/download/apps/42/version/4200", pluginJar(version));
    }

    /**
     * Builds a minimal descriptor-only plugin on the fly; the products'
     * OSGi transformation turns it into an installable bundle.
     */
    private static byte[] pluginJar(
            final String version) throws IOException {

        final String descriptor = "<atlassian-plugin key=\"" + PLUGIN_KEY
                + "\" name=\"BootstrAPI Func Test Plugin\" plugins-version=\"2\">\n"
                + "    <plugin-info>\n"
                + "        <description>Throwaway plugin installed by the UPM func test</description>\n"
                + "        <version>" + version + "</version>\n"
                + "        <vendor name=\"Deft Devs\" url=\"https://deftdevs.com\"/>\n"
                + "    </plugin-info>\n"
                + "</atlassian-plugin>\n";

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (JarOutputStream jar = new JarOutputStream(out)) {
            jar.putNextEntry(new JarEntry("atlassian-plugin.xml"));
            jar.write(descriptor.getBytes(StandardCharsets.UTF_8));
            jar.closeEntry();
        }
        return out.toByteArray();
    }

    private void stub(
            final String path,
            final byte[] body) {

        server.createContext(path, exchange -> {
            requestedPaths.add(exchange.getRequestURI().getPath());
            respond(exchange, body);
        });
    }

    private long countArtifactDownloads() {
        return requestedPaths.stream()
                .filter(path -> path.endsWith(".jar") || path.startsWith("/repository/download/"))
                .count();
    }

    private static void respond(
            final HttpExchange exchange,
            final byte[] body) throws IOException {

        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(body);
        }
    }
}
