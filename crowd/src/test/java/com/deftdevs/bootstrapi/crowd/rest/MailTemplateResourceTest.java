package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.crowd.exception.ObjectNotFoundException;
import com.deftdevs.bootstrapi.crowd.model.MailTemplatesBean;
import com.deftdevs.bootstrapi.crowd.service.api.MailTemplatesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class MailTemplateResourceTest {

    @Mock
    private MailTemplatesService mailTemplatesService;

    private MailTemplatesResourceImpl mailTemplateResource;

    @Before
    public void setup() {
        mailTemplateResource = new MailTemplatesResourceImpl(mailTemplatesService);
    }

    @Test
    public void testGetMailTemplates() throws ObjectNotFoundException {
        final MailTemplatesBean mailTemplatesBean = MailTemplatesBean.EXAMPLE_1;
        doReturn(mailTemplatesBean).when(mailTemplatesService).getMailTemplates();

        final Response response = mailTemplateResource.getMailTemplates();
        assertEquals(200, response.getStatus());

        final MailTemplatesBean responseMailTemplatesBean = (MailTemplatesBean) response.getEntity();
        assertEquals(mailTemplatesBean, responseMailTemplatesBean);
    }

    @Test
    public void testSetMailTemplates() {
        final MailTemplatesBean mailTemplatesBean = MailTemplatesBean.EXAMPLE_2;
        doReturn(mailTemplatesBean).when(mailTemplatesService).setMailTemplates(any(MailTemplatesBean.class));

        final Response response = mailTemplateResource.setMailTemplates(mailTemplatesBean);
        assertEquals(200, response.getStatus());

        final MailTemplatesBean responseMailTemplatesBean = (MailTemplatesBean) response.getEntity();
        assertEquals(mailTemplatesBean, responseMailTemplatesBean);
    }

}
