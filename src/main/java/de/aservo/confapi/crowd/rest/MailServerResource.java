package de.aservo.confapi.crowd.rest;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.manager.mail.MailConfigurationService;
import com.atlassian.crowd.util.mail.SMTPServer;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.ErrorCollection;
import de.aservo.confapi.commons.model.MailServerSmtpBean;
import de.aservo.confapi.crowd.filter.SysadminOnlyResourceFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path(ConfAPI.MAIL_SERVER)
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class MailServerResource {

    private static final Logger log = LoggerFactory.getLogger(MailServerResource.class);

    @ComponentImport
    private final MailConfigurationService mailConfigurationService;

    @Inject
    public MailServerResource(
            final MailConfigurationService mailConfigurationService) {

        this.mailConfigurationService = mailConfigurationService;
    }

    @GET
    @Path(ConfAPI.MAIL_SERVER_SMTP)
    @Operation(
            summary = "Retrieve the current SMTP mail server configuration",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MailServerSmtpBean.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    public Response getMailServerSmtp() {
        final ErrorCollection errorCollection = new ErrorCollection();

        try {
            final MailConfiguration mailConfiguration = mailConfigurationService.getMailConfiguration();
            final MailServerSmtpBean mailConfigurationBean = new MailServerSmtpBean(
                    mailConfiguration.getNotificationEmails().iterator().next(),
                    mailConfiguration.getSmtpServer().getFrom().toString(),
                    mailConfiguration.getSmtpServer().getPrefix(),
                    mailConfiguration.getSmtpServer().getHost()
            );
            return Response.ok(mailConfigurationBean).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }

        return Response.status(NOT_FOUND).entity(errorCollection).build();
    }

    @PUT
    @Path(ConfAPI.MAIL_SERVER_SMTP)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Update the SMTP mail server configuration",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MailServerSmtpBean.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    public Response putMailServerSmtp(
            final MailServerSmtpBean bean) {

        final ErrorCollection errorCollection = new ErrorCollection();

        try {
            final MailConfiguration mailConfiguration = mailConfigurationService.getMailConfiguration();

            final MailConfiguration newMailConfig = MailConfiguration.builder()
                    .setNotificationEmails(Collections.singletonList(bean.getAdminContact()))
                    .setSmtpServer(SMTPServer.builder()
                            .setFrom(new InternetAddress(bean.getFrom()))
                            .setPrefix(bean.getPrefix())
                            .setHost(bean.getHost())
                            .setPort(mailConfiguration.getSmtpServer().getPort())
                            .setUseSSL(mailConfiguration.getSmtpServer().getUseSSL())
                            .setJndiLocation(mailConfiguration.getSmtpServer().getJndiLocation())
                            .setJndiMailActive(mailConfiguration.getSmtpServer().isJndiMailActive())
                            .setUsername(mailConfiguration.getSmtpServer().getUsername())
                            .setPassword(mailConfiguration.getSmtpServer().getPassword())
                            .setTimeout(mailConfiguration.getSmtpServer().getTimeout())
                            .build())
                    .build();
            mailConfigurationService.saveConfiguration(newMailConfig);

            return getMailServerSmtp();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }

        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }

}
