package de.aservo.confapi.crowd.exception;

import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.commons.model.GroupBean;

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
