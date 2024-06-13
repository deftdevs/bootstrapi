package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.exception.GroupNotFoundException;
import com.atlassian.crowd.exception.InvalidGroupException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.exception.ReadOnlyGroupException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.manager.directory.DirectoryPermissionException;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.model.group.GroupTemplate;
import com.atlassian.crowd.model.group.ImmutableGroup;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.model.GroupBean;
import de.aservo.confapi.crowd.exception.NotFoundExceptionForGroup;
import de.aservo.confapi.crowd.model.GroupsBean;
import de.aservo.confapi.crowd.model.util.GroupBeanUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GroupsServiceTest {

    @Mock
    private DirectoryManager directoryManager;

    private GroupsServiceImpl groupsService;

    @Before
    public void setup() {
        groupsService = new GroupsServiceImpl(directoryManager);
    }

    @Test
    public void testGetGroup() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupBean groupBean = groupsService.getGroup(group.getDirectoryId(), group.getName());
        assertEquals(group.getName(), groupBean.getName());
    }

    @Test(expected = NotFoundExceptionForGroup.class)
    public void testGetGroupNotFound() {
        final String groupName = "not_found";
        groupsService.getGroup(0L, groupName);
    }

    @Test
    public void testCreateGroup() throws Exception {
        final GroupBean groupBean = GroupBean.EXAMPLE_1;
        // return the same group as the one we are adding
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).addGroup(anyLong(), any());

        final ArgumentCaptor<GroupTemplate> groupTemplateArgumentCaptor = ArgumentCaptor.forClass(GroupTemplate.class);
        groupsService.createGroup(0L, groupBean);
        verify(directoryManager).addGroup(anyLong(), groupTemplateArgumentCaptor.capture());
        assertEquals(groupBean.getName(), groupTemplateArgumentCaptor.getValue().getName());
        assertEquals(groupBean.getDescription(), groupTemplateArgumentCaptor.getValue().getDescription());
        assertEquals(groupBean.getActive(), groupTemplateArgumentCaptor.getValue().isActive());
    }

    @Test
    public void testCreateGroupActiveByDefault() throws Exception {
        final GroupBean groupBean = GroupBeanUtil.toGroupBean(getTestGroup());
        groupBean.setActive(null);
        // return the same group as the one we are adding
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).addGroup(anyLong(), any());

        final ArgumentCaptor<GroupTemplate> groupTemplateArgumentCaptor = ArgumentCaptor.forClass(GroupTemplate.class);
        groupsService.createGroup(0L, groupBean);
        verify(directoryManager).addGroup(anyLong(), groupTemplateArgumentCaptor.capture());
        assertTrue(groupTemplateArgumentCaptor.getValue().isActive());
    }

    @Test(expected = BadRequestException.class)
    public void testCreateGroupAlreadyExists() throws DirectoryNotFoundException, OperationFailedException, GroupNotFoundException {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupBean groupBean = GroupBeanUtil.toGroupBean(group);
        groupsService.createGroup(group.getDirectoryId(), groupBean);
    }

    @Test(expected = BadRequestException.class)
    public void testCreateGroupNoName() throws DirectoryNotFoundException, OperationFailedException, GroupNotFoundException {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupBean groupBean = GroupBeanUtil.toGroupBean(group);
        groupBean.setName(null);
        groupsService.createGroup(group.getDirectoryId(), groupBean);
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

        final GroupBean renamedGroupBean = GroupBeanUtil.toGroupBean(renamedGroup);
        renamedGroupBean.setDescription("Other description");
        renamedGroupBean.setActive(false);

        final ArgumentCaptor<GroupTemplate> groupTemplateArgumentCaptor = ArgumentCaptor.forClass(GroupTemplate.class);
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), renamedGroupBean);
        verify(directoryManager).updateGroup(anyLong(), groupTemplateArgumentCaptor.capture());
        assertEquals(renamedGroupBean.getName(), groupTemplateArgumentCaptor.getValue().getName());
        assertEquals(renamedGroupBean.getDescription(), groupTemplateArgumentCaptor.getValue().getDescription());
        assertEquals(renamedGroupBean.getActive(), groupTemplateArgumentCaptor.getValue().isActive());
    }

    @Test(expected = NotFoundExceptionForGroup.class)
    public void testUpdateGroupNotFound() {
        final Group group = getTestGroup();
        final GroupBean groupBean = GroupBeanUtil.toGroupBean(group);
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    @Test
    public void testUpdateGroupNoOp() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupBean groupBean = new GroupBean();
        groupBean.setName(group.getName());

        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
        verify(directoryManager, never()).renameGroup(anyLong(), anyString(), anyString());
        verify(directoryManager, never()).updateGroup(anyLong(), any());
    }

    @Test
    public void testSetGroupAddNew() {
        final Group group = getTestGroup();
        final GroupBean groupBean = GroupBean.EXAMPLE_1;
        final GroupsServiceImpl spy = spy(groupsService);
        doReturn(groupBean).when(spy).createGroup(anyLong(), any());

        spy.setGroup(group.getDirectoryId(), groupBean.getName(), groupBean);
        verify(spy).createGroup(anyLong(), any());
    }

    @Test
    public void testSetGroupUpdateExisting() {
        final Group group = getTestGroup();
        final GroupBean groupBean = GroupBeanUtil.toGroupBean(group);
        assertNotNull(groupBean);

        final GroupsServiceImpl spy = spy(groupsService);
        doReturn(group).when(spy).findGroup(group.getDirectoryId(), group.getName());
        doReturn(groupBean).when(spy).updateGroup(group.getDirectoryId(), group.getName(), groupBean);

        spy.setGroup(group.getDirectoryId(), groupBean.getName(), groupBean);
        verify(spy).updateGroup(anyLong(), anyString(), any());
    }

    @Test
    public void testSetGroups() {
        final Collection<GroupBean> groupBeans = new ArrayList<>();
        groupBeans.add(GroupBean.EXAMPLE_1);
        groupBeans.add(GroupBean.EXAMPLE_2);

        final GroupsServiceImpl spy = spy(groupsService);
        doAnswer(invocation -> invocation.getArguments()[2]).when(spy).setGroup(anyLong(), any(), any());

        spy.setGroups(0L, new GroupsBean(groupBeans));
        verify(spy, times(groupBeans.size())).setGroup(anyLong(), any(), any());
    }

    @Test
    public void testSetGroupsNull() {
        assertEquals(new GroupsBean(Collections.emptyList()), groupsService.setGroups(0L, null));
    }

    @Test
    public void testSetGroupsListNull() {
        assertEquals(new GroupsBean(Collections.emptyList()), groupsService.setGroups(0L, new GroupsBean(null)));
    }

    // We kind of need to test all the exceptions here, but it's also pointless to test
    // all the exact mappings, because that's like repeating the implementation.
    // For this reason, we are just using WebApplicationException as a catch-all for now.
    // We are also not testing DirectoryNotFoundException and GroupNotFoundException here,
    // because these cases won't happen anymore after a group has been found by its name.

    @Test(expected = WebApplicationException.class)
    public void testGetGroupDirectoryNotFoundException() throws DirectoryNotFoundException, OperationFailedException, GroupNotFoundException {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());
        doThrow(new DirectoryNotFoundException(group.getDirectoryId())).when(directoryManager).findGroupByName(anyLong(), anyString());
        groupsService.getGroup(group.getDirectoryId(), group.getName());
    }

    @Test(expected = WebApplicationException.class)
    public void testGetGroupOperationFailedException() throws DirectoryNotFoundException, OperationFailedException, GroupNotFoundException {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());
        doThrow(new OperationFailedException()).when(directoryManager).findGroupByName(anyLong(), anyString());
        groupsService.getGroup(group.getDirectoryId(), group.getName());
    }

    @Test(expected = WebApplicationException.class)
    public void testCreateGroupDirectoryPermissionException() throws Exception {
        doThrow(new DirectoryPermissionException()).when(directoryManager).addGroup(anyLong(), any());
        groupsService.createGroup(0L, GroupBean.EXAMPLE_1);
    }

    @Test(expected = WebApplicationException.class)
    public void testCreateGroupInvalidGroupException() throws Exception {
        final Group group = getTestGroup();
        final GroupBean groupBean = GroupBeanUtil.toGroupBean(group);
        doThrow(new InvalidGroupException(group, "Exception")).when(directoryManager).addGroup(anyLong(), any());
        groupsService.createGroup(0L, groupBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testCreateGroupDirectoryNotFoundException() throws Exception {
        final long directoryId = 0L;
        doThrow(new DirectoryNotFoundException(directoryId)).when(directoryManager).addGroup(anyLong(), any());
        groupsService.createGroup(directoryId, GroupBean.EXAMPLE_1);
    }

    @Test(expected = WebApplicationException.class)
    public void testCreateGroupOperationFailedException() throws Exception {
        doThrow(new OperationFailedException()).when(directoryManager).addGroup(anyLong(), any());
        groupsService.createGroup(0L, GroupBean.EXAMPLE_1);
    }

    @Test(expected = WebApplicationException.class)
    public void testUpdateGroupDirectoryPermissionException() throws Exception {
        final Group group = getTestGroup();
        final GroupBean groupBean = GroupBeanUtil.toGroupBean(group);
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        doThrow(new DirectoryPermissionException()).when(directoryManager).updateGroup(anyLong(), any());
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testUpdateGroupInvalidGroupException() throws Exception {
        final Group group = getTestGroup();
        final GroupBean groupBean = GroupBeanUtil.toGroupBean(group);
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        doThrow(new InvalidGroupException(group, "Exception")).when(directoryManager).updateGroup(anyLong(), any());
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testUpdateGroupReadOnlyGroupException() throws Exception {
        final Group group = getTestGroup();
        final GroupBean groupBean = GroupBeanUtil.toGroupBean(group);
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        doThrow(new ReadOnlyGroupException(group.getName())).when(directoryManager).updateGroup(anyLong(), any());
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testUpdateGroupDirectoryNotFoundException() throws Exception {
        final Group group = getTestGroup();
        final GroupBean groupBean = GroupBeanUtil.toGroupBean(group);
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        doThrow(new DirectoryNotFoundException(group.getDirectoryId())).when(directoryManager).updateGroup(anyLong(), any());
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testUpdateGroupGroupNotFoundException() throws Exception {
        final Group group = getTestGroup();
        final GroupBean groupBean = GroupBeanUtil.toGroupBean(group);
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        doThrow(new GroupNotFoundException(group.getName())).when(directoryManager).updateGroup(anyLong(), any());
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testUpdateGroupOperationFailedException() throws Exception {
        final Group group = getTestGroup();
        final GroupBean groupBean = GroupBeanUtil.toGroupBean(group);
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        doThrow(new OperationFailedException()).when(directoryManager).updateGroup(anyLong(), any());
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testRenameGroupDirectoryPermissionException() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupBean groupBean = new GroupBean();
        groupBean.setName("new_group_name");

        doThrow(new DirectoryPermissionException()).when(directoryManager).renameGroup(anyLong(), anyString(), anyString());
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testRenameGroupDirectoryPermissionExceptionAnyDirectory() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupBean groupBean = new GroupBean();
        groupBean.setName("new_group_name");

        doThrow(new DirectoryPermissionException()).when(directoryManager).renameGroup(anyLong(), anyString(), anyString());
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testRenameGroupInvalidGroupException() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupBean groupBean = new GroupBean();
        groupBean.setName("new_group_name");

        doThrow(new InvalidGroupException(group, "message")).when(directoryManager).renameGroup(anyLong(), anyString(), anyString());
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testRenameGroupInvalidGroupExceptionAnyDirectory() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupBean groupBean = new GroupBean();
        groupBean.setName("new_group_name");

        doThrow(new InvalidGroupException(group, "message")).when(directoryManager).renameGroup(anyLong(), anyString(), anyString());
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testRenameGroupOperationFailedException() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupBean groupBean = new GroupBean();
        groupBean.setName("new_group_name");

        doThrow(new OperationFailedException()).when(directoryManager).renameGroup(anyLong(), anyString(), anyString());
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testRenameGroupOperationFailedExceptionAnyDirectory() throws Exception {
        final Group group = getTestGroup();
        doReturn(group).when(directoryManager).findGroupByName(group.getDirectoryId(), group.getName());

        final GroupBean groupBean = new GroupBean();
        groupBean.setName("new_group_name");

        doThrow(new OperationFailedException()).when(directoryManager).renameGroup(anyLong(), anyString(), anyString());
        groupsService.updateGroup(group.getDirectoryId(), group.getName(), groupBean);
    }

    private Group getTestGroup() {
        return ImmutableGroup.builder(0L, "test")
                .setDescription("Test description")
                .setActive(true)
                .build();
    }

}
