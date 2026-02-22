package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

/**
 * Model for user directory settings in REST requests.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI.DIRECTORY + '-' + BootstrAPI.DIRECTORY_CROWD)
public class DirectoryCrowdModel extends AbstractDirectoryExternalModel {

    @XmlElement
    private DirectoryCrowdServer server;

    @XmlElement
    private DirectoryCrowdPermissions permissions;

    @XmlElement
    private DirectoryCrowdAdvanced advanced;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectoryCrowdServer {

        @XmlElement
        @NotNull
        private URI url;

        @XmlElement
        private DirectoryCrowdServerProxy proxy;

        @XmlElement
        @NotNull
        private String appUsername;

        @XmlElement
        @NotNull
        private String appPassword;

        @XmlElement
        private Long connectionTimeoutInMillis;

        @XmlElement
        private Integer maxConnections;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DirectoryCrowdServerProxy {

            @XmlElement
            private String host;

            @XmlElement
            private Integer port;

            @XmlElement
            private String username;

            @XmlElement
            private String password;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectoryCrowdPermissions {

        @XmlElement
        private Boolean readOnly;

        @XmlElement
        private Boolean fullAccess;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectoryCrowdAdvanced {

        @XmlElement
        private Boolean enableNestedGroups;

        @XmlElement
        private Boolean enableIncrementalSync;

        @XmlElement
        private String updateGroupMembershipMethod;

        @XmlElement
        private Integer updateSyncIntervalInMinutes;
    }

    // Example instances for documentation and tests

    public static final DirectoryCrowdModel EXAMPLE_1 = DirectoryCrowdModel.builder()
        .name("example")
        .server(DirectoryCrowdServer.builder()
            .url(URI.create("https://crowd.example.com"))
            .appPassword("p455w0rd")
            .build())
        .build();

    public static final DirectoryCrowdModel EXAMPLE_1_WITH_PROXY = DirectoryCrowdModel.builder()
        .name("example")
        .server(DirectoryCrowdServer.builder()
            .url(URI.create("https://crowd.example.com"))
            .appPassword("p433w0rd")
            .proxy(DirectoryCrowdServer.DirectoryCrowdServerProxy.builder()
                .host("proxy.example.com")
                .username("user")
                .password("pass")
                .build())
            .build())
        .build();

    public static final DirectoryCrowdModel EXAMPLE_2 = DirectoryCrowdModel.builder()
        .name("example2")
        .server(DirectoryCrowdServer.builder()
            .url(URI.create("https://crowd2.example.com"))
            .appPassword("0th3r")
            .build())
        .build();

    public static final DirectoryCrowdModel EXAMPLE_3 = DirectoryCrowdModel.builder()
        .name("example other")
        .server(DirectoryCrowdServer.builder()
            .url(URI.create("https://crowd3.example.com"))
            .appPassword("p466w0rd")
            .build())
        .build();

}
