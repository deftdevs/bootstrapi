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

/**
 * Model for user directory settings in REST requests.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI.DIRECTORY + '-' + BootstrAPI.DIRECTORY_LDAP)
public class DirectoryLdapModel extends AbstractDirectoryExternalModel {

    @XmlElement
    private DirectoryLdapServer server;

    @XmlElement
    private DirectoryLdapSchema schema;

    @XmlElement
    private DirectoryLdapPermissions permissions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectoryLdapServer {

        @XmlElement
        @NotNull
        private String host;

        @XmlElement
        private Integer port;

        @XmlElement
        private Boolean useSsl;

        @XmlElement
        private String username;

        @XmlElement
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectoryLdapSchema {

        @XmlElement
        private String baseDn;

        @XmlElement
        private String userDn;

        @XmlElement
        private String groupDn;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectoryLdapPermissions {

        @XmlElement
        private Boolean readOnly;

        @XmlElement
        private Boolean readOnlyForLocalGroups;

        @XmlElement
        private Boolean fullAccess;
    }
}
