package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingColorSchemeModel;
import com.deftdevs.bootstrapi.confluence.rest.api.SettingsBrandingResource;
import com.deftdevs.bootstrapi.confluence.service.api.SettingsBrandingService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
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
    public Response getBrandingColorScheme() {
        final SettingsBrandingColorSchemeModel colourSchemeModel = brandingService.getColourScheme();
        return Response.ok(colourSchemeModel).build();
    }

    @Override
    public Response setBrandingColorScheme(SettingsBrandingColorSchemeModel bean) {
        final SettingsBrandingColorSchemeModel colourSchemeModel = brandingService.setColourScheme(bean);
        return Response.ok(colourSchemeModel).build();
    }

    @Override
    public Response getBrandingLogo() {
        InputStream logo = brandingService.getLogo();
        return Response.ok(logo).build();
    }

    @Override
    public Response setBrandingLogo(InputStream binaryInputStream) {
        brandingService.setLogo(binaryInputStream);
        return Response.ok().build();
    }

    @Override
    public Response getBrandingFavicon() {
        InputStream favicon = brandingService.getFavicon();
        return Response.ok(favicon).build();
    }

    @Override
    public Response setBrandingFavicon(InputStream binaryInputStream) {
        brandingService.setFavicon(binaryInputStream);
        return Response.ok().build();
    }

}
