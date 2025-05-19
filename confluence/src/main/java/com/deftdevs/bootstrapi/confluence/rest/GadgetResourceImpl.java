package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractGadgetResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;

import javax.ws.rs.Path;

@Path(BootstrAPI.GADGET)
@SystemAdminOnly
public class GadgetResourceImpl extends AbstractGadgetResourceImpl {

    public GadgetResourceImpl(
            final GadgetsService gadgetsService) {

        super(gadgetsService);
    }
}
