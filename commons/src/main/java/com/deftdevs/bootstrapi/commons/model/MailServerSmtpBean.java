package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
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
    private Boolean useTls;

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

    public boolean getUseTls() {
        return Boolean.TRUE.equals(useTls);
    }

    // Example instances for documentation and tests

    public static final MailServerSmtpBean EXAMPLE_1;
    public static final MailServerSmtpBean EXAMPLE_2;

    static {
        EXAMPLE_1 = new MailServerSmtpBean();
        EXAMPLE_1.setName("Example");
        EXAMPLE_1.setAdminContact("admin@example.com");
        EXAMPLE_1.setFrom("mail@example.com");
        EXAMPLE_1.setPrefix("[Example]");
        EXAMPLE_1.setHost("mail.example.com");

        EXAMPLE_2 = new MailServerSmtpBean();
        EXAMPLE_2.setName("Example");
        EXAMPLE_2.setAdminContact(null);
        EXAMPLE_2.setFrom("mail@example.com");
        EXAMPLE_2.setPrefix("[Example]");
        EXAMPLE_2.setHost("mail.example.com");
        EXAMPLE_2.setDescription("test smtp server");
        EXAMPLE_2.setPort("25");
        EXAMPLE_2.setProtocol("smtp");
        EXAMPLE_2.setTimeout(2000L);
        EXAMPLE_2.setUsername("admin");
        EXAMPLE_2.setPassword("password");
    }

}
