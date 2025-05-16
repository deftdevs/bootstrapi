package com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.crowd.model.SessionConfigModel;
import com.deftdevs.bootstrapi.crowd.service.api.SessionConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class SessionConfigResourceTest {

    @Mock
    private SessionConfigService sessionConfigService;

    private SessionConfigResourceImpl sessionConfigResource;

    @BeforeEach
    public void setup() {
        sessionConfigResource = new SessionConfigResourceImpl(sessionConfigService);
    }

    @Test
    public void testGetSessionConfig() {
        final SessionConfigModel sessionConfigModel = SessionConfigModel.EXAMPLE_1;
        doReturn(sessionConfigModel).when(sessionConfigService).getSessionConfig();

        final Response response = sessionConfigResource.getSessionConfig();
        assertEquals(200, response.getStatus());

        final SessionConfigModel responseSessionConfigModel = (SessionConfigModel) response.getEntity();
        assertEquals(sessionConfigModel, responseSessionConfigModel);
    }

    @Test
    public void testSetSessionConfig() {
        final SessionConfigModel sessionConfigModel = SessionConfigModel.EXAMPLE_2;
        doReturn(sessionConfigModel).when(sessionConfigService).setSessionConfig(any(SessionConfigModel.class));

        final Response response = sessionConfigResource.setSessionConfig(sessionConfigModel);
        assertEquals(200, response.getStatus());

        final SessionConfigModel responseSessionConfigModel = (SessionConfigModel) response.getEntity();
        assertEquals(sessionConfigModel, responseSessionConfigModel);
    }

}
