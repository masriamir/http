package com.akm.http.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;

/**
 * Bean utility functions using reflection.
 *
 * @since 0.3
 * @author Amir
 */
public final class BeanUtil {
    /**
     * Iterates over the given {@link AccessibleObject} array using the provided
     * Predicate and Consumer.
     *
     * @param objs
     *            the array of AccessibleObjects
     * @param predicate
     *            the Predicate used to filter the array
     * @param consumer
     *            the Consumer to apply to each element
     */
    public static <T extends AccessibleObject> void iterator(final T[] objs,
            final Predicate<T> predicate, final Consumer<T> consumer) {
        Arrays.stream(objs).parallel().filter(predicate).forEach(consumer);
    }

    /**
     * Collects the given {@link AccessibleObject} array using the provided
     * Predicate and Collector.
     *
     * @param objs
     *            the array of AccessibleObjects
     * @param predicate
     *            the Predicate used to filter the array
     * @param collector
     *            the Collector to use for reducing the elements
     * @return the result of the reduction
     */
    public static <T extends AccessibleObject, A, R> R collector(final T[] objs,
            final Predicate<T> predicate, final Collector<T, A, R> collector) {
        return Arrays.stream(objs).parallel().filter(predicate)
                .collect(collector);
    }

    /**
     * Gets a {@link PropertyDescriptor} from the given class with the given
     * field name.
     *
     * @param field
     *            the field name
     * @param clazz
     *            the class containing the field
     * @return the PropertyDescriptor for this field
     * @throws IntrospectionException
     *             if there are any errors during introspection
     */
    public static PropertyDescriptor findPropertyDescriptor(final String field,
            final Class<?> clazz) throws IntrospectionException {
        return new PropertyDescriptor(field, clazz);
    }

    /**
     * Returns the getter {@link Method} for the given field from the given
     * class.
     *
     * @param field
     *            the field name
     * @param clazz
     *            the class containing the field
     * @return the getter Method for this field
     * @throws IntrospectionException
     *             if there are any errors during introspection
     */
    public static Method findGetter(final String field, final Class<?> clazz)
            throws IntrospectionException {
        return findPropertyDescriptor(field, clazz).getReadMethod();
    }

    /**
     * Returns the setter {@link Method} for the given field from the given
     * class.
     *
     * @param field
     *            the field name
     * @param clazz
     *            the class containing the field
     * @return the getter Method for this field
     * @throws IntrospectionException
     *             if there are any errors during introspection
     */
    public static Method findSetter(final String field, final Class<?> clazz)
            throws IntrospectionException {
        return findPropertyDescriptor(field, clazz).getWriteMethod();
    }

    /**
     * Invokes the getter {@link Method} for the given field from the given
     * class on the provided object with the given argument list.
     *
     * @param field
     *            the field name
     * @param clazz
     *            the class containing the field
     * @param obj
     *            the object to invoke the method on
     * @param args
     *            the arguments for the method
     * @return the result of invoking the Method
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IntrospectionException
     */
    public static Object invokeGetter(final String field, final Class<?> clazz,
            final Object obj, final Object... args)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, IntrospectionException {
        return findGetter(field, clazz).invoke(obj, args);
    }

    /**
     * Invokes the setter {@link Method} for the given field from the given
     * class on the provided object with the given argument list.
     *
     * @param field
     *            the field name
     * @param clazz
     *            the class containing the field
     * @param obj
     *            the object to invoke the method on
     * @param args
     *            the arguments for the method
     * @return the result of invoking the Method
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IntrospectionException
     */
    public static Object invokeSetter(final String field, final Class<?> clazz,
            final Object obj, final Object... args)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, IntrospectionException {
        return findSetter(field, clazz).invoke(obj, args);
    }

    private BeanUtil() {}
}