package com.deftdevs.bootstrapi.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import com.deftdevs.bootstrapi.commons.model.type.SubEntityOf;
import jakarta.xml.bind.annotation.XmlRootElement;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.SETTINGS_SECURITY;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SubEntityOf(AbstractSettingsModel.class)
@XmlRootElement(name = SETTINGS_SECURITY)
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsSecurityModel {

    @XmlElement
    private Boolean webSudoEnabled;

    @XmlElement
    private Long webSudoTimeout;

    // Example instances for documentation and tests

    public static final SettingsSecurityModel EXAMPLE_1 = SettingsSecurityModel.builder()
            .webSudoEnabled(true)
            .webSudoTimeout(30L)
            .build();

}
