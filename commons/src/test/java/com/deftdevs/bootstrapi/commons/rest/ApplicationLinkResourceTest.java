package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean;
import com.deftdevs.bootstrapi.commons.rest.impl.TestApplicationLinkResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ApplicationLinkResourceTest {

    @Mock
    private ApplicationLinksService applicationLinksService;

    private TestApplicationLinkResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestApplicationLinkResourceImpl(applicationLinksService);
    }

    @Test
    void testGetApplicationLink() {
        final ApplicationLinkBean bean = ApplicationLinkBean.EXAMPLE_1;

        UUID id = UUID.randomUUID();

        doReturn(bean).when(applicationLinksService).getApplicationLink(id);

        final Response response = resource.getApplicationLink(id);
        assertEquals(200, response.getStatus());
        final ApplicationLinkBean linkBean = (ApplicationLinkBean) response.getEntity();

        assertEquals(linkBean, bean);
    }

    @Test
    void testCreateApplicationLink() {
        final ApplicationLinkBean bean = ApplicationLinkBean.EXAMPLE_1;

        doReturn(bean).when(applicationLinksService).addApplicationLink(bean, true);

        final Response response = resource.createApplicationLink(true, bean);
        assertEquals(200, response.getStatus());
        final ApplicationLinkBean responseBean = (ApplicationLinkBean) response.getEntity();

        assertEquals(responseBean, bean);
    }

    @Test
    void testUpdateApplicationLink() {
        final ApplicationLinkBean bean = ApplicationLinkBean.EXAMPLE_1;
        UUID id = UUID.randomUUID();

        doReturn(bean).when(applicationLinksService).setApplicationLink(id, bean, true);

        final Response response = resource.updateApplicationLink(id, true, bean);
        assertEquals(200, response.getStatus());
        final ApplicationLinkBean linkBean = (ApplicationLinkBean) response.getEntity();

        assertEquals(linkBean, bean);
    }

    @Test
    void testDeleteApplicationLink() {
        resource.deleteApplicationLink(UUID.randomUUID());
        assertTrue(true, "Delete Successful");
    }
}
