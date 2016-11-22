package com.akm.http.request;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to declare a field as a request parameter.
 * <p>
 * For example, with the following declaration:
 *
 * <pre>
 * &#64;RequestParameter("api_user_key")
 * private String userId;
 * </pre>
 *
 * the value of the field <code>userId</code> will be sent along in the request
 * with the name <code>api_user_key</code>.
 *
 * @since 0.3
 * @author Amir
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParameter {
    /**
     * Returns the request parameter name for the equivalent class field.
     *
     * @return the request parameter name
     */
    String value();

    /**
     * Returns whether the parameter is required to have data set.
     *
     * @return true if the parameter must be set, false otherwise
     */
    boolean required() default false;
}
