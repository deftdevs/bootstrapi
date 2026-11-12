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
 * Model for the cloud connector, the connection of a Data Center instance to
 * an Atlassian cloud organization that cloud product connections and
 * application tunnels are built on.
 * <p>
 * The connector is configured with the client credentials generated in
 * Atlassian Administration. The credentials are write-only and never
 * returned. All other fields are derived from them and are read-only.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI.CLOUD_CONNECTOR)
public class CloudConnectorModel {

    public enum CloudConnectorStatus {
        CREATED,
        CONNECTED,
        INTERRUPTED,
        EXPIRED,
        DISCONNECTED,
    }

    @XmlElement
    @ToString.Exclude
    private String credentials;

    @XmlElement
    private String name;

    @XmlElement
    private String organizationId;

    @XmlElement
    private String organizationName;

    @XmlElement
    private CloudConnectorStatus status;

    // Example instances for documentation and tests

    public static final CloudConnectorModel EXAMPLE_1 = CloudConnectorModel.builder()
            .name("Example Connector")
            .organizationId("a1b2c3d4-e5f6-a7b8-c9d0-e1f2a3b4c5d6")
            .organizationName("Example Organization")
            .status(CloudConnectorStatus.CONNECTED)
            .build();

}
