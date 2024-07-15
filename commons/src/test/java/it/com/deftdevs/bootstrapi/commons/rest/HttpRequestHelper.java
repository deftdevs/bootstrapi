package it.com.deftdevs.bootstrapi.commons.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class HttpRequestHelper {

    private static final String BASE_URL = System.getProperty("baseurl");

    private static final String REST_PATH = "rest/bootstrapi/1";

    private String username = "admin";
    private String password = "admin";
    private String contentMediaType = MediaType.APPLICATION_JSON;
    private String acceptMediaType = MediaType.APPLICATION_JSON;
    private final String resourceName;

    private HttpRequestHelper(final String resourceName) {
        this.resourceName = resourceName;
    }

    public static HttpRequestHelper builder(final String resourceName) {
        return new HttpRequestHelper(resourceName);
    }

    public HttpRequestHelper username(final String username) {
        this.username = username;
        return this;
    }

    public HttpRequestHelper password(final String password) {
        this.password = password;
        return this;
    }

    public HttpRequestHelper acceptMediaType(final String acceptMediaType) {
        this.acceptMediaType = acceptMediaType;
        return this;
    }

    public HttpRequestHelper contentMediaType(final String contentMediaType) {
        this.contentMediaType = contentMediaType;
        return this;
    }

    /**
     * Creates a new REST client for test purposes. Note that the client must be recreated like this for auth tests.
     * (Atlassian uses cookies for authentication which may survive otherwise and lead to false test results regarding auth)
     *
     * @return the built resource
     */
    public HttpResponse<String> request(
            final String method,
            final Object payload) throws IOException, InterruptedException {

        final HttpClient client = HttpClient.newHttpClient();

        final HttpRequest.BodyPublisher bodyPublisher;

        if (payload == null) {
            bodyPublisher = HttpRequest.BodyPublishers.noBody();
        } else if (MediaType.TEXT_PLAIN.equals(contentMediaType)) {
            bodyPublisher = HttpRequest.BodyPublishers.ofString(String.valueOf(payload));
        } else if (MediaType.APPLICATION_JSON.equals(contentMediaType)) {
            final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            final String json = objectWriter.writeValueAsString(payload);
            bodyPublisher = HttpRequest.BodyPublishers.ofString(json);
        } else {
            throw new UnsupportedEncodingException(String.format("Media type %s is not supported", contentMediaType));
        }

        final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/%s/%s", BASE_URL, REST_PATH, resourceName)))
                .method(method, bodyPublisher);

        if (contentMediaType != null) {
            requestBuilder.header("Content-Type", contentMediaType);
        }

        if (acceptMediaType != null) {
            requestBuilder.header("Accept", acceptMediaType);
        }

        if (username != null && password != null) {
            requestBuilder.header("Authorization", "Basic " +
                    Base64.getEncoder().encodeToString((String.format("%s:%s", username, password)).getBytes()));
        }

        return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> request() throws IOException, InterruptedException {
        return request(HttpMethod.GET, null);
    }

}
