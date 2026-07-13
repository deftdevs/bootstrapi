package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI.SESSION_CONFIG)
public class SessionConfigModel {

    @XmlElement
    private Long sessionTimeoutInMinutes;

    @XmlElement
    private Boolean requireConsistentClientIP;

    public static final SessionConfigModel EXAMPLE_1 = SessionConfigModel.builder()
            .sessionTimeoutInMinutes(20L)
            .requireConsistentClientIP(false)
            .build();

    public static final SessionConfigModel EXAMPLE_2 = SessionConfigModel.builder()
            .sessionTimeoutInMinutes(30L)
            .requireConsistentClientIP(true)
            .build();

}
