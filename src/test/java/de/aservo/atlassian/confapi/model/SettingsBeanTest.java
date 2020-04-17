package de.aservo.atlassian.confapi.model;

import com.atlassian.mail.server.DefaultTestSmtpMailServerImpl;
import com.atlassian.mail.server.SMTPMailServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class SettingsBeanTest {

    @Test
    public void testDefaultConstructor() {
        final SettingsBean bean = new SettingsBean();

        assertNull(bean.getBaseUrl());
        assertNull(bean.getMode());
        assertNull(bean.getTitle());
    }

    @Test
    public void testParameterConstructor() throws Exception {
        final String baseUrl = "http://localhost/product";
        final String mode = "PRIVATE";
        final String title = "PRODUCT";

        final SMTPMailServer server = new DefaultTestSmtpMailServerImpl();
        final SettingsBean bean = new SettingsBean();
        bean.setBaseUrl(baseUrl);
        bean.setMode(mode);
        bean.setTitle(title);

        assertEquals(baseUrl, bean.getBaseUrl());
        assertEquals(mode.toLowerCase(), bean.getMode());
        assertEquals(title, bean.getTitle());
    }

}
