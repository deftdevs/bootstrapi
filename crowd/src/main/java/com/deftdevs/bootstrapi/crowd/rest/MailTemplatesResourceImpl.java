package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.crowd.model.MailTemplatesModel;
import com.deftdevs.bootstrapi.crowd.rest.api.MailTemplateResource;
import com.deftdevs.bootstrapi.crowd.service.api.MailTemplatesService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@SystemAdminOnly
@Path(MailTemplateResource.MAIL_TEMPLATES)
public class MailTemplatesResourceImpl implements MailTemplateResource {

    private final MailTemplatesService mailTemplatesService;

    @Inject
    public MailTemplatesResourceImpl(
            final MailTemplatesService mailTemplatesService) {

        this.mailTemplatesService = mailTemplatesService;
    }

    @Override
    public Response getMailTemplates() {
        return Response.ok(mailTemplatesService.getMailTemplates()).build();
    }

    @Override
    public Response setMailTemplates(
            final MailTemplatesModel mailTemplatesModel) {

        return Response.ok(mailTemplatesService.setMailTemplates(mailTemplatesModel)).build();
    }
}
