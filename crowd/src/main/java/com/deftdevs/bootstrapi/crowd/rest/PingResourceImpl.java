package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractPingResourceImpl;

import javax.ws.rs.Path;

@Path(BootstrAPI.PING)
@AnonymousAllowed
public class PingResourceImpl extends AbstractPingResourceImpl {

    // Completely inhering the implementation of AbstractPingResourceImpl

}
