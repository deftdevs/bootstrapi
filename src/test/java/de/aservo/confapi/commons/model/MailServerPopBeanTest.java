package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.junit.AbstractBeanTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class MailServerPopBeanTest extends AbstractBeanTest {

    @Test
    public void testTimeoutHasDefaultValue() {
        final long timeout = 12345L;
        final MailServerPopBean beanWithTimeoutSet = new MailServerPopBean();

        beanWithTimeoutSet.setTimeout(timeout);
        assertEquals(timeout, beanWithTimeoutSet.getTimeout().longValue());

        final MailServerPopBean beanWithoutTimeoutSet = new MailServerPopBean();
        assertNotNull(beanWithoutTimeoutSet.getTimeout());
    }

}
