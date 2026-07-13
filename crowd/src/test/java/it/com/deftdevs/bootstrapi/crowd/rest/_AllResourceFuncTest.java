package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import com.deftdevs.bootstrapi.crowd.model.MailTemplatesModel;
import com.deftdevs.bootstrapi.crowd.model.SessionConfigModel;
import com.deftdevs.bootstrapi.crowd.model.SettingsModel;
import com.deftdevs.bootstrapi.crowd.model._AllModel;
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
        return SettingsGeneralModel.EXAMPLE_1_MINIMAL;
    }

    @Test
    void testSetAllAppliesMultipleEntities() throws Exception {
        // no POP mail server here: setting POP is unsupported by Crowd
        final _AllModel allModel = _AllModel.builder()
                .settings(SettingsModel.builder()
                        .general(getExampleSettingsGeneralModel())
                        .build())
                .mailServer(MailServerModel.builder()
                        .smtp(MailServerSmtpModel.EXAMPLE_2_MINIMAL)
                        .build())
                .mailTemplates(MailTemplatesModel.EXAMPLE_1)
                .sessionConfig(SessionConfigModel.EXAMPLE_2)
                .trustedProxies(Arrays.asList("10.0.0.1", "10.0.0.2"))
                .build();

        assertSetAllApplied(allModel, Arrays.asList(
                FieldNames.pathOf(_AllModel.class, SettingsGeneralModel.class),
                FieldNames.pathOf(_AllModel.class, MailServerSmtpModel.class),
                FieldNames.of(_AllModel.class, MailTemplatesModel.class),
                FieldNames.of(_AllModel.class, SessionConfigModel.class),
                FieldNames.of(_AllModel.class, String.class)));
    }
}
