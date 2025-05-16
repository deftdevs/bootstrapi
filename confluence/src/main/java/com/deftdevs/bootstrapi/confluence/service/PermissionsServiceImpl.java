package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.security.SpacePermission;
import com.atlassian.confluence.security.SpacePermissionManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalBean;
import com.deftdevs.bootstrapi.commons.service.api.PermissionsService;
import com.deftdevs.bootstrapi.confluence.model.util.PermissionsGlobalBeanUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.atlassian.confluence.security.SpacePermission.INVALID_ANONYMOUS_PERMISSIONS;

@Component
public class PermissionsServiceImpl implements PermissionsService {

    @ComponentImport
    private final SpacePermissionManager spacePermissionManager;

    @Inject
    public PermissionsServiceImpl(
            final SpacePermissionManager spacePermissionManager) {

        this.spacePermissionManager = spacePermissionManager;
    }

    @Override
    public PermissionsGlobalBean getPermissionsGlobal() {
        return PermissionsGlobalBeanUtil.toPermissionsGlobalBean(spacePermissionManager.getGlobalPermissions());
    }

    @Override
    public PermissionsGlobalBean setPermissionsGlobal(
            @NotNull final PermissionsGlobalBean permissionsGlobalBean) {

        setPermissionsGlobalForGroups(permissionsGlobalBean);
        setPermissionsGlobalForAnonymous(permissionsGlobalBean);
        return getPermissionsGlobal();
    }

    private void setPermissionsGlobalForGroups(
            @NotNull final PermissionsGlobalBean permissionsGlobalBean) {

        final Map<String, ? extends Collection<String>> requestGroupPermissions = permissionsGlobalBean.getGroupPermissions();

        if (requestGroupPermissions == null) {
            return;
        }

        final Set<String> validGlobalPermissions = new HashSet<>(SpacePermission.GLOBAL_PERMISSIONS);

        final Map<String, Map<String, SpacePermission>> existingGroupPermissions = spacePermissionManager.getGlobalPermissions().stream()
                .filter(SpacePermission::isGroupPermission)
                .filter(permission -> permission.getGroup() != null)
                .filter(permission -> validGlobalPermissions.contains(permission.getType()))
                .collect(Collectors.groupingBy(
                        SpacePermission::getGroup,
                        Collectors.toMap(
                                SpacePermission::getType,
                                Function.identity(),
                                (existing, replacement) -> existing
                        )
                ));

        // remove all global permissions of a group that currently exist but are not contained in the request
        for (String group : existingGroupPermissions.keySet()) {
            // only consider removing global permissions of a group if the group is contained in the request
            if (!requestGroupPermissions.containsKey(group)) {
                continue;
            }

            for (Map.Entry<String, SpacePermission> permissionEntry : existingGroupPermissions.get(group).entrySet()) {
                if (!requestGroupPermissions.get(group).contains(permissionEntry.getKey())) {
                    spacePermissionManager.removePermission(permissionEntry.getValue());  // nosonar: deprecated but no alternative
                }
            }
        }

        // add all global permissions of a group that currently don't exist but are contained in the request
        for (String group : requestGroupPermissions.keySet()) {
            // consider all groups of the request for global permissions that are supposed to be added
            for (String permissionType : requestGroupPermissions.get(group)) {
                if (!validGlobalPermissions.contains(permissionType)) {
                    throw new BadRequestException(String.format("The given global permission '%s' is not valid", permissionType));
                }

                if (!existingGroupPermissions.containsKey(group) || !existingGroupPermissions.get(group).containsKey(permissionType)) {
                    final SpacePermission permission = SpacePermission.createGroupSpacePermission(permissionType, null, group);
                    spacePermissionManager.savePermission(permission);  // nosonar: deprecated but no alternative
                }
            }
        }
    }

    private void setPermissionsGlobalForAnonymous(
            @NotNull final PermissionsGlobalBean permissionsGlobalBean) {

        if (permissionsGlobalBean.getAnonymousPermissions() == null) {
            return;
        }

        final Set<String> invalidAnonymousPermissions = new HashSet<>(INVALID_ANONYMOUS_PERMISSIONS);
        final Map<String, SpacePermission> existingAnonymousPermissions = spacePermissionManager.getGlobalPermissions().stream()
                .filter(SpacePermission::isAnonymousPermission)
                .filter(permission -> !invalidAnonymousPermissions.contains(permission.getType()))
                .collect(Collectors.toMap(
                                SpacePermission::getType,
                                Function.identity(),
                                (existing, replacement) -> existing
                        )
                );

        final Set<String> requestAnonymousPermissions = new HashSet<>(permissionsGlobalBean.getAnonymousPermissions());

        // remove all anonymous global permissions that currently exist but are not contained in the request
        for (Map.Entry<String, SpacePermission> permissionEntry : existingAnonymousPermissions.entrySet()) {
            if (!requestAnonymousPermissions.contains(permissionEntry.getKey())) {
                spacePermissionManager.removePermission(permissionEntry.getValue());  // nosonar: deprecated but no alternative
            }
        }

        // add all anonymous permissions that currently don't exist but are contained in the request
        for (String permissionType : requestAnonymousPermissions) {
            if (invalidAnonymousPermissions.contains(permissionType)) {
                throw new BadRequestException(String.format("The given global permission '%s' is invalid for anonymous", permissionType));
            }

            if (!existingAnonymousPermissions.containsKey(permissionType)) {
                final SpacePermission permission = SpacePermission.createAnonymousSpacePermission(permissionType, null);
                spacePermissionManager.savePermission(permission);  // nosonar: deprecated but no alternative
            }
        }
    }

}
