package de.aservo.atlassian.confapi.model;

import com.atlassian.mail.server.PopMailServer;
import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = ConfAPI.MAIL_SERVER + "-" + ConfAPI.MAIL_SERVER_POP)
public class MailServerPopBean extends AbstractMailServerProtocolBean {

    @Nullable
    public static MailServerPopBean from(
            @Nullable final PopMailServer popMailServer) {

        if (popMailServer == null) {
            return null;
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

    // Example instances for documentation and tests

    public static final MailServerPopBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new MailServerPopBean();
        EXAMPLE_1.setName("Example");
        EXAMPLE_1.setHost("mail.example.com");
    }

}
