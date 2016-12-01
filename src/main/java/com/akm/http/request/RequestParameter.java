package com.akm.http.request;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.akm.http.exception.HttpRequestTranslationException;

/**
 * Annotation used to declare a field as a request parameter.
 * <p>
 * For example, with the following declaration:
 *
 * <pre>
 * &#64;RequestParameter(value = "api_user_key", required = true)
 * private String userId;
 * </pre>
 *
 * the value of the field <code>userId</code> will be sent along in the request
 * with the name <code>api_user_key</code>. Additionally, if
 * <code>required</code> is set to <code>true</code>, then the field's value
 * will be validated to be not blank (not null and not whitespace only). If this
 * validation fails, an {@link HttpRequestTranslationException} will be thrown.
 *
 * @see RequestParameterAdapter
 * @since 0.3
 * @author Amir
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParameter {
    /**
     * The request parameter name.
     */
    String value();

    /**
     * Whether the parameter is required to have data. By default this is set to
     * <code>false</code>.
     */
    boolean required() default false;

    /**
     * The adapter class to convert the request parameter value. By default the
     * <code>toString()</code> method of the Object is used.
     */
    @SuppressWarnings("rawtypes")
    Class<? extends RequestParameterAdapter> adapter() default DummyRequestParameterAdapter.class;
}
