package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.manager.application.DefaultGroupMembershipService;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.model.application.*;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import com.deftdevs.bootstrapi.crowd.model.ApplicationModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashSet;

import static com.deftdevs.bootstrapi.crowd.model.ApplicationModel.EXAMPLE_1;
import static com.deftdevs.bootstrapi.crowd.model.util.ApplicationModelUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ApplicationModelUtilTest {

    @Mock
    private DefaultGroupMembershipService defaultGroupMembershipService;

    @Mock
    private DirectoryManager directoryManager;

    @Test
    public void testMapTypesApplication() {
        for (ApplicationType type : ApplicationType.values()) {
            assertEquals(type, toApplicationType(toApplicationModelType(type)));
        }
        assertNull(toApplicationType(toApplicationModelType(null)));
    }

    @Test
    public void testMapTypesApplicationModel() {
        for (ApplicationModel.ApplicationType type : ApplicationModel.ApplicationType.values()) {
            assertEquals(type, toApplicationModelType(toApplicationType(type)));
        }
        assertNull(toApplicationModelType(toApplicationType(null)));
    }

    @Test
    public void testToApplication() {
        Application application = toApplication(EXAMPLE_1);

        assertEquals(EXAMPLE_1.getName(), application.getName());
        assertEquals(EXAMPLE_1.getType(), toApplicationModelType(application.getType()));
        assertEquals(EXAMPLE_1.getDescription(), application.getDescription());
        assertEquals(EXAMPLE_1.getActive().booleanValue(), application.isActive());
        assertEquals(application.getCredential().getCredential(), EXAMPLE_1.getPassword());
        assertEquals(EXAMPLE_1.getRemoteAddresses(), toStringCollection(application.getRemoteAddresses()));
    }

    @Test
    public void testToApplicationModel() {
        Application application = toApplication(EXAMPLE_1);
        ApplicationModel applicationModel = toApplicationModel(application, defaultGroupMembershipService);

        assertEquals(application.getName(), applicationModel.getName());
        assertEquals(application.getDescription(), applicationModel.getDescription());
        assertEquals(application.isActive(), applicationModel.getActive().booleanValue());
        assertEquals(toApplicationModelType(application.getType()), applicationModel.getType());
        assertEquals(application.getRemoteAddresses(), toAddressSet(applicationModel.getRemoteAddresses()));
    }

    @Test
    public void testToApplicationModelDirectoryMappings() throws OperationFailedException, DirectoryNotFoundException {
        final ApplicationModel applicationModel = EXAMPLE_1;
        final Application application = toApplication(applicationModel);
        // since we cannot persist directory mappings together with the application, the `toApplication` method has no mapper for them,
        // and for this reason the collection is empty
        assertTrue(application.getApplicationDirectoryMappings().isEmpty());

        final ApplicationModel.ApplicationDirectoryMapping applicationModelDirectoryMapping = applicationModel.getDirectoryMappings().iterator().next();
        final Directory directory = ImmutableDirectory
                .builder(applicationModelDirectoryMapping.getDirectoryName(), DirectoryType.INTERNAL, "internal")
                .setId(1L)
                .build();

        // since we cannot persist directory mappings together with the application, the `toApplication` method has no mapper for them,
        // and for this reason we need to construct the directory mapping of the application manually...
        final ApplicationDirectoryMapping applicationDirectoryMapping = ImmutableApplicationDirectoryMapping.builder()
                .setDirectory(directory)
                .setAllowAllToAuthenticate(applicationModelDirectoryMapping.getAuthenticationAllowAll())
                .setAuthorisedGroupNames(new HashSet<>(applicationModelDirectoryMapping.getAuthenticationGroups()))
                .setAllowedOperations(new HashSet<>(applicationModelDirectoryMapping.getAllowedOperations()))
                .build();
        // and for the same reason we now also need to create a new application with it...
        final Application applicationWithDirectoryMappings = ImmutableApplication.builder(application)
                .setApplicationDirectoryMappings(Collections.singletonList(applicationDirectoryMapping))
                .build();
        doReturn(new ArrayList<>(applicationModelDirectoryMapping.getAutoAssignmentGroups()))
                .when(defaultGroupMembershipService).listAll(applicationWithDirectoryMappings, applicationDirectoryMapping);

        final List<ApplicationModel.ApplicationDirectoryMapping> transformedApplicationModelDirectoryMappings
                = toApplicationModelDirectoryMappings(applicationWithDirectoryMappings, defaultGroupMembershipService);
        assertEquals(1, transformedApplicationModelDirectoryMappings.size());

        final ApplicationModel.ApplicationDirectoryMapping transformedApplicationModelDirectoryMapping
                = transformedApplicationModelDirectoryMappings.iterator().next();
        assertEquals(applicationModelDirectoryMapping.getDirectoryName(), transformedApplicationModelDirectoryMapping.getDirectoryName());
        assertEquals(applicationModelDirectoryMapping.getAuthenticationAllowAll(), transformedApplicationModelDirectoryMapping.getAuthenticationAllowAll());
        // if all users are allowed to authenticate, then we also expect the groups that authenticate to be empty
        assertEquals(applicationModelDirectoryMapping.getAuthenticationAllowAll().booleanValue(), transformedApplicationModelDirectoryMapping.getAuthenticationGroups().isEmpty());
        assertCollectionsEqual(applicationModelDirectoryMapping.getAutoAssignmentGroups(), transformedApplicationModelDirectoryMapping.getAutoAssignmentGroups());
        assertCollectionsEqual(applicationModelDirectoryMapping.getAllowedOperations(), transformedApplicationModelDirectoryMapping.getAllowedOperations());
    }

    private static <T> void assertCollectionsEqual(
            final List<T> expected,
            final List<T> actual) {

        assertTrue(actual.containsAll(expected) && expected.containsAll(actual));
    }

}
