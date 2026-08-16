package com.deftdevs.bootstrapi.commons.plugins;

import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.PluginModel;
import com.deftdevs.bootstrapi.commons.model.PluginProxyModel;
import com.deftdevs.bootstrapi.commons.model.PluginResolverModel;
import com.deftdevs.bootstrapi.commons.model.UpmModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

/**
 * Resolves and downloads plugin artifacts through the named resolver a
 * plugin references, according to the resolver's type:
 * <ul>
 * <li>{@code marketplace}: the version is looked up through the Atlassian
 * Marketplace REST API from the plugin key and version name, and the binary
 * link of the embedded artifact is downloaded.</li>
 * <li>{@code maven}: the artifact location is derived from the Maven
 * coordinates and the standard repository layout, without any API call.</li>
 * </ul>
 * All links returned by the Marketplace API are re-resolved against the
 * resolver's base URL, so a proxying repository (e.g. an Artifactory
 * generic remote) serves the API lookup and the binary download alike
 * instead of being bypassed by the absolute URLs in the API response.
 */
public class PluginArtifactDownloader {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration REQUEST_TIMEOUT = Duration.ofMinutes(5);

    private static final String STAGING_DIR_PREFIX = "bootstrapi-plugins-";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Downloads the artifact for the given plugin to a temporary file. The
     * caller is responsible for deleting the file.
     */
    public Path download(
            final UpmModel upmModel,
            final String pluginKey,
            final PluginModel pluginModel) {

        final PluginResolverModel resolverModel = requireResolver(upmModel, pluginModel.getResolver());
        if (resolverModel.getType() == null) {
            throw new BadRequestException("Resolver '" + pluginModel.getResolver()
                    + "' must declare a type ('marketplace' or 'maven')");
        }

        switch (resolverModel.getType()) {
            case MARKETPLACE:
                return downloadFromMarketplace(resolverModel, pluginKey, pluginModel);
            case MAVEN:
                return downloadFromMavenRepository(resolverModel, pluginKey, pluginModel);
            default:
                throw new BadRequestException("Unsupported plugin resolver type: " + resolverModel.getType());
        }
    }

    private Path downloadFromMarketplace(
            final PluginResolverModel resolverModel,
            final String pluginKey,
            final PluginModel pluginModel) {

        final HttpClient client = newHttpClient(resolverModel);

        final URI versionUri = URI.create(baseUrl(resolverModel)
                + "/rest/2/addons/" + encode(pluginKey)
                + "/versions/name/" + encode(pluginModel.getVersion()));
        final HttpResponse<String> versionResponse = send(client, versionUri, HttpResponse.BodyHandlers.ofString());
        if (versionResponse.statusCode() == 404) {
            throw new BadRequestException("Plugin '" + pluginKey + "' version '"
                    + pluginModel.getVersion() + "' was not found in the marketplace");
        }
        requireSuccess(versionUri, versionResponse.statusCode());

        final JsonNode versionNode;
        try {
            versionNode = OBJECT_MAPPER.readTree(versionResponse.body());
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to parse the marketplace response for " + versionUri);
        }

        final String binaryHref = versionNode.path("_embedded").path("artifact")
                .path("_links").path("binary").path("href").asText();
        if (binaryHref.isEmpty()) {
            throw new BadRequestException("The marketplace version of plugin '" + pluginKey
                    + "' does not embed an artifact binary link");
        }

        // the API returns absolute links; keeping only their path and query
        // routes the binary download through the resolver's base URL as well
        final URI binaryLink = URI.create(binaryHref);
        final URI binaryUri = URI.create(baseUrl(resolverModel) + binaryLink.getRawPath()
                + (binaryLink.getRawQuery() != null ? "?" + binaryLink.getRawQuery() : ""));

        return downloadArtifact(client, binaryUri);
    }

    private Path downloadFromMavenRepository(
            final PluginResolverModel resolverModel,
            final String pluginKey,
            final PluginModel pluginModel) {

        if (isBlank(pluginModel.getGroupId()) || isBlank(pluginModel.getArtifactId())) {
            throw new BadRequestException("Plugin '" + pluginKey
                    + "' uses a maven resolver and must provide a groupId and an artifactId");
        }

        final HttpClient client = newHttpClient(resolverModel);
        final URI artifactUri = URI.create(baseUrl(resolverModel)
                + "/" + pluginModel.getGroupId().replace('.', '/')
                + "/" + pluginModel.getArtifactId()
                + "/" + pluginModel.getVersion()
                + "/" + pluginModel.getArtifactId() + "-" + pluginModel.getVersion() + ".jar");

        return downloadArtifact(client, artifactUri);
    }

