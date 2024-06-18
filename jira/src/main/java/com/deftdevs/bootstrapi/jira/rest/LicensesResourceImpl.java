package com.deftdevs.bootstrapi.jira.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicensesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
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
