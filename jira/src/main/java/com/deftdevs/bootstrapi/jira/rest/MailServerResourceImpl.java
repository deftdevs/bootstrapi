package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractMailServerResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;

import javax.ws.rs.Path;

@Path(BootstrAPI.MAIL_SERVER)
@ResourceFilters(SysadminOnlyResourceFilter.class)
public class MailServerResourceImpl extends AbstractMailServerResourceImpl {

    public MailServerResourceImpl(MailServerService mailServerService) {
        super(mailServerService);
    }

}
