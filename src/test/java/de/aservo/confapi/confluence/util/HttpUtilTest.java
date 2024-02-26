package de.aservo.confapi.confluence.util;

import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.core.filters.ServletContextThreadLocal;
import de.aservo.confapi.commons.exception.InternalServerErrorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static de.aservo.confapi.confluence.util.HttpUtil.SERVLET_CONTEXT_INIT_PARAM_EXPORT_TASK;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HttpUtilTest {

    private static final String BASE_URL = "http://localhost:1990/confluence";
    private static final String SERVLET_PATH = "/rest";
    private static final String SERVLET_PATH_OTHER = "/download";
    private static final String REST_PATH = "/confapi/1";
    private static final String ENDPOINT_PATH = "/settings";
    private static final String ENDPOINT_PATH_OTHER = "/mail-server";
    private static final String PATH_INFO = REST_PATH + ENDPOINT_PATH;

    private ConfluenceUserImpl user;

    @Before
    public void setup() {
        user  = new ConfluenceUserImpl("test", "test test", "test@test.de");
    }

    @Test
    public void testGetUser() {
        try (MockedStatic<AuthenticatedUserThreadLocal> authenticatedUserThreadLocalMockedStatic = mockStatic(AuthenticatedUserThreadLocal.class)) {
            authenticatedUserThreadLocalMockedStatic.when(AuthenticatedUserThreadLocal::get).thenReturn(user);
            assertNotNull(HttpUtil.getUser());
        }
    }

    @Test
    public void testGetServletRequest() {
        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getRequest).thenReturn(servletRequest);
            assertNotNull(HttpUtil.getServletRequest());
        }
    }

    @Test
    public void testGetServletContext() {
        final ServletContext servletContext = mock(ServletContext.class);

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getContext).thenReturn(servletContext);
            assertNotNull(HttpUtil.getServletContext());
        }
    }

    @Test
    public void testGetBaseUrl() {
        final String requestUrl = BASE_URL + SERVLET_PATH + PATH_INFO;

        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        doReturn(new StringBuffer(requestUrl)).when(servletRequest).getRequestURL();
        doReturn(SERVLET_PATH).when(servletRequest).getServletPath();
        doReturn(PATH_INFO).when(servletRequest).getPathInfo();

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getRequest).thenReturn(servletRequest);
            assertEquals(URI.create(BASE_URL), HttpUtil.getBaseUrl());
        }
    }

    @Test
    public void testGetBaseUrlNoPathInfo() {
        final String requestUrl = BASE_URL + SERVLET_PATH;

        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        doReturn(new StringBuffer(requestUrl)).when(servletRequest).getRequestURL();
        doReturn(SERVLET_PATH).when(servletRequest).getServletPath();

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getRequest).thenReturn(servletRequest);
            assertEquals(URI.create(BASE_URL), HttpUtil.getBaseUrl());
        }
    }

    @Test
    public void testGetBaseUrlNoServletPath() {
        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        doReturn(new StringBuffer(BASE_URL)).when(servletRequest).getRequestURL();

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getRequest).thenReturn(servletRequest);
            assertEquals(URI.create(BASE_URL), HttpUtil.getBaseUrl());
        }
    }

    @Test
    public void testGetRestUrl() {
        final String requestUrl = BASE_URL + SERVLET_PATH + PATH_INFO;

        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        doReturn(new StringBuffer(requestUrl)).when(servletRequest).getRequestURL();
        doReturn(SERVLET_PATH).when(servletRequest).getServletPath();
        doReturn(PATH_INFO).when(servletRequest).getPathInfo();

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getRequest).thenReturn(servletRequest);
            final URI restUrl = UriBuilder.fromUri(BASE_URL).path(SERVLET_PATH).path(REST_PATH).build();
            assertEquals(restUrl, HttpUtil.getRestUrl());
        }
    }

    @Test(expected = InternalServerErrorException.class)
    public void testGetRestUrlNoServlet() {
        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        doReturn(new StringBuffer(BASE_URL)).when(servletRequest).getRequestURL();

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getRequest).thenReturn(servletRequest);
            HttpUtil.getRestUrl();
        }
    }

    @Test(expected = InternalServerErrorException.class)
    public void testGetRestUrlNotRestServlet() {
        final String filePath = "/file.zip";
        final String requestUrl = BASE_URL + SERVLET_PATH_OTHER + filePath;

        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        doReturn(new StringBuffer(requestUrl)).when(servletRequest).getRequestURL();
        doReturn(SERVLET_PATH_OTHER).when(servletRequest).getServletPath();
        doReturn(filePath).when(servletRequest).getPathInfo();

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getRequest).thenReturn(servletRequest);
            HttpUtil.getRestUrl();
        }
    }

    @Test
    public void testCreateUri() {
        final String requestUrl = BASE_URL + SERVLET_PATH + PATH_INFO;
        final String servlet = "download";
        final String file = "export.zip";

        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        doReturn(new StringBuffer(requestUrl)).when(servletRequest).getRequestURL();
        doReturn(SERVLET_PATH).when(servletRequest).getServletPath();
        doReturn(PATH_INFO).when(servletRequest).getPathInfo();

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getRequest).thenReturn(servletRequest);
            final URI uri = UriBuilder.fromUri(BASE_URL).path(servlet).path(file).build();
            assertEquals(uri, HttpUtil.createUri(servlet, file));
        }
    }

    @Test
    public void testCreateRestUri() {
        final String requestUrl = BASE_URL + SERVLET_PATH + PATH_INFO;

        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        doReturn(new StringBuffer(requestUrl)).when(servletRequest).getRequestURL();
        doReturn(SERVLET_PATH).when(servletRequest).getServletPath();
        doReturn(PATH_INFO).when(servletRequest).getPathInfo();

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getRequest).thenReturn(servletRequest);
            final URI restUri = UriBuilder.fromUri(BASE_URL).path(SERVLET_PATH).path(REST_PATH).path(ENDPOINT_PATH_OTHER).build();
            assertEquals(restUri, HttpUtil.createRestUri(ENDPOINT_PATH_OTHER));
        }
    }

    @Test
    public void testIsLongRunningTaskSupportedTrue() {
        final String serverInfo = "Apache Tomcat/9.0.11";

        final ServletContext servletContext = mock(ServletContext.class);
        doReturn(serverInfo).when(servletContext).getServerInfo();
        doReturn("unsupportedone,otherone").when(servletContext)
                .getInitParameter(SERVLET_CONTEXT_INIT_PARAM_EXPORT_TASK);

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getContext).thenReturn(servletContext);
            assertTrue(HttpUtil.isLongRunningTaskSupported());
        }
    }

    @Test
    public void testIsLongRunningTaskSupportedTrueUnsupportedContainersNull() {
        final String serverInfo = "Apache Tomcat/9.0.11";

        final ServletContext servletContext = mock(ServletContext.class);
        doReturn(serverInfo).when(servletContext).getServerInfo();

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getContext).thenReturn(servletContext);
            assertTrue(HttpUtil.isLongRunningTaskSupported());
        }
    }

    @Test
    public void testIsLongRunningTaskSupportedFalse() {
        final String serverInfo = "UnsupportedOne/1.2.3";

        final ServletContext servletContext = mock(ServletContext.class);
        doReturn(serverInfo).when(servletContext).getServerInfo();
        doReturn("unsupportedone,otherone").when(servletContext)
                .getInitParameter(SERVLET_CONTEXT_INIT_PARAM_EXPORT_TASK);

        try (MockedStatic<ServletContextThreadLocal> servletContextThreadLocalMockedStatic = mockStatic(ServletContextThreadLocal.class)) {
            servletContextThreadLocalMockedStatic.when(ServletContextThreadLocal::getContext).thenReturn(servletContext);
            assertTrue(HttpUtil.isLongRunningTaskSupported());
        }
    }

}
