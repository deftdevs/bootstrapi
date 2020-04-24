package de.aservo.atlassian.confapi.model;

import com.atlassian.mail.server.PopMailServer;
import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.exception.NoContentException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = ConfAPI.MAIL_SERVER + "-" + ConfAPI.MAIL_SERVER_POP)
public class MailServerPopBean extends AbstractMailServerProtocolBean {

    public static MailServerPopBean from(
            final PopMailServer popMailServer) throws NoContentException {

        if (popMailServer == null) {
            throw new NoContentException("No POP mail server defined");
        }

        final MailServerPopBean mailServerPopBean = new MailServerPopBean();
        mailServerPopBean.setName(popMailServer.getName());
        mailServerPopBean.setDescription(popMailServer.getDescription());
        mailServerPopBean.setProtocol(popMailServer.getMailProtocol().getProtocol());
        mailServerPopBean.setHost(popMailServer.getHostname());
        mailServerPopBean.setPort(popMailServer.getPort());
        mailServerPopBean.setTimeout(popMailServer.getTimeout());
        mailServerPopBean.setUsername(popMailServer.getUsername());
        return mailServerPopBean;
    }

}
