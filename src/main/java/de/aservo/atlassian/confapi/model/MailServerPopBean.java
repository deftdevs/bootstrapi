package de.aservo.atlassian.confapi.model;

import com.atlassian.mail.server.PopMailServer;
import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.exception.NoContentException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.MAIL_SERVER_POP)
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

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

}
