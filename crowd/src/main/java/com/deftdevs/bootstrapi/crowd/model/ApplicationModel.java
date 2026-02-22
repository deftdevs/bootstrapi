package com.deftdevs.bootstrapi.crowd.model;

import com.atlassian.crowd.embedded.api.OperationType;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Collections;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = BootstrAPI.APPLICATION)
public class ApplicationModel {

    public enum ApplicationType {
        GENERIC,
        PLUGIN,
        CROWD,
        JIRA,
        CONFLUENCE,
        BITBUCKET,
        FISHEYE,
        CRUCIBLE,
        BAMBOO,
    }

    public enum AccessBasedSynchronisation {
        NO_FILTERING,
        USER_ONLY_FILTERING,
        USER_AND_GROUP_FILTERING,
    }

    @XmlElement
    private Long id;

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private Boolean active;

    @XmlElement
    private ApplicationType type;

    @XmlElement
    private String password;

    @XmlElement
    private Boolean cachedDirectoriesAuthenticationOrderOptimisationEnabled;

    @XmlElement
    private List<ApplicationDirectoryMapping> directoryMappings;

    @XmlElement
    private AccessBasedSynchronisation accessBasedSynchronisation;

    @XmlElement
    private Boolean membershipAggregationEnabled;

    @XmlElement
    private List<String> remoteAddresses;

    @XmlElement
    private Boolean aliasingEnabled;

    @XmlElement
    private Boolean lowercaseOutputEnabled;

    @XmlElement
    private Boolean authenticationWithoutPasswordEnabled;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationDirectoryMapping {

        @XmlElement
        private String directoryName;

        @XmlElement
        private Boolean authenticationAllowAll;

        @XmlElement
        private List<String> authenticationGroups;

        @XmlElement
        private List<String> autoAssignmentGroups;

        @XmlElement
        private List<OperationType> allowedOperations;

    }

    public static final ApplicationModel EXAMPLE_1 = ApplicationModel.builder()
        .id(1L)
        .name("app_name")
        .description("app_description")
        .active(true)
        .type(ApplicationType.GENERIC)
        .password("3x4mpl3")
        .cachedDirectoriesAuthenticationOrderOptimisationEnabled(false)
        .directoryMappings(Collections.singletonList(
            ApplicationDirectoryMapping.builder()
                .directoryName("directory")
                .authenticationAllowAll(true)
                .authenticationGroups(Collections.singletonList("app_access"))
                .autoAssignmentGroups(Collections.singletonList("app_users"))
                .allowedOperations(Collections.singletonList(OperationType.CREATE_USER))
                .build()
        ))
        .accessBasedSynchronisation(AccessBasedSynchronisation.NO_FILTERING)
        .membershipAggregationEnabled(false)
        .remoteAddresses(Collections.singletonList("127.0.0.1"))
        .aliasingEnabled(true)
        .lowercaseOutputEnabled(true)
        .authenticationWithoutPasswordEnabled(true)
        .build();

    public static final ApplicationModel EXAMPLE_2 = ApplicationModel.builder()
        .id(2L)
        .name("app_name2")
        .description("app_description2")
        .active(false)
        .type(ApplicationType.BAMBOO)
        .password("3x4mpl32")
        .cachedDirectoriesAuthenticationOrderOptimisationEnabled(true)
        .directoryMappings(Collections.singletonList(
            ApplicationDirectoryMapping.builder()
                .directoryName("directory")
                .authenticationAllowAll(false)
                .authenticationGroups(Collections.singletonList("app_access2"))
                .autoAssignmentGroups(Collections.singletonList("app_users2"))
                .allowedOperations(Collections.singletonList(OperationType.CREATE_GROUP))
                .build()
        ))
        .accessBasedSynchronisation(AccessBasedSynchronisation.USER_AND_GROUP_FILTERING)
        .membershipAggregationEnabled(true)
        .remoteAddresses(Collections.singletonList("127.0.0.3"))
        .aliasingEnabled(false)
        .lowercaseOutputEnabled(false)
        .authenticationWithoutPasswordEnabled(false)
        .build();

}
