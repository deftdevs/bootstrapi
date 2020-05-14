package de.aservo.atlassian.confapi.model;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = ConfAPI.MAIL_SERVER + "-" + ConfAPI.MAIL_SERVER_SMTP)
public class MailServerSmtpBean extends AbstractMailServerProtocolBean {

    @XmlElement
    private String adminContact;

    @XmlElement
    private String from;

    @XmlElement
    private String prefix;

    @XmlElement
    private boolean tls;

    /**
     * Constructor for {@link MailServerSmtpBean} used in crowd-confapi-plugin.
     *
     * @param adminContact the admin contact email address
     * @param from         the server from email address
     * @param prefix       the subject prefix
     * @param host         the SMTP host
     */
    public MailServerSmtpBean(
            final String adminContact,
            final String from,
            final String prefix,
            final String host) {

        setAdminContact(adminContact);
        setFrom(from);
        setPrefix(prefix);
        setHost(host);
    }

    // Example instances for documentation and tests

    public static final MailServerSmtpBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new MailServerSmtpBean();
        EXAMPLE_1.setName("Example");
        EXAMPLE_1.setAdminContact("admin@example.com");
        EXAMPLE_1.setFrom("mail@example.com");
        EXAMPLE_1.setPrefix("[Example]");
        EXAMPLE_1.setHost("mail.example.com");
    }

}
