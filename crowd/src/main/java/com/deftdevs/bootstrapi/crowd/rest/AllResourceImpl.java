package de.aservo.confapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.crowd.filter.SysadminOnlyResourceFilter;
import de.aservo.confapi.crowd.model.AllBean;
import de.aservo.confapi.crowd.rest.api.AllResource;
import de.aservo.confapi.crowd.service.api.AllService;

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
