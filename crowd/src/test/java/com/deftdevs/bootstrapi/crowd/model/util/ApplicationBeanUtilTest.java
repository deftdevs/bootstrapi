package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.manager.application.DefaultGroupMembershipService;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.model.application.*;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import com.deftdevs.bootstrapi.crowd.model.ApplicationBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static com.deftdevs.bootstrapi.crowd.model.ApplicationBean.EXAMPLE_1;
import static com.deftdevs.bootstrapi.crowd.model.util.ApplicationBeanUtil.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationBeanUtilTest {

    @Mock
    private DefaultGroupMembershipService defaultGroupMembershipService;

    @Mock
    private DirectoryManager directoryManager;

    @Before
    public void setup() throws DirectoryNotFoundException {
    }

    @Test
    public void testMapTypesApplication() {
        for (ApplicationType type : ApplicationType.values()) {
            assertEquals(type, toApplicationType(toApplicationBeanType(type)));
        }
        assertNull(toApplicationType(toApplicationBeanType(null)));
    }

    @Test
    public void testMapTypesApplicationBean() {
        for (ApplicationBean.ApplicationType type : ApplicationBean.ApplicationType.values()) {
            assertEquals(type, toApplicationBeanType(toApplicationType(type)));
        }
        assertNull(toApplicationBeanType(toApplicationType(null)));
    }

    @Test
    public void testToApplication() {
        Application application = toApplication(EXAMPLE_1);

        assertEquals(EXAMPLE_1.getName(), application.getName());
        assertEquals(EXAMPLE_1.getType(), toApplicationBeanType(application.getType()));
        assertEquals(EXAMPLE_1.getDescription(), application.getDescription());
        assertEquals(EXAMPLE_1.getActive().booleanValue(), application.isActive());
        assertEquals(application.getCredential().getCredential(), EXAMPLE_1.getPassword());
        assertEquals(EXAMPLE_1.getRemoteAddresses(), toStringCollection(application.getRemoteAddresses()));
    }

    @Test
    public void testToApplicationBean() {
        Application application = toApplication(EXAMPLE_1);
        ApplicationBean applicationBean = toApplicationBean(application, defaultGroupMembershipService);

        assertEquals(application.getName(), applicationBean.getName());
        assertEquals(application.getDescription(), applicationBean.getDescription());
        assertEquals(application.isActive(), applicationBean.getActive().booleanValue());
        assertEquals(toApplicationBeanType(application.getType()), applicationBean.getType());
        assertEquals(application.getRemoteAddresses(), toAddressSet(applicationBean.getRemoteAddresses()));
    }

    @Test
    public void testToApplicationBeanDirectoryMappings() throws OperationFailedException, DirectoryNotFoundException {
        final ApplicationBean applicationBean = EXAMPLE_1;
        final Application application = toApplication(applicationBean);
        // since we cannot persist directory mappings together with the application, the `toApplication` method has no mapper for them,
        // and for this reason the collection is empty
        assertTrue(application.getApplicationDirectoryMappings().isEmpty());

        final ApplicationBean.ApplicationDirectoryMapping applicationBeanDirectoryMapping = applicationBean.getDirectoryMappings().iterator().next();
        final Directory directory = ImmutableDirectory
                .builder(applicationBeanDirectoryMapping.getDirectoryName(), DirectoryType.INTERNAL, "internal")
                .setId(1L)
                .build();
        doReturn(directory).when(directoryManager).findDirectoryByName(directory.getName());

        // since we cannot persist directory mappings together with the application, the `toApplication` method has no mapper for them,
        // and for this reason we need to construct the directory mapping of the application manually...
        final ApplicationDirectoryMapping applicationDirectoryMapping = ImmutableApplicationDirectoryMapping.builder()
                .setDirectory(directory)
                .setAllowAllToAuthenticate(applicationBeanDirectoryMapping.getAuthenticationAllowAll())
                .setAuthorisedGroupNames(new HashSet<>(applicationBeanDirectoryMapping.getAuthenticationGroups()))
                .setAllowedOperations(new HashSet<>(applicationBeanDirectoryMapping.getAllowedOperations()))
                .build();
        // and for the same reason we now also need to create a new application with it...
        final Application applicationWithDirectoryMappings = ImmutableApplication.builder(application)
                .setApplicationDirectoryMappings(Collections.singletonList(applicationDirectoryMapping))
                .build();
        doReturn(new ArrayList<>(applicationBeanDirectoryMapping.getAutoAssignmentGroups()))
                .when(defaultGroupMembershipService).listAll(applicationWithDirectoryMappings, applicationDirectoryMapping);

        final Collection<ApplicationBean.ApplicationDirectoryMapping> transformedApplicationBeanDirectoryMappings
                = toApplicationBeanDirectoryMappings(applicationWithDirectoryMappings, defaultGroupMembershipService);
        assertEquals(1, transformedApplicationBeanDirectoryMappings.size());

        final ApplicationBean.ApplicationDirectoryMapping transformedApplicationBeanDirectoryMapping
                = transformedApplicationBeanDirectoryMappings.iterator().next();
        assertEquals(applicationBeanDirectoryMapping.getDirectoryName(), transformedApplicationBeanDirectoryMapping.getDirectoryName());
        assertEquals(applicationBeanDirectoryMapping.getAuthenticationAllowAll(), transformedApplicationBeanDirectoryMapping.getAuthenticationAllowAll());
        // if all users are allowed to authenticate, then we also expect the groups that authenticate to be empty
        assertEquals(applicationBeanDirectoryMapping.getAuthenticationAllowAll().booleanValue(), transformedApplicationBeanDirectoryMapping.getAuthenticationGroups().isEmpty());
        assertCollectionsEqual(applicationBeanDirectoryMapping.getAutoAssignmentGroups(), transformedApplicationBeanDirectoryMapping.getAutoAssignmentGroups());
        assertCollectionsEqual(applicationBeanDirectoryMapping.getAllowedOperations(), transformedApplicationBeanDirectoryMapping.getAllowedOperations());
    }

    private static <T> void assertCollectionsEqual(
            final Collection<T> expected,
            final Collection<T> actual) {

        assertTrue(actual.containsAll(expected) && expected.containsAll(actual));
    }

}
