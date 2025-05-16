package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.languages.LocaleManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.gadgets.GadgetParsingException;
import com.atlassian.gadgets.GadgetRequestContext;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpec;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpecId;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpecStore;
import com.atlassian.gadgets.spec.GadgetSpec;
import com.atlassian.gadgets.spec.GadgetSpecFactory;
import com.atlassian.sal.api.user.UserKey;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.GadgetModel;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;
import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GadgetsServiceTest {

    @Mock
    private ExternalGadgetSpecStore externalGadgetSpecStore;

    @Mock
    private GadgetSpecFactory gadgetSpecFactory;

    @Mock
    private LocaleManager localeManager;

    private GadgetsService gadgetsService;

    @BeforeEach
    public void setup() {
        gadgetsService = new GadgetsServiceImpl(externalGadgetSpecStore, gadgetSpecFactory, localeManager);
    }

    @Test
    void testGetGadgets() throws URISyntaxException {
        final ExternalGadgetSpec externalGadgetSpec = createExternalGadgetSpec();
        doReturn(Collections.singletonList(externalGadgetSpec)).when(externalGadgetSpecStore).entries();

        final List<GadgetModel> gadgetModels = gadgetsService.getGadgets();
        assertEquals(externalGadgetSpec.getSpecUri(), gadgetModels.iterator().next().getUrl());
    }

    @Test
    void testGetGadget() throws URISyntaxException {
        ExternalGadgetSpec externalGadgetSpec = createExternalGadgetSpec();
        doReturn(Collections.singletonList(externalGadgetSpec)).when(externalGadgetSpecStore).entries();

        GadgetModel gadgetModel = gadgetsService.getGadget(1L);

        assertEquals(externalGadgetSpec.getSpecUri(), gadgetModel.getUrl());
    }

    @Test
    void testGetGadgetIdNotExisting() {
        assertThrows(NotFoundException.class, () -> {
            gadgetsService.getGadget(0L);
        });
    }

    @Test
    void testAddGadget() throws URISyntaxException, IllegalAccessException {
        ExternalGadgetSpec externalGadgetSpec = createExternalGadgetSpec();
        doReturn(externalGadgetSpec).when(externalGadgetSpecStore).add(any());

        ConfluenceUser user = createConfluenceUser();
        GadgetModel gadgetModel = new GadgetModel();
        gadgetModel.setUrl(externalGadgetSpec.getSpecUri());

        GadgetSpec gadgetSpec = GadgetSpec.gadgetSpec(externalGadgetSpec.getSpecUri()).build();
        doReturn(gadgetSpec).when(gadgetSpecFactory).getGadgetSpec(any(URI.class), any(GadgetRequestContext.class));

        try (MockedStatic<AuthenticatedUserThreadLocal> authenticatedUserThreadLocalMockedStatic = mockStatic(AuthenticatedUserThreadLocal.class)) {
            authenticatedUserThreadLocalMockedStatic.when(AuthenticatedUserThreadLocal::get).thenReturn(user);

            final GadgetModel responseGadgetModel = gadgetsService.addGadget(gadgetModel);
            assertEquals(externalGadgetSpec.getSpecUri(), responseGadgetModel.getUrl());
        }
    }

    @Test
    void testAddGadgetException() throws URISyntaxException, IllegalAccessException {
        ExternalGadgetSpec externalGadgetSpec = createExternalGadgetSpec();
        doReturn(externalGadgetSpec).when(externalGadgetSpecStore).add(any());

        ConfluenceUser user = createConfluenceUser();
        GadgetModel gadgetModel = new GadgetModel();
        gadgetModel.setUrl(externalGadgetSpec.getSpecUri());

        doReturn(Locale.GERMAN).when(localeManager).getLocale(user);
        doThrow(new GadgetParsingException("")).when(gadgetSpecFactory).getGadgetSpec((URI) any(), any());

        try (MockedStatic<AuthenticatedUserThreadLocal> authenticatedUserThreadLocalMockedStatic = mockStatic(AuthenticatedUserThreadLocal.class)) {
            authenticatedUserThreadLocalMockedStatic.when(AuthenticatedUserThreadLocal::get).thenReturn(user);

            assertThrows(BadRequestException.class, () -> {
                gadgetsService.addGadget(gadgetModel);
            });
        }
    }

    @Test
    void testSetGadgets() throws URISyntaxException, IllegalAccessException {
        ExternalGadgetSpec externalGadgetSpec = createExternalGadgetSpec();
        doReturn(Collections.singletonList(externalGadgetSpec)).when(externalGadgetSpecStore).entries();

        ConfluenceUser user = createConfluenceUser();
        GadgetModel gadgetModel = new GadgetModel();
        gadgetModel.setUrl(externalGadgetSpec.getSpecUri());
        List<GadgetModel> gadgetModelsToSet = Collections.singletonList(gadgetModel);

        GadgetSpec gadgetSpec = GadgetSpec.gadgetSpec(externalGadgetSpec.getSpecUri()).build();

        try (MockedStatic<AuthenticatedUserThreadLocal> authenticatedUserThreadLocalMockedStatic = mockStatic(AuthenticatedUserThreadLocal.class)) {
            authenticatedUserThreadLocalMockedStatic.when(AuthenticatedUserThreadLocal::get).thenReturn(user);

            final List<GadgetModel> gadgetModels = gadgetsService.setGadgets(gadgetModelsToSet);
            assertEquals(externalGadgetSpec.getSpecUri(), gadgetModels.iterator().next().getUrl());
        }
    }

    @Test
    void testSetGadget() throws URISyntaxException, IllegalAccessException {
        ExternalGadgetSpec externalGadgetSpec = createExternalGadgetSpec();
        doReturn(Collections.singletonList(externalGadgetSpec)).when(externalGadgetSpecStore).entries();
        doReturn(externalGadgetSpec).when(externalGadgetSpecStore).add(any());

        ConfluenceUser user = createConfluenceUser();
        GadgetModel gadgetModel = new GadgetModel();
        gadgetModel.setId(1L);
        gadgetModel.setUrl(externalGadgetSpec.getSpecUri());
        GadgetSpec gadgetSpec = GadgetSpec.gadgetSpec(externalGadgetSpec.getSpecUri()).build();

        doReturn(Locale.GERMAN).when(localeManager).getLocale(user);
        doReturn(gadgetSpec).when(gadgetSpecFactory).getGadgetSpec(any(URI.class), any(GadgetRequestContext.class));

        try (MockedStatic<AuthenticatedUserThreadLocal> authenticatedUserThreadLocalMockedStatic = mockStatic(AuthenticatedUserThreadLocal.class)) {
            authenticatedUserThreadLocalMockedStatic.when(AuthenticatedUserThreadLocal::get).thenReturn(user);

            GadgetModel responseGadgetModel = gadgetsService.setGadget(1L, gadgetModel);
            assertEquals(externalGadgetSpec.getSpecUri(), responseGadgetModel.getUrl());
        }
    }

    @Test
    void testDeleteGadgets() {
        gadgetsService.deleteGadgets(true);
    }

    @Test
    void testDeleteGadgetsWithoutForceParameter() {
        assertThrows(BadRequestException.class, () -> {
            gadgetsService.deleteGadgets(false);
        });
    }

    @Test
    void testDeleteGadget() throws URISyntaxException {
        ExternalGadgetSpec externalGadgetSpec = createExternalGadgetSpec();
        doReturn(Collections.singletonList(externalGadgetSpec)).when(externalGadgetSpecStore).entries();

        gadgetsService.deleteGadget(1L);
    }

    @Test
    void testDeleteGadgetsIdNotExisting() {
        assertThrows(NotFoundException.class, () -> {
            gadgetsService.deleteGadget(0L);
        });
    }

    private ExternalGadgetSpec createExternalGadgetSpec() throws URISyntaxException {
        ExternalGadgetSpecId id = ExternalGadgetSpecId.valueOf("1");
        return new ExternalGadgetSpec(id, new URI("http://localhost"));
    }

    private ConfluenceUser createConfluenceUser() throws IllegalAccessException {
        ConfluenceUser user = new ConfluenceUserImpl("test", "test test", "test@test.de");
        FieldUtils.writeDeclaredField(user, "key", new UserKey(UUID.randomUUID().toString()), true);
        return user;
    }

}
