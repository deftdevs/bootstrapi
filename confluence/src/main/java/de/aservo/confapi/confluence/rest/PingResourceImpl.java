package de.aservo.confapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.UnrestrictedAccess;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.rest.AbstractPingResourceImpl;

import javax.ws.rs.Path;

@Path(ConfAPI.PING)
@UnrestrictedAccess
public class PingResourceImpl extends AbstractPingResourceImpl {

    // Completely inhering the implementation of AbstractPingResourceImpl

}
