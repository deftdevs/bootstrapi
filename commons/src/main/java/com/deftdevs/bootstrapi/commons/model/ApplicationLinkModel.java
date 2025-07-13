package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.UUID;

/**
 * Model for an application link in REST requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI.APPLICATION_LINK)
public class ApplicationLinkModel {

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
    private String name;

    @XmlElement
    private ApplicationLinkType type;

    @XmlElement
    private URI displayUrl;

    @XmlElement
    private URI rpcUrl;

    @XmlElement
    private ApplicationLinkAuthType outgoingAuthType;

    @XmlElement
    private ApplicationLinkAuthType incomingAuthType;

    @XmlElement
    private Boolean primary;

    @XmlElement
    private ApplicationLinkStatus status;

    @XmlElement
    private Boolean ignoreSetupErrors;

    // Example instances for documentation and tests

    public static final ApplicationLinkModel EXAMPLE_1 = ApplicationLinkModel.builder()
        .uuid(UUID.randomUUID())
        .name("Example")
        .displayUrl(URI.create("http://example.com"))
        .rpcUrl(URI.create("http://rpc.example.com"))
        .outgoingAuthType(ApplicationLinkAuthType.OAUTH)
        .incomingAuthType(ApplicationLinkAuthType.OAUTH)
        .primary(true)
        .type(ApplicationLinkType.JIRA)
        .build();

}
