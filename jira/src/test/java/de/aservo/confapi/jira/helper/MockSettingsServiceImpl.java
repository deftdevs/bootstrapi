package de.aservo.confapi.jira.helper;

import com.atlassian.jira.mock.MockApplicationProperties;
import de.aservo.confapi.commons.model.SettingsBean;
import de.aservo.confapi.jira.service.SettingsServiceImpl;

import java.net.URI;

public class MockSettingsServiceImpl extends SettingsServiceImpl {

    public static final URI BASE_URL = URI.create("http://localhost:2990/jira");
    public static final String MODE = "private";
    public static final String TITLE = "Your Company JIRA";

    public MockSettingsServiceImpl() {
        super(new MockApplicationProperties());

        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setBaseUrl(BASE_URL);
        settingsBean.setMode(MODE);
        settingsBean.setTitle(TITLE);

        setSettings(settingsBean);
    }

}
