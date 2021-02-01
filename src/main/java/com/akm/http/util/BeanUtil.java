package com.akm.http.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;

/**
 * Bean utility functions using reflection.
 *
 * @author Amir
 * @since 0.3
 */
public final class BeanUtil {

  /**
   * Iterates over the given {@link AccessibleObject} array using the provided Predicate and
   * Consumer.
   *
   * @param objs     the array of AccessibleObjects
   * @param filter   the Predicate used to filter the array
   * @param consumer the Consumer to apply to each element
   */
  public static <T extends AccessibleObject> void iterator(final T[] objs,
      final Predicate<T> filter, final Consumer<T> consumer) {
    Arrays.stream(objs).parallel().filter(filter).forEach(consumer);
  }

  /**
   * Collects the given {@link AccessibleObject} array using the provided Predicate and Collector.
   *
   * @param objs      the array of AccessibleObjects
   * @param filter    the Predicate used to filter the array
   * @param collector the Collector to use for reducing the elements
   *
   * @return the result of the reduction
   */
  public static <T extends AccessibleObject, A, R> R collector(final T[] objs,
      final Predicate<T> filter, final Collector<T, A, R> collector) {
    return Arrays.stream(objs).parallel().filter(filter).collect(collector);
  }

  /**
   * Returns an array of all declared fields of the given class and its super classes.
   *
   * @param clazz the class containing the fields to find
   *
   * @return the array of fields
   */
  public static Field[] findAllFields(final Class<?> clazz) {
    Class<?> current = clazz;
    Field[] fields = current.getDeclaredFields();
    int totalLength = fields.length;
    int offset = totalLength;

    // get declared fields of each super class
    while (current.getSuperclass() != null) {
      current = current.getSuperclass();
      final Field[] superFields = current.getDeclaredFields();
      totalLength += superFields.length;
      fields = Arrays.copyOf(fields, totalLength);
      System.arraycopy(superFields, 0, fields, offset,
          superFields.length);
      offset += superFields.length;
    }

    return fields;
  }

  /**
   * Returns a {@link PropertyDescriptor} from the given class with the given field name.
   *
   * @param field the field name
   * @param clazz the class containing the field
   *
   * @return the PropertyDescriptor for this field
   *
   * @throws IntrospectionException if there are any errors during introspection
   */
  public static PropertyDescriptor findPropertyDescriptor(final String field,
      final Class<?> clazz) throws IntrospectionException {
    return new PropertyDescriptor(field, clazz);
  }

  /**
   * Returns a {@link PropertyDescriptor} from the given class with the given field name.
   * <p>
   * Note that this function should only be used when accessing read-only (final) fields or
   * write-only fields. That is, if the field has only a getter or only a setter.
   *
   * @param field    the field name
   * @param clazz    the class containing the field
   * @param readOnly whether the field is read-only or write-only
   *
   * @return the PropertyDescriptor for this field
   *
   * @throws IntrospectionException if there are any errors during introspection
   */
  public static PropertyDescriptor findPropertyDescriptor(final String field,
      final Class<?> clazz, final boolean readOnly)
      throws IntrospectionException {
    final String capitalized = StringUtil.capitalize(field);

    if (readOnly) {
      // check for a boolean field first due to different naming
      // convention
      try {
        return new PropertyDescriptor(field, clazz, "is" + capitalized,
            null);
      } catch (final IntrospectionException e) {
        return new PropertyDescriptor(field, clazz, "get" + capitalized,
            null);
      }
    } else {
      return new PropertyDescriptor(field, clazz, null,
          "set" + capitalized);
    }
  }

  /**
   * Returns the getter {@link Method} for the given field from the given class.
   *
   * @param field the field name
   * @param clazz the class containing the field
   *
   * @return the getter Method for this field
   *
   * @throws IntrospectionException if there are any errors during introspection
   */
  public static Method findGetter(final String field, final Class<?> clazz)
      throws IntrospectionException {
    return findPropertyDescriptor(field, clazz).getReadMethod();
  }

  /**
   * Returns the getter {@link Method} for the given <b>read-only</b> field from the given class.
   *
   * @param field the field name
   * @param clazz the class containing the field
   *
   * @return the getter Method for this field
   *
   * @throws IntrospectionException if there are any errors during introspection
   */
  public static Method findGetterReadOnly(final String field,
      final Class<?> clazz) throws IntrospectionException {
    return findPropertyDescriptor(field, clazz, true).getReadMethod();
  }

  /**
   * Returns the setter {@link Method} for the given field from the given class.
   *
   * @param field the field name
   * @param clazz the class containing the field
   *
   * @return the setter Method for this field
   *
   * @throws IntrospectionException if there are any errors during introspection
   */
  public static Method findSetter(final String field, final Class<?> clazz)
      throws IntrospectionException {
    return findPropertyDescriptor(field, clazz).getWriteMethod();
  }

  /**
   * Returns the setter {@link Method} for the given <b>write-only</b> field from the given class.
   *
   * @param field the field name
   * @param clazz the class containing the field
   *
   * @return the setter Method for this field
   *
   * @throws IntrospectionException if there are any errors during introspection
   */
  public static Method findSetterWriteOnly(final String field,
      final Class<?> clazz) throws IntrospectionException {
    return findPropertyDescriptor(field, clazz, false).getWriteMethod();
  }

  /**
   * Invokes the getter {@link Method} for the given field from the given class on the provided
   * object with the given argument list.
   *
   * @param field the field name
   * @param clazz the class containing the field
   * @param obj   the object to invoke the method on
   * @param args  the arguments for the method
   *
   * @return the result of invoking the Method
   *
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
   * Invokes the getter {@link Method} for the given <b>read-only</b> field from the given class on
   * the provided object with the given argument list.
   *
   * @param field the field name
   * @param clazz the class containing the field
   * @param obj   the object to invoke the method on
   * @param args  the arguments for the method
   *
   * @return the result of invoking the Method
   *
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   * @throws IntrospectionException
   */
  public static Object invokeGetterReadOnly(final String field,
      final Class<?> clazz, final Object obj, final Object... args)
      throws IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, IntrospectionException {
    return findGetterReadOnly(field, clazz).invoke(obj, args);
  }

  /**
   * Invokes the setter {@link Method} for the given field from the given class on the provided
   * object with the given argument list.
   *
   * @param field the field name
   * @param clazz the class containing the field
   * @param obj   the object to invoke the method on
   * @param args  the arguments for the method
   *
   * @return the result of invoking the Method
   *
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

  /**
   * Invokes the setter {@link Method} for the given <b>write-only</b> field from the given class on
   * the provided object with the given argument list.
   *
   * @param field the field name
   * @param clazz the class containing the field
   * @param obj   the object to invoke the method on
   * @param args  the arguments for the method
   *
   * @return the result of invoking the Method
   *
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   * @throws IntrospectionException
   */
  public static Object invokeSetterWriteOnly(final String field,
      final Class<?> clazz, final Object obj, final Object... args)
      throws IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, IntrospectionException {
    return findSetterWriteOnly(field, clazz).invoke(obj, args);
  }

  private BeanUtil() {
  }
}
