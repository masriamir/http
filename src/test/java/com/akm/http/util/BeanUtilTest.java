package com.akm.http.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import com.akm.http.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Provides test cases for the BeanUtil class.
 *
 * @since 0.3
 * @author Amir
 */
public class BeanUtilTest {
    private Dummy dummy = null;

    @BeforeEach
    public void setUp() {
        dummy = new Dummy("5", 10, false, "my message", true, 3040489483L, 23,
                18, "john");
    }

    @AfterEach
    public void tearDown() {
        dummy = null;
    }

    @Test
    public final void testIterator() {
        BeanUtil.iterator(dummy.getClass().getDeclaredFields(), f -> true,
                System.out::println);
    }

    @Test
    public final void testCollector() {
        final List<Field> fields = BeanUtil.collector(
                dummy.getClass().getDeclaredFields(), f -> true,
                Collectors.toList());
        TestUtils.notEmpty(fields, "fields");
        assertAll("collector",
            () -> assertEquals(5, fields.size(), "fields has invalid size"),
            () -> assertEquals("id", fields.get(0).getName(), "field name is invalid"));
    }

    @Test
    public final void testFindAllFields() {
        final Field[] fields = BeanUtil.findAllFields(Dummy.class);
        assertAll("find all fields",
            () -> assertNotNull(fields, "fields is null"),
            () -> assertEquals(9, fields.length, "fields length is invalid"));
    }

    @Test
    public final void testFindPropertyDescriptor()
            throws IntrospectionException, NoSuchFieldException,
            SecurityException {
        final PropertyDescriptor pd = BeanUtil.findPropertyDescriptor("id",
                Dummy.class);
        assertNotNull(pd, "property descriptor is null");

        final Field f = dummy.getClass().getDeclaredField("id");
        assertEquals(f.getName(), pd.getName(), "field name is not equal");
    }

    @Test
    public final void testFindPropertyDescriptorException() {
        assertThrows(IntrospectionException.class, () -> BeanUtil.findPropertyDescriptor("error", Dummy.class));
    }

    @Test
    public final void testFindPropertyDescriptorNoSetterException() {
        assertThrows(IntrospectionException.class, () -> BeanUtil.findPropertyDescriptor("expired", Dummy.class));
    }

    @Test
    public final void testFindPropertyDescriptorReadOnly()
            throws IntrospectionException, NoSuchFieldException,
            SecurityException {
        final PropertyDescriptor pd = BeanUtil.findPropertyDescriptor("levels",
                Dummy.class, true);
        assertNotNull(pd, "property descriptor is null");

        final Field f = dummy.getClass().getDeclaredField("levels");
        assertEquals(f.getName(), pd.getName(), "field name is not equal");
    }

    @Test
    public final void testFindPropertyDescriptorReadOnlyBoolean()
            throws IntrospectionException, NoSuchFieldException,
            SecurityException {
        final PropertyDescriptor pd = BeanUtil.findPropertyDescriptor("expired",
                Dummy.class, true);
        assertNotNull(pd, "property descriptor is null");

        final Field f = dummy.getClass().getDeclaredField("expired");
        assertEquals(f.getName(), pd.getName(), "field name is not equal");
    }

    @Test
    public final void testFindPropertyDescriptorWriteOnly()
            throws IntrospectionException, NoSuchFieldException,
            SecurityException {
        final PropertyDescriptor pd = BeanUtil.findPropertyDescriptor("name",
                Dummy.class, false);
        assertNotNull(pd, "property descriptor is null");

        final Field f = dummy.getClass().getDeclaredField("name");
        assertEquals(f.getName(), pd.getName(), "field name is not equal");
    }

    @Test
    public final void testFindGetter() throws IntrospectionException {
        final Method getter = BeanUtil.findGetter("id", Dummy.class);
        assertAll("find getter",
            () -> assertNotNull(getter, "getter is null"),
            () -> assertEquals("getId", getter.getName(), "getter name is invalid"),
            () -> assertEquals(String.class, getter.getReturnType(), "getter return type is invalid"),
            () -> assertArrayEquals(new Class[0], getter.getParameterTypes(), "getter parameter types are invalid"));
    }

    @Test
    public final void testFindGetterReadOnly() throws IntrospectionException {
        final Method getter = BeanUtil.findGetterReadOnly("expired",
                Dummy.class);
        assertAll("find getter read only",
            () -> assertNotNull(getter, "getter is null"),
            () -> assertEquals("isExpired", getter.getName(), "getter name is invalid"),
            () -> assertEquals(boolean.class, getter.getReturnType(), "getter return type is invalid"),
            () -> assertArrayEquals(new Class[0], getter.getParameterTypes(), "getter parameter types are invalid"));
    }

