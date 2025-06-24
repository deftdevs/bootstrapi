package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractMailServerResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;

import jakarta.inject.Inject;
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
