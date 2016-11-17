package com.akm.http.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Bean utility functions using reflection.
 *
 * @since 0.3
 * @author Amir
 */
public final class BeanUtil {
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
