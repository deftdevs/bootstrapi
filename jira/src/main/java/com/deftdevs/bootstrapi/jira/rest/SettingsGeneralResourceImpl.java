package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsGeneralResourceImpl;
import com.deftdevs.bootstrapi.jira.service.api.JiraSettingsService;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(BootstrAPI.SETTINGS)
@Tag(name = BootstrAPI.SETTINGS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SystemAdminOnly
public class SettingsGeneralResourceImpl extends AbstractSettingsGeneralResourceImpl<SettingsGeneralModel, JiraSettingsService> {

    @Inject
    public SettingsGeneralResourceImpl(
            final JiraSettingsService settingsService) {

        super(settingsService);
    }

}
