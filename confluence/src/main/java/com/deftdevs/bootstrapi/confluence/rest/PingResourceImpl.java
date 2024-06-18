package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.UnrestrictedAccess;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractPingResourceImpl;

import javax.ws.rs.Path;

@Path(ConfAPI.PING)
@UnrestrictedAccess
public class PingResourceImpl extends AbstractPingResourceImpl {

    // Completely inhering the implementation of AbstractPingResourceImpl

}
