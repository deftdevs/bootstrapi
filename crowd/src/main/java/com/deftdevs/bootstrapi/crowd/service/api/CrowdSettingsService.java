package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.commons.util.ServiceResultUtil;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingLoginPageModel;
import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingModel;
import com.deftdevs.bootstrapi.crowd.model.SettingsModel;

import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

public interface CrowdSettingsService extends
        SettingsService<SettingsModel>,
        CrowdSettingsGeneralService,
        CrowdSettingsBrandingService {

    default SettingsModel getSettings() {
        return SettingsModel.builder()
                .general(getSettingsGeneral())
                .branding(SettingsBrandingModel.builder()
                        .loginPage(getSettingsBrandingLoginPage())
                        .build())
                .build();
    }

    default ServiceResult<SettingsModel> setSettings(final SettingsModel settingsModel) {
        final SettingsModel result = new SettingsModel();
        final Map<String, _AllModelStatus> status = new LinkedHashMap<>();

        ServiceResultUtil.setSubEntity(status, SettingsGeneralModel.class, settingsModel.getGeneral(),
                this::setSettingsGeneral, result::setGeneral);

        if (settingsModel.getSecurity() != null) {
            final String key = FieldNames.of(SettingsSecurityModel.class);
            status.put(key, _AllModelStatus.error(Response.Status.BAD_REQUEST,
                    String.format("Failed to apply %s configuration", key),
                    "Crowd does not support security settings"));
        }

        ServiceResultUtil.setSubEntityWithStatus(status, SettingsBrandingModel.class, settingsModel.getBranding(),
                this::setSettingsBranding, result::setBranding);

        return new ServiceResult<>(result, status);
    }

    default ServiceResult<SettingsBrandingModel> setSettingsBranding(final SettingsBrandingModel brandingModel) {
        final SettingsBrandingModel result = new SettingsBrandingModel();
        final Map<String, _AllModelStatus> status = new LinkedHashMap<>();

        ServiceResultUtil.setSubEntity(status, SettingsBrandingLoginPageModel.class, brandingModel.getLoginPage(),
                this::setSettingsBrandingLoginPage, result::setLoginPage);

        return new ServiceResult<>(result, status);
    }

}
