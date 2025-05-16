package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.deftdevs.bootstrapi.jira.model.SettingsBannerModel;
import com.deftdevs.bootstrapi.jira.rest.api.SettingsBannerResource;
import com.deftdevs.bootstrapi.jira.service.api.JiraSettingsService;
import com.sun.jersey.spi.container.ResourceFilters;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_BANNER)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class SettingsBannerResourceImpl implements SettingsBannerResource {

    private final JiraSettingsService settingsService;

    public SettingsBannerResourceImpl(
            final JiraSettingsService settingsService) {

        this.settingsService = settingsService;
    }

    @Override
    public Response getBanner() {
        final SettingsBannerModel result = settingsService.getSettingsBanner();
        return Response.ok(result).build();
    }

    @Override
    public Response setBanner(
            final SettingsBannerModel settingsBannerModel) {

        final SettingsBannerModel result = settingsService.setSettingsBanner(settingsBannerModel);
        return Response.ok(result).build();
    }

}
