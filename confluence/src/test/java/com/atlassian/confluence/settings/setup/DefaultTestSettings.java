package com.atlassian.confluence.settings.setup;

import com.atlassian.confluence.setup.settings.Settings;

public class DefaultTestSettings extends Settings {

    public static final String BASEURL = "https://confluence.deftdevs.com";
    public static final String TITLE = "Deft Devs Confluence";
    public static final String CUSTOM_CONTACT_MESSAGE = "Test";

    public DefaultTestSettings() {
        setBaseUrl(BASEURL);
        setSiteTitle(TITLE);
        setCustomContactMessage(CUSTOM_CONTACT_MESSAGE);
        setExternalUserManagement(true);
    }

}
