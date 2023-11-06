package de.aservo.confapi.crowd.exception;

import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.commons.model.UserBean;

@SuppressWarnings("java:S110")
public class NotFoundExceptionForUser extends NotFoundException {

    public NotFoundExceptionForUser(
            final UserBean userBean) {

        this(userBean.getUsername());
    }

    public NotFoundExceptionForUser(
            final String name) {

        super(String.format("User with name '%s' could not be found", name));
    }

}
