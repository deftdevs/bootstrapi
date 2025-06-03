package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.crowd.directory.RemoteCrowdDirectory;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel.DirectoryCrowdAdvanced;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel.DirectoryCrowdServer;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel.DirectoryCrowdServer.DirectoryCrowdServerProxy;
import com.deftdevs.bootstrapi.commons.model.DirectoryGenericModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryInternalModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryLdapModel;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.atlassian.crowd.directory.RemoteCrowdDirectory.*;
import static com.atlassian.crowd.directory.SynchronisableDirectoryProperties.*;
import static com.atlassian.crowd.model.directory.DirectoryImpl.ATTRIBUTE_KEY_USE_NESTED_GROUPS;
import static com.deftdevs.bootstrapi.commons.util.ConversionUtil.*;

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

            DirectoryCrowdServer serverModel = new DirectoryCrowdServer();
            serverModel.setUrl(URI.create(attributes.get(CROWD_SERVER_URL)));
            if (attributes.containsKey(CROWD_HTTP_PROXY_HOST)) {
                DirectoryCrowdServerProxy proxy = new DirectoryCrowdServerProxy();
                proxy.setUsername(attributes.get(CROWD_HTTP_PROXY_USERNAME));
                proxy.setHost(attributes.get(CROWD_HTTP_PROXY_HOST));
                if (attributes.get(CROWD_HTTP_PROXY_PORT) != null) {
                    proxy.setPort(Integer.valueOf(attributes.get(CROWD_HTTP_PROXY_PORT)));
                }
                serverModel.setProxy(proxy);
            }
            serverModel.setConnectionTimeoutInMillis(toLong(attributes.get(CROWD_HTTP_TIMEOUT)));
            serverModel.setMaxConnections(toInt(attributes.get(CROWD_HTTP_MAX_CONNECTIONS)));
            serverModel.setAppUsername(attributes.get(APPLICATION_NAME));

            DirectoryCrowdAdvanced advanced = new DirectoryCrowdAdvanced();
            advanced.setEnableIncrementalSync(toBoolean(attributes.get(INCREMENTAL_SYNC_ENABLED)));
            advanced.setEnableNestedGroups(toBoolean(attributes.get(ATTRIBUTE_KEY_USE_NESTED_GROUPS)));
            advanced.setUpdateSyncIntervalInMinutes(toInt(attributes.get(CACHE_SYNCHRONISE_INTERVAL)));

            DirectoryCrowdModel directoryCrowdModel = new DirectoryCrowdModel();
            directoryCrowdModel.setServer(serverModel);
            directoryCrowdModel.setAdvanced(advanced);
            directoryModel = directoryCrowdModel;

        } else  {
            directoryModel = new DirectoryGenericModel();
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
