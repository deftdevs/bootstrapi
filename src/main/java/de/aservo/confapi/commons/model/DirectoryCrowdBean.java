package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

/**
 * Bean for user directory settings in REST requests.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = ConfAPI.DIRECTORY_CROWD)
public class DirectoryCrowdBean extends AbstractDirectoryBean {

    @XmlElement
    private DirectoryCrowdServer server;

    @XmlElement
    private DirectoryCrowdPermissions permissions;

    @XmlElement
    private DirectoryCrowdAdvanced advanced;

    @Data
    @NoArgsConstructor
    public static class DirectoryCrowdServer {

        @XmlElement
        @NotNull
        private URI uri;

        @XmlElement
        private CrowdServerProxy proxy;

        @XmlElement
        @NotNull
        @Size(min = 1)
        private String appUsername;

        @XmlElement
        @NotNull
        @Size(min = 1)
        private String appPassword;

        @XmlElement
        private long connectionTimeout;     //in millisecs

        @XmlElement
        private int maxConnections;

        @Data
        @NoArgsConstructor
        public static class CrowdServerProxy {

            @XmlElement
            private String username;

            @XmlElement
            private String password;

            @XmlElement
            private URI uri;
        }
    }

    @Data
    @NoArgsConstructor
    public static class DirectoryCrowdPermissions {

        @XmlElement
        private boolean readonly;

        @XmlElement
        private boolean fullAccess;
    }

    @Data
    @NoArgsConstructor
    public static class DirectoryCrowdAdvanced {

        @XmlElement
        private boolean enableNestedGroups;

        @XmlElement
        private boolean enableIncrementalSync;

        @XmlElement
        private String updateGroupMembershipMethod;

        @XmlElement
        private int updateSyncInterval;     //in minutes
    }

    // Example instances for documentation and tests

    public static final DirectoryCrowdBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new DirectoryCrowdBean();
        EXAMPLE_1.setName("example");
        EXAMPLE_1.setServer(new DirectoryCrowdServer());
        EXAMPLE_1.getServer().setUri(URI.create("https://crowd.example.com"));
        EXAMPLE_1.getServer().setAppPassword("p455w0rd");
    }

    public static final DirectoryCrowdBean EXAMPLE_1_WITH_PROXY;

    static {
        EXAMPLE_1_WITH_PROXY = new DirectoryCrowdBean();
        EXAMPLE_1_WITH_PROXY.setName("example");
        EXAMPLE_1_WITH_PROXY.setServer(new DirectoryCrowdServer());
        EXAMPLE_1_WITH_PROXY.getServer().setUri(URI.create("https://crowd.example.com"));
        DirectoryCrowdServer.CrowdServerProxy proxy = new DirectoryCrowdServer.CrowdServerProxy();
        proxy.setUri(URI.create("https://proxy.example.com"));
        proxy.setUsername("user");
        proxy.setPassword("pass");
        EXAMPLE_1_WITH_PROXY.getServer().setProxy(proxy);
        EXAMPLE_1_WITH_PROXY.getServer().setAppPassword("p433w0rd");
    }

    public static final DirectoryCrowdBean EXAMPLE_2;

    static {
        EXAMPLE_2 = new DirectoryCrowdBean();
        EXAMPLE_2.setName("example2");
        EXAMPLE_2.setServer(new DirectoryCrowdServer());
        EXAMPLE_1.getServer().setUri(URI.create("https://crowd2.example.com"));
        EXAMPLE_2.getServer().setAppPassword("0th3r");
    }

    public static final DirectoryCrowdBean EXAMPLE_3;

    static {
        EXAMPLE_3 = new DirectoryCrowdBean();
        EXAMPLE_3.setName("example other");
        EXAMPLE_3.setServer(new DirectoryCrowdServer());
        EXAMPLE_1.getServer().setUri(URI.create("https://crowd3.example.com"));
        EXAMPLE_3.getServer().setAppPassword("p466w0rd");
    }

}
