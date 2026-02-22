package com.deftdevs.bootstrapi.crowd.config;

import com.atlassian.crowd.manager.application.ApplicationManager;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.URI;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test configuration that provides mocked versions of OSGi services
 * for unit and integration tests that don't need real OSGi services.
 */
@Configuration
public class TestConfig {

    @Bean
    @Primary
    public PropertyManager propertyManager() throws PropertyManagerException {
        PropertyManager mock = mock(PropertyManager.class);
        // Configure basic mock behavior
        when(mock.getBaseUrl()).thenReturn(URI.create("http://localhost:8095/crowd"));
        return mock;
    }

    @Bean
    @Primary
    public ApplicationManager applicationManager() {
        return mock(ApplicationManager.class);
    }

    @Bean
    @Primary
    public DirectoryManager directoryManager() {
        return mock(DirectoryManager.class);
    }
}
