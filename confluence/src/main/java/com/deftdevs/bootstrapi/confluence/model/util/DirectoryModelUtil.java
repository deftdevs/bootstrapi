package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.crowd.directory.RemoteCrowdDirectory;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel.DirectoryCrowdAdvanced;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel.DirectoryCrowdServer;
import com.deftdevs.bootstrapi.commons.model.DirectoryGenericModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryInternalModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryLdapModel;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.atlassian.crowd.directory.RemoteCrowdDirectory.APPLICATION_NAME;
import static com.atlassian.crowd.directory.RemoteCrowdDirectory.APPLICATION_PASSWORD;
import static com.atlassian.crowd.directory.RemoteCrowdDirectory.CROWD_HTTP_MAX_CONNECTIONS;
import static com.atlassian.crowd.directory.RemoteCrowdDirectory.CROWD_HTTP_PROXY_HOST;
import static com.atlassian.crowd.directory.RemoteCrowdDirectory.CROWD_HTTP_PROXY_PASSWORD;
import static com.atlassian.crowd.directory.RemoteCrowdDirectory.CROWD_HTTP_PROXY_PORT;
import static com.atlassian.crowd.directory.RemoteCrowdDirectory.CROWD_HTTP_PROXY_USERNAME;
import static com.atlassian.crowd.directory.RemoteCrowdDirectory.CROWD_HTTP_TIMEOUT;
import static com.atlassian.crowd.directory.RemoteCrowdDirectory.CROWD_SERVER_URL;
import static com.atlassian.crowd.directory.SynchronisableDirectoryProperties.CACHE_SYNCHRONISE_INTERVAL;
import static com.atlassian.crowd.directory.SynchronisableDirectoryProperties.INCREMENTAL_SYNC_ENABLED;
import static com.atlassian.crowd.directory.SynchronisableDirectoryProperties.SYNC_GROUP_MEMBERSHIP_AFTER_SUCCESSFUL_USER_AUTH_ENABLED;
import static com.atlassian.crowd.model.directory.DirectoryImpl.ATTRIBUTE_KEY_USE_NESTED_GROUPS;
import static com.deftdevs.bootstrapi.commons.util.ConversionUtil.toBoolean;
import static com.deftdevs.bootstrapi.commons.util.ConversionUtil.toInt;
import static com.deftdevs.bootstrapi.commons.util.ConversionUtil.toLong;

public class DirectoryModelUtil {

    /**
     * Build directory.
     *
     * @return the directory
     */
    public static Directory toDirectory(
            final DirectoryCrowdModel directoryModel) {

        final Map<String, String> attributes = new HashMap<>();

        if (directoryModel.getServer() != null) {
            attributes.put(CROWD_SERVER_URL, directoryModel.getServer().getUrl().toString());
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
                .setDescription(directoryModel.getDescription())
                .setAttributes(attributes)
                .build();
    }

    /**
     * Build user directory bean user directory bean.
     *
     * @param directory the directory
     * @return the user directory bean
     */
    public static AbstractDirectoryModel toDirectoryModel(
            final Directory directory) {

        final Map<String, String> attributes = directory.getAttributes();
        final AbstractDirectoryModel directoryModel;

        if (DirectoryType.CROWD.equals(directory.getType())) {
            DirectoryCrowdServer.DirectoryCrowdServerProxy proxy = null;

            if (attributes.containsKey(CROWD_HTTP_PROXY_HOST)) {
                proxy = DirectoryCrowdServer.DirectoryCrowdServerProxy.builder()
                    .username(attributes.get(CROWD_HTTP_PROXY_USERNAME))
                    .host(attributes.get(CROWD_HTTP_PROXY_HOST))
                    .port(attributes.get(CROWD_HTTP_PROXY_PORT) != null ? Integer.valueOf(attributes.get(CROWD_HTTP_PROXY_PORT)) : null)
                    .build();
            }
            DirectoryCrowdServer serverModel = DirectoryCrowdServer.builder()
                .url(URI.create(attributes.get(CROWD_SERVER_URL)))
                .proxy(proxy)
                .connectionTimeoutInMillis(toLong(attributes.get(CROWD_HTTP_TIMEOUT)))
                .maxConnections(toInt(attributes.get(CROWD_HTTP_MAX_CONNECTIONS)))
                .appUsername(attributes.get(APPLICATION_NAME))
                .build();

            DirectoryCrowdAdvanced advanced = DirectoryCrowdAdvanced.builder()
                .enableIncrementalSync(toBoolean(attributes.get(INCREMENTAL_SYNC_ENABLED)))
                .enableNestedGroups(toBoolean(attributes.get(ATTRIBUTE_KEY_USE_NESTED_GROUPS)))
                .updateSyncIntervalInMinutes(toInt(attributes.get(CACHE_SYNCHRONISE_INTERVAL)))
                .build();

            directoryModel = DirectoryCrowdModel.builder()
                .server(serverModel)
                .advanced(advanced)
                .build();
        } else  {
            directoryModel = DirectoryGenericModel.builder().build();
        }

        directoryModel.setId(directory.getId());
        directoryModel.setName(directory.getName());
        directoryModel.setActive(directory.isActive());
        directoryModel.setDescription(directory.getDescription());
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
