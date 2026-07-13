package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.jira.model.SettingsBrandingBannerModel;
import com.deftdevs.bootstrapi.jira.rest.api.SettingsBrandingBannerResource;
import com.deftdevs.bootstrapi.jira.service.api.JiraSettingsService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_BRANDING + "/" + BootstrAPI.SETTINGS_BRANDING_BANNER)
@SystemAdminOnly
public class SettingsBrandingBannerResourceImpl implements SettingsBrandingBannerResource {

    private final JiraSettingsService settingsService;

    @Inject
    public SettingsBrandingBannerResourceImpl(
            final JiraSettingsService settingsService) {

        this.settingsService = settingsService;
    }

    @Override
    public Response getSettingsBrandingBanner() {
        final SettingsBrandingBannerModel result = settingsService.getSettingsBrandingBanner();
        return Response.ok(result).build();
    }

    @Override
    public Response setSettingsBrandingBanner(
            final SettingsBrandingBannerModel settingsBannerModel) {

        final SettingsBrandingBannerModel result = settingsService.setSettingsBrandingBanner(settingsBannerModel);
        return Response.ok(result).build();
    }

}
