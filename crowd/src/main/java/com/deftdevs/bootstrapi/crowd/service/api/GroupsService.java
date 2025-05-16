package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.commons.model.GroupModel;

import java.util.List;

public interface GroupsService {

    /**
     * Get the group.
     *
     * @param directoryId the directory id
     * @param groupName the groupName
     * @return the group bean
     */
    GroupModel getGroup(
            final long directoryId,
            final String groupName);

    /**
     * Set (add or update) group.
     *
     * @param directoryId the directory id
     * @param groupName the group name
     * @param groupModel the group bean
     * @return the set user bean
     */
    GroupModel setGroup(
            final long directoryId,
            final String groupName,
            final GroupModel groupModel);

    /**
     * Create a group.
     *
     * @param directoryId the directory id
     * @param groupModel the group bean
     * @return the group bean
     */
    GroupModel createGroup(
            long directoryId,
            GroupModel groupModel);

    /**
     * Update a group.
     *
     * @param directoryId the directory id
     * @param groupName the group name
     * @param groupModel the group bean
     * @return the group bean
     */
    GroupModel updateGroup(
            long directoryId,
            String groupName,
            GroupModel groupModel);

    /**
     * Set (add or update) groups.
     *
     * @param directoryId the directory id
     * @param groupModels the group beans
     * @return the set group beans
     */
    List<GroupModel> setGroups(
            final long directoryId,
            final List<GroupModel> groupModels);

}
