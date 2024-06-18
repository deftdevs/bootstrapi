package atlassian.mail.server;

import com.atlassian.mail.MailConstants;
import com.atlassian.mail.MailProtocol;
import com.atlassian.mail.server.SMTPMailServer;

public interface DefaultTestSmtpMailServer extends DefaultTestMailServer, SMTPMailServer {

    String FROM = "mail@deftdevs.com";
    String PREFIX = "[Deft Devs]";
    boolean TLS_REQUIRED = false;
    MailProtocol PROTOCOL = MailConstants.DEFAULT_SMTP_PROTOCOL;
    String PORT = PROTOCOL.getDefaultPort();

}
