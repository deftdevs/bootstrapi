package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.mail.server.DefaultTestSmtpMailServerImpl;
import com.atlassian.mail.server.SMTPMailServer;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MailServerSmtpModelUtilTest {

    @Test
    void testToMailServerSmtpModel() {
        final SMTPMailServer server = new DefaultTestSmtpMailServerImpl();
        final MailServerSmtpModel bean = MailServerSmtpModelUtil.toMailServerSmtpModel(server);

        assertNotNull(bean);
        assertEquals(server.getName(), bean.getName());
        assertEquals(server.getDescription(), bean.getDescription());
        assertEquals(server.getDefaultFrom(), bean.getFrom());
        assertEquals(server.getPrefix(), bean.getPrefix());
        assertEquals(server.getMailProtocol().getProtocol(), bean.getProtocol());
        assertEquals(server.getHostname(), bean.getHost());
        assertEquals(Integer.valueOf(server.getPort()), bean.getPort());
        assertEquals(server.isTlsRequired(), bean.getUseTls());
        assertEquals(server.getTimeout(), (long) bean.getTimeout());
        assertEquals(server.getUsername(), bean.getUsername());
        assertNull(bean.getPassword());
    }

}
