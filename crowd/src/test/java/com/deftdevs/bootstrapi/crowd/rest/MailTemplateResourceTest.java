package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.crowd.exception.ObjectNotFoundException;
import com.deftdevs.bootstrapi.crowd.model.MailTemplatesModel;
import com.deftdevs.bootstrapi.crowd.service.api.MailTemplatesService;
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
public class MailTemplateResourceTest {

    @Mock
    private MailTemplatesService mailTemplatesService;

    private MailTemplatesResourceImpl mailTemplateResource;

    @BeforeEach
    public void setup() {
        mailTemplateResource = new MailTemplatesResourceImpl(mailTemplatesService);
    }

    @Test
    public void testGetMailTemplates() throws ObjectNotFoundException {
        final MailTemplatesModel mailTemplatesModel = MailTemplatesModel.EXAMPLE_1;
        doReturn(mailTemplatesModel).when(mailTemplatesService).getMailTemplates();

        final Response response = mailTemplateResource.getMailTemplates();
        assertEquals(200, response.getStatus());

        final MailTemplatesModel responseMailTemplatesModel = (MailTemplatesModel) response.getEntity();
        assertEquals(mailTemplatesModel, responseMailTemplatesModel);
    }

    @Test
    public void testSetMailTemplates() {
        final MailTemplatesModel mailTemplatesModel = MailTemplatesModel.EXAMPLE_2;
        doReturn(mailTemplatesModel).when(mailTemplatesService).setMailTemplates(any(MailTemplatesModel.class));

        final Response response = mailTemplateResource.setMailTemplates(mailTemplatesModel);
        assertEquals(200, response.getStatus());

        final MailTemplatesModel responseMailTemplatesModel = (MailTemplatesModel) response.getEntity();
        assertEquals(mailTemplatesModel, responseMailTemplatesModel);
    }

}
