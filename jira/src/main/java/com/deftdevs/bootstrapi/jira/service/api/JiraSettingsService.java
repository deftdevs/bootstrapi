package com.deftdevs.bootstrapi.jira.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service.api.SettingsGeneralService;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.commons.service.api.SettingsSecurityService;
import com.deftdevs.bootstrapi.commons.util.ServiceResultUtil;
import com.deftdevs.bootstrapi.jira.model.SettingsBrandingBannerModel;
import com.deftdevs.bootstrapi.jira.model.SettingsBrandingModel;
import com.deftdevs.bootstrapi.jira.model.SettingsModel;

import java.util.LinkedHashMap;
import java.util.Map;

public interface JiraSettingsService extends
        SettingsService<SettingsModel>,
        SettingsGeneralService<SettingsGeneralModel>,
        SettingsSecurityService<SettingsSecurityModel> {

    SettingsBrandingBannerModel getSettingsBrandingBanner();

    SettingsBrandingBannerModel setSettingsBrandingBanner(
            SettingsBrandingBannerModel settingsBannerModel);

    default SettingsModel getSettings() {
        return SettingsModel.builder()
                .general(getSettingsGeneral())
                .security(getSettingsSecurity())
                .branding(SettingsBrandingModel.builder()
                        .banner(getSettingsBrandingBanner())
                        .build())
                .build();
    }

    default ServiceResult<SettingsModel> setSettings(final SettingsModel settingsModel) {
        final SettingsModel result = new SettingsModel();
        final Map<String, _AllModelStatus> status = new LinkedHashMap<>();

        ServiceResultUtil.setSubEntity(status, SettingsGeneralModel.class, settingsModel.getGeneral(),
                this::setSettingsGeneral, result::setGeneral);
        ServiceResultUtil.setSubEntity(status, SettingsSecurityModel.class, settingsModel.getSecurity(),
                this::setSettingsSecurity, result::setSecurity);
        ServiceResultUtil.setSubEntityWithStatus(status, SettingsBrandingModel.class, settingsModel.getBranding(),
                this::setSettingsBranding, result::setBranding);

        return new ServiceResult<>(result, status);
    }

    default ServiceResult<SettingsBrandingModel> setSettingsBranding(final SettingsBrandingModel brandingModel) {
        final SettingsBrandingModel result = new SettingsBrandingModel();
        final Map<String, _AllModelStatus> status = new LinkedHashMap<>();

        ServiceResultUtil.setSubEntity(status, SettingsBrandingBannerModel.class, brandingModel.getBanner(),
                this::setSettingsBrandingBanner, result::setBanner);

        return new ServiceResult<>(result, status);
    }

}
