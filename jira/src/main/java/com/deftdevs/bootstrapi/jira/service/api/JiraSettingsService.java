package com.deftdevs.bootstrapi.jira.service.api;

import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.jira.model.SettingsBannerBean;

public interface JiraSettingsService extends SettingsService {

    SettingsBannerBean getBanner();

    SettingsBannerBean setBanner(
            SettingsBannerBean settingsBannerBean);

}
