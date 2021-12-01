package de.aservo.confapi.crowd.rest;


import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.crowd.filter.SysadminOnlyResourceFilter;
import de.aservo.confapi.crowd.model.SettingsBrandingLoginPageBean;
import de.aservo.confapi.crowd.rest.api.SettingsBrandingResource;
import de.aservo.confapi.crowd.service.api.SettingsBrandingService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path(ConfAPI.SETTINGS + "/" + ConfAPI.SETTINGS_BRANDING)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class SettingsBrandingResourceImpl implements SettingsBrandingResource {

    private final SettingsBrandingService settingsBrandingService;

    @Inject
    public SettingsBrandingResourceImpl(
            final SettingsBrandingService settingsBrandingService) {

        this.settingsBrandingService = settingsBrandingService;
    }

    @Override
    public Response getLoginPage() {
        return Response.ok(settingsBrandingService.getLoginPage()).build();
    }

    @Override
    public Response setLoginPage(
            SettingsBrandingLoginPageBean settingsBrandingLoginPageBean) {

        return Response.ok(settingsBrandingService.setLoginPage(settingsBrandingLoginPageBean)).build();
    }

    @Override
    public Response setLogo(InputStream inputStream) {

        settingsBrandingService.setLogo(inputStream);
        return Response.ok().build();
    }
}
