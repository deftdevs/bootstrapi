package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI.MAIL_SERVER + "-" + BootstrAPI.MAIL_SERVER_POP)
public class MailServerPopModel extends AbstractMailServerProtocolModel {

    // Example instances for documentation and tests

    public static final MailServerPopModel EXAMPLE_1 = MailServerPopModel.builder()
        .name("Example")
        .host("mail.example.com")
        .build();

    public static final MailServerPopModel EXAMPLE_2 = MailServerPopModel.builder()
        .name("Example")
        .host("mail.example.com")
        .port(110)
        .protocol("pop3")
        .build();

}
