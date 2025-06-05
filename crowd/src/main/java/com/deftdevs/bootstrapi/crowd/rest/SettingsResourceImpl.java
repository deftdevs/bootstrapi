package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceImpl;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsGeneralService;
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
public class SettingsResourceImpl extends AbstractSettingsResourceImpl<SettingsModel, CrowdSettingsGeneralService> {

    @Inject
    public SettingsResourceImpl(
            final CrowdSettingsGeneralService settingsService) {

        super(settingsService);
    }

}
