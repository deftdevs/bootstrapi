package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.exception.*;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.manager.directory.DirectoryPermissionException;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.model.group.GroupTemplate;
import com.atlassian.crowd.model.group.ImmutableGroup;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.GroupModel;
import com.deftdevs.bootstrapi.commons.exception.GroupNotFoundException;
import com.deftdevs.bootstrapi.crowd.model.util.GroupModelUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupsServiceTest {

    @Mock
    private DirectoryManager directoryManager;

    private GroupsServiceImpl groupsService;

    @BeforeEach
    public void setup() {
        groupsService = new GroupsServiceImpl(directoryManager);
    }

    @Test
    public void testGetGroup() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupModel groupModel = groupsService.getGroup(group.getDirectoryId(), group.getName());
        assertEquals(group.getName(), groupModel.getName());
    }

    @Test
    public void testGetGroupNotFound() {
        final String groupName = "not_found";

        assertThrows(GroupNotFoundException.class, () -> {
            groupsService.getGroup(0L, groupName);
        });
    }

    @Test
    public void testCreateGroup() throws Exception {
        final GroupModel groupModel = GroupModel.EXAMPLE_1;
        // return the same group as the one we are adding
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).addGroup(anyLong(), any());

        final ArgumentCaptor<GroupTemplate> groupTemplateArgumentCaptor = ArgumentCaptor.forClass(GroupTemplate.class);
        groupsService.createGroup(0L, groupModel);
        verify(directoryManager).addGroup(anyLong(), groupTemplateArgumentCaptor.capture());
        assertEquals(groupModel.getName(), groupTemplateArgumentCaptor.getValue().getName());
        assertEquals(groupModel.getDescription(), groupTemplateArgumentCaptor.getValue().getDescription());
        assertEquals(groupModel.getActive(), groupTemplateArgumentCaptor.getValue().isActive());
    }

    @Test
    public void testCreateGroupActiveByDefault() throws Exception {
        final GroupModel groupModel = GroupModelUtil.toGroupModel(getTestGroup());
        groupModel.setActive(null);
        // return the same group as the one we are adding
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).addGroup(anyLong(), any());

        final ArgumentCaptor<GroupTemplate> groupTemplateArgumentCaptor = ArgumentCaptor.forClass(GroupTemplate.class);
        groupsService.createGroup(0L, groupModel);
        verify(directoryManager).addGroup(anyLong(), groupTemplateArgumentCaptor.capture());
        assertTrue(groupTemplateArgumentCaptor.getValue().isActive());
    }

    @Test
    public void testCreateGroupAlreadyExists() throws DirectoryNotFoundException, OperationFailedException, com.atlassian.crowd.exception.GroupNotFoundException {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupModel groupModel = GroupModelUtil.toGroupModel(group);

        assertThrows(BadRequestException.class, () -> {
            groupsService.createGroup(group.getDirectoryId(), groupModel);
        });
    }

    @Test
    public void testCreateGroupNoName() throws DirectoryNotFoundException, OperationFailedException, com.atlassian.crowd.exception.GroupNotFoundException {
        final Group group = getTestGroup();

        final GroupModel groupModel = GroupModelUtil.toGroupModel(group);
        groupModel.setName(null);

        assertThrows(BadRequestException.class, () -> {
            groupsService.createGroup(group.getDirectoryId(), groupModel);
        });
    }

    @Test
    public void testUpdateGroup() throws Exception {
        final Group group = getTestGroup();
        final Group renamedGroup = ImmutableGroup.builder(group)
                .setName("other")
                .build();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());
        doReturn(renamedGroup).when(directoryManager).renameGroup(group.getDirectoryId(), group.getName(), renamedGroup.getName());
        // return the same group as the one we are updating
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).updateGroup(anyLong(), any());

        final GroupModel renamedGroupModel = GroupModelUtil.toGroupModel(renamedGroup);
        renamedGroupModel.setDescription("Other description");
        renamedGroupModel.setActive(false);

        final ArgumentCaptor<GroupTemplate> groupTemplateArgumentCaptor = ArgumentCaptor.forClass(GroupTemplate.class);
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), renamedGroupModel);
        verify(directoryManager).updateGroup(anyLong(), groupTemplateArgumentCaptor.capture());
        assertEquals(renamedGroupModel.getName(), groupTemplateArgumentCaptor.getValue().getName());
        assertEquals(renamedGroupModel.getDescription(), groupTemplateArgumentCaptor.getValue().getDescription());
        assertEquals(renamedGroupModel.getActive(), groupTemplateArgumentCaptor.getValue().isActive());
    }

    @Test
    public void testUpdateGroupNotFound() {
        final Group group = getTestGroup();
        final GroupModel groupModel = GroupModelUtil.toGroupModel(group);

        assertThrows(GroupNotFoundException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    @Test
    public void testUpdateGroupNoOp() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupModel groupModel = new GroupModel();
        groupModel.setName(group.getName());

        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        verify(directoryManager, never()).renameGroup(anyLong(), anyString(), anyString());
        verify(directoryManager, never()).updateGroup(anyLong(), any());
    }

    @Test
    public void testSetGroupAddNew() {
        final Group group = getTestGroup();
        final GroupModel groupModel = GroupModel.EXAMPLE_1;
        final GroupsServiceImpl spy = spy(groupsService);
        doReturn(groupModel).when(spy).createGroup(anyLong(), any());

        spy.setGroup(group.getDirectoryId(), groupModel.getName(), groupModel);
        verify(spy).createGroup(anyLong(), any());
    }

    @Test
    public void testSetGroupUpdateExisting() {
        final Group group = getTestGroup();
        final GroupModel groupModel = GroupModelUtil.toGroupModel(group);
        assertNotNull(groupModel);

        final GroupsServiceImpl spy = spy(groupsService);
        doReturn(group).when(spy).findGroup(group.getDirectoryId(), group.getName());
        doReturn(groupModel).when(spy).updateGroup(group.getDirectoryId(), group.getName(), groupModel);

        spy.setGroup(group.getDirectoryId(), groupModel.getName(), groupModel);
        verify(spy).updateGroup(anyLong(), anyString(), any());
    }

    @Test
    public void testSetGroups() {
        final List<GroupModel> groupModels = new ArrayList<>();
        groupModels.add(GroupModel.EXAMPLE_1);
        groupModels.add(GroupModel.EXAMPLE_2);

        final GroupsServiceImpl spy = spy(groupsService);
        doAnswer(invocation -> invocation.getArguments()[2]).when(spy).setGroup(anyLong(), any(), any());

        spy.setGroups(0L, groupModels);
        verify(spy, times(groupModels.size())).setGroup(anyLong(), any(), any());
    }

    @Test
    public void testSetGroupsNull() {
        assertEquals(Collections.emptyList(), groupsService.setGroups(0L, null));
    }

    // We kind of need to test all the exceptions here, but it's also pointless to test
    // all the exact mappings, because that's like repeating the implementation.
    // For this reason, we are just using WebApplicationException as a catch-all for now.
    // We are also not testing DirectoryNotFoundException and GroupNotFoundException here,
    // because these cases won't happen anymore after a group has been found by its name.

    @Test
    public void testGetGroupDirectoryNotFoundException() throws DirectoryNotFoundException, OperationFailedException, com.atlassian.crowd.exception.GroupNotFoundException {
        final Group group = getTestGroup();
        doThrow(new DirectoryNotFoundException(group.getDirectoryId())).when(directoryManager).findGroupByName(anyLong(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.getGroup(group.getDirectoryId(), group.getName());
        });
    }

    @Test
    public void testGetGroupOperationFailedException() throws DirectoryNotFoundException, OperationFailedException, com.atlassian.crowd.exception.GroupNotFoundException {
        final Group group = getTestGroup();
        doThrow(new OperationFailedException()).when(directoryManager).findGroupByName(anyLong(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.getGroup(group.getDirectoryId(), group.getName());
        });
    }

    @Test
    public void testCreateGroupDirectoryPermissionException() throws Exception {
        doThrow(new DirectoryPermissionException()).when(directoryManager).addGroup(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.createGroup(0L, GroupModel.EXAMPLE_1);
        });
    }

    @Test
    public void testCreateGroupInvalidGroupException() throws Exception {
        final Group group = getTestGroup();
        final GroupModel groupModel = GroupModelUtil.toGroupModel(group);
        doThrow(new InvalidGroupException(group, "Exception")).when(directoryManager).addGroup(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.createGroup(0L, groupModel);
        });
    }

    @Test
    public void testCreateGroupDirectoryNotFoundException() throws Exception {
        final long directoryId = 0L;
        doThrow(new DirectoryNotFoundException(directoryId)).when(directoryManager).addGroup(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.createGroup(directoryId, GroupModel.EXAMPLE_1);
        });
    }

    @Test
    public void testCreateGroupOperationFailedException() throws Exception {
        doThrow(new OperationFailedException()).when(directoryManager).addGroup(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.createGroup(0L, GroupModel.EXAMPLE_1);
        });
    }

    @Test
    public void testUpdateGroupDirectoryPermissionException() throws Exception {
        final Group group = getTestGroup();
        final GroupModel groupModel = GroupModelUtil.toGroupModel(group);
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        doThrow(new DirectoryPermissionException()).when(directoryManager).updateGroup(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    @Test
    public void testUpdateGroupInvalidGroupException() throws Exception {
        final Group group = getTestGroup();
        final GroupModel groupModel = GroupModelUtil.toGroupModel(group);
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        doThrow(new InvalidGroupException(group, "Exception")).when(directoryManager).updateGroup(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    @Test
    public void testUpdateGroupReadOnlyGroupException() throws Exception {
        final Group group = getTestGroup();
        final GroupModel groupModel = GroupModelUtil.toGroupModel(group);
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        doThrow(new ReadOnlyGroupException(group.getName())).when(directoryManager).updateGroup(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    @Test
    public void testUpdateGroupDirectoryNotFoundException() throws Exception {
        final Group group = getTestGroup();
        final GroupModel groupModel = GroupModelUtil.toGroupModel(group);
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        doThrow(new DirectoryNotFoundException(group.getDirectoryId())).when(directoryManager).updateGroup(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    @Test
    public void testUpdateGroupGroupNotFoundException() throws Exception {
        final Group group = getTestGroup();
        final GroupModel groupModel = GroupModelUtil.toGroupModel(group);
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        doThrow(new com.atlassian.crowd.exception.GroupNotFoundException(group.getName())).when(directoryManager).updateGroup(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    @Test
    public void testUpdateGroupOperationFailedException() throws Exception {
        final Group group = getTestGroup();
        final GroupModel groupModel = GroupModelUtil.toGroupModel(group);
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        doThrow(new OperationFailedException()).when(directoryManager).updateGroup(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    @Test
    public void testRenameGroupDirectoryPermissionException() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupModel groupModel = new GroupModel();
        groupModel.setName("new_group_name");

        doThrow(new DirectoryPermissionException()).when(directoryManager).renameGroup(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    @Test
    public void testRenameGroupDirectoryPermissionExceptionAnyDirectory() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupModel groupModel = new GroupModel();
        groupModel.setName("new_group_name");

        doThrow(new DirectoryPermissionException()).when(directoryManager).renameGroup(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    @Test
    public void testRenameGroupInvalidGroupException() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupModel groupModel = new GroupModel();
        groupModel.setName("new_group_name");

        doThrow(new InvalidGroupException(group, "message")).when(directoryManager).renameGroup(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    @Test
    public void testRenameGroupInvalidGroupExceptionAnyDirectory() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupModel groupModel = new GroupModel();
        groupModel.setName("new_group_name");

        doThrow(new InvalidGroupException(group, "message")).when(directoryManager).renameGroup(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    @Test
    public void testRenameGroupOperationFailedException() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupModel groupModel = new GroupModel();
        groupModel.setName("new_group_name");

        doThrow(new OperationFailedException()).when(directoryManager).renameGroup(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    @Test
    public void testRenameGroupOperationFailedExceptionAnyDirectory() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupModel groupModel = new GroupModel();
        groupModel.setName("new_group_name");

        doThrow(new OperationFailedException()).when(directoryManager).renameGroup(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupModel);
        });
    }

    private Group getTestGroup() {
        return ImmutableGroup.builder(0L, "test")
                .setDescription("Test description")
                .setActive(true)
                .build();
    }

}
