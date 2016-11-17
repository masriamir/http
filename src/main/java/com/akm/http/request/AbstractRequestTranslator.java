package com.akm.http.request;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

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
    private static final Predicate<Field> IS_ANNOTATED = (field) -> field
            .isAnnotationPresent(RequestParameter.class);

    @Override
    public Map<String, String> translate()
            throws HttpRequestTranslationException {
        final Map<String, String> map = new HashMap<>();
        final List<Field> fields = Arrays
                .asList(getClass().getDeclaredFields());

        fields.parallelStream().filter(IS_ANNOTATED).forEach(field -> {
            try {
                final String name = field.getAnnotation(RequestParameter.class)
                        .value();
                final String value = BeanUtil
                        .invokeGetter(field.getName(), getClass(), this)
                        .toString();
                map.put(name, value);
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | IntrospectionException e) {
                throw new HttpRequestTranslationException(
                        "unable to translate request parameter fields", e);
            }
        });

        return map;
    }
}
