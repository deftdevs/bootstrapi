package com.deftdevs.bootstrapi.jira.model.util;

import com.atlassian.jira.security.GlobalPermissionEntry;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalBean;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PermissionsGlobalBeanUtil {

    @NotNull
    public static PermissionsGlobalBean toPermissionsGlobalBean(
            @NotNull final Collection<GlobalPermissionEntry> globalPermissions) {

        final TreeMap<String, TreeSet<String>> groupGlobalPermissions = new TreeMap<>();

        // group global permissions
        for (GlobalPermissionEntry globalPermission : globalPermissions.stream().filter(p -> p.getGroup() != null).collect(Collectors.toList())) {
            final String group = globalPermission.getGroup();
            groupGlobalPermissions.putIfAbsent(group, new TreeSet<>());
            groupGlobalPermissions.get(group).add(globalPermission.getPermissionKey());
        }

        final PermissionsGlobalBean permissionsGlobalBean = new PermissionsGlobalBean();

        if (!groupGlobalPermissions.isEmpty()) {
            permissionsGlobalBean.setGroupPermissions(groupGlobalPermissions);
        }

        return permissionsGlobalBean;
    }

    private PermissionsGlobalBeanUtil() {
    }

}