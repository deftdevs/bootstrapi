package de.aservo.atlassian.crowd.confapi.bean;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.MockDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DirectoryBeanTest {

    private Directory directory = new MockDirectory();

    @Test
    public void testConstructWithDefaultConstructor() {
        final DirectoryBean directoryBean = new DirectoryBean();

        assertNull(directoryBean.getId());
        assertNull(directoryBean.getName());
        assertNull(directoryBean.getAttributes());
    }

    @Test
    public void testConstruct() {
        final DirectoryBean directoryBean = new DirectoryBean(directory);
        final DirectoryAttributesBean directoryAttributesBean = new DirectoryAttributesBean(directory);

        assertThat(directoryBean.getId(), equalTo(directory.getId()));
        assertThat(directoryBean.getName(), equalTo(directory.getName()));
        assertThat(directoryBean.getAttributes(), equalTo(directoryAttributesBean));
    }

    @Test
    public void testEqual() {
        final DirectoryBean directoryBean1 = new DirectoryBean(directory);
        final DirectoryBean directoryBean2 = new DirectoryBean(directory);

        assertThat(directoryBean1, equalTo(directoryBean2));
    }

}
