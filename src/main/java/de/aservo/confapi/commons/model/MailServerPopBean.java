package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
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
    public static final MailServerPopBean EXAMPLE_2;

    static {
        EXAMPLE_1 = new MailServerPopBean();
        EXAMPLE_1.setName("Example");
        EXAMPLE_1.setHost("mail.example.com");

        EXAMPLE_2 = new MailServerPopBean();
        EXAMPLE_2.setName("Example");
        EXAMPLE_2.setHost("mail.example.com");
        EXAMPLE_2.setPort("110");
        EXAMPLE_2.setProtocol("pop3");
    }
}
