package com.deftdevs.bootstrapi.commons.cli;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A cookie-aware HTTP session for driving the product setup wizards from the
 * command line. Deliberately built on the JDK HTTP client only: the setup
 * CLIs run the plain plugin JAR outside the application, where none of the
 * provided dependencies are available.
 */
public class SetupHttpSession {

    // single wizard steps can take very long, e.g. Jira initialising the database
    private static final Duration REQUEST_TIMEOUT = Duration.ofMinutes(10);

    static {
        // the JDK HTTP client follows at most five redirects by default,
        // but e.g. the Confluence setup redirect chain is longer
        if (System.getProperty("jdk.httpclient.redirects.retrylimit") == null) {
            System.setProperty("jdk.httpclient.redirects.retrylimit", "10");
        }
    }

    private final URI baseUri;
    private final HttpClient followingClient;
    private final HttpClient plainClient;

    public SetupHttpSession(
            final String baseUrl) {

        this.baseUri = URI.create(baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl);
        final CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        this.followingClient = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.plainClient = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Polls the given path until it answers with a success status.
     */
    public void waitUntilAvailable(
            final String path,
            final Duration timeout,
            final Duration pollInterval) {

        final long deadline = System.nanoTime() + timeout.toNanos();
        while (true) {
            try {
                final HttpResponse<String> response = send(followingClient, getRequest(path));
                if (response.statusCode() < 300) {
                    return;
                }
            } catch (SetupException e) {
                // not reachable yet
            }

            if (System.nanoTime() > deadline) {
                throw new SetupException("Timed out waiting for " + baseUri + path + " to become available");
            }

            System.out.println("Waiting for " + baseUri + " to become available...");
            try {
                Thread.sleep(pollInterval.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SetupException("Interrupted while waiting for " + baseUri, e);
            }
        }
    }

    /**
     * Polls the given path until its body contains one of the expected
     * markers, e.g. the application state in the status endpoint of Jira and
     * Confluence, which answers long before the application is ready.
     */
    public String waitForAnyState(
            final String path,
            final Duration timeout,
            final Duration pollInterval,
            final String... states) {

        final long deadline = System.nanoTime() + timeout.toNanos();
        while (true) {
            try {
                final String body = get(path);
                for (final String state : states) {
                    if (body.contains(state)) {
                        return body;
                    }
                }
            } catch (SetupException e) {
                // not reachable yet
            }

            if (System.nanoTime() > deadline) {
                throw new SetupException("Timed out waiting for " + baseUri + path
                        + " to report one of " + String.join(", ", states));
            }

            System.out.println("Waiting for " + baseUri + " to become ready...");
            try {
                Thread.sleep(pollInterval.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SetupException("Interrupted while waiting for " + baseUri, e);
            }
        }
    }

    /**
     * GET following redirects; fails on a non-success status.
     */
    public String get(
            final String path) {

        final HttpResponse<String> response = send(followingClient, getRequest(path));
        requireSuccess("GET", path, response);
        return response.body();
    }

    /**
     * GET without following redirects, returning the redirect target if any.
     */
    public Optional<String> getLocation(
            final String path) {

        final HttpResponse<String> response = send(plainClient, getRequest(path));
        return response.headers().firstValue("location");
    }

    /**
     * POST a form following redirects; fails on a non-success status.
     */
    public String postForm(
            final String path,
            final Map<String, String> form) {

        final String body = form.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
        final HttpRequest request = HttpRequest.newBuilder(resolve(path))
                .timeout(REQUEST_TIMEOUT)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        final HttpResponse<String> response = send(followingClient, request);
        requireSuccess("POST", path, response);
        return response.body();
    }

    /**
     * Extracts the value of a named form input from an HTML page, e.g. the
     * {@code atl_token} XSRF token of a setup wizard step.
     */
    public static String parseFormInput(
            final String html,
            final String name) {

        // attributes may be spread over multiple lines
        final String stripped = html.replaceAll("\\s", "");
        final String marker = "name=\"" + name + "\"";

        final int nameIndex = stripped.indexOf(marker);
        if (nameIndex < 0) {
            throw new SetupException("No form input '" + name + "' found in the response page");
        }

        final int tagStart = Math.max(stripped.lastIndexOf('<', nameIndex), 0);
        final int tagEnd = stripped.indexOf('>', nameIndex);
        final String tag = stripped.substring(tagStart, tagEnd > 0 ? tagEnd : stripped.length());

        final Matcher matcher = Pattern.compile("value=\"([^\"]*)\"").matcher(tag);
        if (!matcher.find()) {
            throw new SetupException("No value found for form input '" + name + "'");
        }
        return matcher.group(1);
    }

    private HttpRequest getRequest(
            final String path) {

        return HttpRequest.newBuilder(resolve(path))
                .timeout(REQUEST_TIMEOUT)
                .GET()
                .build();
    }

    private URI resolve(
            final String path) {

        return URI.create(baseUri + path);
    }

    private HttpResponse<String> send(
            final HttpClient client,
            final HttpRequest request) {

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new SetupException("Request to " + request.uri() + " failed: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SetupException("Request to " + request.uri() + " was interrupted", e);
        }
    }

    private static void requireSuccess(
            final String method,
            final String path,
            final HttpResponse<String> response) {

        if (response.statusCode() >= 300) {
            final String body = response.body() != null ? response.body() : "";
            throw new SetupException(method + " " + path + " failed with status " + response.statusCode()
                    + (body.isBlank() ? "" : ": " + body.substring(0, Math.min(body.length(), 500))));
        }
    }

    private static String encode(
            final String value) {

        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
