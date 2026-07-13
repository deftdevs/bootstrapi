package com.deftdevs.bootstrapi.commons.model.type;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the parent model containing this model as a sub-field, so that
 * status-map keys can be derived from a sub-model class alone by walking up
 * the model hierarchy (e.g. {@code MailServerPopModel} → field {@code pop}
 * of {@code MailServerModel}). The parent must be a commons-visible model
 * declaring a field of the annotated type; product models that mirror the
 * canonical parent's fields share its keys by construction.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubEntityOf {

    /** The parent model class declaring a field of the annotated type. */
    Class<?> value();

}
