package com.deftdevs.bootstrapi.jira.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityBean;
import com.deftdevs.bootstrapi.commons.service.api.SettingsSecurityService;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.jira.model.SettingsBannerBean;

public interface JiraSettingsService extends
        SettingsService<SettingsBean>,
        SettingsSecurityService<SettingsSecurityBean> {

    SettingsBannerBean getSettingsBanner();

    SettingsBannerBean setSettingsBanner(
            SettingsBannerBean settingsBannerBean);

}
