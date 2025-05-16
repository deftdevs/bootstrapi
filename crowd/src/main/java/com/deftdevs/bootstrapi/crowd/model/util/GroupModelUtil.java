package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.model.group.Group;
import com.deftdevs.bootstrapi.commons.model.GroupModel;

import javax.annotation.Nonnull;

public class GroupModelUtil {

    /**
     * Build group bean.
     *
     * @param group the group
     * @return the group bean
     */
    @Nonnull
    public static GroupModel toGroupModel(
            @Nonnull final Group group) {

        final GroupModel groupModel = new GroupModel();
        groupModel.setName(group.getName());
        groupModel.setDescription(group.getDescription());
        groupModel.setActive(group.isActive());

        return groupModel;
    }

    private GroupModelUtil() {
    }

}
