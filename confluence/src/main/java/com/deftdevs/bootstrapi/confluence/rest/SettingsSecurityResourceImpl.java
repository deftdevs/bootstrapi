package com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlBean;
import com.deftdevs.bootstrapi.confluence.model.SettingsSecurityBean;
import com.deftdevs.bootstrapi.confluence.rest.api.SettingsSecurityResource;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;
import com.sun.jersey.spi.container.ResourceFilters;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_SECURITY)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class SettingsSecurityResourceImpl implements SettingsSecurityResource {

    private final ConfluenceSettingsService settingsService;

    @Inject
    public SettingsSecurityResourceImpl(
            final ConfluenceSettingsService settingsService) {

        this.settingsService = settingsService;
    }

    @Override
    public Response getSecurity() {
        final SettingsSecurityBean result = settingsService.getSecurity();
        return Response.ok(result).build();
    }

    @Override
    public Response setSecurity(
            final SettingsSecurityBean bean) {

        final SettingsSecurityBean result = settingsService.setSecurity(bean);
        return Response.ok(result).build();
    }
}
