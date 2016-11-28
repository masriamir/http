package com.akm.http.request;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.http.util.TextUtils;

import com.akm.http.exception.HttpRequestTranslationException;
import com.akm.http.util.BeanUtil;
import com.akm.http.util.CollectionUtil;

/**
 * Interface used to provide a way for objects to translate their fields to
 * request parameters. By default, all fields expected to be translated must be
 * annotated with {@link RequestParameter}.
 *
 * @see RequestParameter
 * @since 0.3
 * @author Amir
 */
public interface RequestObject {
    /**
     * Converts the given request parameter object to a String.
     * <p>
     * Note that this method is used for all {@link RequestParameter} fields in
     * a class. You can override this method if you only need to change how the
     * objects are converted to Strings.
     *
     * @param obj
     *            the request parameter object
     * @return the String representation of the object
     */
    default String convertParameter(final Object obj) {
        return obj.toString();
    }

    /**
     * Returns a Predicate to post-filter the generated parameter map after a
     * {@link #translate()} is executed.
     * <p>
     * Note that by default a Predicate is returned that will remove all
     * parameters with a null or empty value.
     *
     * @return the Predicate
     */
    default Predicate<Map.Entry<String, String>> postFilterParameters() {
        return entry -> !TextUtils.isBlank(entry.getValue());
    }

    /**
     * Returns a Predicate to determine if a Field is a request parameter.
     *
     * @return the Predicate
     */
    default Predicate<Field> isRequestParameter() {
        return field -> field.isAnnotationPresent(RequestParameter.class);
    }

    /**
     * Returns a Function to generate parameter names from a Field.
     *
     * @return the key mapper function
     */
    default Function<Field, String> getParameterKeyMapper() {
        return field -> field.getAnnotation(RequestParameter.class).value();
    }

    /**
     * Returns a Function to generate parameter values from a Field.
     * <p>
     * Note that by default the <code>toString()</code> method is used for each
     * field. You can override the {@link #convertParameter(Object)} method if
     * you just need to change how the parameter values are converted to
     * Strings.
     *
     * @return the value mapper function
     */
    default Function<Field, String> getParameterValueMapper() {
        return field -> {
            try {
                final RequestParameter parameter = field
                        .getAnnotation(RequestParameter.class);
                Object obj = null;
                String value = null;

                if (Modifier.isPublic(field.getModifiers())) {
                    obj = field.get(this);
                } else {
                    // field not public, invoke getter
                    obj = BeanUtil.invokeGetter(field.getName(), getClass(),
                            this);
                }

                if (obj != null) {
                    value = convertParameter(obj);
                }

                final boolean blankValue = TextUtils.isBlank(value);
                if (parameter.required() && blankValue) {
                    throw new HttpRequestTranslationException(String.format(
                            "request parameter %s was required, but no value was found",
                            parameter.value()));
                } else if (!blankValue) {
                    return value;
                } else {
                    return "";
                }
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | IntrospectionException e) {
                throw new HttpRequestTranslationException(
                        "unable to translate request parameter fields", e);
            }
        };
    }

    /**
     * Generate a map of request parameters and their values from all request
     * parameter fields.
     *
     * @return a map containing all request parameters and their values
     * @throws HttpRequestTranslationException
     *             if any errors occur while translating fields
     */
    default Map<String, String> translate()
            throws HttpRequestTranslationException {
        return CollectionUtil.filterMap(
                BeanUtil.collector(getClass().getDeclaredFields(),
                        isRequestParameter(),
                        Collectors.toMap(getParameterKeyMapper(),
                                getParameterValueMapper())),
                postFilterParameters());
    }
}
