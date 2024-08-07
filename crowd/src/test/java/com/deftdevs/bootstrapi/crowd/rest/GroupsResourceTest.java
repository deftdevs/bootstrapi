package com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.GroupBean;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class GroupsResourceTest {

    @Mock
    private GroupsService groupsService;

    private GroupsResourceImpl groupsResource;

    @BeforeEach
    public void setup() {
        groupsResource = new GroupsResourceImpl(groupsService);
    }

    @Test
    public void testSetGroups() {
        final List<GroupBean> groupBeans = Collections.singletonList(GroupBean.EXAMPLE_1);
        doReturn(groupBeans).when(groupsService).setGroups(anyLong(), any());

        final Response response = groupsResource.setGroups(0L, groupBeans);
        assertEquals(200, response.getStatus());

        final List<GroupBean> responseGroupBeans = (List<GroupBean>) response.getEntity();
        assertEquals(groupBeans, responseGroupBeans);
    }

}
