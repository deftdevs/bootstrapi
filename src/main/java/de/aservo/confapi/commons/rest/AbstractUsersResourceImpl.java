package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.UserBean;
import de.aservo.confapi.commons.rest.api.UsersResource;
import de.aservo.confapi.commons.service.api.UsersService;

import javax.ws.rs.core.Response;

public class AbstractUsersResourceImpl implements UsersResource {

    private final UsersService usersService;

    public AbstractUsersResourceImpl(final UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public Response getUser(
            final String userName) {

        final UserBean userBean = usersService.getUser(userName);
        return Response.ok(userBean).build();
    }

    @Override
    public Response setUser(
            final String userName,
            final UserBean userBean) {

        final UserBean updatedUserBean = usersService.updateUser(userName, userBean);
        return Response.ok(updatedUserBean).build();
    }

    @Override
    public Response setUserPassword(
            final String userName,
            final String password) {

        final UserBean updatedUserBean = usersService.updatePassword(userName, password);
        return Response.ok(updatedUserBean).build();
    }

}
