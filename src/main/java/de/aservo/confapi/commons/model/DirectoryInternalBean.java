package de.aservo.confapi.commons.model;

import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;

/**
 * Bean for user directory settings in REST requests.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = ConfAPI.DIRECTORY_INTERNAL)
public class DirectoryInternalBean extends AbstractDirectoryBean {

    @XmlElement
    private DirectoryInternalCredentialPolicy credentialPolicy;

    @XmlElement
    private DirectoryInternalAdvanced advanced;

    @XmlElement
    private DirectoryInternalPermissions permissions;

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
        private List<Integer> passwordExpiryNotificationDays;

        @XmlElement
        private String passwordEncryptionMethod;
    }

    @Data
    @NoArgsConstructor
    public static class DirectoryInternalAdvanced {

        @XmlElement
        private boolean enableNestedGroups;
    }

    @Data
    @NoArgsConstructor
    public static class DirectoryInternalPermissions {

        @XmlElement
        private boolean addGroup;               //Allow groups to be added to the directory.

        @XmlElement
        private boolean addUser;                //Allow users to be added to the directory.

        @XmlElement
        private boolean modifyGroup;            //Allow groups to be modified in the directory.

        @XmlElement
        private boolean modifyUser;             //Allow users to be modified in the directory.

        @XmlElement
        private boolean modifyGroupAttributes;  //Allow group attributes to be modified in the directory.

        @XmlElement
        private boolean modifyUserAttributes;   //Allow user attributes to be modified in the directory.

        @XmlElement
        private boolean removeGroup;            //Allow groups to be removed from the directory.

        @XmlElement
        private boolean removeUser;             //Allow users to be removed from the directory.
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
    }
}
