package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.UUID;

/**
 * Model for an application link in REST requests.
 */
@Data
@Builder
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
