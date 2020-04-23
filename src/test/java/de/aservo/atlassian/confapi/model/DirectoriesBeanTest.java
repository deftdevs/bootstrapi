package de.aservo.atlassian.confapi.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DirectoriesBeanTest {

    @Test
    public void testParameterConstructor() {
        final DirectoryBean directoryBean = new DirectoryBean();
        directoryBean.setName("Directory");

        final Collection<DirectoryBean> directoryBeans = Collections.singletonList(directoryBean);
        final DirectoriesBean directoriesBean = new DirectoriesBean(directoryBeans);

        assertEquals(directoriesBean.getDirectories(), directoryBeans);
    }

}
