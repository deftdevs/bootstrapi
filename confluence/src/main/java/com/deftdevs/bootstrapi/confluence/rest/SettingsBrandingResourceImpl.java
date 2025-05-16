package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsBrandingResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.SettingsBrandingService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_BRANDING)
@SystemAdminOnly
@Component
public class SettingsBrandingResourceImpl extends AbstractSettingsBrandingResourceImpl {

    @Inject
    public SettingsBrandingResourceImpl(
            final SettingsBrandingService brandingService) {

        super(brandingService);
    }
}
