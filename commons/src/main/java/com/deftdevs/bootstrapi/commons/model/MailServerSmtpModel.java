package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI.MAIL_SERVER + "-" + BootstrAPI.MAIL_SERVER_SMTP)
public class MailServerSmtpModel extends AbstractMailServerProtocolModel {

    @XmlElement
    private String adminContact;

    @XmlElement
    private String from;

    @XmlElement
    private String prefix;

    @XmlElement
    private Boolean useTls;

    // Removed explicit constructor to allow Lombok builder and inheritance to work

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

    public static final MailServerSmtpModel EXAMPLE_2 = MailServerSmtpModel.builder()
        .name("Example")
        .adminContact(null)
        .from("mail@example.com")
        .prefix("[Example]")
        .host("mail.example.com")
        .description("test smtp server")
        .port(25)
        .protocol("smtp")
        .timeout(2000L)
        .username("admin")
        .password("password")
        .build();

}
