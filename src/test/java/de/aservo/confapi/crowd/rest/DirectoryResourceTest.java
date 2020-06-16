package de.aservo.confapi.crowd.rest;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.MockDirectory;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import de.aservo.confapi.crowd.helper.CrowdWebAuthenticationHelper;
import de.aservo.confapi.crowd.model.DirectoryAttributesBean;
import de.aservo.confapi.crowd.model.DirectoryBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DirectoryResourceTest {

    @Mock
    private DirectoryManager directoryManager;

    @Mock
    CrowdWebAuthenticationHelper webAuthenticationHelper;

    @InjectMocks
    private DirectoryResource directoryResource;

    private final Directory directory = new MockDirectory();

    @Test
    public void testGetDirectory() throws DirectoryNotFoundException {
        when(directoryManager.findDirectoryById(directory.getId())).thenReturn(directory);
        final Response response = directoryResource.getDirectory(directory.getId());
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(DirectoryBean.class));

        final DirectoryBean directoryBean = (DirectoryBean) responseEntity;

        assertThat(directoryBean.getId(), equalTo(directory.getId()));
        assertThat(directoryBean.getName(), equalTo(directory.getName()));
        assertThat(directoryBean.getAttributes(), equalTo(DirectoryAttributesBean.from(directory)));
    }

    @Test
    public void testGetDirectoryNotFound() throws DirectoryNotFoundException {
        when(directoryManager.findDirectoryById(directory.getId())).thenThrow(new DirectoryNotFoundException(directory.getId()));
        final Response response = directoryResource.getDirectory(directory.getId());

        assertThat(response.getStatus(), equalTo(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void testGetDirectoryAttributes() throws DirectoryNotFoundException {
        when(directoryManager.findDirectoryById(directory.getId())).thenReturn(directory);
        final Response response = directoryResource.getDirectoryAttributes(directory.getId());
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(DirectoryAttributesBean.class));

        final DirectoryAttributesBean directoryAttributesBean = (DirectoryAttributesBean) responseEntity;

        assertThat(directoryAttributesBean, equalTo(DirectoryAttributesBean.from(directory)));
    }

    @Test
    public void testGetDirectoryAttributesNotFound() throws DirectoryNotFoundException {
        when(directoryManager.findDirectoryById(directory.getId())).thenThrow(new DirectoryNotFoundException(directory.getId()));
        final Response response = directoryResource.getDirectoryAttributes(directory.getId());

        assertThat(response.getStatus(), equalTo(Response.Status.NOT_FOUND.getStatusCode()));
    }

    /*
    @Test
    public void testSetSettingsCausingExceptions() {
        final String baseUrl = "thisUrlIsNotValid";
        final String mode = "invalid";
        final String title = StringUtils.repeat("A", 256);

        final SettingsBean settingsBean = new SettingsBean(
                baseUrl,
                mode,
                title);

        final Response response = settingsResource.setSettings(settingsBean);
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(ErrorCollection.class));

        final ErrorCollection errorCollection = (ErrorCollection) responseEntity;

        assertThat(errorCollection.getErrorMessages(), hasSize(3));
        assertThat(errorCollection.getErrors(), equalTo(Collections.EMPTY_MAP));
    }
    */

}
