package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.permission.GlobalPermissionType;
import com.atlassian.jira.security.GlobalPermissionEntry;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalModel;
import com.deftdevs.bootstrapi.commons.service.api.PermissionsService;
import com.deftdevs.bootstrapi.jira.model.util.PermissionsGlobalModelUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PermissionsServiceImpl implements PermissionsService {

    private final GlobalPermissionManager globalPermissionManager;

    public PermissionsServiceImpl(
            final GlobalPermissionManager globalPermissionManager) {

        this.globalPermissionManager = globalPermissionManager;
    }

    @Override
    public PermissionsGlobalModel getPermissionsGlobal() {
        return PermissionsGlobalModelUtil.toPermissionsGlobalModel(getGlobalPermissions());
    }

    @Override
    public PermissionsGlobalModel setPermissionsGlobal(
            @NotNull final PermissionsGlobalModel permissionsGlobalModel) {

        setPermissionsGlobalForGroup(permissionsGlobalModel);
        return getPermissionsGlobal();
    }

    private void setPermissionsGlobalForGroup(
            @NotNull final PermissionsGlobalModel permissionsGlobalModel) {

        final Map<String, Set<String>> existingGroupPermissions = getGlobalPermissions().stream()
                .filter(permission -> permission.getGroup() != null)
                .collect(Collectors.groupingBy(
                        GlobalPermissionEntry::getGroup,
                        Collectors.mapping(GlobalPermissionEntry::getPermissionKey, Collectors.toSet())
                ));

        final Map<String, ? extends Collection<String>> requestGroupPermissions = permissionsGlobalModel.getGroupPermissions();
        final Set<String> validGlobalPermissions = Stream.concat(
                GlobalPermissionKey.DEFAULT_APP_GLOBAL_PERMISSIONS.stream(),
                Stream.of(GlobalPermissionKey.ADMINISTER, GlobalPermissionKey.SYSTEM_ADMIN)
        ).map(GlobalPermissionKey::getKey).collect(Collectors.toSet());

        if (requestGroupPermissions == null) {
            return;
        }

        // remove all global permissions of a group that currently exist but are not contained in the request
        for (String group : existingGroupPermissions.keySet()) {
            // only consider removing global permissions of a group if the group is contained in the request
            if (!requestGroupPermissions.containsKey(group)) {
                continue;
            }

            for (String permission : existingGroupPermissions.get(group)) {
                if (!validGlobalPermissions.contains(permission)) {
                    throw new InternalServerErrorException(String.format("The given global permission '%s' is not valid", permission));
                }

                if (!requestGroupPermissions.get(group).contains(permission)) {
                    final GlobalPermissionType permissionType = getGlobalPermissionType(permission);
                    assert permissionType != null;  // we've already checked if it's valid
                    globalPermissionManager.removePermission(permissionType, group);
                }
            }
        }

        // add all global permissions of a group that currently don't exist but are contained in the request
        for (String group : requestGroupPermissions.keySet()) {
            // consider all groups of the request for global permissions that are supposed to be added
            for (String permission : requestGroupPermissions.get(group)) {
                if (!validGlobalPermissions.contains(permission)) {
                    throw new BadRequestException(String.format("The given global permission '%s' is not valid", permission));
                }

                if (!existingGroupPermissions.containsKey(group) || !existingGroupPermissions.get(group).contains(permission)) {
                    final GlobalPermissionType permissionType = getGlobalPermissionType(permission);
                    assert permissionType != null;  // we've already checked if it's valid
                    globalPermissionManager.addPermission(permissionType, group);
                }
            }
        }
    }

    List<GlobalPermissionEntry> getGlobalPermissions() {
        return globalPermissionManager.getAllGlobalPermissions().stream()
                .map(GlobalPermissionType::getGlobalPermissionKey)
                .flatMap(key -> globalPermissionManager.getPermissions(key).stream())
                .collect(Collectors.toList());
    }

    @Nullable
    GlobalPermissionType getGlobalPermissionType(
            @Nonnull final String key) {

        return globalPermissionManager.getGlobalPermission(key).getOrNull();
    }

}
