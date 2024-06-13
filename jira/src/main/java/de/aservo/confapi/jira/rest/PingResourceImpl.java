package de.aservo.confapi.jira.rest;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.rest.AbstractPingResourceImpl;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Path(ConfAPI.PING)
@AnonymousAllowed
@Component
public class PingResourceImpl extends AbstractPingResourceImpl {

    // Completely inhering the implementation of AbstractPingResourceImpl

}
