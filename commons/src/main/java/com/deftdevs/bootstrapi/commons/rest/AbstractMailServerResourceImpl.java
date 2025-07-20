package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.rest.api.MailServerResource;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;

import javax.ws.rs.core.Response;

public class AbstractMailServerResourceImpl implements MailServerResource {

    private final MailServerService mailServerService;

    public AbstractMailServerResourceImpl(
            final MailServerService mailServerService) {

        this.mailServerService = mailServerService;
    }

    @Override
    public Response getMailServerSmtp() {
        final MailServerSmtpModel smtpModel = mailServerService.getMailServerSmtp();
        return Response.ok(smtpModel).build();
    }

    @Override
    public Response setMailServerSmtp(
            final MailServerSmtpModel bean) {

        final MailServerSmtpModel updatedSmtpModel = mailServerService.setMailServerSmtp(bean);
        return Response.ok(updatedSmtpModel).build();
    }

    @Override
    public Response getMailServerPop() {
        final MailServerPopModel popModel = mailServerService.getMailServerPop();

        if (popModel == null) {
            return Response.noContent().build();
        }

        return Response.ok(popModel).build();
    }

    @Override
    public Response setMailServerPop(
            final MailServerPopModel bean) {

        final MailServerPopModel updatedPopModel = mailServerService.setMailServerPop(bean);
        return Response.ok(updatedPopModel).build();
    }
}
