package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicensesResourceImpl;
import com.deftdevs.bootstrapi.commons.rest.api.LicensesResource;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;

import javax.ws.rs.Path;

@Path(BootstrAPI.LICENSES)
@ResourceFilters(SysadminOnlyResourceFilter.class)
public class LicensesResourceImpl extends AbstractLicensesResourceImpl implements LicensesResource {

    public LicensesResourceImpl(
            final LicensesService licensesService) {

        super(licensesService);
    }

}
