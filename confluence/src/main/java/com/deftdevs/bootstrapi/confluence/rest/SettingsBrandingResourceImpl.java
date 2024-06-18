package com.deftdevs.bootstrapi.confluence.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsBrandingResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.SettingsBrandingService;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.BRANDING)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class SettingsBrandingResourceImpl extends AbstractSettingsBrandingResourceImpl {

    @Inject
    public SettingsBrandingResourceImpl(SettingsBrandingService brandingService) {
        super(brandingService);
    }
}
