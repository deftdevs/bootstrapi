package de.aservo.confapi.jira.util;

import com.atlassian.mail.MailProtocol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class MailProtocolUtilTest {

    @Test
    void testFind() {
        final MailProtocol protocolImap = MailProtocolUtil.find(MailProtocol.IMAP.getProtocol(), null);
        assertEquals(MailProtocol.IMAP, protocolImap);
    }

    @Test
    void testFindNotFoundDefaultValue() {
        final MailProtocol protocolDefault = MailProtocolUtil.find("abc", null);
        assertNull(protocolDefault);
    }

    @Test
    void testFindBlankDefaultValue() {
        final MailProtocol protocolDefault = MailProtocolUtil.find("", null);
        assertNull(protocolDefault);
    }

}
