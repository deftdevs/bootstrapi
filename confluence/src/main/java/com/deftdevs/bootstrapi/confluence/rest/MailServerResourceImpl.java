package com.deftdevs.bootstrapi.confluence.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractMailServerResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.MAIL_SERVER)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class MailServerResourceImpl extends AbstractMailServerResourceImpl {

    @Inject
    public MailServerResourceImpl(MailServerService mailServerService) {
        super(mailServerService);
    }
}
