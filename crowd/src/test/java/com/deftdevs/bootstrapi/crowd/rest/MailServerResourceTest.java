package com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
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
public class MailServerResourceTest {

    @Mock
    private MailServerService mailServerService;

    private MailServerResourceImpl mailServerResource;

    @BeforeEach
    public void setup() {
        mailServerResource = new MailServerResourceImpl(mailServerService);
    }

    @Test
    public void testGetMailServerSmtp() {
        final MailServerSmtpModel mailServerSmtpModel = MailServerSmtpModel.EXAMPLE_1;
        doReturn(mailServerSmtpModel).when(mailServerService).getMailServerSmtp();

        final Response response = mailServerResource.getMailServerSmtp();
        assertEquals(200, response.getStatus());

        final MailServerSmtpModel responseMailServerSmtpModel = (MailServerSmtpModel) response.getEntity();
        assertEquals(mailServerSmtpModel, responseMailServerSmtpModel);
    }

    @Test
    public void testGetMailServerSmtpNotConfigured() {
        final Response response = mailServerResource.getMailServerSmtp();
        assertEquals(204, response.getStatus());
    }

    @Test
    public void testSetMailServerSmtp() {
        final MailServerSmtpModel mailServerSmtpModel = MailServerSmtpModel.EXAMPLE_2;
        doReturn(mailServerSmtpModel).when(mailServerService).setMailServerSmtp(any(MailServerSmtpModel.class));

        final Response response = mailServerResource.setMailServerSmtp(mailServerSmtpModel);
        assertEquals(200, response.getStatus());

        final MailServerSmtpModel responseMailServerSmtpModel = (MailServerSmtpModel) response.getEntity();
        assertEquals(mailServerSmtpModel, responseMailServerSmtpModel);
    }

}
