package com.deftdevs.bootstrapi.commons.util;

import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ServiceResultUtil {

    private static final Logger log = LoggerFactory.getLogger(ServiceResultUtil.class);

    /**
     * Applies one sub-field of a composite update and records its outcome in
     * the status map: a success entry when the update function returns, or an
     * error entry (via {@link #toErrorStatus(String, Exception)}) when it
     * throws. Does nothing when {@code input} is null, i.e. when the field was
     * not part of the request.
     * <p>
     * The status key is derived from the models via
     * {@link FieldNames#of(Class)} from the sub-model's {@link com.deftdevs.bootstrapi.commons.model.type.SubEntityOf} declaration: the sub-field's name within the
     * composite's model, so that status keys line up with the JSON structure
     * of the request. Composites record their sub-fields under local keys
     * ({@code general}, {@code smtp}); the _all endpoint prefixes them with
     * the composite's own field name when merging ({@code settings/general},
     * {@code mailServer/smtp}).
     */
    public static <T> void setSubEntity(
            final Map<String, _AllModelStatus> statusMap,
            final Class<?> subEntityClass,
            final T input,
            final UnaryOperator<T> updateFunction,
            final Consumer<T> resultConsumer) {

        setEntity(statusMap, FieldNames.of(subEntityClass), input, updateFunction, resultConsumer);
    }

    /**
     * Applies one field of a composite update and records its outcome under
     * the given status key: a success entry when the update function returns,
     * or an error entry (via {@link #toErrorStatus(String, Exception)}) when
     * it throws. Does nothing when {@code input} is null, i.e. when the field
     * was not part of the request.
     */
    public static <I, O> void setEntity(
            final Map<String, _AllModelStatus> statusMap,
            final String key,
            final I input,
            final Function<I, O> updateFunction,
            final Consumer<O> resultConsumer) {

        if (input == null) {
            return;
        }

        try {
            resultConsumer.accept(updateFunction.apply(input));
            statusMap.put(key, _AllModelStatus.success());
        } catch (Exception e) {
            statusMap.put(key, toErrorStatus(key, e));
        }
    }

    /**
     * Applies one composite sub-field (a field that itself produces
     * fine-grained statuses) and merges those statuses into the given map,
     * prefixed with the sub-field's name: {@code key/subField}. The status
     * key is derived from the sub-model's {@code @SubEntityOf} declaration,
     * like in {@link #setSubEntity}. Does nothing when {@code input} is null.
     */
    public static <I, O> void setSubEntityWithStatus(
            final Map<String, _AllModelStatus> statusMap,
            final Class<?> subEntityClass,
            final I input,
            final Function<I, ServiceResult<O>> updateFunction,
            final Consumer<O> resultConsumer) {

        setEntityWithStatus(statusMap, FieldNames.of(subEntityClass), input, updateFunction, resultConsumer);
    }

    /**
     * Core of the composite variants: records each entry of the returned
     * {@link ServiceResult} under {@code key/subField}. If the update
     * function threw before producing fine-grained entries (e.g. an NPE on a
     * sub-field accessor), the failure is attributed to the composite's key
     * as a whole.
     */
    public static <I, O> void setEntityWithStatus(
            final Map<String, _AllModelStatus> statusMap,
            final String key,
            final I input,
            final Function<I, ServiceResult<O>> updateFunction,
            final Consumer<O> resultConsumer) {

        if (input == null) {
            return;
        }

        try {
            final ServiceResult<O> serviceResult = updateFunction.apply(input);
            resultConsumer.accept(serviceResult.getModel());
            serviceResult.getStatus().forEach(
                    (subField, status) -> statusMap.put(key + "/" + subField, status));
        } catch (Exception e) {
            statusMap.put(key, toErrorStatus(key, e));
        }
    }

    public static _AllModelStatus toErrorStatus(final String entityType, final Exception e) {
        final String message = String.format("Failed to apply %s configuration", entityType);
        if (e instanceof WebApplicationException) {
            final Response response = ((WebApplicationException) e).getResponse();
            final int statusCode = response != null
                    ? response.getStatus()
                    : Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            return _AllModelStatus.error(statusCode, message, e.getMessage());
        }
        log.warn("Unexpected error applying {} configuration", entityType, e);
        return _AllModelStatus.error(Response.Status.INTERNAL_SERVER_ERROR, message, null);
    }

    private ServiceResultUtil() {
    }

}
