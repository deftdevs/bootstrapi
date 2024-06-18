package com.deftdevs.bootstrapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.crowd.filter.SysadminOnlyResourceFilter;
import com.deftdevs.bootstrapi.crowd.model.MailTemplatesBean;
import com.deftdevs.bootstrapi.crowd.rest.api.MailTemplateResource;
import com.deftdevs.bootstrapi.crowd.service.api.MailTemplatesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(MailTemplateResource.MAIL_TEMPLATES)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
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
            final MailTemplatesBean mailTemplatesBean) {

        return Response.ok(mailTemplatesService.setMailTemplates(mailTemplatesBean)).build();
    }
}
