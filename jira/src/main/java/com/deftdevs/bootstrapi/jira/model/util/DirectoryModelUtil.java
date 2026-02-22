package com.deftdevs.bootstrapi.jira.model.util;

import com.atlassian.crowd.directory.RemoteCrowdDirectory;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryGenericModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryInternalModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryLdapModel;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.atlassian.crowd.directory.RemoteCrowdDirectory.*;
import static com.atlassian.crowd.directory.SynchronisableDirectoryProperties.*;
import static com.atlassian.crowd.model.directory.DirectoryImpl.ATTRIBUTE_KEY_USE_NESTED_GROUPS;
import static com.deftdevs.bootstrapi.commons.util.ConversionUtil.*;

public class DirectoryModelUtil {

    /**
     * Build directory directory.
     *
     * @return the directory
     */
    @NotNull
    public static Directory toDirectory(
            final DirectoryCrowdModel directoryModel) {

        final Map<String, String> attributes = new HashMap<>();
        if (directoryModel.getServer() != null) {
            if (directoryModel.getServer().getUrl() != null) {
                attributes.put(CROWD_SERVER_URL, directoryModel.getServer().getUrl().toString());
            }
            attributes.put(APPLICATION_NAME, directoryModel.getServer().getAppUsername());
            attributes.put(APPLICATION_PASSWORD, directoryModel.getServer().getAppPassword());
            if (directoryModel.getServer().getProxy() != null) {
                attributes.put(CROWD_HTTP_PROXY_HOST, directoryModel.getServer().getProxy().getHost());
                if (directoryModel.getServer().getProxy().getPort() != null) {
                    attributes.put(CROWD_HTTP_PROXY_PORT, directoryModel.getServer().getProxy().getPort().toString());
                }
                attributes.put(CROWD_HTTP_PROXY_USERNAME, directoryModel.getServer().getProxy().getUsername());
                attributes.put(CROWD_HTTP_PROXY_PASSWORD, directoryModel.getServer().getProxy().getPassword());
            }
        }
        if (directoryModel.getAdvanced() != null) {
            attributes.put(CACHE_SYNCHRONISE_INTERVAL, directoryModel.getAdvanced().getUpdateSyncIntervalInMinutes() != 0 ?
                    String.valueOf(directoryModel.getAdvanced().getUpdateSyncIntervalInMinutes()) : "3600");
            attributes.put(ATTRIBUTE_KEY_USE_NESTED_GROUPS, String.valueOf(directoryModel.getAdvanced().getEnableNestedGroups()));
            attributes.put(INCREMENTAL_SYNC_ENABLED, String.valueOf(directoryModel.getAdvanced().getEnableIncrementalSync()));
            attributes.put(SYNC_GROUP_MEMBERSHIP_AFTER_SUCCESSFUL_USER_AUTH_ENABLED, directoryModel.getAdvanced().getUpdateGroupMembershipMethod());
        }

        return ImmutableDirectory.builder(directoryModel.getName(), DirectoryModelUtil.getDirectoryType(directoryModel), RemoteCrowdDirectory.class.getName())
                .setActive(directoryModel.getActive() != null && directoryModel.getActive())
                .setAttributes(attributes)
                .build();
    }

    /**
     * Build user directory bean user directory bean.
     *
     * @param directory the directory
     * @return the user directory bean
     */
    @NotNull
    public static AbstractDirectoryModel toDirectoryModel(
            final Directory directory) {

        final Map<String, String> attributes = directory.getAttributes();
        final AbstractDirectoryModel directoryModel;

        if (DirectoryType.CROWD.equals(directory.getType())) {
            DirectoryCrowdModel.DirectoryCrowdServer.DirectoryCrowdServerProxy proxy = null;
            if (attributes.containsKey(CROWD_HTTP_PROXY_HOST)) {
                proxy = DirectoryCrowdModel.DirectoryCrowdServer.DirectoryCrowdServerProxy.builder()
                    .username(attributes.get(CROWD_HTTP_PROXY_USERNAME))
                    .host(attributes.get(CROWD_HTTP_PROXY_HOST))
                    .port(attributes.get(CROWD_HTTP_PROXY_PORT) != null ? Integer.valueOf(attributes.get(CROWD_HTTP_PROXY_PORT)) : null)
                    .password(attributes.get(CROWD_HTTP_PROXY_PASSWORD))
                    .build();
            }
            DirectoryCrowdModel.DirectoryCrowdServer serverModel = DirectoryCrowdModel.DirectoryCrowdServer.builder()
                .url(URI.create(attributes.get(CROWD_SERVER_URL)))
                .proxy(proxy)
                .connectionTimeoutInMillis(toLong(attributes.get(CROWD_HTTP_TIMEOUT)))
                .maxConnections(toInt(attributes.get(CROWD_HTTP_MAX_CONNECTIONS)))
                .appUsername(attributes.get(APPLICATION_NAME))
                .appPassword(attributes.get(APPLICATION_PASSWORD))
                .build();

            DirectoryCrowdModel.DirectoryCrowdAdvanced advanced = DirectoryCrowdModel.DirectoryCrowdAdvanced.builder()
                .enableIncrementalSync(toBoolean(attributes.get(INCREMENTAL_SYNC_ENABLED)))
                .enableNestedGroups(toBoolean(attributes.get(ATTRIBUTE_KEY_USE_NESTED_GROUPS)))
                .updateSyncIntervalInMinutes(toInt(attributes.get(CACHE_SYNCHRONISE_INTERVAL)))
                .updateGroupMembershipMethod(attributes.get(SYNC_GROUP_MEMBERSHIP_AFTER_SUCCESSFUL_USER_AUTH_ENABLED))
                .build();

            DirectoryCrowdModel directoryCrowdModel = DirectoryCrowdModel.builder()
                .server(serverModel)
                .advanced(advanced)
                .name(directory.getName())
                .active(directory.isActive())
                .description(directory.getDescription())
                .id(directory.getId())
                .build();
            directoryModel = directoryCrowdModel;
        } else  {
            directoryModel = DirectoryGenericModel.builder()
                .name(directory.getName())
                .active(directory.isActive())
                .description(directory.getDescription())
                .id(directory.getId())
                .build();
        }
        return directoryModel;
    }

    public static DirectoryType getDirectoryType(
            final AbstractDirectoryModel directoryModel) {
        if (directoryModel instanceof DirectoryInternalModel) {
            return DirectoryType.INTERNAL;
        } else if (directoryModel instanceof DirectoryCrowdModel) {
            return DirectoryType.CROWD;
        } else if (directoryModel instanceof DirectoryLdapModel) {
            return DirectoryType.DELEGATING;
        } else {
            return DirectoryType.UNKNOWN;
        }
    }

    private DirectoryModelUtil() {
    }

}
