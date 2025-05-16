package com.deftdevs.bootstrapi.confluence.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.service.api.SettingsSecurityService;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlModel;

public interface ConfluenceSettingsService extends
        SettingsService<SettingsModel>,
        SettingsSecurityService<SettingsSecurityModel> {

    SettingsCustomHtmlModel getCustomHtml();

    SettingsCustomHtmlModel setCustomHtml(
            SettingsCustomHtmlModel settingsCustomHtmlModel);

}
