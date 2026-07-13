package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingCustomHtmlModel;
import com.deftdevs.bootstrapi.confluence.rest.api.SettingsBrandingCustomHtmlResource;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_BRANDING + "/" + BootstrAPI.SETTINGS_BRANDING_CUSTOM_HTML)
@SystemAdminOnly
public class SettingsBrandingCustomHtmlResourceImpl implements SettingsBrandingCustomHtmlResource {

    private final ConfluenceSettingsService settingsService;

    @Inject
    public SettingsBrandingCustomHtmlResourceImpl(
            final ConfluenceSettingsService settingsService) {

        this.settingsService = settingsService;
    }

    @Override
    public Response getSettingsBrandingCustomHtml() {
        final SettingsBrandingCustomHtmlModel result = settingsService.getSettingsBrandingCustomHtml();
        return Response.ok(result).build();
    }

    @Override
    public Response setSettingsBrandingCustomHtml(
            final SettingsBrandingCustomHtmlModel bean) {

        final SettingsBrandingCustomHtmlModel result = settingsService.setSettingsBrandingCustomHtml(bean);
        return Response.ok(result).build();
    }
}