    @Test
    public final void testFindGetterException() {
        assertThrows(IntrospectionException.class, () -> BeanUtil.findGetter("error", Dummy.class));
    }

    @Test
    public final void testFindSetter() throws IntrospectionException {
        final Method setter = BeanUtil.findSetter("id", Dummy.class);
        assertAll("find setter",
            () -> assertNotNull(setter, "setter is null"),
            () -> assertEquals("setId", setter.getName(), "setter name is invalid"),
            () -> assertEquals(void.class, setter.getReturnType(), "setter return type is invalid"),
            () -> assertArrayEquals(new Class[] { String.class }, setter.getParameterTypes(), "setter parameter types are invalid"));
    }

    @Test
    public final void testFindSetterWriteOnly() throws IntrospectionException {
        final Method setter = BeanUtil.findSetterWriteOnly("name", Dummy.class);
        assertAll("find setter write only",
            () -> assertNotNull(setter, "setter is null"),
            () -> assertEquals("setName", setter.getName(), "setter name is invalid"),
            () -> assertEquals(void.class, setter.getReturnType(), "setter return type is invalid"),
            () -> assertArrayEquals(new Class[] { String.class }, setter.getParameterTypes(), "setter parameter types are invalid"));
    }

    @Test
    public final void testFindSetterException() {
        assertThrows(IntrospectionException.class, () -> BeanUtil.findSetter("error", Dummy.class));
    }

    @Test
    public final void testInvokeGetter()
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, IntrospectionException {
        final String id = (String) BeanUtil.invokeGetter("id", Dummy.class,
                dummy);
        TestUtils.notBlank(id, "id");
        assertAll("invoke getter",
            () -> assertEquals("5", dummy.getId(), "id value is invalid"),
            () -> assertEquals("5", id, "id value is invalid"),
            () -> assertEquals(dummy.getId(), id, "id value is invalid"));
    }

    @Test
    public final void testInvokeGetterIntrospectionException()
            throws IllegalArgumentException {
        assertThrows(IntrospectionException.class, () -> BeanUtil.invokeGetter("error", Dummy.class, dummy));
    }

    @Test
    public final void testInvokeGetterIllegalArgumentException()
            throws IllegalArgumentException {
        assertThrows(IllegalArgumentException.class, () -> BeanUtil.invokeGetter("count", Dummy.class, dummy, 10));
    }

    @Test
    public final void testInvokeSetter()
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, IntrospectionException {
        BeanUtil.invokeSetter("id", Dummy.class, dummy, "2");
        assertEquals("2", dummy.getId(), "id value is invalid");
    }

    @Test
    public final void testInvokeSetterIntrospectionException()
            throws IllegalArgumentException {
        assertThrows(IntrospectionException.class, () -> BeanUtil.invokeSetter("error", Dummy.class, dummy, "error"));
    }

    @Test
    public final void testInvokeSetterIllegalArgumentException()
            throws IllegalArgumentException {
        assertThrows(IllegalArgumentException.class, () -> BeanUtil.invokeSetter("count", Dummy.class, dummy, "error"));
    }

    public static class SuperSuperDummy {
        private int code;

        public SuperSuperDummy(final int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setCode(final int code) {
            this.code = code;
        }
    }

    public static class SuperDummy extends SuperSuperDummy {
        private String message;
        private boolean fatal;
        public long time;

        public SuperDummy(final String message, final boolean fatal,
                final long time, final int code) {
            super(code);
            this.message = message;
            this.fatal = fatal;
            this.time = time;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }

        public boolean isFatal() {
            return fatal;
        }

        public void setFatal(final boolean fatal) {
            this.fatal = fatal;
        }
    }

    public static final class Dummy extends SuperDummy {
        private String id;
        private int count;
        private final boolean expired;
        private final int levels;
        private String name;

        public Dummy(final String id, final int count, final boolean expired,
                final String message, final boolean fatal, final long time,
                final int code, final int levels, final String name) {
            super(message, fatal, time, code);
            this.id = id;
            this.count = count;
            this.expired = expired;
            this.levels = levels;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(final int count) {
            this.count = count;
        }

        public boolean isExpired() {
            return expired;
        }

        public int getLevels() {
            return levels;
        }

        public void setName(final String name) {
            this.name = name;
        }
    }
}
