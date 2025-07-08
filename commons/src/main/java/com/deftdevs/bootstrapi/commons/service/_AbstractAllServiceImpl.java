package com.deftdevs.bootstrapi.commons.service;

import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service.api._AllService;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class _AbstractAllServiceImpl<_A> implements _AllService<_A> {

    protected <T> _AllModelStatus setEntity(
            final T entity,
            final Function<T, T> updateFunction,
            final Consumer<T> resultConsumer) {

        if (entity == null) {
            return null;
        }

        try {
            final T updatedEntity = updateFunction.apply(entity);
            resultConsumer.accept(updatedEntity);
            return _AllModelStatus.success();
        } catch (Exception e) {
            return _AllModelStatus.error(
                   Response.Status.INTERNAL_SERVER_ERROR,
                   "Failed to apply ...",
                   e.getMessage()
            );
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> _AllModelStatus setEntities(
            final Map<String, T> entityMap,
            final Function<T, String> getIdentifier,
            final Function<Map<String, T>, Map<String, ? extends T>> updateFunction,
            final Consumer<Map<String, T>> resultConsumer) {

        if (entityMap == null || entityMap.isEmpty()) {
            return null;
        }

        try {
            final Map<String, ? extends T> updatedEntities = updateFunction.apply(entityMap);
            resultConsumer.accept((Map<String, T>) updatedEntities);
            return _AllModelStatus.success();
        } catch (Exception e) {
            return _AllModelStatus.error(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    "Failed to apply ...",
                    e.getMessage()
            );
        }
    }

}
