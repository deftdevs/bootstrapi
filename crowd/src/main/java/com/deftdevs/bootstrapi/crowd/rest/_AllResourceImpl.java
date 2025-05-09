package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest._AbstractAllResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api._AllService;
import com.deftdevs.bootstrapi.crowd.model._AllBean;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Tag(name = BootstrAPI._ALL)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SystemAdminOnly
@Component
public class _AllResourceImpl extends _AbstractAllResourceImpl<_AllBean> {

    @Inject
    public _AllResourceImpl(
            final _AllService<_AllBean> allService) {

        super(allService);
    }

}
