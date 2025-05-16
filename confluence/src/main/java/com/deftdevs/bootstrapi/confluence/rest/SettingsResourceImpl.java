package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceImpl;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;
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
@SystemAdminOnly
@Component
public class SettingsResourceImpl extends AbstractSettingsResourceImpl<SettingsModel, ConfluenceSettingsService> {

    @Inject
    public SettingsResourceImpl(
            final ConfluenceSettingsService settingsService) {

        super(settingsService);
    }

}
