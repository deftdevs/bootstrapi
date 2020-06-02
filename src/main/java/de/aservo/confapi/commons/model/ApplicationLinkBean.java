package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.type.ApplicationLinkTypes;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

/**
 * Bean for an application link in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.APPLICATION_LINK)
public class ApplicationLinkBean {

    @XmlElement
    private String serverId;

    @XmlElement
    private String appType;

    @XmlElement
    @NotNull
    private ApplicationLinkTypes linkType;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String name;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String displayUrl;

    @XmlElement
    @NotNull
    @Size(min = 1)
    private String rpcUrl;

    @XmlElement
    private boolean primary;

    @XmlElement
    private String username;

    @XmlElement
    private String password;

    // Example instances for documentation and tests

    public static final ApplicationLinkBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new ApplicationLinkBean();
        EXAMPLE_1.setName("Example");
        EXAMPLE_1.setDisplayUrl("http://example.com");
        EXAMPLE_1.setRpcUrl("http://rpc.example.com");
        EXAMPLE_1.setPrimary(true);
        EXAMPLE_1.setServerId(UUID.randomUUID().toString());
        EXAMPLE_1.setAppType("jira");
        EXAMPLE_1.setUsername("username");
        EXAMPLE_1.setPassword("p455w0rd");
    }

}
