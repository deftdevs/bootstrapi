package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.commons.model.GroupBean;

import java.util.List;

public interface GroupsService {

    /**
     * Get the group.
     *
     * @param directoryId the directory id
     * @param groupName the groupName
     * @return the group bean
     */
    GroupBean getGroup(
            final long directoryId,
            final String groupName);

    /**
     * Set (add or update) group.
     *
     * @param directoryId the directory id
     * @param groupName the group name
     * @param groupBean the group bean
     * @return the set user bean
     */
    GroupBean setGroup(
            final long directoryId,
            final String groupName,
            final GroupBean groupBean);

    /**
     * Create a group.
     *
     * @param directoryId the directory id
     * @param groupBean the group bean
     * @return the group bean
     */
    GroupBean createGroup(
            long directoryId,
            GroupBean groupBean);

    /**
     * Update a group.
     *
     * @param directoryId the directory id
     * @param groupName the group name
     * @param groupBean the group bean
     * @return the group bean
     */
    GroupBean updateGroup(
            long directoryId,
            String groupName,
            GroupBean groupBean);

    /**
     * Set (add or update) groups.
     *
     * @param directoryId the directory id
     * @param List<groupsBeanBeans> the groups bean
     * @return the set group beans
     */
    List<GroupBean> setGroups(
            final long directoryId,
            final List<GroupBean> groupBeans);

}
