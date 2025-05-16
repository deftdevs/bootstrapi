package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.UserModel;
import com.deftdevs.bootstrapi.commons.rest.impl.TestUserResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserResourceTest {

    @Mock
    private UsersService usersService;

    private TestUserResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestUserResourceImpl(usersService);
    }

    @Test
    void testGetUser() {
        final UserModel bean = UserModel.EXAMPLE_1;

        doReturn(bean).when(usersService).getUser(bean.getUsername());

        final Response response = resource.getUser(bean.getUsername());
        assertEquals(200, response.getStatus());
        final UserModel userModel = (UserModel) response.getEntity();

        assertEquals(userModel, bean);
    }

    @Test
    void testUpdateUser() {
        final UserModel bean = UserModel.EXAMPLE_1;

        doReturn(bean).when(usersService).updateUser(bean.getUsername(), bean);

        final Response response = resource.setUser(bean.getUsername(), bean);
        assertEquals(200, response.getStatus());
        final UserModel userModel = (UserModel) response.getEntity();

        assertEquals(userModel, bean);
    }

    @Test
    void testUpdateUserPassword() {
        final UserModel bean = UserModel.EXAMPLE_1;

        doReturn(bean).when(usersService).updatePassword(bean.getUsername(), bean.getPassword());

        final Response response = resource.setUserPassword(bean.getUsername(), bean.getPassword());
        assertEquals(200, response.getStatus());
        final UserModel userModel = (UserModel) response.getEntity();

        assertEquals(userModel, bean);
    }

}
