package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsSecurityResourceImpl;
import com.deftdevs.bootstrapi.jira.service.api.JiraSettingsService;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path(BootstrAPI.SETTINGS + '/' + BootstrAPI.SETTINGS_SECURITY)
@Tag(name = BootstrAPI.SETTINGS)
@Consumes({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
@Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
@SystemAdminOnly
public class SettingsSecurityResourceImpl extends AbstractSettingsSecurityResourceImpl<SettingsSecurityModel, JiraSettingsService> {

    @Inject
    public SettingsSecurityResourceImpl(
            final JiraSettingsService settingsService) {

        super(settingsService);
    }

}
