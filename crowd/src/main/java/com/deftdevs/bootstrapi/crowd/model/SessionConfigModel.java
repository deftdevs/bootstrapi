package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.crowd.rest.api.SessionConfigResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = SessionConfigResource.SESSION_CONFIG)
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
