package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Model for the cloud application tunnel, through which Atlassian cloud products
 * reach this instance without it being exposed to the internet. An instance
 * has at most one tunnel.
 * <p>
 * The tunnel is configured with the key generated when creating the tunnel in
 * the cloud. The key is write-only and never returned. The name, organization
 * and the cloud page the tunnel was created on are derived from it and are
 * read-only. The upstream URL, to which the tunnel routes the traffic, can be
 * set along with the key.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI.CLOUD_TUNNEL)
public class CloudTunnelModel {

    public enum CloudTunnelStatus {
        CONNECTED,
        CONNECTING,
        DEGRADED,
        UNAVAILABLE,
        ERROR,
    }

    @XmlElement
    @ToString.Exclude
    private String key;

    @XmlElement
    private String name;

    @XmlElement
    private String organizationName;

    @XmlElement
    private String cloudPageUrl;

    @XmlElement
    private String upstreamUrl;

    @XmlElement
    private CloudTunnelStatus status;

    // Example instances for documentation and tests

    public static final CloudTunnelModel EXAMPLE_1 = CloudTunnelModel.builder()
            .name("Example Tunnel")
            .organizationName("Example Organization")
            .cloudPageUrl("https://example.atlassian.net")
            .upstreamUrl("https://jira.example.com")
            .status(CloudTunnelStatus.CONNECTED)
            .build();

}
