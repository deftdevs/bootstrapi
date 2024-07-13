package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractPingResourceFuncTest {

    @Test
    void testGetPing() throws Exception {
        final HttpResponse<String> pingResponse = HttpRequestHelper.builder(BootstrAPI.PING).acceptMediaType(MediaType.TEXT_PLAIN).request();
        assertEquals(Response.Status.OK.getStatusCode(), pingResponse.statusCode());
    }
}
