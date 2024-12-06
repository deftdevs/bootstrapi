package com.deftdevs.bootstrapi.confluence.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.MailServerPopBean;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI.MAIL_SERVER)
public class MailServerBean {

    @XmlElement
    private MailServerPopBean pop;

    @XmlElement
    private MailServerSmtpBean smtp;

    // Example instances for documentation and tests

    public static final MailServerBean EXAMPLE_1 = MailServerBean.builder()
            .pop(MailServerPopBean.EXAMPLE_1)
            .smtp(MailServerSmtpBean.EXAMPLE_1)
            .build();

}
