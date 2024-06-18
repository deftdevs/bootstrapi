package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.deftdevs.bootstrapi.crowd.model.SessionConfigBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SessionConfigServiceTest {

    @Mock
    private PropertyManager propertyManager;

    private SessionConfigServiceImpl sessionConfigService;

    @Before
    public void setup() {
        sessionConfigService = new SessionConfigServiceImpl(propertyManager);
    }

    @Test
    public void testGetSessionConfig() {
        doReturn(SessionConfigBean.EXAMPLE_2.getSessionTimeoutInMinutes()).when(propertyManager).getSessionTime();
        doReturn(SessionConfigBean.EXAMPLE_2.getRequireConsistentClientIP()).when(propertyManager).isIncludeIpAddressInValidationFactors();

        final SessionConfigBean sessionConfigBean = sessionConfigService.getSessionConfig();
        assertEquals(SessionConfigBean.EXAMPLE_2, sessionConfigBean);
    }

    @Test
    public void testSetSessionConfig() {
        doReturn(SessionConfigBean.EXAMPLE_2.getSessionTimeoutInMinutes()).when(propertyManager).getSessionTime();
        doReturn(SessionConfigBean.EXAMPLE_2.getRequireConsistentClientIP()).when(propertyManager).isIncludeIpAddressInValidationFactors();

        final ArgumentCaptor<Long> sessionTimeoutInMinutesCaptor = ArgumentCaptor.forClass(Long.class);
        final ArgumentCaptor<Boolean> requireConsistentClientIPCaptor = ArgumentCaptor.forClass(Boolean.class);
        sessionConfigService.setSessionConfig(SessionConfigBean.EXAMPLE_1);
        verify(propertyManager).setSessionTime(sessionTimeoutInMinutesCaptor.capture());
        verify(propertyManager).setIncludeIpAddressInValidationFactors(requireConsistentClientIPCaptor.capture());

        final Long sessionTimeoutInMinutes = sessionTimeoutInMinutesCaptor.getValue();
        final Boolean requireConsistentClientIP = requireConsistentClientIPCaptor.getValue();

        assertEquals(SessionConfigBean.EXAMPLE_1.getSessionTimeoutInMinutes(), sessionTimeoutInMinutes);
        assertEquals(SessionConfigBean.EXAMPLE_1.getRequireConsistentClientIP(), requireConsistentClientIP);
    }

}
