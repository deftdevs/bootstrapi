package de.aservo.confapi.jira.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.rest.AbstractDirectoriesResourceImpl;
import de.aservo.confapi.commons.service.api.DirectoriesService;
import de.aservo.confapi.jira.filter.SysadminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(ConfAPI.DIRECTORIES)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class DirectoriesResourceImpl extends AbstractDirectoriesResourceImpl {

    @Inject
    public DirectoriesResourceImpl(DirectoriesService directoryService) {
        super(directoryService);
    }

}
