package de.aservo.confapi.crowd.model;

import com.atlassian.crowd.embedded.api.OperationType;
import de.aservo.confapi.commons.constants.ConfAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@XmlRootElement(name = ConfAPI.APPLICATION)
public class ApplicationBean {

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
        ;
    }

    public enum AccessBasedSynchronisation {
        NO_FILTERING,
        USER_ONLY_FILTERING,
        USER_AND_GROUP_FILTERING,
        ;
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
    private Collection<ApplicationDirectoryMapping> directoryMappings;

    @XmlElement
    private AccessBasedSynchronisation accessBasedSynchronisation;

    @XmlElement
    private Boolean membershipAggregationEnabled;

    @XmlElement
    private Collection<String> remoteAddresses;

    @XmlElement
    private Boolean aliasingEnabled;

    @XmlElement
    private Boolean lowercaseOutputEnabled;

    @XmlElement
    private Boolean authenticationWithoutPasswordEnabled;

    @Data
    @NoArgsConstructor
    public static class ApplicationDirectoryMapping {

        @XmlElement
        private String directoryName;

        @XmlElement
        private Boolean authenticationAllowAll;

        @XmlElement
        private Collection<String> authenticationGroups;

        @XmlElement
        private Collection<String> autoAssignmentGroups;

        @XmlElement
        private Collection<OperationType> allowedOperations;

    }

    public static final ApplicationBean EXAMPLE_1;
    public static final ApplicationBean EXAMPLE_2;

    static {
        EXAMPLE_1 = new ApplicationBean();
        EXAMPLE_1.setId(1L);
        EXAMPLE_1.setName("app_name");
        EXAMPLE_1.setDescription("app_description");
        EXAMPLE_1.setActive(true);
        EXAMPLE_1.setType(ApplicationType.GENERIC);
        EXAMPLE_1.setPassword("3x4mpl3");
        EXAMPLE_1.setCachedDirectoriesAuthenticationOrderOptimisationEnabled(false);
        final ApplicationDirectoryMapping directoryMapping = new ApplicationDirectoryMapping();
        directoryMapping.setDirectoryName("directory");
        directoryMapping.setAuthenticationAllowAll(true);
        directoryMapping.setAuthenticationGroups(Collections.singleton("app_access"));
        directoryMapping.setAutoAssignmentGroups(Collections.singleton("app_users"));
        directoryMapping.setAllowedOperations(Collections.singleton(OperationType.CREATE_USER));
        EXAMPLE_1.setDirectoryMappings(Collections.singleton(directoryMapping));
        EXAMPLE_1.setAccessBasedSynchronisation(AccessBasedSynchronisation.NO_FILTERING);
        EXAMPLE_1.setMembershipAggregationEnabled(false);
        EXAMPLE_1.setRemoteAddresses(Collections.singletonList("127.0.0.1"));
        EXAMPLE_1.setAliasingEnabled(true);
        EXAMPLE_1.setLowercaseOutputEnabled(true);
        EXAMPLE_1.setAuthenticationWithoutPasswordEnabled(true);
    }

    static {
        EXAMPLE_2 = new ApplicationBean();
        EXAMPLE_2.setId(2L);
        EXAMPLE_2.setName("app_name2");
        EXAMPLE_2.setDescription("app_description2");
        EXAMPLE_2.setActive(false);
        EXAMPLE_2.setType(ApplicationType.BAMBOO);
        EXAMPLE_2.setPassword("3x4mpl32");
        EXAMPLE_2.setCachedDirectoriesAuthenticationOrderOptimisationEnabled(true);
        final ApplicationDirectoryMapping directoryMapping = new ApplicationDirectoryMapping();
        directoryMapping.setDirectoryName("directory");
        directoryMapping.setAuthenticationAllowAll(false);
        directoryMapping.setAuthenticationGroups(Collections.singleton("app_access2"));
        directoryMapping.setAutoAssignmentGroups(Collections.singleton("app_users2"));
        directoryMapping.setAllowedOperations(Collections.singleton(OperationType.CREATE_GROUP));
        EXAMPLE_2.setDirectoryMappings(Collections.singleton(directoryMapping));
        EXAMPLE_2.setAccessBasedSynchronisation(AccessBasedSynchronisation.USER_AND_GROUP_FILTERING);
        EXAMPLE_2.setMembershipAggregationEnabled(true);
        EXAMPLE_2.setRemoteAddresses(Collections.singletonList("127.0.0.3"));
        EXAMPLE_2.setAliasingEnabled(false);
        EXAMPLE_2.setLowercaseOutputEnabled(false);
        EXAMPLE_2.setAuthenticationWithoutPasswordEnabled(false);
    }
}
