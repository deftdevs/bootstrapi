package com.deftdevs.bootstrapi.commons.exception;

import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryBean;

@SuppressWarnings("java:S110")
public class DirectoryNotFoundException extends NotFoundException {

    public DirectoryNotFoundException(
            final AbstractDirectoryBean directoryBean) {

        super(directoryBean.getName());
    }

    public DirectoryNotFoundException(
            final String name) {

        super(String.format("Directory with name '%s' could not be found", name));
    }

    public DirectoryNotFoundException(
            final long id) {

        super(String.format("Directory with id '%d' could not be found", id));
    }

}
