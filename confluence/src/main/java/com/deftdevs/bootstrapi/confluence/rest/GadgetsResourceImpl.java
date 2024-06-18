package com.deftdevs.bootstrapi.confluence.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractGadgetsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(ConfAPI.GADGETS)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class GadgetsResourceImpl extends AbstractGadgetsResourceImpl {

    @Inject
    public GadgetsResourceImpl(GadgetsService gadgetsService) {
        super(gadgetsService);
    }
}
