package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.type._AllModelAccessor;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.rest.api._AllResource;
import com.deftdevs.bootstrapi.commons.service.api._AllService;

import javax.ws.rs.core.Response;
import java.util.Map;

public abstract class _AbstractAllResourceImpl<_AllModel extends _AllModelAccessor>
        implements _AllResource<_AllModel> {

    private final _AllService<_AllModel> allService;

    public _AbstractAllResourceImpl(
            final _AllService<_AllModel> allService) {

        this.allService = allService;
    }

    public Response setAll(
            final _AllModel allModel) {

        final _AllModel result = allService.setAll(allModel);
        final int overallStatus = computeOverallStatus(result.getStatus());
        return Response.status(overallStatus).entity(result).build();
    }

    private static int computeOverallStatus(
            final Map<String, _AllModelStatus> statusMap) {

        if (statusMap == null || statusMap.isEmpty()) {
            return Response.Status.OK.getStatusCode();
        }

        return statusMap.values().stream()
                .mapToInt(_AllModelStatus::getStatus)
                .max()
                .orElse(Response.Status.OK.getStatusCode());
    }
}
