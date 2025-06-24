package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlModel;
import com.deftdevs.bootstrapi.confluence.rest.api.SettingsCustomHtmlResource;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;

import jakarta.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_CUSTOM_HTML)
@SystemAdminOnly
public class SettingsCustomHtmlResourceImpl implements SettingsCustomHtmlResource {

    private final ConfluenceSettingsService settingsService;

    @Inject
    public SettingsCustomHtmlResourceImpl(
            final ConfluenceSettingsService settingsService) {

        this.settingsService = settingsService;
    }

    @Override
    public Response getCustomHtml() {
        final SettingsCustomHtmlModel result = settingsService.getCustomHtml();
        return Response.ok(result).build();
    }

    @Override
    public Response setCustomHtml(
            final SettingsCustomHtmlModel bean) {

        final SettingsCustomHtmlModel result = settingsService.setCustomHtml(bean);
        return Response.ok(result).build();
    }
}
