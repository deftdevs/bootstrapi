package com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerSmtpBean;
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
        final MailServerSmtpBean mailServerSmtpBean = MailServerSmtpBean.EXAMPLE_1;
        doReturn(mailServerSmtpBean).when(mailServerService).getMailServerSmtp();

        final Response response = mailServerResource.getMailServerSmtp();
        assertEquals(200, response.getStatus());

        final MailServerSmtpBean responseMailServerSmtpBean = (MailServerSmtpBean) response.getEntity();
        assertEquals(mailServerSmtpBean, responseMailServerSmtpBean);
    }

    @Test
    public void testGetMailServerSmtpNotConfigured() {
        final Response response = mailServerResource.getMailServerSmtp();
        assertEquals(204, response.getStatus());
    }

    @Test
    public void testSetMailServerSmtp() {
        final MailServerSmtpBean mailServerSmtpBean = MailServerSmtpBean.EXAMPLE_2;
        doReturn(mailServerSmtpBean).when(mailServerService).setMailServerSmtp(any(MailServerSmtpBean.class));

        final Response response = mailServerResource.setMailServerSmtp(mailServerSmtpBean);
        assertEquals(200, response.getStatus());

        final MailServerSmtpBean responseMailServerSmtpBean = (MailServerSmtpBean) response.getEntity();
        assertEquals(mailServerSmtpBean, responseMailServerSmtpBean);
    }

}
