package de.aservo.confapi.crowd.bean;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.MockDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DirectoryAttributesBeanTest {

    private Directory directory = new MockDirectory();

    @Test
    public void testConstructWithDefaultConstructor() {
        final DirectoryAttributesBean directoryAttributesBean = new DirectoryAttributesBean();

        assertNull(directoryAttributesBean.getPasswordRegex());
        assertNull(directoryAttributesBean.getPasswordComplexityMessage());
        assertNull(directoryAttributesBean.getPasswordMaxAttempts());
        assertNull(directoryAttributesBean.getPasswordHistoryCount());
        assertNull(directoryAttributesBean.getPasswordMaxChangeTime());
    }

    @Test
    public void testConstruct() {
        final DirectoryAttributesBean directoryAttributesBean = DirectoryAttributesBean.from(directory);

        assertThat(directoryAttributesBean.getPasswordRegex(), equalTo(MockDirectory.ATTRIBUTE_PASSWORD_REGEX_VALUE));
        assertThat(directoryAttributesBean.getPasswordComplexityMessage(), equalTo(MockDirectory.ATTRIBUTE_PASSWORD_COMPLEXITY_MESSAGE_VALUE));
        assertThat(directoryAttributesBean.getPasswordMaxAttempts(), equalTo(MockDirectory.ATTRIBUTE_PASSWORD_MAX_ATTEMPTS_VALUE));
        assertThat(directoryAttributesBean.getPasswordHistoryCount(), equalTo(MockDirectory.ATTRIBUTE_PASSWORD_HISTORY_COUNT_VALUE));
        assertThat(directoryAttributesBean.getPasswordMaxChangeTime(), equalTo(MockDirectory.ATTRIBUTE_PASSWORD_MAX_CHANGE_TIME_VALUE));
    }

    @Test
    public void testEquals() {
        final DirectoryAttributesBean directoryAttributesBean1 = DirectoryAttributesBean.from(directory);
        final DirectoryAttributesBean directoryAttributesBean2 = DirectoryAttributesBean.from(directory);

        assertThat(directoryAttributesBean1, equalTo(directoryAttributesBean2));
    }

}
