package com.deftdevs.bootstrapi.commons.service;

import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.model.type._AllModelAccessor;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import org.junit.jupiter.api.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class _AbstractAllServiceImplTest {

    private static class TestGroupModel {
    }

    private static class TestEntityModel {
    }

    private static class TestEntryModel {
    }

    @SuppressWarnings("unused")
    private static class TestAllModel implements _AllModelAccessor {

        private TestGroupModel group;
        private TestEntityModel entity;
        private Map<String, TestEntryModel> entries;
        private Map<String, _AllModelStatus> status;

        @Override
        public Map<String, _AllModelStatus> getStatus() {
            return status;
        }
    }

    private static class TestAllService extends _AbstractAllServiceImpl<TestAllModel> {

        @Override
        public TestAllModel setAll(final TestAllModel allModel) {
            return allModel;
        }
    }

    private final TestAllService service = new TestAllService();

    // setEntity

    @Test
    void setEntitySkipsNullInput() {
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();

        service.setEntity(TestEntityModel.class, null,
                input -> input, result -> {}, statusMap);

        assertTrue(statusMap.isEmpty());
    }

    @Test
    void setEntitySuccessRecordsStatusAndResultUnderTheFieldName() {
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();
        final AtomicReference<TestEntityModel> result = new AtomicReference<>();
        final TestEntityModel updated = new TestEntityModel();

        service.setEntity(TestEntityModel.class, new TestEntityModel(),
                input -> updated, result::set, statusMap);

        assertEquals(200, statusMap.get("entity").getStatus());
        assertEquals(updated, result.get());
    }

    @Test
    void setEntityUsesWebApplicationExceptionStatusCode() {
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();

        service.setEntity(TestEntityModel.class, new TestEntityModel(),
                input -> {
                    throw new WebApplicationException("conflict", Response.Status.CONFLICT);
                },
                result -> {}, statusMap);

        assertEquals(409, statusMap.get("entity").getStatus());
        assertEquals("Failed to apply entity configuration", statusMap.get("entity").getMessage());
    }

    @Test
    void setEntityMapsUnexpectedExceptionTo500WithoutDetails() {
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();

        service.setEntity(TestEntityModel.class, new TestEntityModel(),
                input -> {
                    throw new IllegalStateException("internal details that must not leak");
                },
                result -> {}, statusMap);

        assertEquals(500, statusMap.get("entity").getStatus());
        assertNull(statusMap.get("entity").getDetails());
    }

    // setEntities

    @Test
    void setEntitiesSkipsNullAndEmptyMap() {
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();

        service.setEntities(TestEntryModel.class, null,
                entities -> entities, result -> {}, statusMap);
        service.setEntities(TestEntryModel.class, Collections.emptyMap(),
                entities -> entities, result -> {}, statusMap);
        assertTrue(statusMap.isEmpty());
    }

    @Test
    void setEntitiesSuccessRecordsStatusAndResultUnderTheFieldName() {
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();
        final AtomicReference<Map<String, TestEntryModel>> result = new AtomicReference<>();
        final Map<String, TestEntryModel> entities = Collections.singletonMap("key", new TestEntryModel());

        service.setEntities(TestEntryModel.class, entities,
                input -> input, result::set, statusMap);

        assertEquals(200, statusMap.get("entries").getStatus());
        assertEquals(entities, result.get());
    }

    @Test
    void setEntitiesRecordsErrorStatusOnException() {
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();
        final Map<String, TestEntryModel> entities = Collections.singletonMap("key", new TestEntryModel());

        service.setEntities(TestEntryModel.class, entities,
                input -> {
                    throw new WebApplicationException(Response.Status.BAD_REQUEST);
                },
                result -> {}, statusMap);

        assertEquals(400, statusMap.get("entries").getStatus());
    }

    // setEntityWithStatus

    @Test
    void setEntityWithStatusSkipsNullInput() {
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();

        service.setEntityWithStatus(TestGroupModel.class, null,
                input -> new ServiceResult<>(input, Collections.emptyMap()),
                result -> {}, statusMap);

        assertTrue(statusMap.isEmpty());
    }

    @Test
    void setEntityWithStatusPrefixesSubFieldStatusesWithTheFieldName() {
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();
        final AtomicReference<TestGroupModel> result = new AtomicReference<>();
        final TestGroupModel updated = new TestGroupModel();

        final Map<String, _AllModelStatus> subStatus = new LinkedHashMap<>();
        subStatus.put("first", _AllModelStatus.success());
        subStatus.put("second", _AllModelStatus.error(Response.Status.BAD_REQUEST, "bad", null));

        service.setEntityWithStatus(TestGroupModel.class, new TestGroupModel(),
                input -> new ServiceResult<>(updated, subStatus),
                result::set, statusMap);

        assertEquals(updated, result.get());
        assertEquals(2, statusMap.size());
        assertEquals(200, statusMap.get("group/first").getStatus());
        assertEquals(400, statusMap.get("group/second").getStatus());
    }

    @Test
    void setEntityWithStatusFallsBackToTheFieldNameOnThrow() {
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();

        service.setEntityWithStatus(TestGroupModel.class, new TestGroupModel(),
                input -> {
                    throw new IllegalStateException("boom");
                },
                result -> {}, statusMap);

        assertEquals(500, statusMap.get("group").getStatus());
    }

    @Test
    void setEntityWithStatusUsesWebApplicationExceptionStatusCodeOnThrow() {
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();

        service.setEntityWithStatus(TestGroupModel.class, new TestGroupModel(),
                input -> {
                    throw new WebApplicationException(Response.Status.CONFLICT);
                },
                result -> {}, statusMap);

        assertEquals(409, statusMap.get("group").getStatus());
    }
}
