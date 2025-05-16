package com.deftdevs.bootstrapi.jira.model.util;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.atlassian.crowd.directory.RemoteCrowdDirectory.*;
import static com.atlassian.crowd.directory.SynchronisableDirectoryProperties.INCREMENTAL_SYNC_ENABLED;
import static com.atlassian.crowd.model.directory.DirectoryImpl.ATTRIBUTE_KEY_USE_NESTED_GROUPS;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DirectoryModelUtilTest {

    @Test
    void testToDirectoryWithoutProxy() {
        final DirectoryCrowdModel bean = DirectoryCrowdModel.EXAMPLE_1;
        final Directory directory = DirectoryModelUtil.toDirectory(bean);

        assertNotNull(directory);
        assertEquals(directory.getName(), bean.getName());

        final Map<String, String> attributes = directory.getAttributes();
        assertEquals(attributes.get(CROWD_SERVER_URL), bean.getServer().getUrl().toString());
        assertEquals(attributes.get(APPLICATION_PASSWORD), bean.getServer().getAppPassword());
    }

    @Test
    void testToDirectoryWithProxy() {
        final DirectoryCrowdModel bean = DirectoryCrowdModel.EXAMPLE_1_WITH_PROXY;
        final Directory directory = DirectoryModelUtil.toDirectory(bean);

        assertNotNull(directory);
        assertEquals(directory.getName(), bean.getName());

        final Map<String, String> attributes = directory.getAttributes();
        assertEquals(bean.getServer().getUrl().toString(), attributes.get(CROWD_SERVER_URL));
        assertEquals(bean.getServer().getAppPassword(), attributes.get(APPLICATION_PASSWORD));
        assertEquals(bean.getServer().getProxy().getHost(), attributes.get(CROWD_HTTP_PROXY_HOST));
        assertNull(bean.getServer().getProxy().getPort());
        assertNull(attributes.get(CROWD_HTTP_PROXY_PORT));
        assertEquals(bean.getServer().getProxy().getUsername(), attributes.get(CROWD_HTTP_PROXY_USERNAME));
        assertEquals(bean.getServer().getProxy().getPassword(), attributes.get(CROWD_HTTP_PROXY_PASSWORD));
    }

    @Test
    void testToDirectoryModelWithProxy() {
        final DirectoryImpl directory = new DirectoryImpl("test", DirectoryType.CROWD, "test.class");
        directory.setAttribute(CROWD_SERVER_URL, "http://localhost");
        directory.setAttribute(APPLICATION_PASSWORD, "test");
        directory.setAttribute(APPLICATION_NAME, "confluence-client");
        directory.setAttribute(CROWD_HTTP_PROXY_HOST, "http://localhost/proxy");
        directory.setAttribute(CROWD_HTTP_PROXY_PORT, "8080");
        directory.setAttribute(CROWD_HTTP_PROXY_USERNAME, "user");
        directory.setAttribute(CROWD_HTTP_PROXY_PASSWORD, "pass");
        directory.setAttribute(CROWD_HTTP_TIMEOUT, "");
        directory.setAttribute(CROWD_HTTP_MAX_CONNECTIONS, "");
        directory.setAttribute(APPLICATION_NAME, "");
        directory.setAttribute(APPLICATION_PASSWORD, "");
        directory.setAttribute(INCREMENTAL_SYNC_ENABLED, "");
        directory.setAttribute(ATTRIBUTE_KEY_USE_NESTED_GROUPS, "");
        directory.setAttribute(INCREMENTAL_SYNC_ENABLED, "");
        directory.setAttribute(INCREMENTAL_SYNC_ENABLED, "");

        final DirectoryCrowdModel directoryModel = (DirectoryCrowdModel) DirectoryModelUtil.toDirectoryModel(directory);

        assertNotNull(directoryModel);
        assertEquals(directory.getName(), directoryModel.getName());

        final Map<String, String> attributes = directory.getAttributes();
        assertEquals(directory.getId(), directoryModel.getId());
        assertEquals(attributes.get(CROWD_SERVER_URL), directoryModel.getServer().getUrl().toString());
        assertNull(directoryModel.getServer().getAppPassword());
        assertEquals(attributes.get(CROWD_HTTP_PROXY_HOST), directoryModel.getServer().getProxy().getHost());
        assertEquals(attributes.get(CROWD_HTTP_PROXY_PORT), directoryModel.getServer().getProxy().getPort().toString());
        assertEquals(attributes.get(CROWD_HTTP_PROXY_USERNAME), directoryModel.getServer().getProxy().getUsername());
        assertNull(directoryModel.getServer().getProxy().getPassword());
    }

}
