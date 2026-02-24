package it.com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractSettingsGeneralResourceFuncTest;

public class SettingsGeneralResourceFuncTest extends AbstractSettingsGeneralResourceFuncTest {

    @Override
    protected SettingsGeneralModel getExampleModel() {
        return SettingsGeneralModel.EXAMPLE_1_NO_MODE;
    }
}
