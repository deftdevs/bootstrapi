package com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.crowd.model.SessionConfigBean;
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
        final SessionConfigBean sessionConfigBean = SessionConfigBean.EXAMPLE_1;
        doReturn(sessionConfigBean).when(sessionConfigService).getSessionConfig();

        final Response response = sessionConfigResource.getSessionConfig();
        assertEquals(200, response.getStatus());

        final SessionConfigBean responseSessionConfigBean = (SessionConfigBean) response.getEntity();
        assertEquals(sessionConfigBean, responseSessionConfigBean);
    }

    @Test
    public void testSetSessionConfig() {
        final SessionConfigBean sessionConfigBean = SessionConfigBean.EXAMPLE_2;
        doReturn(sessionConfigBean).when(sessionConfigService).setSessionConfig(any(SessionConfigBean.class));

        final Response response = sessionConfigResource.setSessionConfig(sessionConfigBean);
        assertEquals(200, response.getStatus());

        final SessionConfigBean responseSessionConfigBean = (SessionConfigBean) response.getEntity();
        assertEquals(sessionConfigBean, responseSessionConfigBean);
    }

}
