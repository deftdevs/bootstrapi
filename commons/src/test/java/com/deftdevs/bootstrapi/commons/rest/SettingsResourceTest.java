package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.AbstractSettingsModel;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SettingsResourceTest {

    private static class TestSettingsModel extends AbstractSettingsModel {
    }

    private static class TestSettingsResourceImpl extends AbstractSettingsResourceImpl<TestSettingsModel> {

        TestSettingsResourceImpl(final SettingsService<TestSettingsModel> settingsService) {
            super(settingsService);
        }
    }

    @Mock
    private SettingsService<TestSettingsModel> settingsService;

    private TestSettingsResourceImpl settingsResource;

    @BeforeEach
    void setup() {
        settingsResource = new TestSettingsResourceImpl(settingsService);
    }

    @Test
    void testGetSettings() {
        final TestSettingsModel settingsModel = new TestSettingsModel();
        doReturn(settingsModel).when(settingsService).getSettings();

        final Response response = settingsResource.getSettings();

        assertEquals(200, response.getStatus());
        assertEquals(settingsModel, response.getEntity());
    }

    @Test
    void testSetSettingsWithoutBodyIsBadRequest() {
        assertThrows(BadRequestException.class, () -> settingsResource.setSettings(null));
    }

    @Test
    void testSetSettingsReturnsModelWithStatus() {
        final TestSettingsModel result = new TestSettingsModel();
        final Map<String, _AllModelStatus> status =
                Collections.singletonMap(FieldNames.of(SettingsGeneralModel.class), _AllModelStatus.success());
        doReturn(new ServiceResult<>(result, status)).when(settingsService).setSettings(result);

        final Response response = settingsResource.setSettings(result);

        assertEquals(200, response.getStatus());
        assertEquals(status, ((TestSettingsModel) response.getEntity()).getStatus());
    }

    @Test
    void testSetSettingsReturnsHighestSubFieldStatusCode() {
        final TestSettingsModel result = new TestSettingsModel();
        final Map<String, _AllModelStatus> status = new LinkedHashMap<>();
        status.put(FieldNames.of(SettingsGeneralModel.class), _AllModelStatus.success());
        status.put(FieldNames.of(SettingsSecurityModel.class), _AllModelStatus.error(Response.Status.BAD_REQUEST, "bad", null));
        doReturn(new ServiceResult<>(result, status)).when(settingsService).setSettings(result);

        final Response response = settingsResource.setSettings(result);

        assertEquals(400, response.getStatus());
        assertEquals(status, ((TestSettingsModel) response.getEntity()).getStatus());
    }
}
