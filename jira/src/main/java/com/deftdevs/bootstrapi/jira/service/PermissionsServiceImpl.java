package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.permission.GlobalPermissionType;
import com.atlassian.jira.security.GlobalPermissionEntry;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalBean;
import com.deftdevs.bootstrapi.commons.service.api.PermissionsService;
import com.deftdevs.bootstrapi.jira.model.util.PermissionsGlobalBeanUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@ExportAsService(PermissionsService.class)
public class PermissionsServiceImpl implements PermissionsService {

    @ComponentImport
    private final GlobalPermissionManager globalPermissionManager;

    @Inject
    public PermissionsServiceImpl(
            final GlobalPermissionManager globalPermissionManager) {

        this.globalPermissionManager = globalPermissionManager;
    }

    @Override
    public PermissionsGlobalBean getPermissionsGlobal() {
        return PermissionsGlobalBeanUtil.toPermissionsGlobalBean(getGlobalPermissions());
    }

    @Override
    public PermissionsGlobalBean setPermissionsGlobal(
            @NotNull final PermissionsGlobalBean permissionsGlobalBean) {

        setPermissionsGlobalForGroup(permissionsGlobalBean);
        return getPermissionsGlobal();
    }

    private void setPermissionsGlobalForGroup(
            @NotNull final PermissionsGlobalBean permissionsGlobalBean) {

        final Map<String, Set<String>> existingGroupPermissions = getGlobalPermissions().stream()
                .filter(permission -> permission.getGroup() != null)
                .collect(Collectors.groupingBy(
                        GlobalPermissionEntry::getGroup,
                        Collectors.mapping(GlobalPermissionEntry::getPermissionKey, Collectors.toSet())
                ));

        final Map<String, ? extends Collection<String>> requestGroupPermissions = permissionsGlobalBean.getGroupPermissions();
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
