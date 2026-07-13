package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.deftdevs.bootstrapi.commons.model.type.SubEntityOf;
import jakarta.xml.bind.annotation.XmlRootElement;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SubEntityOf(MailServerModel.class)
@XmlRootElement(name = BootstrAPI.MAIL_SERVER_POP)
public class MailServerPopModel extends AbstractMailServerProtocolModel {

    // Example instances for documentation and tests

    public static final MailServerPopModel EXAMPLE_1 = MailServerPopModel.builder()
        .name("Example")
        .host("mail.example.com")
        .build();

    // greenmail-compatible so functional tests can apply it against the integration
    // test environment (see ci.yaml)
    public static final MailServerPopModel EXAMPLE_2 = MailServerPopModel.builder()
        .name("Example")
        .protocol("pop3")
        .host("localhost")
        .port(3110)
        .timeout(5000L)
        .build();

}
