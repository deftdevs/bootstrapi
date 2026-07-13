package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.xml.bind.annotation.XmlElement;
import com.deftdevs.bootstrapi.commons.model.type.SubEntityOf;
import jakarta.xml.bind.annotation.XmlRootElement;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SubEntityOf(MailServerModel.class)
@XmlRootElement(name = BootstrAPI.MAIL_SERVER_SMTP)
public class MailServerSmtpModel extends AbstractMailServerProtocolModel {

    @XmlElement
    private String adminContact;

    @XmlElement
    private String from;

    @XmlElement
    private String prefix;

    @XmlElement
    private Boolean useTls;

    public boolean getUseTls() {
        return Boolean.TRUE.equals(useTls);
    }

    // Example instances for documentation and tests

    public static final MailServerSmtpModel EXAMPLE_1 = MailServerSmtpModel.builder()
        .name("Example")
        .adminContact("admin@example.com")
        .from("mail@example.com")
        .prefix("[Example]")
        .host("mail.example.com")
        .build();

    // greenmail-compatible so functional tests can apply it against the integration
    // test environment (see ci.yaml); only fields that products echo back are set
    public static final MailServerSmtpModel EXAMPLE_2 = MailServerSmtpModel.builder()
        .name("Example")
        .from("mail@example.com")
        .prefix("[Example]")
        .protocol("smtp")
        .host("localhost")
        .port(3025)
        .useTls(false)
        .timeout(5000L)
        .build();

    // restricted to the fields supported by all products (e.g. Crowd does not echo name and protocol)
    public static final MailServerSmtpModel EXAMPLE_2_MINIMAL = MailServerSmtpModel.builder()
        .from(EXAMPLE_2.getFrom())
        .prefix(EXAMPLE_2.getPrefix())
        .host(EXAMPLE_2.getHost())
        .port(EXAMPLE_2.getPort())
        .useTls(EXAMPLE_2.getUseTls())
        .timeout(EXAMPLE_2.getTimeout())
        .build();

}
