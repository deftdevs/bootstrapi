package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;

import javax.inject.Inject;
import javax.ws.rs.Path;

@SystemAdminOnly
@Path(BootstrAPI.SETTINGS)
public class SettingsResourceImpl extends AbstractSettingsResourceImpl {

    @Inject
    public SettingsResourceImpl(SettingsService settingsService) {
        super(settingsService);
    }

}
