package it.com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceFuncTest;

public class SettingsResourceFuncTest extends AbstractSettingsResourceFuncTest {

    @Override
    protected SettingsGeneralModel getExampleSettingsGeneralModel() {
        return SettingsGeneralModel.EXAMPLE_1_NO_MODE;
    }
}
