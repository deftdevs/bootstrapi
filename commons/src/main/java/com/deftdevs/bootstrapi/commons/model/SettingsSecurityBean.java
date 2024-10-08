package com.deftdevs.bootstrapi.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.SETTINGS;
import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.SETTINGS_SECURITY;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = SETTINGS + "-" + SETTINGS_SECURITY)
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsSecurityBean {

    @XmlElement
    private Boolean webSudoEnabled;

    @XmlElement
    private Long webSudoTimeout;

    // Example instances for documentation and tests

    public static final SettingsSecurityBean EXAMPLE_1 = SettingsSecurityBean.builder()
            .webSudoEnabled(true)
            .webSudoTimeout(30L)
            .build();

}
