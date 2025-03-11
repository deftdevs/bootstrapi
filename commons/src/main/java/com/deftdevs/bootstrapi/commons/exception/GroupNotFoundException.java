package com.deftdevs.bootstrapi.commons.exception;

import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.GroupBean;

@SuppressWarnings("java:S110")
public class GroupNotFoundException extends NotFoundException {

    public static final String ENTITY_NAME = "Group";

    public GroupNotFoundException(
            final String name) {

        super(ENTITY_NAME, name);
    }

    public GroupNotFoundException(
            final long id) {

        super(ENTITY_NAME, id);
    }

    public GroupNotFoundException(
            final GroupBean groupBean) {

        this(groupBean.getName());
    }

}
