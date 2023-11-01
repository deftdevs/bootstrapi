package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.type.DirectoryPermissions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = ConfAPI.DIRECTORY + '-' + ConfAPI.DIRECTORY_DELEGATING)
public class DirectoryDelegatingBean extends AbstractDirectoryBean {

    @XmlElement
    private DirectoryDelegatingConnector connector;

    @XmlElement
    private DirectoryDelegatingConfiguration configuration;

    @XmlElement
    private DirectoryPermissions permissions;

    // Can be extended with ldapConnectionPooling in the future,
    // current (unimplemented) behaviour is LDAP connection pooling: JNDI

    @Data
    @NoArgsConstructor
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
        private Boolean synchronizeUserDetails;

        @XmlElement
        private Boolean synchronizeGroupMemberships;

        @XmlElement
        private Boolean usePagedResults;

        @XmlElement
        private Integer pagedResultsSize;

        @XmlElement
        private Integer readTimeoutInMillis;

        @XmlElement
        private Integer searchTimeoutInMillis;

        @XmlElement
        private Integer connectionTimeoutInMillis;

        @XmlElement
        private String baseDn;

        @XmlElement
        private String username;

        @XmlElement
        private String password;

        public enum ConnectorType {
            MICROSOFT_ACTIVE_DIRECTORY,
            ;
        }

        public enum SslType {
            NONE,
            LDAPS,
            START_TLS,
            ;
        }
    }

    @Data
    @NoArgsConstructor
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
    }

    // Example instances for documentation and tests

    public static final DirectoryDelegatingBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new DirectoryDelegatingBean();
        EXAMPLE_1.setId(1L);
        EXAMPLE_1.setName("Example");
        EXAMPLE_1.setActive(true);
        EXAMPLE_1.setDescription("Example Directory");
        EXAMPLE_1.setConnector(new DirectoryDelegatingConnector());
        EXAMPLE_1.getConnector().setType(DirectoryDelegatingConnector.ConnectorType.MICROSOFT_ACTIVE_DIRECTORY);
        EXAMPLE_1.getConnector().setUrl("ldaps://example.com:636");
        EXAMPLE_1.getConnector().setSsl(DirectoryDelegatingConnector.SslType.LDAPS);
        EXAMPLE_1.getConnector().setUseNodeReferrals(false);
        EXAMPLE_1.getConnector().setNestedGroupsDisabled(true);
        EXAMPLE_1.getConnector().setSynchronizeUserDetails(false);
        EXAMPLE_1.getConnector().setSynchronizeGroupMemberships(false);
        EXAMPLE_1.getConnector().setUsePagedResults(true);
        EXAMPLE_1.getConnector().setPagedResultsSize(999);
        EXAMPLE_1.getConnector().setReadTimeoutInMillis(120000);
        EXAMPLE_1.getConnector().setSearchTimeoutInMillis(60000);
        EXAMPLE_1.getConnector().setConnectionTimeoutInMillis(10000);
        EXAMPLE_1.getConnector().setBaseDn("DC=example,DC=com");
        EXAMPLE_1.getConnector().setUsername("domain\\example");
        EXAMPLE_1.getConnector().setPassword("p455w0rd");
        EXAMPLE_1.setConfiguration(new DirectoryDelegatingConfiguration());
        EXAMPLE_1.getConfiguration().setUserDn("");
        EXAMPLE_1.getConfiguration().setUserObjectClass("user");
        EXAMPLE_1.getConfiguration().setUserObjectFilter("TODO");
        EXAMPLE_1.getConfiguration().setUserNameAttribute("sAMAccountName");
        EXAMPLE_1.getConfiguration().setUserNameRdnAttribute("cn");
        EXAMPLE_1.getConfiguration().setUserFirstNameAttribute("givenName");
        EXAMPLE_1.getConfiguration().setUserLastNameAttribute("sn");
        EXAMPLE_1.getConfiguration().setUserDisplayNameAttribute("displayName");
        EXAMPLE_1.getConfiguration().setUserEmailAttribute("email");
        EXAMPLE_1.getConfiguration().setUserGroupAttribute("memberOf");
        EXAMPLE_1.getConfiguration().setUserUniqueIdAttribute("userID");
        EXAMPLE_1.setPermissions(new DirectoryPermissions());
        EXAMPLE_1.getPermissions().setAddGroup(true);
        EXAMPLE_1.getPermissions().setAddUser(true);
        EXAMPLE_1.getPermissions().setModifyGroup(true);
        EXAMPLE_1.getPermissions().setModifyUser(true);
        EXAMPLE_1.getPermissions().setModifyGroupAttributes(true);
        EXAMPLE_1.getPermissions().setModifyUserAttributes(true);
        EXAMPLE_1.getPermissions().setRemoveGroup(true);
        EXAMPLE_1.getPermissions().setRemoveUser(true);
    }

}
