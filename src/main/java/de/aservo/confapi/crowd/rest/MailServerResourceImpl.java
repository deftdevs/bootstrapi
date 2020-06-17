package de.aservo.confapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.MailServerSmtpBean;
import de.aservo.confapi.commons.rest.api.MailServerSmtpResource;
import de.aservo.confapi.commons.service.api.MailServerService;
import de.aservo.confapi.crowd.filter.SysadminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(ConfAPI.MAIL_SERVER)
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
        return Response.ok(smtpBean).build();
    }

    @Override
    public Response setMailServerSmtp(MailServerSmtpBean bean) {
        final MailServerSmtpBean updatedSmtpBean = mailServerService.setMailServerSmtp(bean);
        return Response.ok(updatedSmtpBean).build();
    }

}
