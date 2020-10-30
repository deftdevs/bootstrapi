package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.SettingsBrandingColourSchemeBean;
import de.aservo.confapi.commons.rest.api.SettingsBrandingResource;
import de.aservo.confapi.commons.service.api.BrandingService;

import javax.ws.rs.core.Response;
import java.io.InputStream;

public abstract class AbstractSettingsBrandingResourceImpl implements SettingsBrandingResource {

    private final BrandingService brandingService;

    public AbstractSettingsBrandingResourceImpl(final BrandingService brandingService) {
        this.brandingService = brandingService;
    }

    @Override
    public Response getColourScheme() {
        final SettingsBrandingColourSchemeBean colourSchemeBean = brandingService.getColourScheme();
        return Response.ok(colourSchemeBean).build();
    }

    @Override
    public Response setColourScheme(SettingsBrandingColourSchemeBean bean) {
        final SettingsBrandingColourSchemeBean colourSchemeBean = brandingService.setColourScheme(bean);
        return Response.ok(colourSchemeBean).build();
    }

    @Override
    public Response getLogo() {
        InputStream logo = brandingService.getLogo();
        return Response.ok(logo).build();
    }

    @Override
    public Response setLogo(InputStream binaryInputStream) {
        brandingService.setLogo(binaryInputStream);
        return Response.ok().build();
    }

    @Override
    public Response getFavicon() {
        InputStream favicon = brandingService.getFavicon();
        return Response.ok(favicon).build();
    }

    @Override
    public Response setFavicon(InputStream binaryInputStream) {
        brandingService.setFavicon(binaryInputStream);
        return Response.ok().build();
    }
}
