package com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractGadgetResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.GADGET)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class GadgetResourceImpl extends AbstractGadgetResourceImpl {

    @Inject
    public GadgetResourceImpl(GadgetsService gadgetsService) {
        super(gadgetsService);
    }
}
