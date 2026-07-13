package com.deftdevs.bootstrapi.commons.model.type;

import java.io.Serializable;
import java.util.function.Function;

/**
 * A {@link Function} that is also {@link Serializable}. Getter references
 * ({@code Model::getField}) assigned to this type retain their method
 * metadata at runtime, which {@link com.deftdevs.bootstrapi.commons.util.FieldNames}
 * uses to derive the referenced field's serialized name.
 */
@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
}
