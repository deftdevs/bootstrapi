package it.de.aservo.atlassian.crowd.confapi.rest;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.client.handlers.BasicAuthSecurityHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PingResourceFuncTest {

    @Test
    public void testPing() {
        final BasicAuthSecurityHandler basicAuthHandler = new BasicAuthSecurityHandler();
        basicAuthHandler.setUserName("admin");
        basicAuthHandler.setPassword("admin");

        final ClientConfig config = new ClientConfig();
        config.handlers(basicAuthHandler);

        final RestClient client = new RestClient(config);

        final String baseUrl = System.getProperty("baseurl");
        final String resourceUrl = baseUrl + "/rest/confapi/1/ping";
        final Resource resource = client.resource(resourceUrl);

        assertEquals(200, resource.get().getStatusCode());
    }

}
