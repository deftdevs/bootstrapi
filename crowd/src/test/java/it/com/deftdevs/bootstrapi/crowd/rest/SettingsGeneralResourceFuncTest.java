package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractSettingsGeneralResourceFuncTest;

public class SettingsGeneralResourceFuncTest extends AbstractSettingsGeneralResourceFuncTest {

    @Override
    protected SettingsGeneralModel getExampleModel() {
        return SettingsGeneralModel.builder()
                .baseUrl(SettingsGeneralModel.EXAMPLE_1.getBaseUrl())
                .title(SettingsGeneralModel.EXAMPLE_1.getTitle())
                .build();
    }

}
