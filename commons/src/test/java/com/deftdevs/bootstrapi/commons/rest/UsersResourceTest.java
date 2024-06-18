package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.UserBean;
import com.deftdevs.bootstrapi.commons.rest.impl.TestUsersResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UsersResourceTest {

    @Mock
    private UsersService usersService;

    private TestUsersResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestUsersResourceImpl(usersService);
    }

    @Test
    void testGetUser() {
        final UserBean bean = UserBean.EXAMPLE_1;

        doReturn(bean).when(usersService).getUser(bean.getUsername());

        final Response response = resource.getUser(bean.getUsername());
        assertEquals(200, response.getStatus());
        final UserBean userBean = (UserBean) response.getEntity();

        assertEquals(userBean, bean);
    }

    @Test
    void testUpdateUser() {
        final UserBean bean = UserBean.EXAMPLE_1;

        doReturn(bean).when(usersService).updateUser(bean.getUsername(), bean);

        final Response response = resource.setUser(bean.getUsername(), bean);
        assertEquals(200, response.getStatus());
        final UserBean userBean = (UserBean) response.getEntity();

        assertEquals(userBean, bean);
    }

    @Test
    void testUpdateUserPassword() {
        final UserBean bean = UserBean.EXAMPLE_1;

        doReturn(bean).when(usersService).updatePassword(bean.getUsername(), bean.getPassword());

        final Response response = resource.setUserPassword(bean.getUsername(), bean.getPassword());
        assertEquals(200, response.getStatus());
        final UserBean userBean = (UserBean) response.getEntity();

        assertEquals(userBean, bean);
    }

}
