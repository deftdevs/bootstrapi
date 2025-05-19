package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicenseResourceImpl;
import com.deftdevs.bootstrapi.commons.rest.api.LicenseResource;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;

import javax.ws.rs.Path;

@Path(BootstrAPI.LICENSE)
@ResourceFilters(SysadminOnlyResourceFilter.class)
public class LicenseResourceImpl extends AbstractLicenseResourceImpl implements LicenseResource {

    public LicenseResourceImpl(
            final LicensesService licensesService) {

        super(licensesService);
    }

}
