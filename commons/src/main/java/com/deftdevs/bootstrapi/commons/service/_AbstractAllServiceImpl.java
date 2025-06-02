package com.deftdevs.bootstrapi.commons.service;

import com.deftdevs.bootstrapi.commons.service.api._AllService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class _AbstractAllServiceImpl<_A> implements _AllService<_A> {

    protected <T> void setEntity(
            final T entity,
            final Function<T, T> updateFunction) {


    }

    protected <T> void setEntities(
            final Map<String, T> entityMap,
            final Function<T, String> getIdentifier,
            final Function<List<T>, List<T>> updateFunction) {

        if (entityMap == null || entityMap.isEmpty()) {
            return;
        }

    }

}
