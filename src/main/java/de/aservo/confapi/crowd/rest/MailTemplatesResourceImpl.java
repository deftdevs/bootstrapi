package de.aservo.confapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.crowd.filter.SysadminOnlyResourceFilter;
import de.aservo.confapi.crowd.model.MailTemplatesBean;
import de.aservo.confapi.crowd.rest.api.MailTemplateResource;
import de.aservo.confapi.crowd.service.api.MailTemplatesService;
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
