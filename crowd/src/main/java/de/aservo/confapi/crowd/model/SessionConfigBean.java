package de.aservo.confapi.crowd.model;

import de.aservo.confapi.crowd.rest.api.SessionConfigResource;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = SessionConfigResource.SESSION_CONFIG)
public class SessionConfigBean {

    @XmlElement
    private Long sessionTimeoutInMinutes;

    @XmlElement
    private Boolean requireConsistentClientIP;

    public static final SessionConfigBean EXAMPLE_1;
    public static final SessionConfigBean EXAMPLE_2;

    static {
        EXAMPLE_1 = new SessionConfigBean();
        EXAMPLE_1.setSessionTimeoutInMinutes(20L);
        EXAMPLE_1.setRequireConsistentClientIP(false);
    }

    static {
        EXAMPLE_2 = new SessionConfigBean();
        EXAMPLE_2.setSessionTimeoutInMinutes(30L);
        EXAMPLE_2.setRequireConsistentClientIP(true);
    }
}
