package de.aservo.confapi.crowd.model.util;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.MockDirectory;
import de.aservo.confapi.commons.model.DirectoryInternalBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static com.atlassian.crowd.directory.AbstractInternalDirectory.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DirectoryInternalBeanUtilTest {

    @Test
    public void testToDirectoryBean() {
        final Directory directory = new MockDirectory();
        final DirectoryInternalBean directoryBean = DirectoryInternalBeanUtil.toDirectoryInternalBean(directory);

        assertNotNull(directoryBean);
        assertEquals(directoryBean.getId(), directory.getId());
        assertEquals(directoryBean.getName(), directory.getName());
        assertEquals(directoryBean.getDescription(), directory.getDescription());
        assertEquals(directoryBean.isActive(), directory.isActive());

        final Map<String, String> attributes = directory.getAttributes();
        assertNotNull(directoryBean.getCredentialPolicy());
        assertEquals(attributes.get(ATTRIBUTE_PASSWORD_REGEX), directoryBean.getCredentialPolicy().getPasswordRegex());
        assertEquals(attributes.get(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE), directoryBean.getCredentialPolicy().getPasswordComplexityMessage());
        assertEquals(Long.valueOf(attributes.get(ATTRIBUTE_PASSWORD_MAX_ATTEMPTS)), directoryBean.getCredentialPolicy().getPasswordMaxAttempts());
        assertNull(directoryBean.getCredentialPolicy().getPasswordHistoryCount());
        assertEquals(Long.valueOf(attributes.get(ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME)), directoryBean.getCredentialPolicy().getPasswordMaxChangeTime());
        assertNotNull(directoryBean.getCredentialPolicy().getPasswordMaxChangeTime());
        assertEquals(attributes.get(ATTRIBUTE_USER_ENCRYPTION_METHOD), directoryBean.getCredentialPolicy().getPasswordEncryptionMethod());
    }

    @Test
    public void testToDirectoryBeanWithNull() {
        assertNull(DirectoryInternalBeanUtil.toDirectoryInternalBean(null));
    }

    @Test
    public void testToDirectory() {
        final DirectoryInternalBean directoryBean = DirectoryInternalBean.EXAMPLE_1;
        final Directory directory = DirectoryInternalBeanUtil.toDirectory(directoryBean);

        assertNotNull(directory);
        assertEquals(directory.getId(), directoryBean.getId());
        assertEquals(directory.getName(), directoryBean.getName());

        final Map<String, String> attributes = directory.getAttributes();
        assertNotNull(directory.getAttributes());
        assertEquals(directoryBean.getCredentialPolicy().getPasswordRegex(), attributes.get(ATTRIBUTE_PASSWORD_REGEX));
        assertEquals(directoryBean.getCredentialPolicy().getPasswordComplexityMessage(), attributes.get(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE));
        assertEquals(String.valueOf(directoryBean.getCredentialPolicy().getPasswordMaxAttempts()), attributes.get(ATTRIBUTE_PASSWORD_MAX_ATTEMPTS));
        assertEquals(String.valueOf(directoryBean.getCredentialPolicy().getPasswordHistoryCount()), attributes.get(ATTRIBUTE_PASSWORD_HISTORY_COUNT));
        assertEquals(String.valueOf(directoryBean.getCredentialPolicy().getPasswordMaxChangeTime()), attributes.get(ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME));
        assertNotNull(attributes.get(ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS));
        assertEquals(directoryBean.getCredentialPolicy().getPasswordEncryptionMethod(), attributes.get(ATTRIBUTE_USER_ENCRYPTION_METHOD));
    }

    @Test
    public void testToDirectoryWithNull() {
        assertNull(DirectoryInternalBeanUtil.toDirectory(null));
    }

}
