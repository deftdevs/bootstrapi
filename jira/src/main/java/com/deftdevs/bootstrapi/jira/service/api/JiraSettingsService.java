package com.deftdevs.bootstrapi.jira.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service.api.SettingsGeneralService;
import com.deftdevs.bootstrapi.commons.service.api.SettingsSecurityService;
import com.deftdevs.bootstrapi.commons.util.ServiceResultUtil;
import com.deftdevs.bootstrapi.jira.model.SettingsBannerModel;
import com.deftdevs.bootstrapi.jira.model.SettingsModel;

import java.util.LinkedHashMap;
import java.util.Map;

public interface JiraSettingsService extends
        SettingsGeneralService<SettingsGeneralModel>,
        SettingsSecurityService<SettingsSecurityModel> {

    SettingsBannerModel getSettingsBanner();

    SettingsBannerModel setSettingsBanner(
            SettingsBannerModel settingsBannerModel);

    default SettingsModel getSettings() {
        return SettingsModel.builder()
                .general(getSettingsGeneral())
                .security(getSettingsSecurity())
                .banner(getSettingsBanner())
                .build();
    }

    default ServiceResult<SettingsModel> setSettings(final SettingsModel settingsModel) {
        final SettingsModel result = new SettingsModel();
        final Map<String, _AllModelStatus> status = new LinkedHashMap<>();

        ServiceResultUtil.setSubEntity(status, SettingsGeneralModel.class, settingsModel.getGeneral(),
                this::setSettingsGeneral, result::setGeneral);
        ServiceResultUtil.setSubEntity(status, SettingsSecurityModel.class, settingsModel.getSecurity(),
                this::setSettingsSecurity, result::setSecurity);
        ServiceResultUtil.setSubEntity(status, SettingsBannerModel.class, settingsModel.getBanner(),
                this::setSettingsBanner, result::setBanner);

        return new ServiceResult<>(result, status);
    }

}
