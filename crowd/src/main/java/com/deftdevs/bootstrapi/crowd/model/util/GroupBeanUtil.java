package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.model.group.Group;
import com.deftdevs.bootstrapi.commons.model.GroupBean;

import javax.annotation.Nonnull;

public class GroupBeanUtil {

    /**
     * Build group bean.
     *
     * @param group the group
     * @return the group bean
     */
    @Nonnull
    public static GroupBean toGroupBean(
            @Nonnull final Group group) {

        final GroupBean groupBean = new GroupBean();
        groupBean.setName(group.getName());
        groupBean.setDescription(group.getDescription());
        groupBean.setActive(group.isActive());

        return groupBean;
    }

    private GroupBeanUtil() {
    }

}
