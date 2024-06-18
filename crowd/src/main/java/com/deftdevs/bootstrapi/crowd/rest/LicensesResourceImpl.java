package com.deftdevs.bootstrapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicensesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.crowd.filter.SysadminOnlyResourceFilter;

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
