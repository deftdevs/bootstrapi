package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsBrandingResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.SettingsBrandingService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;

import javax.ws.rs.Path;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_BRANDING)
@ResourceFilters(SysadminOnlyResourceFilter.class)
public class SettingsBrandingResourceImpl extends AbstractSettingsBrandingResourceImpl {

    public SettingsBrandingResourceImpl(
            final SettingsBrandingService brandingService) {

        super(brandingService);
    }
}
