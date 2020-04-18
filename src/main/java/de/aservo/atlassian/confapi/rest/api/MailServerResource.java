package de.aservo.atlassian.confapi.rest.api;

import de.aservo.atlassian.confapi.constants.ConfAPI;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(ConfAPI.MAIL_SERVER)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MailServerResource extends MailServerPopResource, MailServerSmtpResource {

}
