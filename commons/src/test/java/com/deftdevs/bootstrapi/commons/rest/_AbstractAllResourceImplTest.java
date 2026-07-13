package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class _AbstractAllResourceImplTest {

    @Test
    void emptyMapYieldsOk() {
        assertEquals(200, _AbstractAllResourceImpl.computeOverallStatus(Collections.emptyMap()));
    }

    @Test
    void nullMapYieldsOk() {
        assertEquals(200, _AbstractAllResourceImpl.computeOverallStatus(null));
    }

    @Test
    void allSuccessYieldsOk() {
        final Map<String, _AllModelStatus> map = new LinkedHashMap<>();
        map.put("a", _AllModelStatus.success());
        map.put("b", _AllModelStatus.success());
        assertEquals(200, _AbstractAllResourceImpl.computeOverallStatus(map));
    }

    @Test
    void mixedOutcomesYieldTheHighestCode() {
        final Map<String, _AllModelStatus> map = new LinkedHashMap<>();
        map.put("a", _AllModelStatus.success());
        map.put("b", _AllModelStatus.error(Response.Status.BAD_REQUEST, "x", null));
        map.put("c", _AllModelStatus.error(Response.Status.INTERNAL_SERVER_ERROR, "x", null));
        assertEquals(500, _AbstractAllResourceImpl.computeOverallStatus(map));
    }

    @Test
    void allSameServerErrorBubblesThat() {
        final Map<String, _AllModelStatus> map = new LinkedHashMap<>();
        map.put("a", _AllModelStatus.error(Response.Status.INTERNAL_SERVER_ERROR, "x", null));
        map.put("b", _AllModelStatus.error(Response.Status.INTERNAL_SERVER_ERROR, "y", null));
        assertEquals(500, _AbstractAllResourceImpl.computeOverallStatus(map));
    }

    @Test
    void mixedSuccessAndServerErrorYields500() {
        final Map<String, _AllModelStatus> map = new LinkedHashMap<>();
        map.put("a", _AllModelStatus.success());
        map.put("b", _AllModelStatus.error(Response.Status.INTERNAL_SERVER_ERROR, "x", null));
        assertEquals(500, _AbstractAllResourceImpl.computeOverallStatus(map));
    }

    @Test
    void mixedSuccessAndClientErrorYields400() {
        final Map<String, _AllModelStatus> map = new LinkedHashMap<>();
        map.put("a", _AllModelStatus.success());
        map.put("b", _AllModelStatus.error(Response.Status.BAD_REQUEST, "x", null));
        assertEquals(400, _AbstractAllResourceImpl.computeOverallStatus(map));
    }

    @Test
    void allSameClientErrorBubblesThat() {
        final Map<String, _AllModelStatus> map = new LinkedHashMap<>();
        map.put("a", _AllModelStatus.error(Response.Status.BAD_REQUEST, "x", null));
        map.put("b", _AllModelStatus.error(Response.Status.BAD_REQUEST, "y", null));
        assertEquals(400, _AbstractAllResourceImpl.computeOverallStatus(map));
    }

    @Test
    void differentClientErrorsYieldTheHighestCode() {
        final Map<String, _AllModelStatus> map = new LinkedHashMap<>();
        map.put("a", _AllModelStatus.error(Response.Status.BAD_REQUEST, "x", null));
        map.put("b", _AllModelStatus.error(Response.Status.CONFLICT, "y", null));
        assertEquals(409, _AbstractAllResourceImpl.computeOverallStatus(map));
    }
}
