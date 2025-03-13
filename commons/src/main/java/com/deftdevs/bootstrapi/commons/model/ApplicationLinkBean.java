package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.UUID;

/**
 * Bean for an application link in REST requests.
 */
@Data
@NoArgsConstructor
@XmlRootElement(name = BootstrAPI.APPLICATION_LINK)
public class ApplicationLinkBean {

    public enum ApplicationLinkType {
        BAMBOO,
        JIRA,
        BITBUCKET,
        CONFLUENCE,
        FISHEYE,
        CROWD,
    }

    public enum ApplicationLinkAuthType {
        OAUTH,
        OAUTH_IMPERSONATION,
        DISABLED,
    }

    public enum ApplicationLinkStatus {
        AVAILABLE,
        UNAVAILABLE,
        CONFIGURATION_ERROR,
    }

    @XmlElement
    private UUID uuid;

    @XmlElement
    @NotNull
    private String name;

    @XmlElement
    @NotNull
    private ApplicationLinkType type;

    @XmlElement
    @NotNull
    private URI displayUrl;

    @XmlElement
    @NotNull
    private URI rpcUrl;

    @XmlElement
    private ApplicationLinkAuthType outgoingAuthType;

    @XmlElement
    private ApplicationLinkAuthType incomingAuthType;

    @XmlElement
    private boolean primary;

    @XmlElement
    private ApplicationLinkStatus status;

    // Example instances for documentation and tests

    public static final ApplicationLinkBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new ApplicationLinkBean();
        EXAMPLE_1.setUuid(UUID.randomUUID());
        EXAMPLE_1.setName("Example");
        EXAMPLE_1.setDisplayUrl(URI.create("http://example.com"));
        EXAMPLE_1.setRpcUrl(URI.create("http://rpc.example.com"));
        EXAMPLE_1.setOutgoingAuthType(ApplicationLinkAuthType.OAUTH);
        EXAMPLE_1.setIncomingAuthType(ApplicationLinkAuthType.OAUTH);
        EXAMPLE_1.setPrimary(true);
        EXAMPLE_1.setType(ApplicationLinkType.JIRA);
    }

}
