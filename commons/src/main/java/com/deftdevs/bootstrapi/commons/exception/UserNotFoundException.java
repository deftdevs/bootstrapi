package com.deftdevs.bootstrapi.commons.exception;

import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.UserBean;

@SuppressWarnings("java:S110")
public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(
            final UserBean userBean) {

        super(userBean.getUsername());
    }

    public UserNotFoundException(
            final String name) {

        super(String.format("User with name '%s' could not be found", name));
    }

}
