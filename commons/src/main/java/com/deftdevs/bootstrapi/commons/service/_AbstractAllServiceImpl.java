package com.deftdevs.bootstrapi.commons.service;

import com.deftdevs.bootstrapi.commons.model.type.SerializableFunction;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.model.type._AllModelAccessor;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service.api._AllService;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import com.deftdevs.bootstrapi.commons.util.ServiceResultUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Base class for the product _all services. Status-map keys mirror the JSON
 * structure of the request and are derived from the model classes via
 * {@link FieldNames}: top-level fields are recorded under their field name
 * ({@code directories}, {@code permissionsGlobal}), and the sub-field
 * statuses returned by composite services are prefixed with the composite's
 * field name ({@code settings/general}, {@code mailServer/smtp}).
 */
public abstract class _AbstractAllServiceImpl<_AllModel extends _AllModelAccessor> implements _AllService<_AllModel> {

    private final Class<?> allModelClass = resolveAllModelClass();

    /**
     * Resolves the concrete _AllModel class from the subclass's declaration
     * ({@code class _AllServiceImpl extends _AbstractAllServiceImpl<_AllModel>}),
     * where the type argument is reified despite erasure.
     */
    private Class<?> resolveAllModelClass() {
        Class<?> subclass = getClass();
        while (subclass.getSuperclass() != _AbstractAllServiceImpl.class) {
            subclass = subclass.getSuperclass();
        }

        final Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            final Type typeArgument = ((ParameterizedType) superclass).getActualTypeArguments()[0];
            if (typeArgument instanceof Class) {
                return (Class<?>) typeArgument;
            }
        }
        throw new IllegalStateException(getClass().getName()
                + " must bind the _AllModel type parameter to a concrete model class");
    }

    protected <I, O> void setEntityWithStatus(
            final Class<?> fieldType,
            final I input,
            final Function<I, ServiceResult<O>> updateFunction,
            final Consumer<O> resultConsumer,
            final Map<String, _AllModelStatus> statusMap) {

        ServiceResultUtil.setEntityWithStatus(statusMap, FieldNames.of(allModelClass, fieldType),
                input, updateFunction, resultConsumer);
    }

    protected <I, O> void setEntity(
            final Class<?> fieldType,
            final I input,
            final Function<I, O> updateFunction,
            final Consumer<O> resultConsumer,
            final Map<String, _AllModelStatus> statusMap) {

        ServiceResultUtil.setEntity(statusMap, FieldNames.of(allModelClass, fieldType),
                input, updateFunction, resultConsumer);
    }

    /**
     * Variant for fields whose type is not a model class (e.g. a list of
     * strings): the status key and the input are derived from the getter
     * reference itself instead of a type-based field lookup.
     */
    protected <I, O> void setEntity(
            final SerializableFunction<_AllModel, I> getter,
            final _AllModel allModel,
            final Function<I, O> updateFunction,
            final Consumer<O> resultConsumer,
            final Map<String, _AllModelStatus> statusMap) {

        ServiceResultUtil.setEntity(statusMap, FieldNames.of(getter),
                getter.apply(allModel), updateFunction, resultConsumer);
    }

    protected <T> void setEntities(
            final Class<T> entityType,
            final Map<String, T> entityMap,
            final Function<Map<String, T>, Map<String, ? extends T>> updateFunction,
            final Consumer<Map<String, T>> resultConsumer,
            final Map<String, _AllModelStatus> statusMap) {

        if (entityMap == null || entityMap.isEmpty()) {
            return;
        }

        ServiceResultUtil.setEntity(statusMap, FieldNames.of(allModelClass, entityType),
                entityMap, entities -> new LinkedHashMap<>(updateFunction.apply(entities)), resultConsumer);
    }

}
