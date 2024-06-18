package com.deftdevs.bootstrapi.crowd.rest;


import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.crowd.filter.SysadminOnlyResourceFilter;
import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingLoginPageBean;
import com.deftdevs.bootstrapi.crowd.rest.api.SettingsBrandingResource;
import com.deftdevs.bootstrapi.crowd.service.api.SettingsBrandingService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_BRANDING)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class SettingsBrandingResourceImpl implements SettingsBrandingResource {

    private final SettingsBrandingService settingsBrandingService;

    @Inject
    public SettingsBrandingResourceImpl(
            final SettingsBrandingService settingsBrandingService) {

        this.settingsBrandingService = settingsBrandingService;
    }

    @Override
    public Response getLoginPage() {
        return Response.ok(settingsBrandingService.getLoginPage()).build();
    }

    @Override
    public Response setLoginPage(
            SettingsBrandingLoginPageBean settingsBrandingLoginPageBean) {

        return Response.ok(settingsBrandingService.setLoginPage(settingsBrandingLoginPageBean)).build();
    }

    @Override
    public Response setLogo(InputStream inputStream) {

        settingsBrandingService.setLogo(inputStream);
        return Response.ok().build();
    }
}
