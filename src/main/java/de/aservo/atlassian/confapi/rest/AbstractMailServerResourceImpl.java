package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.model.MailServerPopBean;
import de.aservo.atlassian.confapi.model.MailServerSmtpBean;
import de.aservo.atlassian.confapi.rest.api.MailServerResource;
import de.aservo.atlassian.confapi.service.api.MailServerService;

import javax.ws.rs.core.Response;

public class AbstractMailServerResourceImpl implements MailServerResource {

    private final MailServerService mailServerService;

    public AbstractMailServerResourceImpl(
            final MailServerService mailServerService) {
        this.mailServerService = mailServerService;
    }

    @Override
    public Response getMailServerSmtp() {
        final MailServerSmtpBean smtpBean = mailServerService.getMailServerSmtp();
        return Response.ok(smtpBean).build();
    }

    @Override
    public Response setMailServerSmtp(MailServerSmtpBean bean) {
        final MailServerSmtpBean updatedSmtpBean = mailServerService.setMailServerSmtp(bean);
        return Response.ok(updatedSmtpBean).build();
    }

    @Override
    public Response getMailServerPop() {
        final MailServerPopBean popBean = mailServerService.getMailServerPop();
        return Response.ok(popBean).build();
    }

    @Override
    public Response setMailServerPop(MailServerPopBean bean) {
        final MailServerPopBean updatedPopBean = mailServerService.setMailServerPop(bean);
        return Response.ok(updatedPopBean).build();
    }
}
