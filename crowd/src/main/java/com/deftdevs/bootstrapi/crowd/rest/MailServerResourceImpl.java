package com.deftdevs.bootstrapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpBean;
import com.deftdevs.bootstrapi.commons.rest.api.MailServerSmtpResource;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import com.deftdevs.bootstrapi.crowd.filter.SysadminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(BootstrAPI.MAIL_SERVER)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
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
