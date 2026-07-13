package it.com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingCustomHtmlModel;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingModel;
import com.deftdevs.bootstrapi.confluence.model.SettingsModel;
import com.deftdevs.bootstrapi.confluence.model._AllModel;
import it.com.deftdevs.bootstrapi.commons.rest._AbstractAllResourceFuncTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class _AllResourceFuncTest extends _AbstractAllResourceFuncTest {

    @Override
    protected Object getExampleAllModel() {
        return _AllModel.builder()
                .settings(SettingsModel.builder()
                        .general(getExampleSettingsGeneralModel())
                        .build())
                .build();
    }

    @Override
    protected String getSettingsGeneralStatusKey() {
        return FieldNames.pathOf(_AllModel.class, SettingsGeneralModel.class);
    }

    @Override
    protected SettingsGeneralModel getExampleSettingsGeneralModel() {
        return SettingsGeneralModel.EXAMPLE_1_NO_MODE;
    }

    @Test
    void testSetAllAppliesMultipleEntities() throws Exception {
        final _AllModel allModel = _AllModel.builder()
                .settings(SettingsModel.builder()
                        .general(getExampleSettingsGeneralModel())
                        .branding(SettingsBrandingModel.builder()
                                .customHtml(SettingsBrandingCustomHtmlModel.EXAMPLE_1)
                                .build())
                        .build())
                .mailServer(MailServerModel.builder()
                        .smtp(MailServerSmtpModel.EXAMPLE_2)
                        .pop(MailServerPopModel.EXAMPLE_2)
                        .build())
                .build();

        assertSetAllApplied(allModel, Arrays.asList(
                FieldNames.pathOf(_AllModel.class, SettingsGeneralModel.class),
                FieldNames.pathOf(_AllModel.class, SettingsBrandingCustomHtmlModel.class),
                FieldNames.pathOf(_AllModel.class, MailServerSmtpModel.class),
                FieldNames.pathOf(_AllModel.class, MailServerPopModel.class)));
    }
}
