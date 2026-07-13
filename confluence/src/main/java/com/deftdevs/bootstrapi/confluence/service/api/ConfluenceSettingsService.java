package com.deftdevs.bootstrapi.confluence.service.api;

import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingColorSchemeModel;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingModel;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service.api.SettingsGeneralService;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.commons.service.api.SettingsSecurityService;
import com.deftdevs.bootstrapi.commons.util.ServiceResultUtil;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingCustomHtmlModel;
import com.deftdevs.bootstrapi.confluence.model.SettingsModel;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ConfluenceSettingsService extends
        SettingsService<SettingsModel>,
        SettingsGeneralService<SettingsGeneralModel>,
        SettingsSecurityService<SettingsSecurityModel>,
        SettingsBrandingService {

    SettingsBrandingCustomHtmlModel getSettingsBrandingCustomHtml();

    SettingsBrandingCustomHtmlModel setSettingsBrandingCustomHtml(
            SettingsBrandingCustomHtmlModel settingsCustomHtmlModel);

    default SettingsModel getSettings() {
        return SettingsModel.builder()
                .general(getSettingsGeneral())
                .security(getSettingsSecurity())
                .branding(SettingsBrandingModel.builder()
                        .colorScheme(getSettingsBrandingColorScheme())
                        .customHtml(getSettingsBrandingCustomHtml())
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

        ServiceResultUtil.setSubEntity(status, SettingsBrandingColorSchemeModel.class, brandingModel.getColorScheme(),
                this::setSettingsBrandingColorScheme, result::setColorScheme);
        ServiceResultUtil.setSubEntity(status, SettingsBrandingCustomHtmlModel.class, brandingModel.getCustomHtml(),
                this::setSettingsBrandingCustomHtml, result::setCustomHtml);

        return new ServiceResult<>(result, status);
    }

}
