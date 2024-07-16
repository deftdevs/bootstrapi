package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.crowd.model.AllBean;
import com.deftdevs.bootstrapi.crowd.rest.api.AllResource;
import com.deftdevs.bootstrapi.crowd.service.api.AllService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Component
@SystemAdminOnly
@Path(BootstrAPI.ALL)
public class AllResourceImpl implements AllResource {

    private final AllService allService;

    @Inject
    public AllResourceImpl(
            final AllService allService) {

        this.allService = allService;
    }

    @Override
    public Response setAll(
            final AllBean allBean) {

        allService.setAll(allBean);
        return Response.ok().build();
    }

}
