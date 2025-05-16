package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.UnrestrictedAccess;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractPingResourceImpl;

import javax.ws.rs.Path;

@UnrestrictedAccess
@Path(BootstrAPI.PING)
public class PingResourceImpl extends AbstractPingResourceImpl {

    // Completely inhering the implementation of AbstractPingResourceImpl

}
