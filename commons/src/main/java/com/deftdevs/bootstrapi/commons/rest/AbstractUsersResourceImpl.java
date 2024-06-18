package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.UserBean;
import com.deftdevs.bootstrapi.commons.rest.api.UsersResource;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;

import javax.ws.rs.core.Response;

public class AbstractUsersResourceImpl implements UsersResource {

    private final UsersService usersService;

    public AbstractUsersResourceImpl(final UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public Response getUser(
            final String username) {

        final UserBean userBean = usersService.getUser(username);
        return Response.ok(userBean).build();
    }

    @Override
    public Response setUser(
            final String username,
            final UserBean userBean) {

        final UserBean updatedUserBean = usersService.updateUser(username, userBean);
        return Response.ok(updatedUserBean).build();
    }

    @Override
    public Response setUserPassword(
            final String username,
            final String password) {

        final UserBean updatedUserBean = usersService.updatePassword(username, password);
        return Response.ok(updatedUserBean).build();
    }

}
