package de.aservo.atlassian.confapi.model;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = ConfAPI.MAIL_SERVER + "-" + ConfAPI.MAIL_SERVER_POP)
public class MailServerPopBean extends AbstractMailServerProtocolBean {

    // Example instances for documentation and tests

    public static final MailServerPopBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new MailServerPopBean();
        EXAMPLE_1.setName("Example");
        EXAMPLE_1.setHost("mail.example.com");
    }

}
