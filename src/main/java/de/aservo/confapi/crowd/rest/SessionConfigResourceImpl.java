package de.aservo.confapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.crowd.filter.SysadminOnlyResourceFilter;
import de.aservo.confapi.crowd.model.SessionConfigBean;
import de.aservo.confapi.crowd.rest.api.SessionConfigResource;
import de.aservo.confapi.crowd.service.api.SessionConfigService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(SessionConfigResource.SESSION_CONFIG)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class SessionConfigResourceImpl implements SessionConfigResource {

    private final SessionConfigService sessionConfigService;

    @Inject
    public SessionConfigResourceImpl(
            final SessionConfigService sessionConfigService) {

        this.sessionConfigService = sessionConfigService;
    }

    @Override
    public Response getSessionConfig() {
        return Response.ok(sessionConfigService.getSessionConfig()).build();
    }

    @Override
    public Response setSessionConfig(
            final SessionConfigBean sessionConfigBean) {

        return Response.ok(sessionConfigService.setSessionConfig(sessionConfigBean)).build();
    }

}
