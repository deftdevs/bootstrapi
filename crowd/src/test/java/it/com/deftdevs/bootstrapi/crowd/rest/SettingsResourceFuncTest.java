package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceFuncTest;

public class SettingsResourceFuncTest extends AbstractSettingsResourceFuncTest {

    @Override
    protected SettingsGeneralModel getExampleSettingsGeneralModel() {
        return SettingsGeneralModel.EXAMPLE_1_MINIMAL;
    }
}
