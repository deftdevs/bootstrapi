package com.deftdevs.bootstrapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.crowd.filter.SysadminOnlyResourceFilter;
import com.deftdevs.bootstrapi.crowd.model.AllBean;
import com.deftdevs.bootstrapi.crowd.rest.api.AllResource;
import com.deftdevs.bootstrapi.crowd.service.api.AllService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Named
@Path(ConfAPI.ALL)
@ResourceFilters(SysadminOnlyResourceFilter.class)
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
