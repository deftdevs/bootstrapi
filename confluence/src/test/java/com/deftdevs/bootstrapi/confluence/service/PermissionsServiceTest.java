package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.security.SpacePermission;
import com.atlassian.confluence.security.SpacePermissionManager;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalBean;
import com.deftdevs.bootstrapi.confluence.model.util.PermissionsGlobalBeanUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.atlassian.confluence.security.SpacePermission.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionsServiceTest {

    @Mock
    private SpacePermissionManager spacePermissionManager;

    private PermissionsServiceImpl permissionsService;

    @BeforeEach
    public void setup() {
        permissionsService = new PermissionsServiceImpl(spacePermissionManager);
    }

    @Test
    void testGetPermissionsGlobalForGroups() {
        final String group = "jira-administrators";
        final SpacePermission globalPermissionEntry = SpacePermission.createGroupSpacePermission(SYSTEM_ADMINISTRATOR_PERMISSION, null, group);

        final PermissionsServiceImpl spy = spy(permissionsService);
        doReturn(Collections.singletonList(globalPermissionEntry)).when(spacePermissionManager).getGlobalPermissions();

        final PermissionsGlobalBean permissionsGlobalBean = spy.getPermissionsGlobal();
        final Map<String, ? extends Collection<String>> groupPermissions = permissionsGlobalBean.getGroupPermissions();
        assertTrue(groupPermissions.containsKey(group));

        final Set<String> permissions = new HashSet<>(groupPermissions.get(group));
        assertTrue(permissions.contains(globalPermissionEntry.getType()));
    }

    @Test
    void testSetPermissionsGlobalForGroups() {
        final String group = "jira-administrators";
        final SpacePermission globalPermissionEntryToAdd = SpacePermission.createGroupSpacePermission(CONFLUENCE_ADMINISTRATOR_PERMISSION, null, group);
        final SpacePermission globalPermissionEntryToRetain = SpacePermission.createGroupSpacePermission(SYSTEM_ADMINISTRATOR_PERMISSION, null, group);
        final SpacePermission globalPermissionEntryToRemove = SpacePermission.createGroupSpacePermission(USE_CONFLUENCE_PERMISSION, null, group);
        doReturn(Arrays.asList(globalPermissionEntryToRetain, globalPermissionEntryToRemove)).when(spacePermissionManager).getGlobalPermissions();

        final List<SpacePermission> requestGlobalPermissions = Arrays.asList(globalPermissionEntryToAdd, globalPermissionEntryToRetain);
        final PermissionsGlobalBean requestPermissionsGLobalBean = PermissionsGlobalBeanUtil.toPermissionsGlobalBean(requestGlobalPermissions);
        permissionsService.setPermissionsGlobal(requestPermissionsGLobalBean);

        verify(spacePermissionManager).savePermission(globalPermissionEntryToAdd);
        verify(spacePermissionManager).removePermission(globalPermissionEntryToRemove);
    }

    @Test
    void testGetPermissionsGlobalForAnonymous() {
        final SpacePermission globalPermissionEntry = SpacePermission.createAnonymousSpacePermission(USE_CONFLUENCE_PERMISSION, null);

        final PermissionsServiceImpl spy = spy(permissionsService);
        doReturn(Collections.singletonList(globalPermissionEntry)).when(spacePermissionManager).getGlobalPermissions();

        final PermissionsGlobalBean permissionsGlobalBean = spy.getPermissionsGlobal();
        final Set<String> anonymousPermissions = new HashSet<>(permissionsGlobalBean.getAnonymousPermissions());
        assertTrue(anonymousPermissions.contains(globalPermissionEntry.getType()));
    }

    @Test
    void testSetPermissionsGlobalForAnonymous() {
        final SpacePermission globalPermissionEntryToAdd = SpacePermission.createAnonymousSpacePermission(VIEWSPACE_PERMISSION, null);
        final SpacePermission globalPermissionEntryToRemove = SpacePermission.createAnonymousSpacePermission(BROWSE_USERS_PERMISSION, null);
        doReturn(List.of(globalPermissionEntryToRemove)).when(spacePermissionManager).getGlobalPermissions();

        final List<SpacePermission> requestGlobalPermissions = List.of(globalPermissionEntryToAdd);
        final PermissionsGlobalBean requestPermissionsGLobalBean = PermissionsGlobalBeanUtil.toPermissionsGlobalBean(requestGlobalPermissions);
        permissionsService.setPermissionsGlobal(requestPermissionsGLobalBean);

        verify(spacePermissionManager).savePermission(globalPermissionEntryToAdd);
        verify(spacePermissionManager).removePermission(globalPermissionEntryToRemove);
    }

}
