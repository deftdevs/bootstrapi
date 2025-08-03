package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceFuncTest;

public class SettingsResourceFuncTest extends AbstractSettingsResourceFuncTest {

    @Override
    protected SettingsModel getExampleModel() {
        return SettingsModel.builder()
                .baseUrl(SettingsModel.EXAMPLE_1.getBaseUrl())
                .title(SettingsModel.EXAMPLE_1.getTitle())
                .build();
    }

}
