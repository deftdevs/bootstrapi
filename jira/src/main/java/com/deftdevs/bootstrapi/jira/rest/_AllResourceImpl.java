package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest._AbstractAllResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api._AllService;
import com.deftdevs.bootstrapi.jira.model._AllModel;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(BootstrAPI._ROOT)
@Tag(name = BootstrAPI._ALL)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SystemAdminOnly
public class _AllResourceImpl extends _AbstractAllResourceImpl<_AllModel> {

    public _AllResourceImpl(
            final _AllService<_AllModel> allService) {

        super(allService);
    }

}
