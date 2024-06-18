package com.deftdevs.bootstrapi.confluence.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicensesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(ConfAPI.LICENSES)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class LicencesResourceImpl extends AbstractLicensesResourceImpl {

    @Inject
    public LicencesResourceImpl(LicensesService licensesService) {
        super(licensesService);
    }

    // Completely inhering the implementation of AbstractLicensesResourceImpl
}
