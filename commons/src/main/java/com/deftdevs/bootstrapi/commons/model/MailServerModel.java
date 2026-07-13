package com.deftdevs.bootstrapi.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.MAIL_SERVER;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = MAIL_SERVER)
public class MailServerModel {

    @XmlElement
    private MailServerSmtpModel smtp;

    @XmlElement
    private MailServerPopModel pop;

}
