package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.rest.impl.TestMailServerResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MailServerResourceTest {

    @Mock
    private MailServerService mailServerService;

    private TestMailServerResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestMailServerResourceImpl(mailServerService);
    }

    @Test
    void testGetMailServerSmtpSettings() {
        final MailServerSmtpModel bean = MailServerSmtpModel.EXAMPLE_1;

        doReturn(bean).when(mailServerService).getMailServerSmtp();

        final Response response = resource.getMailServerSmtp();
        assertEquals(200, response.getStatus());
        final MailServerSmtpModel smtpModel = (MailServerSmtpModel) response.getEntity();

        assertEquals(smtpModel, bean);
    }

    @Test
    void testSetMailServerSmtpSettings() {
        final MailServerSmtpModel bean = MailServerSmtpModel.EXAMPLE_1;

        doReturn(bean).when(mailServerService).setMailServerSmtp(bean);

        final Response response = resource.setMailServerSmtp(bean);
        assertEquals(200, response.getStatus());
        final MailServerSmtpModel smtpModel = (MailServerSmtpModel) response.getEntity();

        assertEquals(smtpModel, bean);
    }

    @Test
    void testGetMailServerPopSettings() {
        final MailServerPopModel bean = MailServerPopModel.EXAMPLE_1;

        doReturn(bean).when(mailServerService).getMailServerPop();

        final Response response = resource.getMailServerPop();
        assertEquals(200, response.getStatus());
        final MailServerPopModel popModel = (MailServerPopModel) response.getEntity();

        assertEquals(popModel, bean);
    }

    @Test
    void testSetMailServerPopSettings() {
        final MailServerPopModel bean = MailServerPopModel.EXAMPLE_1;

        doReturn(bean).when(mailServerService).setMailServerPop(bean);

        final Response response = resource.setMailServerPop(bean);
        assertEquals(200, response.getStatus());
        final MailServerPopModel popModel = (MailServerPopModel) response.getEntity();

        assertEquals(popModel, bean);
    }
}
