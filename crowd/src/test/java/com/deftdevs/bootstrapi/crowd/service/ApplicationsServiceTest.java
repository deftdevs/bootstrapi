package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.exception.ApplicationAlreadyExistsException;
import com.atlassian.crowd.exception.ApplicationNotFoundException;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.exception.InvalidCredentialException;
import com.atlassian.crowd.manager.application.ApplicationManager;
import com.atlassian.crowd.manager.application.ApplicationManagerException;
import com.atlassian.crowd.manager.application.DefaultGroupMembershipService;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.model.application.Application;
import com.atlassian.crowd.model.application.ImmutableApplication;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.exception.InternalServerErrorException;
import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.crowd.model.ApplicationBean;
import de.aservo.confapi.crowd.model.ApplicationsBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static de.aservo.confapi.crowd.model.ApplicationBean.EXAMPLE_1;
import static de.aservo.confapi.crowd.model.ApplicationBean.EXAMPLE_2;
import static de.aservo.confapi.crowd.model.util.ApplicationBeanUtil.toApplication;
import static de.aservo.confapi.crowd.model.util.ApplicationBeanUtil.toStringCollection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationsServiceTest {

    @Mock
    private ApplicationManager applicationManager;

    @Mock
    private DefaultGroupMembershipService defaultGroupMembershipService;

    @Mock
    private DirectoryManager directoryManager;

    private ApplicationsServiceImpl applicationsService;

    @Before
    public void setup() throws DirectoryNotFoundException {
        for (Directory directory : getDirectories()) {
            doReturn(directory).when(directoryManager).findDirectoryByName(directory.getName());
        }

        applicationsService = new ApplicationsServiceImpl(applicationManager, defaultGroupMembershipService, directoryManager);
    }

    @Test
    public void testGetApplication() throws ApplicationNotFoundException {
        Application application = toApplication(EXAMPLE_1);
        doReturn(application).when(applicationManager).findById(anyLong());

        ApplicationBean resultingApplicationBean = applicationsService.getApplication(1L);
        assertEquals(EXAMPLE_1.getName(), resultingApplicationBean.getName());
        assertEquals(EXAMPLE_1.getDescription(), resultingApplicationBean.getDescription());
        assertEquals(EXAMPLE_1.getActive(), resultingApplicationBean.getActive());
        assertEquals(EXAMPLE_1.getType(), resultingApplicationBean.getType());
        assertEquals(EXAMPLE_1.getRemoteAddresses(), resultingApplicationBean.getRemoteAddresses());
    }

    @Test
    public void testGetApplications() {
        Application application = toApplication(EXAMPLE_1);
        Collection<Application> applications = Collections.singletonList(application);

        doReturn(applications).when(applicationManager).findAll();

        ApplicationsBean applicationBeans = applicationsService.getApplications();
        assertNotNull(applicationBeans);
        Collection<ApplicationBean> applicationsCollection = applicationBeans.getApplications();
        assertNotNull(applicationsCollection);
        assertEquals(1,applicationsCollection.size());

        ApplicationBean resultApplicationBean = applicationsCollection.iterator().next();

        assertEquals(EXAMPLE_1.getName(), resultApplicationBean.getName());
        assertEquals(EXAMPLE_1.getDescription(), resultApplicationBean.getDescription());
        assertEquals(EXAMPLE_1.getActive(), resultApplicationBean.getActive());
        assertEquals(EXAMPLE_1.getType(), resultApplicationBean.getType());
        assertEquals(EXAMPLE_1.getRemoteAddresses(), resultApplicationBean.getRemoteAddresses());
    }

    @Test
    public void testAddApplication() throws InvalidCredentialException, ApplicationAlreadyExistsException, ApplicationNotFoundException {
        Application application = toApplication(EXAMPLE_1);
        doReturn(application).when(applicationManager).findById(application.getId());
        doReturn(application).when(applicationManager).add(application);

        ApplicationBean resultApplicationBean = applicationsService.addApplication(EXAMPLE_1);
        assertEquals(EXAMPLE_1.getActive(), resultApplicationBean.getActive());
        assertEquals(EXAMPLE_1.getName(), resultApplicationBean.getName());
        assertEquals(EXAMPLE_1.getDescription(), resultApplicationBean.getDescription());
        assertEquals(EXAMPLE_1.getType(), resultApplicationBean.getType());
        assertEquals(EXAMPLE_1.getRemoteAddresses(), resultApplicationBean.getRemoteAddresses());
    }

    @Test
    public void testAddApplicationEnsurePersistenceCalls() throws InvalidCredentialException, ApplicationAlreadyExistsException, ApplicationNotFoundException {
        final ApplicationBean applicationBean = EXAMPLE_1;
        final Application applicationWithDirectoryMappings = ImmutableApplication.builder(toApplication(applicationBean))
                .setApplicationDirectoryMappings(applicationsService.toApplicationDirectoryMappings(applicationBean.getDirectoryMappings()))
                .build();
        doReturn(applicationWithDirectoryMappings).when(applicationManager).add(any(Application.class));
        doReturn(applicationWithDirectoryMappings).when(applicationManager).findById(applicationWithDirectoryMappings.getId());

        final ApplicationsServiceImpl spy = spy(applicationsService);
        spy.addApplication(applicationBean);
        verify(spy).persistApplicationDirectoryMappings(any(), any());
        verify(spy).persistApplicationBeanAuthenticationGroups(any(), any());
        verify(spy).persistApplicationBeanAutoAssignmentGroups(any(), any());
    }

    @Test
    public void testSetApplicationAllAttributes() throws ApplicationNotFoundException, ApplicationManagerException {
        Application application1 = toApplication(EXAMPLE_1);
        ApplicationBean requestApplicationBean = new ApplicationBean();
        requestApplicationBean.setId(application1.getId());
        requestApplicationBean.setName("Changed Name");
        requestApplicationBean.setDescription("Changed Description");
        requestApplicationBean.setActive(false);
        requestApplicationBean.setPassword("password1");
        requestApplicationBean.setRemoteAddresses(Collections.singletonList("127.0.0.5"));
        Application application2 = toApplication(requestApplicationBean);

        doReturn(application1).when(applicationManager).findById(anyLong());
        doReturn(application2).when(applicationManager).update(any(Application.class));

        final ArgumentCaptor<Application> applicationCaptor = ArgumentCaptor.forClass(Application.class);
        applicationsService.setApplication(EXAMPLE_1.getId(), requestApplicationBean);
        verify(applicationManager).update(applicationCaptor.capture());
        verify(applicationManager).updateCredential(any(), any());
        final Application updatedApplication = applicationCaptor.getValue();

        assertNotNull(updatedApplication);
        assertEquals(requestApplicationBean.getName(), updatedApplication.getName());
        assertEquals(requestApplicationBean.getDescription(), updatedApplication.getDescription());
        assertEquals(requestApplicationBean.getActive(), updatedApplication.isActive());
        // it's not possible to change the application type
        assertEquals(requestApplicationBean.getRemoteAddresses() , toStringCollection(updatedApplication.getRemoteAddresses()));
    }

    @Test
    public void testSetApplicationNoAttributes() throws ApplicationNotFoundException, ApplicationManagerException {
        Application application = toApplication(EXAMPLE_1);
        doReturn(application).when(applicationManager).findById(anyLong());
        doReturn(application).when(applicationManager).update(any(Application.class));
        ApplicationBean requestApplicationBean = new ApplicationBean();

        final ArgumentCaptor<Application> applicationCaptor = ArgumentCaptor.forClass(Application.class);
        applicationsService.setApplication(100, requestApplicationBean);
        verify(applicationManager).update(applicationCaptor.capture());
        final Application updatedApplication = applicationCaptor.getValue();

        assertNotNull(updatedApplication);
        assertEquals(application.getName(), updatedApplication.getName());
        assertEquals(application.getDescription(), updatedApplication.getDescription());
        assertEquals(application.isActive(), updatedApplication.isActive());
        assertEquals(application.getType(), updatedApplication.getType());
        assertEquals(application.getRemoteAddresses() , updatedApplication.getRemoteAddresses());
    }

    @Test
    public void testSetApplicationEnsurePersistenceCalls() throws ApplicationNotFoundException, ApplicationManagerException {
        final ApplicationBean applicationBean = EXAMPLE_1;
        final Application applicationWithDirectoryMappings = ImmutableApplication.builder(toApplication(applicationBean))
                .setApplicationDirectoryMappings(applicationsService.toApplicationDirectoryMappings(applicationBean.getDirectoryMappings()))
                .build();
        doReturn(applicationWithDirectoryMappings).when(applicationManager).findById(applicationWithDirectoryMappings.getId());
        doReturn(applicationWithDirectoryMappings).when(applicationManager).update(any(Application.class));

        final ApplicationsServiceImpl spy = spy(applicationsService);
        spy.setApplication(applicationBean.getId(), applicationBean);
        verify(spy).persistApplicationDirectoryMappings(any(), any());
        verify(spy).persistApplicationBeanAuthenticationGroups(any(), any());
        verify(spy).persistApplicationBeanAutoAssignmentGroups(any(), any());
    }

    @Test
    public void testSetApplications() throws ApplicationNotFoundException {
        Application applicationExample1 = ImmutableApplication.builder(toApplication(EXAMPLE_1))
                .setId(EXAMPLE_1.getId())
                .build();

        List<ApplicationBean> applicationList = new ArrayList<>();
        applicationList.add(EXAMPLE_1);
        applicationList.add(EXAMPLE_2);

        ApplicationsBean applicationsBean = new ApplicationsBean(applicationList);

        doReturn(applicationExample1).when(applicationManager).findByName(EXAMPLE_1.getName());
        doThrow(new ApplicationNotFoundException("")).when(applicationManager).findByName(EXAMPLE_2.getName());

        ApplicationsServiceImpl spy = spy(applicationsService);
        doReturn(EXAMPLE_1).when(spy).setApplication(EXAMPLE_1.getId(), EXAMPLE_1);
        doReturn(EXAMPLE_2).when(spy).addApplication(EXAMPLE_2);

        ApplicationsBean responseApplicationsBean = spy.setApplications(applicationsBean);

        verify(spy).setApplication(EXAMPLE_1.getId(), EXAMPLE_1);
        verify(spy).addApplication(EXAMPLE_2);
        assertEquals(applicationList.size(), responseApplicationsBean.getApplications().size());
    }

    @Test
    public void testDeleteApplication() throws ApplicationNotFoundException, ApplicationManagerException {
        Application application = toApplication(EXAMPLE_1);

        doReturn(application).when(applicationManager).findById(anyLong());
        applicationsService.deleteApplication(1);

        verify(applicationManager).remove(application);
    }

    @Test
    public void testDeleteApplications() throws ApplicationNotFoundException {
        Application application1 = ImmutableApplication.builder(toApplication(EXAMPLE_1))
                .setId(1L)
                .build();

        Application application2 = ImmutableApplication.builder(toApplication(EXAMPLE_2))
                .setId(2L)
                .build();

        ArrayList<Application> arrayList = new ArrayList<>();
        arrayList.add(application1);
        arrayList.add(application2);

        doReturn(arrayList).when(applicationManager).findAll();
        doReturn(application1).when(applicationManager).findById(1);
        doReturn(application2).when(applicationManager).findById(2);

        ApplicationsServiceImpl spy = spy(applicationsService);
        spy.deleteApplications(true);

        verify(spy).deleteApplication(application1.getId());
        verify(spy).deleteApplication(application2.getId());
    }

    // exception test cases to feed Sonarqube

    @Test(expected = BadRequestException.class)
    public void testDeleteApplicationsForceFalse() {
        doReturn(Collections.emptyList()).when(applicationManager).findAll();

        applicationsService.deleteApplications(false);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteApplicationBadRequestException() throws ApplicationNotFoundException {
        doThrow(new ApplicationNotFoundException("any")).when(applicationManager).findById(anyLong());
        applicationsService.deleteApplication(1);
    }

    @Test(expected = InternalServerErrorException.class)
    public void testDeleteApplicationInternalServerErrorException() throws ApplicationManagerException {
        doThrow(new ApplicationManagerException()).when(applicationManager).remove(any(Application.class));
        applicationsService.deleteApplication(1);
    }

    @Test(expected = NotFoundException.class)
    public void testSetBeanApplicationNotFoundException() throws ApplicationNotFoundException {
        doThrow(new ApplicationNotFoundException("app")).when(applicationManager).findById(anyLong());
        applicationsService.setApplication(1, EXAMPLE_1);
    }

    @Test(expected = InternalServerErrorException.class)
    public void testSetBeanApplicationManagerException() throws ApplicationNotFoundException, ApplicationManagerException {
        doReturn(toApplication(EXAMPLE_1)).when(applicationManager).findById(anyLong());
        doThrow(new ApplicationManagerException("x")).when(applicationManager).update(any(Application.class));
        applicationsService.setApplication(EXAMPLE_1.getId(), EXAMPLE_1);
    }

    @Test(expected = BadRequestException.class)
    public void testAddApplicationCredentialsException() throws ApplicationAlreadyExistsException, InvalidCredentialException {
        doThrow(new InvalidCredentialException()).when(applicationManager).add(any(Application.class));
        applicationsService.addApplication(EXAMPLE_1);
    }

    @Test(expected = NotFoundException.class)
    public void testGetApplicationException() throws ApplicationNotFoundException {
        doThrow(new ApplicationNotFoundException("")).when(applicationManager).findById(anyLong());
        applicationsService.getApplication(1L);
    }

    private static Collection<Directory> getDirectories() {
        final Directory directory = ImmutableDirectory
                .builder("directory", DirectoryType.INTERNAL, "internal")
                .setId(1L)
                .build();

        return Collections.singletonList(directory);
    }

}
