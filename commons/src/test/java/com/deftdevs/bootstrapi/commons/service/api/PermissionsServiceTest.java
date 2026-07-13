package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalModel;
import com.deftdevs.bootstrapi.commons.model.PermissionsModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class PermissionsServiceTest {

    private PermissionsService permissionsService;

    private PermissionsGlobalModel globalModel;

    @BeforeEach
    void setup() {
        permissionsService = mock(PermissionsService.class, CALLS_REAL_METHODS);

        globalModel = new PermissionsGlobalModel();
    }

    @Test
    void testSetPermissionsAppliesGlobal() {
        doReturn(globalModel).when(permissionsService).setPermissionsGlobal(globalModel);

        final ServiceResult<PermissionsModel> result = permissionsService.setPermissions(
                new PermissionsModel(globalModel));

        assertEquals(globalModel, result.getModel().getGlobal());
        assertEquals(200, result.getStatus().get(FieldNames.of(PermissionsGlobalModel.class)).getStatus());
    }

    @Test
    void testSetPermissionsSkipsNullGlobal() {
        final ServiceResult<PermissionsModel> result = permissionsService.setPermissions(
                new PermissionsModel());

        assertNull(result.getModel().getGlobal());
        assertTrue(result.getStatus().isEmpty());
    }

    @Test
    void testSetPermissionsRecordsFailure() {
        doThrow(new BadRequestException("invalid permissions")).when(permissionsService)
                .setPermissionsGlobal(globalModel);

        final ServiceResult<PermissionsModel> result = permissionsService.setPermissions(
                new PermissionsModel(globalModel));

        assertNull(result.getModel().getGlobal());
        assertEquals(400, result.getStatus().get(FieldNames.of(PermissionsGlobalModel.class)).getStatus());
    }
}
