package com.deftdevs.bootstrapi.confluence.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityBean;
import com.deftdevs.bootstrapi.commons.service.api.SettingsSecurityService;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlBean;

public interface ConfluenceSettingsService extends
        SettingsService<SettingsBean>,
        SettingsSecurityService<SettingsSecurityBean> {

    SettingsCustomHtmlBean getCustomHtml();

    SettingsCustomHtmlBean setCustomHtml(
            SettingsCustomHtmlBean settingsCustomHtmlBean);

}
