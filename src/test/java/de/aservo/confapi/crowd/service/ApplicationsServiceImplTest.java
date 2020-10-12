package de.aservo.confapi.crowd.service;
import com.atlassian.crowd.exception.ApplicationAlreadyExistsException;
import com.atlassian.crowd.exception.ApplicationNotFoundException;
import com.atlassian.crowd.exception.InvalidCredentialException;
import com.atlassian.crowd.manager.application.ApplicationManager;
import com.atlassian.crowd.manager.application.ApplicationManagerException;
import com.atlassian.crowd.model.application.Application;
import com.atlassian.crowd.model.application.ImmutableApplication;
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

import java.util.*;

import static de.aservo.confapi.crowd.model.ApplicationBean.EXAMPLE_1;
import static de.aservo.confapi.crowd.model.ApplicationBean.EXAMPLE_2;
import static de.aservo.confapi.crowd.model.util.ApplicationBeanUtil.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationsServiceImplTest {

    @Mock
    private ApplicationManager applicationManager;

    private ApplicationsServiceImpl applicationsService;

    @Before
    public void setup() {
        applicationsService = new ApplicationsServiceImpl(applicationManager);
    }

    @Test
    public void testAddApplication() throws InvalidCredentialException, ApplicationAlreadyExistsException {
        Application application = toApplication(EXAMPLE_1);

        doReturn(application).when(applicationManager).add(application);
        ApplicationBean resultApplicationBean = applicationsService.addApplication(EXAMPLE_1);

        assertEquals(EXAMPLE_1.getActive(), resultApplicationBean.getActive());
        assertEquals(EXAMPLE_1.getName(), resultApplicationBean.getName());
        assertEquals(EXAMPLE_1.getDescription(), resultApplicationBean.getDescription());
        assertEquals(EXAMPLE_1.getType(), resultApplicationBean.getType());
    }

    @Test
    public void testSetApplicationAllAttributes() throws ApplicationNotFoundException, ApplicationManagerException {
        Application application1 = toApplication(EXAMPLE_1);
        ApplicationBean requestApplicationBean = new ApplicationBean();
        requestApplicationBean.setName("Changed Name");
        requestApplicationBean.setDescription("Changed Description");
        requestApplicationBean.setActive(false);
        requestApplicationBean.setType(ApplicationBean.ApplicationType.BAMBOO);
        Application application2 = toApplication(requestApplicationBean);

        doReturn(application1).when(applicationManager).findById(anyLong());
        doReturn(application2).when(applicationManager).update(any(Application.class));

        final ArgumentCaptor<Application> applicationCaptor = ArgumentCaptor.forClass(Application.class);
        applicationsService.setApplication(EXAMPLE_1.getId(), requestApplicationBean);
        verify(applicationManager).update(applicationCaptor.capture());
        final Application updatedApplication = applicationCaptor.getValue();

        assertNotNull(updatedApplication);
        assertEquals(requestApplicationBean.getName(), updatedApplication.getName());
        assertEquals(requestApplicationBean.getDescription(), updatedApplication.getDescription());
        assertEquals(requestApplicationBean.getActive(), updatedApplication.isActive());
        assertEquals(requestApplicationBean.getType(), toApplicationBeanType(updatedApplication.getType()));
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
    }

    @Test
    public void testSetApplications() throws ApplicationNotFoundException, ApplicationManagerException {
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

    @Test
    public void testGetApplication() throws ApplicationNotFoundException {
        Application application = toApplication(EXAMPLE_1);
        doReturn(application).when(applicationManager).findById(anyLong());

        ApplicationBean resultingApplicationBean = applicationsService.getApplication(1L);
        assertEquals(EXAMPLE_1.getName(), resultingApplicationBean.getName());
        assertEquals(EXAMPLE_1.getDescription(), resultingApplicationBean.getDescription());
        assertEquals(EXAMPLE_1.getActive(), resultingApplicationBean.getActive());
        assertEquals(EXAMPLE_1.getType(), resultingApplicationBean.getType());
    }

    @Test(expected = NotFoundException.class)
    public void testGetApplicationException() throws ApplicationNotFoundException {
        doThrow(new ApplicationNotFoundException("")).when(applicationManager).findById(anyLong());
        applicationsService.getApplication(1L);
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
    }
}
