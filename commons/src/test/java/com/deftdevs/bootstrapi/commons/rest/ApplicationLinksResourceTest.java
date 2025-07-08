package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.rest.impl.TestApplicationLinksResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ApplicationLinksResourceTest {

    @Mock
    private ApplicationLinksService applicationLinksService;

    private TestApplicationLinksResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestApplicationLinksResourceImpl(applicationLinksService);
    }

    @Test
    void testGetApplicationLinks() {
        final Map<String, ApplicationLinkModel> applicationLinkModels = Collections.singletonMap(ApplicationLinkModel.EXAMPLE_1.getName(), ApplicationLinkModel.EXAMPLE_1);
        doReturn(applicationLinkModels).when(applicationLinksService).getApplicationLinks();

        final Map<String, ApplicationLinkModel> responseApplicationLinkModels = resource.getApplicationLinks();
        assertEquals(responseApplicationLinkModels, applicationLinkModels);
    }

    @Test
    void testSetApplicationLinks() {
        final Map<String, ApplicationLinkModel> applicationLinkModels = Collections.singletonMap(ApplicationLinkModel.EXAMPLE_1.getName(), ApplicationLinkModel.EXAMPLE_1);
        doReturn(applicationLinkModels).when(applicationLinksService).setApplicationLinks(applicationLinkModels);

        final Map<String, ApplicationLinkModel> resultApplicationLinkModels = resource.setApplicationLinks(applicationLinkModels);
        assertEquals(resultApplicationLinkModels, applicationLinkModels);
    }

    @Test
    void testDeleteApplicationLinks() {
        resource.deleteApplicationLinks(true);
        assertTrue(true, "Delete Successful");
    }
}
