package com.deftdevs.bootstrapi.confluence.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.service.api.SettingsSecurityService;
import com.deftdevs.bootstrapi.commons.service.api.SettingsGeneralService;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlModel;

public interface ConfluenceSettingsService extends
        SettingsGeneralService<SettingsGeneralModel>,
        SettingsSecurityService<SettingsSecurityModel> {

    SettingsCustomHtmlModel getCustomHtml();

    SettingsCustomHtmlModel setCustomHtml(
            SettingsCustomHtmlModel settingsCustomHtmlModel);

}
