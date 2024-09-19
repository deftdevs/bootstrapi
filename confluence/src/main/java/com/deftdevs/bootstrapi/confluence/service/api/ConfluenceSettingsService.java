package com.deftdevs.bootstrapi.confluence.service.api;

import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlBean;

public interface ConfluenceSettingsService extends SettingsService {

    SettingsCustomHtmlBean getCustomHtml();

    SettingsCustomHtmlBean setCustomHtml(
            SettingsCustomHtmlBean settingsCustomHtmlBean);

}
