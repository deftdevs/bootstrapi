package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.type.DirectoryPermissions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Bean for directory settings in REST requests.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI.DIRECTORY + '-' + BootstrAPI.DIRECTORY_INTERNAL)
public class DirectoryInternalBean extends AbstractDirectoryBean {

    @XmlElement
    private DirectoryInternalCredentialPolicy credentialPolicy;

    @XmlElement
    private DirectoryInternalAdvanced advanced;

    @XmlElement
    private DirectoryPermissions permissions;

    // It is not clear yet whether we want to support setting directory groups and users
    // in external directories also, so we just start with the internal directory for now.

    @XmlElement
    private Collection<GroupBean> groups;

    @XmlElement
    private Collection<UserBean> users;

    @Data
    @NoArgsConstructor
    public static class DirectoryInternalCredentialPolicy {

        @XmlElement
        private String passwordRegex;

        @XmlElement
        private String passwordComplexityMessage;

        @XmlElement
        private Long passwordMaxAttempts;

        @XmlElement
        private Long passwordHistoryCount;

        @XmlElement
        private Long passwordMaxChangeTime;

        @XmlElement
        private Collection<Integer> passwordExpiryNotificationDays;

        @XmlElement
        private String passwordEncryptionMethod;
    }

    @Data
    @NoArgsConstructor
    public static class DirectoryInternalAdvanced {

        @XmlElement
        private Boolean enableNestedGroups;
    }

    // examples

    public static final DirectoryInternalBean EXAMPLE_1;

    static {
        EXAMPLE_1 = new DirectoryInternalBean();
        EXAMPLE_1.setId(1L);
        EXAMPLE_1.setName("Example");
        EXAMPLE_1.setActive(true);
        EXAMPLE_1.setDescription("Example Directory");
        EXAMPLE_1.setCredentialPolicy(new DirectoryInternalCredentialPolicy());
        EXAMPLE_1.getCredentialPolicy().setPasswordRegex("[a-zA-Z0-9]+");
        EXAMPLE_1.getCredentialPolicy().setPasswordComplexityMessage("Only alphanumeric characters");
        EXAMPLE_1.getCredentialPolicy().setPasswordMaxAttempts(3L);
        EXAMPLE_1.getCredentialPolicy().setPasswordHistoryCount(10L);
        EXAMPLE_1.getCredentialPolicy().setPasswordMaxChangeTime(60L);
        EXAMPLE_1.getCredentialPolicy().setPasswordExpiryNotificationDays(Arrays.asList(1, 7));
        EXAMPLE_1.getCredentialPolicy().setPasswordEncryptionMethod("ATLASSIAN_SECURITY_ENCODER");
        EXAMPLE_1.setPermissions(new DirectoryPermissions());
        EXAMPLE_1.getPermissions().setAddGroup(true);
        EXAMPLE_1.getPermissions().setAddUser(true);
        EXAMPLE_1.getPermissions().setModifyGroup(true);
        EXAMPLE_1.getPermissions().setModifyUser(true);
        EXAMPLE_1.getPermissions().setModifyGroupAttributes(true);
        EXAMPLE_1.getPermissions().setModifyUserAttributes(true);
        EXAMPLE_1.getPermissions().setRemoveGroup(true);
        EXAMPLE_1.getPermissions().setRemoveUser(true);
        EXAMPLE_1.setGroups(Collections.singletonList(GroupBean.EXAMPLE_1));
        EXAMPLE_1.setUsers(Collections.singletonList(UserBean.EXAMPLE_1));
    }
}
