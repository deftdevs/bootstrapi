package com.deftdevs.bootstrapi.commons.util;

import com.deftdevs.bootstrapi.commons.model.type.SubEntityOf;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import org.junit.jupiter.api.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceResultUtilTest {

    @SuppressWarnings("unused")
    private static class GroupModel {
        private GeneralModel general;
    }

    @SubEntityOf(GroupModel.class)
    private static class GeneralModel {
    }

    @Test
    void setSubEntitySkipsNullInput() {
        final Map<String, _AllModelStatus> statusMap = new LinkedHashMap<>();

        ServiceResultUtil.setSubEntity(statusMap, GeneralModel.class, null,
                input -> input, result -> {});

        assertTrue(statusMap.isEmpty());
    }

    @Test
    void setSubEntityRecordsSuccessAndResultUnderTheFieldName() {
        final Map<String, _AllModelStatus> statusMap = new LinkedHashMap<>();
        final AtomicReference<GeneralModel> result = new AtomicReference<>();
        final GeneralModel updated = new GeneralModel();

        ServiceResultUtil.setSubEntity(statusMap, GeneralModel.class, new GeneralModel(),
                input -> updated, result::set);

        assertEquals(updated, result.get());
        assertEquals(200, statusMap.get("general").getStatus());
    }

    @Test
    void setSubEntityRecordsErrorStatusOnException() {
        final Map<String, _AllModelStatus> statusMap = new LinkedHashMap<>();
        final AtomicReference<GeneralModel> result = new AtomicReference<>();

        ServiceResultUtil.setSubEntity(statusMap, GeneralModel.class, new GeneralModel(),
                input -> {
                    throw new WebApplicationException("bad", Response.Status.BAD_REQUEST);
                },
                result::set);

        assertNull(result.get());
        assertEquals(400, statusMap.get("general").getStatus());
    }

    @Test
    void toErrorStatusWithStandardWaeUsesItsStatusCode() {
        final WebApplicationException e = new WebApplicationException("bad input", 400);
        final _AllModelStatus status = ServiceResultUtil.toErrorStatus("settings", e);
        assertEquals(400, status.getStatus());
        assertEquals("bad input", status.getDetails());
        assertTrue(status.getMessage().contains("settings"));
    }

    @Test
    void toErrorStatusWithNonStandardCodeDoesNotNpe() {
        final WebApplicationException e = new WebApplicationException("rate limited",
                Response.status(429).build());
        final _AllModelStatus status = ServiceResultUtil.toErrorStatus("settings", e);
        assertEquals(429, status.getStatus());
    }

    @Test
    void toErrorStatusForNonWaeIsGenericAndDropsDetails() {
        final _AllModelStatus status = ServiceResultUtil.toErrorStatus("settings",
                new RuntimeException("internal secret"));
        assertEquals(500, status.getStatus());
        assertNull(status.getDetails());
    }
}
