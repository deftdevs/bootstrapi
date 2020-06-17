package de.aservo.confapi.crowd.model.util;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.MockDirectory;
import de.aservo.confapi.crowd.model.DirectoryBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static com.atlassian.crowd.directory.AbstractInternalDirectory.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DirectoryBeanUtilTest {

    @Test
    public void testToDirectoryBean() {
        final Directory directory = new MockDirectory();
        final DirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directory);

        assertNotNull(directoryBean);
        assertEquals(directoryBean.getId(), directory.getId());
        assertEquals(directoryBean.getName(), directory.getName());
        assertEquals(directoryBean.getDescription(), directory.getDescription());
        assertEquals(directoryBean.getActive(), directory.isActive());

        final Map<String, String> attributes = directory.getAttributes();
        assertNotNull(directoryBean.getConfiguration());
        assertEquals(attributes.get(ATTRIBUTE_PASSWORD_REGEX), directoryBean.getConfiguration().getPasswordRegex());
        assertEquals(attributes.get(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE), directoryBean.getConfiguration().getPasswordComplexityMessage());
        assertEquals(Long.valueOf(attributes.get(ATTRIBUTE_PASSWORD_MAX_ATTEMPTS)), directoryBean.getConfiguration().getPasswordMaxAttempts());
        assertNull(directoryBean.getConfiguration().getPasswordHistoryCount());
        assertEquals(Long.valueOf(attributes.get(ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME)), directoryBean.getConfiguration().getPasswordMaxChangeTime());
        assertNotNull(directoryBean.getConfiguration().getPasswordMaxChangeTime());
        assertEquals(attributes.get(ATTRIBUTE_USER_ENCRYPTION_METHOD), directoryBean.getConfiguration().getPasswordEncryptionMethod());
    }

    @Test
    public void testToDirectoryBeanWithNull() {
        assertNull(DirectoryBeanUtil.toDirectoryBean(null));
    }

    @Test
    public void testToDirectory() {
        final DirectoryBean directoryBean = DirectoryBean.EXAMPLE_1;
        final Directory directory = DirectoryBeanUtil.toDirectory(directoryBean);

        assertNotNull(directory);
        assertEquals(directory.getId(), directoryBean.getId());
        assertEquals(directory.getName(), directoryBean.getName());

        final Map<String, String> attributes = directory.getAttributes();
        assertNotNull(directory.getAttributes());
        assertEquals(directoryBean.getConfiguration().getPasswordRegex(), attributes.get(ATTRIBUTE_PASSWORD_REGEX));
        assertEquals(directoryBean.getConfiguration().getPasswordComplexityMessage(), attributes.get(ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE));
        assertEquals(String.valueOf(directoryBean.getConfiguration().getPasswordMaxAttempts()), attributes.get(ATTRIBUTE_PASSWORD_MAX_ATTEMPTS));
        assertEquals(String.valueOf(directoryBean.getConfiguration().getPasswordHistoryCount()), attributes.get(ATTRIBUTE_PASSWORD_HISTORY_COUNT));
        assertEquals(String.valueOf(directoryBean.getConfiguration().getPasswordMaxChangeTime()), attributes.get(ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME));
        assertNotNull(attributes.get(ATTRIBUTE_PASSWORD_EXPIRATION_NOTIFICATION_PERIODS));
        assertEquals(directoryBean.getConfiguration().getPasswordEncryptionMethod(), attributes.get(ATTRIBUTE_USER_ENCRYPTION_METHOD));
    }

    @Test
    public void testToDirectoryWithNull() {
        assertNull(DirectoryBeanUtil.toDirectory(null));
    }

}
