package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractGadgetResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.GADGET)
@SystemAdminOnly
@Component
public class GadgetResourceImpl extends AbstractGadgetResourceImpl {

    @Inject
    public GadgetResourceImpl(
            final GadgetsService gadgetsService) {

        super(gadgetsService);
    }
}
