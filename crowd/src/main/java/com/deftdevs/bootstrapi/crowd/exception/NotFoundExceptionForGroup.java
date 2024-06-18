package com.deftdevs.bootstrapi.crowd.exception;

import com.deftdevs.bootstrapi.commons.exception.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.GroupBean;

@SuppressWarnings("java:S110")
public class NotFoundExceptionForGroup extends NotFoundException {

    public NotFoundExceptionForGroup(
            final GroupBean groupBean) {

        this(groupBean.getName());
    }

    public NotFoundExceptionForGroup(
            final String name) {

        super(String.format("Group with name '%s' could not be found", name));
    }

}
