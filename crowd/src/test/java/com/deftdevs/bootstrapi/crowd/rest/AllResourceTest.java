package com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.crowd.model.ApplicationModel;
import com.deftdevs.bootstrapi.crowd.model._AllModel;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service.api._AllService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AllResourceTest {

    @Mock
    private _AllService<_AllModel> allService;

    private _AllResourceImpl configurationResource;

    private _AllModel allModel;

    @BeforeEach
    public void setup() {
        configurationResource = new _AllResourceImpl(allService);

        // Setup test data
        allModel = new _AllModel();
        allModel.setSettings(SettingsModel.EXAMPLE_1);

        // Setup applications map
        Map<String, ApplicationModel> applications = new HashMap<>();
        applications.put(ApplicationModel.EXAMPLE_1.getName(), ApplicationModel.EXAMPLE_1);
        allModel.setApplications(applications);

        Map<String, _AllModelStatus> status = new HashMap<>();
        status.put("settings", _AllModelStatus.success());
        status.put("users", _AllModelStatus.success());
        status.put("groups", _AllModelStatus.success());
        status.put("applications", _AllModelStatus.success());
        allModel.setStatus(status);
    }

    @Test
    public void testSetConfiguration() {
        doReturn(allModel).when(allService).setAll(any());

        Response response = configurationResource.setAll(allModel);
        assertEquals(200, response.getStatus());

        _AllModel responseModel = (_AllModel) response.getEntity();
        assertEquals(allModel, responseModel);

        verify(allService).setAll(allModel);
    }
}
