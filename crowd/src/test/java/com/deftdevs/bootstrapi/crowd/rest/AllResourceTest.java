package com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import com.deftdevs.bootstrapi.commons.model.UserBean;
import com.deftdevs.bootstrapi.commons.model.GroupBean;
import com.deftdevs.bootstrapi.crowd.model.ApplicationBean;
import com.deftdevs.bootstrapi.crowd.model._AllBean;
import com.deftdevs.bootstrapi.commons.model.type._AllBeanStatus;
import com.deftdevs.bootstrapi.commons.service.api._AllService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AllResourceTest {

    @Mock
    private _AllService<_AllBean> allService;

    private _AllResourceImpl configurationResource;

    private _AllBean allBean;

    @BeforeEach
    public void setup() {
        configurationResource = new _AllResourceImpl(allService);

        // Setup test data
        allBean = new _AllBean();
        allBean.setSettings(SettingsBean.EXAMPLE_1);

        // Setup users map
        Map<String, UserBean> users = new HashMap<>();
        users.put(UserBean.EXAMPLE_1.getUsername(), UserBean.EXAMPLE_1);
        allBean.setUsers(users);

        // Setup groups map
        Map<String, GroupBean> groups = new HashMap<>();
        groups.put(GroupBean.EXAMPLE_1.getName(), GroupBean.EXAMPLE_1);
        allBean.setGroups(groups);

        // Setup applications map
        Map<String, ApplicationBean> applications = new HashMap<>();
        applications.put(ApplicationBean.EXAMPLE_1.getName(), ApplicationBean.EXAMPLE_1);
        allBean.setApplications(applications);

        Map<String, _AllBeanStatus> status = new HashMap<>();
        status.put("settings", _AllBeanStatus.success());
        status.put("users", _AllBeanStatus.success());
        status.put("groups", _AllBeanStatus.success());
        status.put("applications", _AllBeanStatus.success());
        allBean.setStatus(status);
    }

    @Test
    public void testSetConfiguration() {
        doReturn(allBean).when(allService).setAll(any());

        Response response = configurationResource.setAll(allBean);
        assertEquals(200, response.getStatus());

        _AllBean responseBean = (_AllBean) response.getEntity();
        assertEquals(allBean, responseBean);

        verify(allService).setAll(allBean);
    }
}
