package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceFuncTest;

public class SettingsResourceFuncTest extends AbstractSettingsResourceFuncTest {

    @Override
    protected SettingsModel getExampleModel() {
        final SettingsModel settingsModel = new SettingsModel();
        settingsModel.setBaseUrl(SettingsModel.EXAMPLE_1.getBaseUrl());
        settingsModel.setTitle(SettingsModel.EXAMPLE_1.getTitle());
        return settingsModel;
    }

}
