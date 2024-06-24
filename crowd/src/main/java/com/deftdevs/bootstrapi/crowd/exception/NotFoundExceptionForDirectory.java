package com.deftdevs.bootstrapi.crowd.exception;

import com.deftdevs.bootstrapi.commons.exception.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryBean;

@SuppressWarnings("java:S110")
public class NotFoundExceptionForDirectory extends NotFoundException {

    public NotFoundExceptionForDirectory(
            final AbstractDirectoryBean directoryBean) {

        super(directoryBean.getName());
    }

    public NotFoundExceptionForDirectory(
            final String name) {

        super(String.format("Directory with name '%s' could not be found", name));
    }

    public NotFoundExceptionForDirectory(
            final long id) {

        super(String.format("Directory with id '%d' could not be found", id));
    }

}
