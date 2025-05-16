package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.permission.GlobalPermissionType;
import com.atlassian.jira.security.GlobalPermissionEntry;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalModel;
import com.deftdevs.bootstrapi.jira.model.util.PermissionsGlobalModelUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionsServiceTest {

    @Mock
    private GlobalPermissionManager globalPermissionManager;

    private PermissionsServiceImpl permissionsService;

    @BeforeEach
    public void setup() {
        permissionsService = new PermissionsServiceImpl(globalPermissionManager);
    }

    @Test
    void testGetPermissionsGlobalForGroups() {
        final String group = "jira-administrators";
        final GlobalPermissionKey globalPermissionKey = GlobalPermissionKey.SYSTEM_ADMIN;
        final GlobalPermissionEntry globalPermissionEntry = new GlobalPermissionEntry(globalPermissionKey, group);

        final PermissionsServiceImpl spy = spy(permissionsService);
        doReturn(Collections.singletonList(globalPermissionEntry)).when(spy).getGlobalPermissions();

        final PermissionsGlobalModel permissionsGlobalModel = spy.getPermissionsGlobal();
        final Map<String, ? extends Collection<String>> groupPermissions = permissionsGlobalModel.getGroupPermissions();
        assertTrue(groupPermissions.containsKey(group));

        final Set<String> permissions = new HashSet<>(groupPermissions.get(group));
        assertTrue(permissions.contains(globalPermissionKey.getKey()));
    }

    @Test
    void testSetPermissionsGlobalForGroups() {
        final String group = "jira-administrators";
        final GlobalPermissionKey globalPermissionKeyToAdd = GlobalPermissionKey.ADMINISTER;
        final GlobalPermissionKey globalPermissionKeyToRetain = GlobalPermissionKey.SYSTEM_ADMIN;
        final GlobalPermissionKey globalPermissionKeyToRemove = GlobalPermissionKey.BULK_CHANGE;
        final GlobalPermissionEntry globalPermissionEntryToAdd = new GlobalPermissionEntry(globalPermissionKeyToAdd, group);
        final GlobalPermissionEntry globalPermissionEntryToRetain = new GlobalPermissionEntry(globalPermissionKeyToRetain, group);
        final GlobalPermissionEntry globalPermissionEntryToRemove = new GlobalPermissionEntry(globalPermissionKeyToRemove, group);

        final PermissionsServiceImpl spy = spy(permissionsService);
        doReturn(Arrays.asList(globalPermissionEntryToRetain, globalPermissionEntryToRemove)).when(spy).getGlobalPermissions();

        final GlobalPermissionType globalPermissionTypeToAdd = new GlobalPermissionType(globalPermissionKeyToAdd.getKey(), null, null, true);
        final GlobalPermissionType globalPermissionTypeToRemove = new GlobalPermissionType(globalPermissionKeyToRemove.getKey(), null, null, true);
        doReturn(globalPermissionTypeToAdd).when(spy).getGlobalPermissionType(globalPermissionTypeToAdd.getKey());
        doReturn(globalPermissionTypeToRemove).when(spy).getGlobalPermissionType(globalPermissionTypeToRemove.getKey());

        final List<GlobalPermissionEntry> requestGlobalPermissions = Arrays.asList(globalPermissionEntryToAdd, globalPermissionEntryToRetain);
        final PermissionsGlobalModel requestPermissionsGLobalModel = PermissionsGlobalModelUtil.toPermissionsGlobalModel(requestGlobalPermissions);
        spy.setPermissionsGlobal(requestPermissionsGLobalModel);

        verify(globalPermissionManager).addPermission(globalPermissionTypeToAdd, group);
        verify(globalPermissionManager).removePermission(globalPermissionTypeToRemove, group);
    }

}
