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

        final AbstractMailServerProtocolModel beanWithTimeoutSet = new AbstractMailServerProtocolModel() {};
        beanWithTimeoutSet.setTimeout(timeout);
        assertEquals(timeout, beanWithTimeoutSet.getTimeout().longValue());

        final AbstractMailServerProtocolModel beanWithoutTimeoutSet = new AbstractMailServerProtocolModel() {};
        assertNotNull(beanWithoutTimeoutSet.getTimeout());
    }

    @Test
    void testSetPortUsingIntAndString() {
        final int port = 1337;

        final AbstractMailServerProtocolModel beanUsingInt = new AbstractMailServerProtocolModel() {};
        beanUsingInt.setPort(port);

        final AbstractMailServerProtocolModel beanUsingString = new AbstractMailServerProtocolModel() {};
        beanUsingString.setPort(String.valueOf(port));

        assertEquals(beanUsingInt.getPort(), beanUsingString.getPort());
    }

}
