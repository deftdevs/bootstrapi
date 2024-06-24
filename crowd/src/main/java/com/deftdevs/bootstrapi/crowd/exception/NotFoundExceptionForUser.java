package com.deftdevs.bootstrapi.crowd.exception;

import com.deftdevs.bootstrapi.commons.exception.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.UserBean;

@SuppressWarnings("java:S110")
public class NotFoundExceptionForUser extends NotFoundException {

    public NotFoundExceptionForUser(
            final UserBean userBean) {

        super(userBean.getUsername());
    }

    public NotFoundExceptionForUser(
            final String name) {

        super(String.format("User with name '%s' could not be found", name));
    }

}
