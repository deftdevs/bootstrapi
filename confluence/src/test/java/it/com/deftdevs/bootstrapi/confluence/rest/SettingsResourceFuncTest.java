package it.com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceFuncTest;

public class SettingsResourceFuncTest extends AbstractSettingsResourceFuncTest {

    @Override
    protected SettingsModel getExampleModel() {
        return SettingsModel.EXAMPLE_1_NO_MODE;
    }
}
