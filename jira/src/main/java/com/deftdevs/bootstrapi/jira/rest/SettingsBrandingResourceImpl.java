package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsBrandingResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.SettingsBrandingService;

import javax.ws.rs.Path;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_BRANDING)
@SystemAdminOnly
public class SettingsBrandingResourceImpl extends AbstractSettingsBrandingResourceImpl {

    public SettingsBrandingResourceImpl(
            final SettingsBrandingService brandingService) {

        super(brandingService);
    }
}
