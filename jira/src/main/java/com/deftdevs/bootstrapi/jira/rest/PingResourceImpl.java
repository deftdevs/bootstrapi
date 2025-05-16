package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.common.security.UnrestrictedAccess;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractPingResourceImpl;

import javax.ws.rs.Path;

@Path(BootstrAPI.PING)
@UnrestrictedAccess
public class PingResourceImpl extends AbstractPingResourceImpl {

    // Completely inhering the implementation of AbstractPingResourceImpl

}
