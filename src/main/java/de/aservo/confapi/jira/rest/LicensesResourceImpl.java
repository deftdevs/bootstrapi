package de.aservo.confapi.jira.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.rest.AbstractLicensesResourceImpl;
import de.aservo.confapi.commons.service.api.LicensesService;
import de.aservo.confapi.jira.filter.SysadminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(ConfAPI.LICENSES)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class LicensesResourceImpl extends AbstractLicensesResourceImpl {

    @Inject
    public LicensesResourceImpl(LicensesService licensesService) {
        super(licensesService);
    }

}
