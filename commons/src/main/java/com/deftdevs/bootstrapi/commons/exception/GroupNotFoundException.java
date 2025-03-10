package com.deftdevs.bootstrapi.commons.exception;

import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.GroupBean;

@SuppressWarnings("java:S110")
public class GroupNotFoundException extends NotFoundException {

    public GroupNotFoundException(
            final GroupBean groupBean) {

        super(groupBean.getName());
    }

    public GroupNotFoundException(
            final String name) {

        super(String.format("Group with name '%s' could not be found", name));
    }

}
