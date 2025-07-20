package com.deftdevs.bootstrapi.commons.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AbstractMailServerProtocolModelTest {

    @Test
    void testTimeoutHasDefaultValue() {
        final long timeout = 12345L;

        final AbstractMailServerProtocolModel beanWithTimeoutSet = MailServerPopModel.builder().timeout(timeout).build();
        assertEquals(timeout, beanWithTimeoutSet.getTimeout().longValue());

        final AbstractMailServerProtocolModel beanWithoutTimeoutSet = MailServerPopModel.builder().build();
        assertNotNull(beanWithoutTimeoutSet.getTimeout());
    }

    @Test
    void testSetPortUsingIntAndString() {
        final int port = 1337;

        final AbstractMailServerProtocolModel beanUsingInt = MailServerPopModel.builder().port(port).build();
        final AbstractMailServerProtocolModel beanUsingString = MailServerPopModel.builder().port(port).build();
        assertEquals(beanUsingInt.getPort(), beanUsingString.getPort());
    }

}
