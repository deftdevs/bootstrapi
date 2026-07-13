package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
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

    @Override
    public Response setAll(
            final _AllModel allModel) {

        if (allModel == null) {
            throw new BadRequestException("A configuration must be provided in the request body");
        }

        final _AllModel result = allService.setAll(allModel);
        final int overallStatus = computeOverallStatus(result.getStatus());
        return Response.status(overallStatus).entity(result).build();
    }

    /**
     * Aggregates per-sub-field statuses into a single HTTP response code:
     * 200 when the status map is empty or every sub-field succeeded,
     * otherwise the highest (most severe) sub-field status code.
     * <p>
     * Failing loud is preferred over WebDAV-style 207 Multi-Status because
     * 207 belongs to the 2xx success class, so scripted callers (e.g.
     * {@code curl -f}) would treat partial failure as success unless they
     * inspect the body. The apply is declarative and not transactional:
     * sub-fields that succeeded stay applied, re-submitting the same payload
     * is safe, and the per-sub-field {@code status} map in the response body
     * still distinguishes partial from total failure.
     */
    static int computeOverallStatus(
            final Map<String, _AllModelStatus> statusMap) {

        int overallStatus = Response.Status.OK.getStatusCode();
        if (statusMap != null) {
            for (final _AllModelStatus entry : statusMap.values()) {
                overallStatus = Math.max(overallStatus, entry.getStatus());
            }
        }
        return overallStatus;
    }
}
