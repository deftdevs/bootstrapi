package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.model.UserBean;
import de.aservo.atlassian.confapi.rest.api.UsersResource;
import de.aservo.atlassian.confapi.service.api.UserService;

import javax.ws.rs.core.Response;

public class AbstractUsersResourceImpl implements UsersResource {

    private final UserService userService;

    public AbstractUsersResourceImpl(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response getUser(
            final String userName) {

        final UserBean userBean = userService.getUser(userName);
        return Response.ok(userBean).build();
    }

    @Override
    public Response updateUser(
            final String userName,
            final UserBean userBean) {

        final UserBean updatedUserBean = userService.updateUser(userName, userBean);
        return Response.ok(updatedUserBean).build();
    }

    @Override
    public Response setUserPassword(
            final String userName,
            final String password) {

        final UserBean updatedUserBean = userService.updatePassword(userName, password);
        return Response.ok(updatedUserBean).build();
    }

}