    private Path downloadArtifact(
            final HttpClient client,
            final URI artifactUri) {

        final Path artifactFile;
        try {
            // the artifact is staged in a fresh private directory (created
            // owner-only where the filesystem supports it), so no other local
            // user can read or replace it between download and installation
            artifactFile = Files.createTempDirectory(STAGING_DIR_PREFIX).resolve("plugin.jar");
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to create a staging directory for the plugin artifact");
        }

        try {
            final HttpResponse<Path> response = send(client, artifactUri, HttpResponse.BodyHandlers.ofFile(artifactFile));
            requireSuccess(artifactUri, response.statusCode());
            return artifactFile;
        } catch (RuntimeException e) {
            deleteArtifact(artifactFile);
            throw e;
        }
    }

    /**
     * Deletes a downloaded artifact together with its staging directory.
     */
    public static void deleteArtifact(
            final Path artifactFile) {

        deleteQuietly(artifactFile);
        final Path stagingDirectory = artifactFile.getParent();
        if (stagingDirectory != null
                && stagingDirectory.getFileName().toString().startsWith(STAGING_DIR_PREFIX)) {
            deleteQuietly(stagingDirectory);
        }
    }

    private static PluginResolverModel requireResolver(
            final UpmModel upmModel,
            final String resolverKey) {

        if (isBlank(resolverKey)) {
            throw new BadRequestException("A plugin must name its resolver explicitly");
        }

        final PluginResolverModel resolverModel = upmModel.getResolvers() != null
                ? upmModel.getResolvers().get(resolverKey)
                : null;
        if (resolverModel == null) {
            throw new BadRequestException("Resolver '" + resolverKey + "' is not declared in the resolvers map");
        }
        if (isBlank(resolverModel.getBaseUrl())) {
            throw new BadRequestException("Resolver '" + resolverKey + "' does not configure a base URL");
        }
        return resolverModel;
    }

    private static HttpClient newHttpClient(
            final PluginResolverModel resolverModel) {

        final HttpClient.Builder builder = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(CONNECT_TIMEOUT);

        final PluginProxyModel proxyModel = resolverModel.getProxy();
        if (proxyModel != null) {
            if (isBlank(proxyModel.getHost()) || proxyModel.getPort() == null) {
                throw new BadRequestException("A resolver proxy must provide a host and a port");
            }
            builder.proxy(ProxySelector.of(new InetSocketAddress(proxyModel.getHost(), proxyModel.getPort())));
        }

        builder.authenticator(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                if (getRequestorType() == RequestorType.PROXY && proxyModel != null
                        && !isBlank(proxyModel.getUsername())) {
                    return new PasswordAuthentication(proxyModel.getUsername(),
                            proxyModel.getPassword() != null ? proxyModel.getPassword().toCharArray() : new char[0]);
                }
                if (getRequestorType() == RequestorType.SERVER && !isBlank(resolverModel.getUsername())) {
                    return new PasswordAuthentication(resolverModel.getUsername(),
                            resolverModel.getPassword() != null ? resolverModel.getPassword().toCharArray() : new char[0]);
                }
                return null;
            }
        });

        return builder.build();
    }

    private <T> HttpResponse<T> send(
            final HttpClient client,
            final URI uri,
            final HttpResponse.BodyHandler<T> bodyHandler) {

        final HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(REQUEST_TIMEOUT)
                .header("Accept", "application/json, */*")
                .GET()
                .build();

        try {
            return client.send(request, bodyHandler);
        } catch (IOException e) {
            throw new InternalServerErrorException("Request to " + uri + " failed: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InternalServerErrorException("Request to " + uri + " was interrupted");
        }
    }

    private static void requireSuccess(
            final URI uri,
            final int statusCode) {

        if (statusCode >= 300) {
            throw new InternalServerErrorException("Request to " + uri + " failed with status " + statusCode);
        }
    }

    private static String baseUrl(
            final PluginResolverModel resolverModel) {

        final String baseUrl = resolverModel.getBaseUrl();
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    private static String encode(
            final String value) {

        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static boolean isBlank(
            final String value) {

        return value == null || value.isBlank();
    }

    private static void deleteQuietly(
            final Path file) {

        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            // the temporary file is left behind, nothing more to do
        }
    }
}
