package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Component
@SystemAdminOnly
@Path(BootstrAPI.SETTINGS)
public class SettingsResourceImpl extends AbstractSettingsResourceImpl {

    @Inject
    public SettingsResourceImpl(SettingsService settingsService) {
        super(settingsService);
    }

}
