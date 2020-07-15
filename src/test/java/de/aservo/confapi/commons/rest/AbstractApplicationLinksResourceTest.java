package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.ApplicationLinkBean;
import de.aservo.confapi.commons.model.ApplicationLinksBean;
import de.aservo.confapi.commons.service.api.ApplicationLinksService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class AbstractApplicationLinksResourceTest {

    @Mock
    private ApplicationLinksService applicationLinksService;

    private TestApplicationLinksResourceImpl resource;

    @Before
    public void setup() {
        resource = new TestApplicationLinksResourceImpl(applicationLinksService);
    }

    @Test
    public void testGetApplicationLinks() {
        final ApplicationLinksBean bean = ApplicationLinksBean.EXAMPLE_1;

        doReturn(bean).when(applicationLinksService).getApplicationLinks();

        final Response response = resource.getApplicationLinks();
        assertEquals(200, response.getStatus());
        final ApplicationLinksBean linksBean = (ApplicationLinksBean) response.getEntity();

        assertEquals(linksBean, bean);
    }

    @Test
    public void testSetApplicationLinks() {
        final ApplicationLinksBean bean = ApplicationLinksBean.EXAMPLE_1;

        doReturn(bean).when(applicationLinksService).setApplicationLinks(bean);

        final Response response = resource.setApplicationLinks(bean);
        assertEquals(200, response.getStatus());
        final ApplicationLinksBean linksBean = (ApplicationLinksBean) response.getEntity();

        assertEquals(linksBean, bean);
    }

    @Test
    public void testAddApplicationLink() {
        final ApplicationLinkBean bean = ApplicationLinkBean.EXAMPLE_1;

        doReturn(bean).when(applicationLinksService).addApplicationLink(bean);

        final Response response = resource.addApplicationLink(bean);
        assertEquals(200, response.getStatus());
        final ApplicationLinkBean responseBean = (ApplicationLinkBean) response.getEntity();

        assertEquals(responseBean, bean);
    }
}
