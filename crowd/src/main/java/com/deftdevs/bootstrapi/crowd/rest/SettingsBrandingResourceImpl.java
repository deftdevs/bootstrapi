package com.deftdevs.bootstrapi.crowd.rest;


import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingLoginPageModel;
import com.deftdevs.bootstrapi.crowd.rest.api.SettingsBrandingResource;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsBrandingService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@SystemAdminOnly
@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_BRANDING)
public class SettingsBrandingResourceImpl implements SettingsBrandingResource {

    private final CrowdSettingsBrandingService crowdSettingsBrandingService;

    @Inject
    public SettingsBrandingResourceImpl(
            final CrowdSettingsBrandingService crowdSettingsBrandingService) {

        this.crowdSettingsBrandingService = crowdSettingsBrandingService;
    }

    @Override
    public Response getLoginPage() {
        return Response.ok(crowdSettingsBrandingService.getLoginPage()).build();
    }

    @Override
    public Response setLoginPage(
            SettingsBrandingLoginPageModel settingsBrandingLoginPageModel) {

        return Response.ok(crowdSettingsBrandingService.setLoginPage(settingsBrandingLoginPageModel)).build();
    }

    @Override
    public Response setLogo(InputStream inputStream) {

        crowdSettingsBrandingService.setLogo(inputStream);
        return Response.ok().build();
    }
}
