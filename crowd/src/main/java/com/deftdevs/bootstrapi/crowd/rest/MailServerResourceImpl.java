package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.rest.api.MailServerSmtpResource;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@SystemAdminOnly
@Path(BootstrAPI.MAIL_SERVER)
public class MailServerResourceImpl implements MailServerSmtpResource {

    private final MailServerService mailServerService;

    public MailServerResourceImpl(
            final MailServerService mailServerService) {

        this.mailServerService = mailServerService;
    }

    @Override
    public Response getMailServerSmtp() {
        final MailServerSmtpModel smtpModel = mailServerService.getMailServerSmtp();

        if (smtpModel == null) {
            return Response.noContent().build();
        }

        return Response.ok(smtpModel).build();
    }

    @Override
    public Response setMailServerSmtp(MailServerSmtpModel bean) {
        final MailServerSmtpModel updatedSmtpModel = mailServerService.setMailServerSmtp(bean);
        return Response.ok(updatedSmtpModel).build();
    }

}
