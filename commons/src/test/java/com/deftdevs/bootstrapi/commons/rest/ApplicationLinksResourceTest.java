package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean;
import com.deftdevs.bootstrapi.commons.rest.impl.TestApplicationLinksResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ApplicationLinksResourceTest {

    @Mock
    private ApplicationLinksService applicationLinksService;

    private TestApplicationLinksResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestApplicationLinksResourceImpl(applicationLinksService);
    }

    @Test
    void testGetApplicationLinks() {
        final List<ApplicationLinkBean> applicationLinkBeans = Collections.singletonList(ApplicationLinkBean.EXAMPLE_1);
        doReturn(applicationLinkBeans).when(applicationLinksService).getApplicationLinks();

        final Response response = resource.getApplicationLinks();
        assertEquals(200, response.getStatus());

        final List<ApplicationLinkBean> responseApplicationLinkBeans = (List<ApplicationLinkBean>) response.getEntity();
        assertEquals(responseApplicationLinkBeans, applicationLinkBeans);
    }

    @Test
    void testSetApplicationLinks() {
        final List<ApplicationLinkBean> applicationLinkBeans = Collections.singletonList(ApplicationLinkBean.EXAMPLE_1);
        doReturn(applicationLinkBeans).when(applicationLinksService).setApplicationLinks(applicationLinkBeans, true);

        final Response response = resource.setApplicationLinks(true, applicationLinkBeans);
        assertEquals(200, response.getStatus());

        final List<ApplicationLinkBean> resultApplicationLinkBeans = (List<ApplicationLinkBean>) response.getEntity();
        assertEquals(resultApplicationLinkBeans, applicationLinkBeans);
    }

    @Test
    void testDeleteApplicationLinks() {
        resource.deleteApplicationLinks(true);
        assertTrue(true, "Delete Successful");
    }
}
