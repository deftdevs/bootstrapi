package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.exception.BadRequestException;
import de.aservo.atlassian.confapi.exception.NotFoundException;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confapi.model.UserBean;
import de.aservo.atlassian.confapi.service.api.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class AbstractUsersResourceTest {

    @Mock
    private UserService userService;

    private TestUsersResourceImpl resource;

    @Before
    public void setup() {
        resource = new TestUsersResourceImpl(userService);
    }

    @Test
    public void testGetUser() throws NotFoundException {
        final UserBean bean = UserBean.EXAMPLE_1;

        doReturn(bean).when(userService).getUser(bean.getUserName());

        final Response response = resource.getUser(bean.getUserName());
        assertEquals(200, response.getStatus());
        final UserBean userBean = (UserBean) response.getEntity();

        assertEquals(userBean, bean);
    }

    @Test
    public void testGetUserWithError() throws NotFoundException {
        doThrow(new NotFoundException("user")).when(userService).getUser(any(String.class));

        final Response response = resource.getUser("user");
        assertEquals(404, response.getStatus());

        assertNotNull(response.getEntity());
        assertEquals(ErrorCollection.class, response.getEntity().getClass());
    }

    @Test
    public void testUpdateUser() throws NotFoundException, BadRequestException {
        final UserBean bean = UserBean.EXAMPLE_1;

        doReturn(bean).when(userService).updateUser(bean.getUserName(), bean);

        final Response response = resource.updateUser(bean.getUserName(), bean);
        assertEquals(200, response.getStatus());
        final UserBean userBean = (UserBean) response.getEntity();

        assertEquals(userBean, bean);
    }

    @Test
    public void testUpdateUserWithError() throws NotFoundException, BadRequestException {
        final UserBean bean = UserBean.EXAMPLE_1;

        doThrow(new NotFoundException("user")).when(userService).updateUser(anyString(), any(UserBean.class));

        final Response response = resource.updateUser(bean.getUserName(), bean);
        assertEquals(404, response.getStatus());

        assertNotNull(response.getEntity());
        assertEquals(ErrorCollection.class, response.getEntity().getClass());
    }

    @Test
    public void testUpdateUserPassword() throws NotFoundException, BadRequestException {
        final UserBean bean = UserBean.EXAMPLE_1;

        doReturn(bean).when(userService).updatePassword(bean.getUserName(), bean.getPassword());

        final Response response = resource.setUserPassword(bean.getUserName(), bean.getPassword());
        assertEquals(200, response.getStatus());
        final UserBean userBean = (UserBean) response.getEntity();

        assertEquals(userBean, bean);
    }

    @Test
    public void testUpdateUserPasswordWithError() throws NotFoundException, BadRequestException {
        final UserBean bean = UserBean.EXAMPLE_1;

        doThrow(new NotFoundException("user")).when(userService).updatePassword(any(String.class), any(String.class));

        final Response response = resource.setUserPassword(bean.getUserName(), bean.getPassword());
        assertEquals(404, response.getStatus());

        assertNotNull(response.getEntity());
        assertEquals(ErrorCollection.class, response.getEntity().getClass());
    }
}
