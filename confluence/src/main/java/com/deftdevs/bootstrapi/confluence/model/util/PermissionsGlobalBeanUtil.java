package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.confluence.security.SpacePermission;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalBean;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PermissionsGlobalBeanUtil {

    @NotNull
    public static PermissionsGlobalBean toPermissionsGlobalBean(
            @NotNull final Collection<SpacePermission> spacePermissions) {

        final HashSet<SpacePermission> globalPermissions = new HashSet<>(spacePermissions);
        final TreeSet<String> anonymousGlobalPermissions = new TreeSet<>();
        final TreeMap<String, TreeSet<String>> groupGlobalPermissions = new TreeMap<>();

        // group global permissions
        for (SpacePermission globalPermission : globalPermissions.stream().filter(SpacePermission::isGroupPermission).collect(Collectors.toList())) {
            final String group = globalPermission.getGroup();
            groupGlobalPermissions.putIfAbsent(group, new TreeSet<>());
            groupGlobalPermissions.get(group).add(globalPermission.getType());
        }

        // anonymous global permissions
        for (SpacePermission globalPermission : globalPermissions.stream().filter(SpacePermission::isAnonymousPermission).collect(Collectors.toList())) {
            anonymousGlobalPermissions.add(globalPermission.getType());
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