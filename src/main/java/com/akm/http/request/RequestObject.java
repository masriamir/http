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
     * This method uses each Field's {@link RequestParameterAdapter} to convert
     * its value to a String.
     *
     * @return the value mapper function
     */
    @SuppressWarnings("unchecked")
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
                    if (Modifier.isFinal(field.getModifiers())) {
                        // no setter method, will throw exception otherwise
                        obj = BeanUtil.invokeGetterReadOnly(field.getName(),
                                getClass(), this);
                    } else {
                        obj = BeanUtil.invokeGetter(field.getName(), getClass(),
                                this);
                    }
                }

                if (obj != null) {
                    // use adapter to convert parameter value
                    value = parameter.adapter().newInstance().convert(obj);
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
            } catch (IntrospectionException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException
                    | InstantiationException e) {
                throw new HttpRequestTranslationException(
                        "unable to translate request parameter fields", e);
            }
        };
    }

    /**
     * Generate a Map of request parameters and their values from all request
     * parameter fields. The Map is then post-filtered based on the Predicate
     * returned by {@link #postFilterParameters()}.
     *
     * @return a map containing all request parameters and their values
     * @throws HttpRequestTranslationException
     *             if any errors occur while translating fields
     */
    default Map<String, String> translate()
            throws HttpRequestTranslationException {
        return CollectionUtil.filterMap(
                BeanUtil.collector(BeanUtil.findAllFields(getClass()),
                        isRequestParameter(),
                        Collectors.toMap(getParameterKeyMapper(),
                                getParameterValueMapper())),
                postFilterParameters());
    }
}
