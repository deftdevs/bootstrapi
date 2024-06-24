package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.confluence.security.SpacePermission;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalBean;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

public class PermissionsGlobalBeanUtil {

    @NotNull
    public static PermissionsGlobalBean toPermissionsGlobalBean(
            @NotNull final Collection<SpacePermission> spacePermissions) {

        final HashSet<SpacePermission> globalPermissions = new HashSet<>(spacePermissions);
        final TreeSet<String> anonymousGlobalPermissions = new TreeSet<>();
        final TreeMap<String, TreeSet<String>> groupGlobalPermissions = new TreeMap<>();

        final Set<String> validGlobalPermissions = new HashSet<>(SpacePermission.GLOBAL_PERMISSIONS);

        final Collection<SpacePermission> globalGroupPermissions = globalPermissions.stream()
                .filter(SpacePermission::isGroupPermission)
                .filter(p -> validGlobalPermissions.contains(p.getType()))
                .collect(Collectors.toList());

        // group global permissions
        for (SpacePermission globalGroupPermission : globalGroupPermissions) {
            final String group = globalGroupPermission.getGroup();
            groupGlobalPermissions.putIfAbsent(group, new TreeSet<>());
            groupGlobalPermissions.get(group).add(globalGroupPermission.getType());
        }

        final Set<String> invalidAnonymousPermissions = new HashSet<>(SpacePermission.INVALID_ANONYMOUS_PERMISSIONS);

        final List<SpacePermission> globalAnonymousPermissions = globalPermissions.stream()
                .filter(SpacePermission::isAnonymousPermission)
                .filter(p -> !invalidAnonymousPermissions.contains(p.getType()))
                .collect(Collectors.toList());

        // anonymous global permissions
        for (SpacePermission globalAnonymousPermission : globalAnonymousPermissions) {
            anonymousGlobalPermissions.add(globalAnonymousPermission.getType());
        }

        final PermissionsGlobalBean permissionsGlobalBean = new PermissionsGlobalBean();

        if (!groupGlobalPermissions.isEmpty()) {
            permissionsGlobalBean.setGroupPermissions(groupGlobalPermissions);
        }

        if (!anonymousGlobalPermissions.isEmpty()) {
            permissionsGlobalBean.setAnonymousPermissions(anonymousGlobalPermissions);
        }

        return permissionsGlobalBean;
    }

    private PermissionsGlobalBeanUtil() {
    }

}