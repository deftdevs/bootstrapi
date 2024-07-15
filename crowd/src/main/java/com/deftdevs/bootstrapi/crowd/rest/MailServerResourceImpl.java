package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpBean;
import com.deftdevs.bootstrapi.commons.rest.api.MailServerSmtpResource;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Component
@SystemAdminOnly
@Path(BootstrAPI.MAIL_SERVER)
public class MailServerResourceImpl implements MailServerSmtpResource {

    private final MailServerService mailServerService;

    @Inject
    public MailServerResourceImpl(
            final MailServerService mailServerService) {

        this.mailServerService = mailServerService;
    }

    @Override
    public Response getMailServerSmtp() {
        final MailServerSmtpBean smtpBean = mailServerService.getMailServerSmtp();

        if (smtpBean == null) {
            return Response.noContent().build();
        }

        return Response.ok(smtpBean).build();
    }

    @Override
    public Response setMailServerSmtp(MailServerSmtpBean bean) {
        final MailServerSmtpBean updatedSmtpBean = mailServerService.setMailServerSmtp(bean);
        return Response.ok(updatedSmtpBean).build();
    }

}
