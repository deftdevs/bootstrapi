package com.deftdevs.bootstrapi.jira.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsBrandingResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.SettingsBrandingService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.BRANDING)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class SettingsBrandingResourceImpl extends AbstractSettingsBrandingResourceImpl {

    @Inject
    public SettingsBrandingResourceImpl(SettingsBrandingService brandingService) {
        super(brandingService);
    }
}
