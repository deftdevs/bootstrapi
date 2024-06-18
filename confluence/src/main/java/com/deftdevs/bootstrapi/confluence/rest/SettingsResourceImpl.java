package com.deftdevs.bootstrapi.confluence.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.SETTINGS)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class SettingsResourceImpl extends AbstractSettingsResourceImpl {

    @Inject
    public SettingsResourceImpl(SettingsService settingsService) {
        super(settingsService);
    }
}
