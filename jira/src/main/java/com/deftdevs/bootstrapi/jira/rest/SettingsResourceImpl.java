package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceImpl;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.deftdevs.bootstrapi.jira.service.api.JiraSettingsService;
import com.sun.jersey.spi.container.ResourceFilters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(BootstrAPI.SETTINGS)
@Tag(name = BootstrAPI.SETTINGS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class SettingsResourceImpl extends AbstractSettingsResourceImpl<SettingsModel, JiraSettingsService> {

    @Inject
    public SettingsResourceImpl(JiraSettingsService settingsService) {
        super(settingsService);
    }

}
