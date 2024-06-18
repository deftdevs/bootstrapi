package de.aservo.confapi.confluence.model.util;

import com.atlassian.mail.server.DefaultTestPopMailServerImpl;
import com.atlassian.mail.server.PopMailServer;
import de.aservo.confapi.commons.model.MailServerPopBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MailServerPopBeanUtilTest {

    @Test
    void testToMailServerPopBean() {
        final PopMailServer server = new DefaultTestPopMailServerImpl();
        final MailServerPopBean bean = MailServerPopBeanUtil.toMailServerPopBean(server);

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
