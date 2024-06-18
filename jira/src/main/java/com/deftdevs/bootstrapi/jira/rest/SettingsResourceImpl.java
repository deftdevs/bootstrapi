package com.deftdevs.bootstrapi.jira.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(ConfAPI.SETTINGS)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class SettingsResourceImpl extends AbstractSettingsResourceImpl {

    @Inject
    public SettingsResourceImpl(SettingsService settingsService) {
        super(settingsService);
    }

}
