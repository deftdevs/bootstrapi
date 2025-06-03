package com.deftdevs.bootstrapi.jira.model.util;

import com.atlassian.jira.security.GlobalPermissionEntry;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalModel;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PermissionsGlobalModelUtil {

    @NotNull
    public static PermissionsGlobalModel toPermissionsGlobalModel(
            final List<GlobalPermissionEntry> globalPermissions) {

        final TreeMap<String, TreeSet<String>> groupGlobalPermissions = new TreeMap<>();

        // group global permissions
        for (GlobalPermissionEntry globalPermission : globalPermissions.stream().filter(p -> p.getGroup() != null).collect(Collectors.toList())) {
            final String group = globalPermission.getGroup();
            groupGlobalPermissions.putIfAbsent(group, new TreeSet<>());
            groupGlobalPermissions.get(group).add(globalPermission.getPermissionKey());
        }

        final PermissionsGlobalModel permissionsGlobalModel = new PermissionsGlobalModel();

        if (!groupGlobalPermissions.isEmpty()) {
            permissionsGlobalModel.setGroupPermissions(groupGlobalPermissions);
        }

        return permissionsGlobalModel;
    }

    private PermissionsGlobalModelUtil() {
    }

}