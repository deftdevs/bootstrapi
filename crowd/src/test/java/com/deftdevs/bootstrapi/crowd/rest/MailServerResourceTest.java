package com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerSmtpBean;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
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
public class MailServerResourceTest {

    @Mock
    private MailServerService mailServerService;

    private MailServerResourceImpl mailServerResource;

    @Before
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
