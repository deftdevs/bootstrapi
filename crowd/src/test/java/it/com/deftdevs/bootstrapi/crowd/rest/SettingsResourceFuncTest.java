package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceFuncTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SettingsResourceFuncTest extends AbstractSettingsResourceFuncTest {

    @Override
    protected SettingsBean getExampleBean() {
        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setBaseUrl(SettingsBean.EXAMPLE_1.getBaseUrl());
        settingsBean.setTitle(SettingsBean.EXAMPLE_1.getTitle());
        return settingsBean;
    }

    @Test
    @Disabled("SystemAdminOnly filter only handles 401")
    void testGetSettingsUnauthorized() {
    }

    @Test
    @Disabled("SystemAdminOnly filter only handles 401")
    void testSetSettingsUnauthorized() {
    }

}
