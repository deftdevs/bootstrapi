package com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import com.deftdevs.bootstrapi.commons.service.api._AllService;
import com.deftdevs.bootstrapi.confluence.model.SettingsModel;
import com.deftdevs.bootstrapi.confluence.model._AllModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static com.deftdevs.bootstrapi.commons.constants.BootstrAPI.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class _AllResourceTest {

    @Mock
    private _AllService<_AllModel> allService;

    private _AllResourceImpl allResource;

    private _AllModel allModel;

    @BeforeEach
    public void setup() {
        allResource = new _AllResourceImpl(allService);

        allModel = new _AllModel();
        final SettingsModel settings = new SettingsModel();
        settings.setGeneral(SettingsGeneralModel.EXAMPLE_1);
        allModel.setSettings(settings);

        final Map<String, _AllModelStatus> status = new HashMap<>();
        status.put(FieldNames.pathOf(_AllModel.class, SettingsGeneralModel.class), _AllModelStatus.success());
        status.put(DIRECTORIES, _AllModelStatus.success());
        status.put(APPLICATION_LINKS, _AllModelStatus.success());
        allModel.setStatus(status);
    }

    @Test
    public void testSetAll() {
        doReturn(allModel).when(allService).setAll(any());

        final Response response = allResource.setAll(allModel);
        assertEquals(200, response.getStatus());

        final _AllModel responseModel = (_AllModel) response.getEntity();
        assertEquals(allModel, responseModel);

        verify(allService).setAll(allModel);
    }

    @Test
    public void testSetAllReturns500OnMixedSuccessAndServerError() {
        final _AllModel result = new _AllModel();
        final Map<String, _AllModelStatus> status = new HashMap<>();
        status.put(FieldNames.pathOf(_AllModel.class, SettingsGeneralModel.class), _AllModelStatus.success());
        status.put(DIRECTORIES,
                _AllModelStatus.error(Response.Status.INTERNAL_SERVER_ERROR, "boom", null));
        result.setStatus(status);
        doReturn(result).when(allService).setAll(any());

        final Response response = allResource.setAll(allModel);
        assertEquals(500, response.getStatus());
    }

    @Test
    public void testSetAllReturns400OnMixedSuccessAndClientError() {
        final _AllModel result = new _AllModel();
        final Map<String, _AllModelStatus> status = new HashMap<>();
        status.put(FieldNames.pathOf(_AllModel.class, SettingsGeneralModel.class), _AllModelStatus.success());
        status.put(DIRECTORIES,
                _AllModelStatus.error(Response.Status.BAD_REQUEST, "bad", null));
        result.setStatus(status);
        doReturn(result).when(allService).setAll(any());

        final Response response = allResource.setAll(allModel);
        assertEquals(400, response.getStatus());
    }
}
