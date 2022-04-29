package com.akm.http.request;

import com.akm.http.exception.HttpRequestTranslationException;
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
 * &#64;RequestParameter(value = "api_user_key", required = true)
 * private String userId;
 * </pre>
 * <p>
 * the value of the field <code>userId</code> will be sent along in the request with the name
 * <code>api_user_key</code>. Additionally, if
 * <code>required</code> is set to <code>true</code>, then the field's value
 * will be validated to be not blank (not null and not whitespace only). If this validation fails,
 * an {@link HttpRequestTranslationException} will be thrown.
 *
 * @author Amir
 * @see RequestParameterAdapter
 * @since 0.3
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParameter {

  /**
   * The request parameter name.
   *
   * @return the name of the parameter
   */
  String value();

  /**
   * Whether the parameter is required to have data. By default, this is set to
   * <code>false</code>.
   *
   * @return <code>true</code> if the parameter is required, <code>false</code> otherwise
   */
  boolean required() default false;

  /**
   * The adapter class to convert the request parameter value. By default, the
   * <code>toString()</code> method of the Object is used.
   *
   * @return the <code>RequestParameterAdapter</code> class
   */
  Class<? extends RequestParameterAdapter> adapter() default DummyRequestParameterAdapter.class;
}
