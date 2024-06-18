package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractPingResourceImpl;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Path(BootstrAPI.PING)
@AnonymousAllowed
@Component
public class PingResourceImpl extends AbstractPingResourceImpl {

    // Completely inhering the implementation of AbstractPingResourceImpl

}
