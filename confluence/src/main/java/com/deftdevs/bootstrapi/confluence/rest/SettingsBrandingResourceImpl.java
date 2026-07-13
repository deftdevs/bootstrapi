package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingColorSchemeModel;
import com.deftdevs.bootstrapi.confluence.rest.api.SettingsBrandingResource;
import com.deftdevs.bootstrapi.confluence.service.api.SettingsBrandingService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_BRANDING)
@SystemAdminOnly
public class SettingsBrandingResourceImpl implements SettingsBrandingResource {

    private final SettingsBrandingService brandingService;

    @Inject
    public SettingsBrandingResourceImpl(
            final SettingsBrandingService brandingService) {

        this.brandingService = brandingService;
    }

    @Override
    public Response getSettingsBrandingColorScheme() {
        final SettingsBrandingColorSchemeModel colourSchemeModel = brandingService.getSettingsBrandingColorScheme();
        return Response.ok(colourSchemeModel).build();
    }

    @Override
    public Response setSettingsBrandingColorScheme(SettingsBrandingColorSchemeModel bean) {
        final SettingsBrandingColorSchemeModel colourSchemeModel = brandingService.setSettingsBrandingColorScheme(bean);
        return Response.ok(colourSchemeModel).build();
    }

    @Override
    public Response getSettingsBrandingLogo() {
        InputStream logo = brandingService.getSettingsBrandingLogo();
        return Response.ok(logo).build();
    }

    @Override
    public Response setSettingsBrandingLogo(InputStream binaryInputStream) {
        brandingService.setSettingsBrandingLogo(binaryInputStream);
        return Response.ok().build();
    }

    @Override
    public Response getSettingsBrandingFavicon() {
        InputStream favicon = brandingService.getSettingsBrandingFavicon();
        return Response.ok(favicon).build();
    }

    @Override
    public Response setSettingsBrandingFavicon(InputStream binaryInputStream) {
        brandingService.setSettingsBrandingFavicon(binaryInputStream);
        return Response.ok().build();
    }

}
