package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicensesResourceImpl;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.deftdevs.bootstrapi.jira.rest.api.JiraLicensesResource;
import com.deftdevs.bootstrapi.jira.service.api.JiraLicensesService;
import com.sun.jersey.spi.container.ResourceFilters;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path(BootstrAPI.LICENSES)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class LicensesResourceImpl extends AbstractLicensesResourceImpl implements JiraLicensesResource {

    private final JiraLicensesService licensesService;

    @Inject
    public LicensesResourceImpl(
            final JiraLicensesService licensesService) {

        super(licensesService);
        this.licensesService = licensesService;
    }

    @Override
    public Response setLicenses(
            final Collection<LicenseBean> licenseBeans) {

        return Response.ok(licensesService.setLicenses(licenseBeans)).build();
    }

}
