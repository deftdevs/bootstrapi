package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeBean;
import com.deftdevs.bootstrapi.commons.rest.api.SettingsBrandingResource;
import com.deftdevs.bootstrapi.commons.service.api.SettingsBrandingService;

import javax.ws.rs.core.Response;
import java.io.InputStream;

public abstract class AbstractSettingsBrandingResourceImpl implements SettingsBrandingResource {

    private final SettingsBrandingService brandingService;

    public AbstractSettingsBrandingResourceImpl(final SettingsBrandingService brandingService) {
        this.brandingService = brandingService;
    }

    @Override
    public Response getBrandingColorScheme() {
        final SettingsBrandingColorSchemeBean colourSchemeBean = brandingService.getColourScheme();
        return Response.ok(colourSchemeBean).build();
    }

    @Override
    public Response setBrandingColorScheme(SettingsBrandingColorSchemeBean bean) {
        final SettingsBrandingColorSchemeBean colourSchemeBean = brandingService.setColourScheme(bean);
        return Response.ok(colourSchemeBean).build();
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
