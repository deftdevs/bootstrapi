package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractGadgetsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;

import javax.ws.rs.Path;

@Path(BootstrAPI.GADGETS)
@SystemAdminOnly
public class GadgetsResourceImpl extends AbstractGadgetsResourceImpl {

    public GadgetsResourceImpl(
            final GadgetsService gadgetsService) {

        super(gadgetsService);
    }
}
