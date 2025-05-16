package com.deftdevs.bootstrapi.commons.exception;

import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;

@SuppressWarnings("java:S110")
public class DirectoryNotFoundException extends NotFoundException {

    public static final String ENTITY_NAME = "Directory";

    public DirectoryNotFoundException(
            final String name) {

        super(ENTITY_NAME, name);
    }

    public DirectoryNotFoundException(
            final long id) {

        super(ENTITY_NAME, id);
    }

    public DirectoryNotFoundException(
            final AbstractDirectoryModel directoryModel) {

        this(directoryModel.getName());
    }

}
