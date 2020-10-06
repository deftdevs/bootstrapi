package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.junit.AbstractBeanTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class MailServerSmtpBeanTest extends AbstractBeanTest {

    @Test
    public void testTimeoutHasDefaultValue() {
        final long timeout = 12345L;
        final MailServerSmtpBean beanWithTimeoutSet = new MailServerSmtpBean();

        beanWithTimeoutSet.setTimeout(timeout);
        assertEquals(timeout, beanWithTimeoutSet.getTimeout().longValue());

        final MailServerSmtpBean beanWithoutTimeoutSet = new MailServerSmtpBean();
        assertNotNull(beanWithoutTimeoutSet.getTimeout());
    }

}
