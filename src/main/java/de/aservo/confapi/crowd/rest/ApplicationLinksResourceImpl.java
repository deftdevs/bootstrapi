package de.aservo.confapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.rest.AbstractApplicationLinksResourceImpl;
import de.aservo.confapi.commons.service.api.ApplicationLinksService;
import de.aservo.confapi.crowd.filter.SysadminOnlyResourceFilter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

@Named
@Path(ConfAPI.APPLICATION_LINKS)
@ResourceFilters(SysadminOnlyResourceFilter.class)
public class ApplicationLinksResourceImpl extends AbstractApplicationLinksResourceImpl {

    @Inject
    public ApplicationLinksResourceImpl(ApplicationLinksService applicationLinkService) {
        super(applicationLinkService);
    }
}
