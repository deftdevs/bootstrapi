package de.aservo.confapi.confluence.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.rest.AbstractAuthenticationResourceImpl;
import de.aservo.confapi.commons.service.api.AuthenticationService;
import de.aservo.confapi.confluence.filter.SysAdminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(ConfAPI.AUTHENTICATION)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class AuthenticationResourceImpl extends AbstractAuthenticationResourceImpl {

    @Inject
    public AuthenticationResourceImpl(AuthenticationService authenticationService) {
        super(authenticationService);
    }

}
