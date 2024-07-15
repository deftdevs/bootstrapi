package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.UnrestrictedAccess;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractPingResourceImpl;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@UnrestrictedAccess
@Path(BootstrAPI.PING)
public class PingResourceImpl extends AbstractPingResourceImpl {

    // Completely inhering the implementation of AbstractPingResourceImpl

}
