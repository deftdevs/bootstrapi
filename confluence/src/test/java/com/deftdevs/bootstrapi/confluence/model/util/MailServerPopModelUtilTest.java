package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.mail.server.DefaultTestPopMailServerImpl;
import com.atlassian.mail.server.PopMailServer;
import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MailServerPopModelUtilTest {

    @Test
    void testToMailServerPopModel() {
        final PopMailServer server = new DefaultTestPopMailServerImpl();
        final MailServerPopModel bean = MailServerPopModelUtil.toMailServerPopModel(server);

        assertNotNull(bean);
        assertEquals(server.getName(), bean.getName());
        assertEquals(server.getDescription(), bean.getDescription());
        assertEquals(server.getMailProtocol().getProtocol(), bean.getProtocol());
        assertEquals(server.getHostname(), bean.getHost());
        assertEquals(Integer.valueOf(server.getPort()), bean.getPort());
        assertEquals(server.getTimeout(), (long) bean.getTimeout());
        assertEquals(server.getUsername(), bean.getUsername());
        assertNull(bean.getPassword());
    }

}
