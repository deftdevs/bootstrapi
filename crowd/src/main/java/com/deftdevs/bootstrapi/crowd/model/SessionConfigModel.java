package com.deftdevs.bootstrapi.crowd.model;

import com.deftdevs.bootstrapi.crowd.rest.api.SessionConfigResource;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = SessionConfigResource.SESSION_CONFIG)
public class SessionConfigModel {

    @XmlElement
    private Long sessionTimeoutInMinutes;

    @XmlElement
    private Boolean requireConsistentClientIP;

    public static final SessionConfigModel EXAMPLE_1;
    public static final SessionConfigModel EXAMPLE_2;

    static {
        EXAMPLE_1 = new SessionConfigModel();
        EXAMPLE_1.setSessionTimeoutInMinutes(20L);
        EXAMPLE_1.setRequireConsistentClientIP(false);
    }

    static {
        EXAMPLE_2 = new SessionConfigModel();
        EXAMPLE_2.setSessionTimeoutInMinutes(30L);
        EXAMPLE_2.setRequireConsistentClientIP(true);
    }
}
