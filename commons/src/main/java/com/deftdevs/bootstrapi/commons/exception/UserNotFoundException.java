package com.deftdevs.bootstrapi.commons.exception;

import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.UserBean;

@SuppressWarnings("java:S110")
public class UserNotFoundException extends NotFoundException {

    public static final String ENTITY_NAME = "User";

    public UserNotFoundException(
            final String name) {

        super(ENTITY_NAME, name);
    }

    public UserNotFoundException(
            final long id) {

        super(ENTITY_NAME, id);
    }

    public UserNotFoundException(
            final UserBean userBean) {

        this(userBean.getUsername());
    }

}
