package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.service.api.UserService;

public class TestUsersResourceImpl extends AbstractUsersResourceImpl {

    public TestUsersResourceImpl(UserService userService) {
        super(userService);
    }

}
