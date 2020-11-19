package de.aservo.confapi.jira.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.rest.AbstractSettingsBrandingResourceImpl;
import de.aservo.confapi.commons.service.api.SettingsBrandingService;
import de.aservo.confapi.jira.filter.SysadminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(ConfAPI.SETTINGS + "/" + ConfAPI.BRANDING)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class SettingsBrandingResourceImpl extends AbstractSettingsBrandingResourceImpl {

    @Inject
    public SettingsBrandingResourceImpl(SettingsBrandingService brandingService) {
        super(brandingService);
    }
}
