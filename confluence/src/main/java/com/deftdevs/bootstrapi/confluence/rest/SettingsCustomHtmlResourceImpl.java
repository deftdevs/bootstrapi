package com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlBean;
import com.deftdevs.bootstrapi.confluence.rest.api.SettingsCustomHtmlResource;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;
import com.sun.jersey.spi.container.ResourceFilters;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_CUSTOM_HTML)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class SettingsCustomHtmlResourceImpl implements SettingsCustomHtmlResource {

    private final ConfluenceSettingsService settingsService;

    @Inject
    public SettingsCustomHtmlResourceImpl(
            final ConfluenceSettingsService settingsService) {

        this.settingsService = settingsService;
    }

    @Override
    public Response getCustomHtml() {
        final SettingsCustomHtmlBean result = settingsService.getCustomHtml();
        return Response.ok(result).build();
    }

    @Override
    public Response setCustomHtml(
            final SettingsCustomHtmlBean bean) {

        final SettingsCustomHtmlBean result = settingsService.setCustomHtml(bean);
        return Response.ok(result).build();
    }
}
