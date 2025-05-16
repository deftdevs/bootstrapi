package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractGadgetsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.GADGETS)
@SystemAdminOnly
@Component
public class GadgetsResourceImpl extends AbstractGadgetsResourceImpl {

    @Inject
    public GadgetsResourceImpl(
            final GadgetsService gadgetsService) {

        super(gadgetsService);
    }
}
