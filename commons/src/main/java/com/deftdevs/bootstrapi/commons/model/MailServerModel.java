package com.deftdevs.bootstrapi.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.MAIL_SERVER;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = MAIL_SERVER)
public class MailServerModel {

    @XmlElement
    private MailServerSmtpModel smtp;

    @XmlElement
    private MailServerPopModel pop;

    // Example instances for documentation and tests

    public static final MailServerModel EXAMPLE_1 = new MailServerModel(
            MailServerSmtpModel.EXAMPLE_1,
            MailServerPopModel.EXAMPLE_1
    );

}
