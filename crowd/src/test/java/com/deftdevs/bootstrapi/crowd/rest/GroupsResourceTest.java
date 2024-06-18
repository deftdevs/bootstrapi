package de.aservo.confapi.crowd.rest;

import de.aservo.confapi.commons.model.GroupBean;
import de.aservo.confapi.crowd.model.GroupsBean;
import de.aservo.confapi.crowd.service.api.GroupsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class GroupsResourceTest {

    @Mock
    private GroupsService groupsService;

    private GroupsResourceImpl groupsResource;

    @Before
    public void setup() {
        groupsResource = new GroupsResourceImpl(groupsService);
    }

    @Test
    public void testGetGroup() {
        final GroupBean groupBean = GroupBean.EXAMPLE_1;
        doReturn(groupBean).when(groupsService).getGroup(anyLong(), anyString());

        final Response response = groupsResource.getGroup(0L, groupBean.getName());
        assertEquals(200, response.getStatus());

        final GroupBean responseGroupBean = (GroupBean) response.getEntity();
        assertEquals(groupBean, responseGroupBean);
    }

    @Test
    public void testCreateGroup() {
        final GroupBean groupBean = GroupBean.EXAMPLE_1;
        doReturn(groupBean).when(groupsService).createGroup(anyLong(), any());

        final Response response = groupsResource.createGroup(0L, groupBean);
        assertEquals(200, response.getStatus());

        final GroupBean responseGroupBean = (GroupBean) response.getEntity();
        assertEquals(groupBean, responseGroupBean);
    }

    @Test
    public void testUpdateGroup() {
        final GroupBean groupBean = GroupBean.EXAMPLE_1;
        doReturn(groupBean).when(groupsService).updateGroup(anyLong(), anyString(), any());

        final Response response = groupsResource.updateGroup(0L, groupBean.getName(), groupBean);
        assertEquals(200, response.getStatus());

        final GroupBean responseGroupBean = (GroupBean) response.getEntity();
        assertEquals(groupBean, responseGroupBean);
    }

    @Test
    public void testSetGroups() {
        final GroupsBean groupsBean = new GroupsBean(Collections.singletonList(GroupBean.EXAMPLE_1));
        doReturn(groupsBean).when(groupsService).setGroups(anyLong(), any());

        final Response response = groupsResource.setGroups(0L, groupsBean);
        assertEquals(200, response.getStatus());

        final GroupsBean responseGroupsBean = (GroupsBean) response.getEntity();
        assertEquals(groupsBean, responseGroupsBean);
    }

}
