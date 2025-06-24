package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractMailServerResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.MAIL_SERVER)
@SystemAdminOnly
public class MailServerResourceImpl extends AbstractMailServerResourceImpl {

    @Inject
    public MailServerResourceImpl(
            final MailServerService mailServerService) {

        super(mailServerService);
    }
}
