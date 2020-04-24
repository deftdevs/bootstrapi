package de.aservo.atlassian.confapi.junit;

import de.aservo.atlassian.confapi.exception.api.AbstractClientErrorWebException;
import de.aservo.atlassian.confapi.exception.api.AbstractServerErrorWebException;
import de.aservo.atlassian.confapi.exception.api.AbstractSuccessWebException;
import de.aservo.atlassian.confapi.exception.api.AbstractWebException;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public abstract class AbstractWebExceptionTest extends AbstractTest {

    private static final String MESSAGE = "message";
    private static final String THROWABLE_MESSAGE = "throwable-message";
    private static final Throwable THROWABLE = new Throwable(THROWABLE_MESSAGE);

    @Test
    public void testParentClassMatchesStatusCodeCategory() throws NoSuchMethodException {
        final Class<?> baseClass = getBaseClass();
        final Constructor<?> constructor = getBaseClass().getConstructor(String.class);

        final AbstractWebException exception;
        try {
            exception = (AbstractWebException) constructor.newInstance(MESSAGE);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("The web exception should inherit from one of AbstractWebException's subclasses");
            return;
        }

        final Response.Status.Family exceptionStatusFamily = exception.getStatus().getFamily();

        if (AbstractSuccessWebException.class.isAssignableFrom(baseClass)) {
            assertEquals("The success web exception status code family should be SUCCESSFUL",
                    Response.Status.Family.SUCCESSFUL, exceptionStatusFamily);

        } else if (AbstractClientErrorWebException.class.isAssignableFrom(baseClass)) {
            assertEquals("The client error web exception status code family should be CLIENT_ERROR",
                    Response.Status.Family.CLIENT_ERROR, exceptionStatusFamily);

        } else if (AbstractServerErrorWebException.class.isAssignableFrom(baseClass)) {
            assertEquals("The server error exception status code family should be SERVER_ERROR",
                    Response.Status.Family.SERVER_ERROR, exceptionStatusFamily);

        } else if (AbstractWebException.class.isAssignableFrom(baseClass)) {
            fail("The web exception should not directory inherit from AbstractWebException");
        }
    }

    // simple tests to make Sonar happy that constructor behaviour is tested

    @Test
    public void testMessageConstructor() throws Exception {
        final Constructor<?> constructor = getBaseClass().getConstructor(String.class);
        final AbstractWebException exception = (AbstractWebException) constructor.newInstance(MESSAGE);

        assertEquals(MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageAndThrowableConstructor() throws Exception {
        final Constructor<?> constructor = getBaseClass().getConstructor(String.class, Throwable.class);
        final AbstractWebException exception = (AbstractWebException) constructor.newInstance(MESSAGE, THROWABLE);

        assertEquals(MESSAGE, exception.getMessage());
        assertEquals(THROWABLE, exception.getCause());
    }

    @Test
    public void testThrowableConstructor() throws Exception {
        final Constructor<?> constructor = getBaseClass().getConstructor(Throwable.class);
        final AbstractWebException exception = (AbstractWebException) constructor.newInstance(THROWABLE);

        assertTrue(exception.getMessage().endsWith(THROWABLE_MESSAGE));
        assertEquals(THROWABLE, exception.getCause());
    }

}
