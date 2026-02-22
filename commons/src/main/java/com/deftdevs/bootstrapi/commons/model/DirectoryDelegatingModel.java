package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.type.DirectoryPermissions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Builder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI.DIRECTORY + '-' + BootstrAPI.DIRECTORY_DELEGATING)
public class DirectoryDelegatingModel extends AbstractDirectoryExternalModel {

    @XmlElement
    private DirectoryDelegatingConnector connector;

    @XmlElement
    private DirectoryDelegatingConfiguration configuration;

    @XmlElement
    private DirectoryPermissions permissions;

    // Can be extended with ldapConnectionPooling in the future,
    // current (unimplemented) behaviour is LDAP connection pooling: JNDI

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectoryDelegatingConnector {

        @XmlElement
        private ConnectorType type;

        @XmlElement
        private String url;

        @XmlElement
        private SslType ssl;

        @XmlElement
        private Boolean useNodeReferrals;

        @XmlElement
        private Boolean nestedGroupsDisabled;

        @XmlElement
        private Boolean synchronizeUsers;

        @XmlElement
        private Boolean synchronizeUserDetails;

        @XmlElement
        private Boolean synchronizeGroupMemberships;

        @XmlElement
        private Boolean useUserMembershipAttribute;

        @XmlElement
        private Boolean usePagedResults;

        @XmlElement
        private Long pagedResultsSize;

        @XmlElement
        private Long readTimeoutInMillis;

        @XmlElement
        private Long searchTimeoutInMillis;

        @XmlElement
        private Long connectionTimeoutInMillis;

        @XmlElement
        private String baseDn;

        @XmlElement
        private String username;

        @XmlElement
        private String password;

        public enum ConnectorType {
            MICROSOFT_ACTIVE_DIRECTORY,
        }

        public enum SslType {
            NONE,
            LDAPS,
            START_TLS,
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectoryDelegatingConfiguration {

        @XmlElement
        private String userDn;

        @XmlElement
        private String userObjectClass;

        @XmlElement
        private String userObjectFilter;

        @XmlElement
        private String userNameAttribute;

        @XmlElement
        private String userNameRdnAttribute;

        @XmlElement
        private String userFirstNameAttribute;

        @XmlElement
        private String userLastNameAttribute;

        @XmlElement
        private String userDisplayNameAttribute;

        @XmlElement
        private String userEmailAttribute;

        @XmlElement
        private String userGroupAttribute;

        @XmlElement
        private String userUniqueIdAttribute;

        @XmlElement
        private String groupDn;

        @XmlElement
        private String groupObjectClass;

        @XmlElement
        private String groupObjectFilter;

        @XmlElement
        private String groupNameAttribute;

        @XmlElement
        private String groupDescriptionAttribute;

        @XmlElement
        private String groupMembersAttribute;
    }

    // Example instances for documentation and tests

    public static final DirectoryDelegatingModel EXAMPLE_1 = DirectoryDelegatingModel.builder()
        .id(1L)
        .name("Example")
        .active(true)
        .description("Example Directory")
        .connector(DirectoryDelegatingConnector.builder()
            .type(DirectoryDelegatingConnector.ConnectorType.MICROSOFT_ACTIVE_DIRECTORY)
            .url("ldaps://example.com:636")
            .ssl(DirectoryDelegatingConnector.SslType.LDAPS)
            .useNodeReferrals(false)
            .nestedGroupsDisabled(true)
            .synchronizeUsers(false)
            .synchronizeUserDetails(false)
            .synchronizeGroupMemberships(false)
            .useUserMembershipAttribute(false)
            .usePagedResults(true)
            .pagedResultsSize(999L)
            .readTimeoutInMillis(120000L)
            .searchTimeoutInMillis(60000L)
            .connectionTimeoutInMillis(10000L)
            .baseDn("DC=example,DC=com")
            .username("domain\\example")
            .password("p455w0rd")
            .build())
        .configuration(DirectoryDelegatingConfiguration.builder()
            .userDn("")
            .userObjectClass("user")
            .userObjectFilter("(objectClass=user)")
            .userNameAttribute("sAMAccountName")
            .userNameRdnAttribute("cn")
            .userFirstNameAttribute("givenName")
            .userLastNameAttribute("sn")
            .userDisplayNameAttribute("displayName")
            .userEmailAttribute("email")
            .userGroupAttribute("memberOf")
            .userUniqueIdAttribute("userID")
            .groupDn("")
            .groupObjectClass("group")
            .groupObjectFilter("(objectClass=group)")
            .groupNameAttribute("gn")
            .groupDescriptionAttribute("description")
            .groupMembersAttribute("member")
            .build())
        .permissions(DirectoryPermissions.builder()
            .addGroup(true)
            .addUser(true)
            .modifyGroup(true)
            .modifyUser(true)
            .modifyGroupAttributes(true)
            .modifyUserAttributes(true)
            .removeGroup(true)
            .removeUser(true)
            .build())
        .build();

}
