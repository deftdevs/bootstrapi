package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.deftdevs.bootstrapi.crowd.model.SessionConfigModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SessionConfigServiceTest {

    @Mock
    private PropertyManager propertyManager;

    private SessionConfigServiceImpl sessionConfigService;

    @BeforeEach
    public void setup() {
        sessionConfigService = new SessionConfigServiceImpl(propertyManager);
    }

    @Test
    public void testGetSessionConfig() {
        doReturn(SessionConfigModel.EXAMPLE_2.getSessionTimeoutInMinutes()).when(propertyManager).getSessionTime();
        doReturn(SessionConfigModel.EXAMPLE_2.getRequireConsistentClientIP()).when(propertyManager).isIncludeIpAddressInValidationFactors();

        final SessionConfigModel sessionConfigModel = sessionConfigService.getSessionConfig();
        assertEquals(SessionConfigModel.EXAMPLE_2, sessionConfigModel);
    }

    @Test
    public void testSetSessionConfig() {
        doReturn(SessionConfigModel.EXAMPLE_2.getSessionTimeoutInMinutes()).when(propertyManager).getSessionTime();
        doReturn(SessionConfigModel.EXAMPLE_2.getRequireConsistentClientIP()).when(propertyManager).isIncludeIpAddressInValidationFactors();

        final ArgumentCaptor<Long> sessionTimeoutInMinutesCaptor = ArgumentCaptor.forClass(Long.class);
        final ArgumentCaptor<Boolean> requireConsistentClientIPCaptor = ArgumentCaptor.forClass(Boolean.class);
        sessionConfigService.setSessionConfig(SessionConfigModel.EXAMPLE_1);
        verify(propertyManager).setSessionTime(sessionTimeoutInMinutesCaptor.capture());
        verify(propertyManager).setIncludeIpAddressInValidationFactors(requireConsistentClientIPCaptor.capture());

        final Long sessionTimeoutInMinutes = sessionTimeoutInMinutesCaptor.getValue();
        final Boolean requireConsistentClientIP = requireConsistentClientIPCaptor.getValue();

        assertEquals(SessionConfigModel.EXAMPLE_1.getSessionTimeoutInMinutes(), sessionTimeoutInMinutes);
        assertEquals(SessionConfigModel.EXAMPLE_1.getRequireConsistentClientIP(), requireConsistentClientIP);
    }

}
