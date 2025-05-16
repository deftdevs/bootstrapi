package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.UserModel;
import com.deftdevs.bootstrapi.commons.rest.api.UserResource;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;

import javax.ws.rs.core.Response;

public class AbstractUserResourceImpl implements UserResource {

    private final UsersService usersService;

    public AbstractUserResourceImpl(final UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public Response getUser(
            final String username) {

        final UserModel userModel = usersService.getUser(username);
        return Response.ok(userModel).build();
    }

    @Override
    public Response setUser(
            final String username,
            final UserModel userModel) {

        final UserModel updatedUserModel = usersService.updateUser(username, userModel);
        return Response.ok(updatedUserModel).build();
    }

    @Override
    public Response setUserPassword(
            final String username,
            final String password) {

        final UserModel updatedUserModel = usersService.updatePassword(username, password);
        return Response.ok(updatedUserModel).build();
    }

}
