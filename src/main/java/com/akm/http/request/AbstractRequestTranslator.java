package com.akm.http.request;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.akm.http.exception.HttpRequestTranslationException;
import com.akm.http.util.BeanUtil;

/**
 * Base abstract class that provides default functionality for request parameter
 * translation.
 * <p>
 * All request object classes should extend this class.
 *
 * @see RequestParameter
 * @see RequestTranslator
 * @since 0.3
 * @author Amir
 */
public abstract class AbstractRequestTranslator implements RequestTranslator {
    /**
     * Predicate used to determine if a field has the RequestParameter
     * annotation for translation.
     */
    private static final Predicate<Field> IS_PARAM = (field) -> field
            .isAnnotationPresent(RequestParameter.class);

    /**
     * Key mapper Function for RequestParameter fields.
     */
    private static final Function<Field, String> PARAM_KEY_MAPPER = field -> field
            .getAnnotation(RequestParameter.class).value();

    /**
     * Value mapper Function for RequestParameter fields.
     */
    private final Function<Field, String> PARAM_VALUE_MAPPER = field -> {
        try {
            return BeanUtil.invokeGetter(field.getName(), getClass(), this)
                    .toString();
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | IntrospectionException e) {
            throw new HttpRequestTranslationException(
                    "unable to translate request parameter fields", e);
        }
    };

    @Override
    public Map<String, String> translate()
            throws HttpRequestTranslationException {
        return BeanUtil.collector(getClass().getDeclaredFields(), IS_PARAM,
                Collectors.toMap(PARAM_KEY_MAPPER, PARAM_VALUE_MAPPER));
    }
}
