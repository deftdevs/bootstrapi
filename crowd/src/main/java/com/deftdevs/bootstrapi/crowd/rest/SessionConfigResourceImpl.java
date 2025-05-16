package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.crowd.model.SessionConfigModel;
import com.deftdevs.bootstrapi.crowd.rest.api.SessionConfigResource;
import com.deftdevs.bootstrapi.crowd.service.api.SessionConfigService;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@SystemAdminOnly
@Path(SessionConfigResource.SESSION_CONFIG)
public class SessionConfigResourceImpl implements SessionConfigResource {

    private final SessionConfigService sessionConfigService;

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
            final SessionConfigModel sessionConfigModel) {

        return Response.ok(sessionConfigService.setSessionConfig(sessionConfigModel)).build();
    }

}
