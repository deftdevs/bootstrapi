package com.deftdevs.bootstrapi.commons.service;

import com.deftdevs.bootstrapi.commons.model.type._AllModelAccessor;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service.api._AllService;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class _AbstractAllServiceImpl<_AllModel extends _AllModelAccessor> implements _AllService<_AllModel> {

    protected <T> _AllModelStatus setEntity(
            final String entityType,
            final T entity,
            final Function<T, T> updateFunction,
            final Consumer<T> resultConsumer,
            final Map<String, _AllModelStatus> statusMap) {

        if (entity == null) {
            return null;
        }

        try {
            final T updatedEntity = updateFunction.apply(entity);
            resultConsumer.accept(updatedEntity);
            final _AllModelStatus status = _AllModelStatus.success();
            statusMap.put(entityType, status);
            return status;
        } catch (Exception e) {
            final _AllModelStatus status = _AllModelStatus.error(
                    resolveStatus(e),
                    String.format("Failed to apply %s configuration", entityType),
                    e.getMessage()
            );
            statusMap.put(entityType, status);
            return status;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> _AllModelStatus setEntities(
            final String entityType,
            final Map<String, T> entityMap,
            final Function<Map<String, T>, Map<String, ? extends T>> updateFunction,
            final Consumer<Map<String, T>> resultConsumer,
            final Map<String, _AllModelStatus> statusMap) {

        if (entityMap == null || entityMap.isEmpty()) {
            return null;
        }

        try {
            final Map<String, ? extends T> updatedEntities = updateFunction.apply(entityMap);
            resultConsumer.accept((Map<String, T>) updatedEntities);
            final _AllModelStatus status = _AllModelStatus.success();
            statusMap.put(entityType, status);
            return status;
        } catch (Exception e) {
            final _AllModelStatus status = _AllModelStatus.error(
                    resolveStatus(e),
                    String.format("Failed to apply %s configuration", entityType),
                    e.getMessage()
            );
            statusMap.put(entityType, status);
            return status;
        }
    }

    private static Response.Status resolveStatus(final Exception e) {
        if (e instanceof com.deftdevs.bootstrapi.commons.exception.web.BadRequestException) {
            return Response.Status.BAD_REQUEST;
        }
        if (e instanceof com.deftdevs.bootstrapi.commons.exception.web.NotFoundException) {
            return Response.Status.NOT_FOUND;
        }
        return Response.Status.INTERNAL_SERVER_ERROR;
    }

}
