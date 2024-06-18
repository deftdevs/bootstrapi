package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractPingResourceImpl;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Path(ConfAPI.PING)
@AnonymousAllowed
@Component
public class PingResourceImpl extends AbstractPingResourceImpl {

    // Completely inhering the implementation of AbstractPingResourceImpl

}
