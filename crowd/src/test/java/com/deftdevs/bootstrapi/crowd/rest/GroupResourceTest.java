package com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.GroupModel;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class GroupResourceTest {

    @Mock
    private GroupsService groupsService;

    private GroupResourceImpl groupResource;

    @BeforeEach
    public void setup() {
        groupResource = new GroupResourceImpl(groupsService);
    }

    @Test
    public void testGetGroup() {
        final GroupModel groupModel = GroupModel.EXAMPLE_1;
        doReturn(groupModel).when(groupsService).getGroup(anyLong(), anyString());

        final Response response = groupResource.getGroup(0L, groupModel.getName());
        assertEquals(200, response.getStatus());

        final GroupModel responseGroupModel = (GroupModel) response.getEntity();
        assertEquals(groupModel, responseGroupModel);
    }

    @Test
    public void testCreateGroup() {
        final GroupModel groupModel = GroupModel.EXAMPLE_1;
        doReturn(groupModel).when(groupsService).createGroup(anyLong(), any());

        final Response response = groupResource.createGroup(0L, groupModel);
        assertEquals(200, response.getStatus());

        final GroupModel responseGroupModel = (GroupModel) response.getEntity();
        assertEquals(groupModel, responseGroupModel);
    }

    @Test
    public void testUpdateGroup() {
        final GroupModel groupModel = GroupModel.EXAMPLE_1;
        doReturn(groupModel).when(groupsService).updateGroup(anyLong(), anyString(), any());

        final Response response = groupResource.updateGroup(0L, groupModel.getName(), groupModel);
        assertEquals(200, response.getStatus());

        final GroupModel responseGroupModel = (GroupModel) response.getEntity();
        assertEquals(groupModel, responseGroupModel);
    }

}
