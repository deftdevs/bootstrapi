package it.com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import com.deftdevs.bootstrapi.jira.model.SettingsBrandingBannerModel;
import com.deftdevs.bootstrapi.jira.model.SettingsBrandingModel;
import com.deftdevs.bootstrapi.jira.model.SettingsModel;
import com.deftdevs.bootstrapi.jira.model._AllModel;
import it.com.deftdevs.bootstrapi.commons.rest._AbstractAllResourceFuncTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class _AllResourceFuncTest extends _AbstractAllResourceFuncTest {

    @Override
    protected String getSettingsGeneralStatusKey() {
        return FieldNames.pathOf(_AllModel.class, SettingsGeneralModel.class);
    }

    @Override
    protected Object getExampleAllModel() {
        return _AllModel.builder()
                .settings(SettingsModel.builder()
                        .general(getExampleSettingsGeneralModel())
                        .build())
                .build();
    }

    @Test
    void testSetAllAppliesMultipleEntities() throws Exception {
        final _AllModel allModel = _AllModel.builder()
                .settings(SettingsModel.builder()
                        .general(getExampleSettingsGeneralModel())
                        .branding(SettingsBrandingModel.builder()
                                .banner(SettingsBrandingBannerModel.EXAMPLE_1)
                                .build())
                        .build())
                .mailServer(MailServerModel.builder()
                        .smtp(MailServerSmtpModel.EXAMPLE_2)
                        .pop(MailServerPopModel.EXAMPLE_2)
                        .build())
                .build();

        assertSetAllApplied(allModel, Arrays.asList(
                FieldNames.pathOf(_AllModel.class, SettingsGeneralModel.class),
                FieldNames.pathOf(_AllModel.class, SettingsBrandingBannerModel.class),
                FieldNames.pathOf(_AllModel.class, MailServerSmtpModel.class),
                FieldNames.pathOf(_AllModel.class, MailServerPopModel.class)));
    }
}
