package org.vizzoid.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.vizzoid.utils.Persistence.NONE;

/**
 * Transient fields are not serialized and are not included in DataMaps
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Transient {

    Persistence value() default NONE;

}
