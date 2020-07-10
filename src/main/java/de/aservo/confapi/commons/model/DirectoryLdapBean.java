package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean for user directory settings in REST requests.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = ConfAPI.DIRECTORY_LDAP)
public class DirectoryLdapBean extends AbstractDirectoryBean {

    @XmlElement
    private DirectoryLdapServer server;

    @XmlElement
    private DirectoryLdapSchema schema;

    @XmlElement
    private DirectoryLdapPermissions permissions;

    @Data
    @NoArgsConstructor
    public static class DirectoryLdapServer {

        @XmlElement
        @NotNull
        @Size(min = 1)
        private String hostName;

        @XmlElement
        private int port;

        @XmlElement
        private boolean useSsl;

        @XmlElement
        private String username;

        @XmlElement
        private String password;
    }

    @Data
    @NoArgsConstructor
    public static class DirectoryLdapSchema {

        @XmlElement
        private String baseDn;

        @XmlElement
        private String userDn;

        @XmlElement
        private String groupDn;
    }

    @Data
    @NoArgsConstructor
    public static class DirectoryLdapPermissions {

        @XmlElement
        private boolean readonly;

        @XmlElement
        private boolean readonlyForLocalGroups;

        @XmlElement
        private boolean fullAccess;
    }
}
