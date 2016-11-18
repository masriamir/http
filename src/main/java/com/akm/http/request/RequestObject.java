package com.akm.http.request;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.akm.http.exception.HttpRequestTranslationException;
import com.akm.http.util.BeanUtil;

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
     *
     * @return the value mapper function
     */
    default Function<Field, String> getParameterValueMapper() {
        return field -> {
            try {
                if (Modifier.isPublic(field.getModifiers())) {
                    return field.get(this).toString();
                } else {
                    // field not public, invoke getter
                    return BeanUtil
                            .invokeGetter(field.getName(), getClass(), this)
                            .toString();
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
        return BeanUtil.collector(getClass().getDeclaredFields(),
                isRequestParameter(), Collectors.toMap(getParameterKeyMapper(),
                        getParameterValueMapper()));
    }
}
