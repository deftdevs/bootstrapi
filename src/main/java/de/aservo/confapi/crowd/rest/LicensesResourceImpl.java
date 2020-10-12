package de.aservo.confapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.rest.AbstractLicensesResourceImpl;
import de.aservo.confapi.commons.service.api.LicensesService;
import de.aservo.confapi.crowd.filter.SysadminOnlyResourceFilter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

@Named
@Path(ConfAPI.LICENSES)
@ResourceFilters(SysadminOnlyResourceFilter.class)
public class LicensesResourceImpl extends AbstractLicensesResourceImpl {

    @Inject
    public LicensesResourceImpl(LicensesService licensesService) {
        super(licensesService);
    }
}
