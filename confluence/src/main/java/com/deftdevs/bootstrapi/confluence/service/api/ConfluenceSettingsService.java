package com.deftdevs.bootstrapi.confluence.service.api;

import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlBean;
import com.deftdevs.bootstrapi.confluence.model.SettingsSecurityBean;

public interface ConfluenceSettingsService extends SettingsService {

    SettingsCustomHtmlBean getCustomHtml();

    SettingsCustomHtmlBean setCustomHtml(
            SettingsCustomHtmlBean settingsCustomHtmlBean);

    SettingsSecurityBean getSecurity();

    SettingsSecurityBean setSecurity(
            SettingsSecurityBean bean);

}
