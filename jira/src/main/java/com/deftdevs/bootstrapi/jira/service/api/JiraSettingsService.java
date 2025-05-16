package com.deftdevs.bootstrapi.jira.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.service.api.SettingsSecurityService;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.jira.model.SettingsBannerModel;

public interface JiraSettingsService extends
        SettingsService<SettingsModel>,
        SettingsSecurityService<SettingsSecurityModel> {

    SettingsBannerModel getSettingsBanner();

    SettingsBannerModel setSettingsBanner(
            SettingsBannerModel settingsBannerModel);

}
