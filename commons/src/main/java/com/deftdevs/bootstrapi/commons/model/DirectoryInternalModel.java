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
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

/**
 * Model for directory settings in REST requests.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI.DIRECTORY + '-' + BootstrAPI.DIRECTORY_INTERNAL)
public class DirectoryInternalModel extends AbstractDirectoryModel {

    @XmlElement
    private DirectoryInternalCredentialPolicy credentialPolicy;

    @XmlElement
    private DirectoryInternalAdvanced advanced;

    @XmlElement
    private DirectoryPermissions permissions;

    // It is not clear yet whether we want to support setting directory groups and users
    // in external directories also, so we just start with the internal directory for now.

    @XmlElement
    private List<GroupModel> groups;

    @XmlElement
    private List<UserModel> users;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectoryInternalAdvanced {

        @XmlElement
        private Boolean enableNestedGroups;
    }

    // Example instances for documentation and tests

    public static final DirectoryInternalModel EXAMPLE_1 = DirectoryInternalModel.builder()
        .id(1L)
        .name("Example")
        .active(true)
        .description("Example Directory")
        .credentialPolicy(DirectoryInternalCredentialPolicy.builder()
            .passwordRegex("[a-zA-Z0-9]+")
            .passwordComplexityMessage("Only alphanumeric characters")
            .passwordMaxAttempts(3L)
            .passwordHistoryCount(10L)
            .passwordMaxChangeTime(60L)
            .passwordExpiryNotificationDays(Arrays.asList(1, 7))
            .passwordEncryptionMethod("ATLASSIAN_SECURITY_ENCODER")
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
        .groups(Collections.singletonList(GroupModel.EXAMPLE_1))
        .users(Collections.singletonList(UserModel.EXAMPLE_1))
        .build();

}
