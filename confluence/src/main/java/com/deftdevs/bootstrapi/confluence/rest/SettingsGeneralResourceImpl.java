package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsGeneralResourceImpl;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_GENERAL)
@Tag(name = BootstrAPI.SETTINGS)
@Consumes({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
@Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
@SystemAdminOnly
public class SettingsGeneralResourceImpl extends AbstractSettingsGeneralResourceImpl<SettingsGeneralModel, ConfluenceSettingsService> {

    @Inject
    public SettingsGeneralResourceImpl(
            final ConfluenceSettingsService settingsService) {

        super(settingsService);
    }

}
