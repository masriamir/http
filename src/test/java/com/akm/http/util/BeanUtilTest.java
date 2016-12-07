package com.akm.http.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.akm.http.TestUtils;

/**
 * Provides test cases for the BeanUtil class.
 *
 * @since 0.3
 * @author Amir
 */
public class BeanUtilTest {
    private Dummy dummy = null;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        dummy = new Dummy("5", 10, false, "my message", true, 3040489483L, 23,
                18, "john");
    }

    @After
    public void tearDown() throws Exception {
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
        assertEquals("fields has invalid size", 5, fields.size());
        assertEquals("field name is invalid", "id", fields.get(0).getName());
    }

    @Test
    public final void testFindAllFields() {
        final Field[] fields = BeanUtil.findAllFields(Dummy.class);
        assertNotNull("fields is null", fields);
        assertEquals("fields length is invalid", 9, fields.length);
    }

    @Test
    public final void testFindPropertyDescriptor()
            throws IntrospectionException, NoSuchFieldException,
            SecurityException {
        final PropertyDescriptor pd = BeanUtil.findPropertyDescriptor("id",
                Dummy.class);
        assertNotNull("property descriptor is null", pd);

        final Field f = dummy.getClass().getDeclaredField("id");
        assertEquals("field name is not equal", f.getName(), pd.getName());
    }

    @Test
    public final void testFindPropertyDescriptorException()
            throws IntrospectionException {
        thrown.expect(IntrospectionException.class);
        BeanUtil.findPropertyDescriptor("error", Dummy.class);
    }

    @Test
    public final void testFindPropertyDescriptorNoSetterException()
            throws IntrospectionException {
        thrown.expect(IntrospectionException.class);
        BeanUtil.findPropertyDescriptor("expired", Dummy.class);
    }

    @Test
    public final void testFindPropertyDescriptorReadOnly()
            throws IntrospectionException, NoSuchFieldException,
            SecurityException {
        final PropertyDescriptor pd = BeanUtil.findPropertyDescriptor("levels",
                Dummy.class, true);
        assertNotNull("property descriptor is null", pd);

        final Field f = dummy.getClass().getDeclaredField("levels");
        assertEquals("field name is not equal", f.getName(), pd.getName());
    }

    @Test
    public final void testFindPropertyDescriptorReadOnlyBoolean()
            throws IntrospectionException, NoSuchFieldException,
            SecurityException {
        final PropertyDescriptor pd = BeanUtil.findPropertyDescriptor("expired",
                Dummy.class, true);
        assertNotNull("property descriptor is null", pd);

        final Field f = dummy.getClass().getDeclaredField("expired");
        assertEquals("field name is not equal", f.getName(), pd.getName());
    }

    @Test
    public final void testFindPropertyDescriptorWriteOnly()
            throws IntrospectionException, NoSuchFieldException,
            SecurityException {
        final PropertyDescriptor pd = BeanUtil.findPropertyDescriptor("name",
                Dummy.class, false);
        assertNotNull("property descriptor is null", pd);

        final Field f = dummy.getClass().getDeclaredField("name");
        assertEquals("field name is not equal", f.getName(), pd.getName());
    }

    @Test
    public final void testFindGetter() throws IntrospectionException {
        final Method getter = BeanUtil.findGetter("id", Dummy.class);
        assertNotNull("getter is null", getter);
        assertEquals("getter name is invalid", "getId", getter.getName());
        assertEquals("getter return type is invalid", String.class,
                getter.getReturnType());
        assertArrayEquals("getter parameter types are invalid", new Class[0],
                getter.getParameterTypes());
    }

    @Test
    public final void testFindGetterReadOnly() throws IntrospectionException {
        final Method getter = BeanUtil.findGetterReadOnly("expired",
                Dummy.class);
        assertNotNull("getter is null", getter);
        assertEquals("getter name is invalid", "isExpired", getter.getName());
        assertEquals("getter return type is invalid", boolean.class,
                getter.getReturnType());
        assertArrayEquals("getter parameter types are invalid", new Class[0],
                getter.getParameterTypes());
    }

    @Test
    public final void testFindGetterException() throws IntrospectionException {
        thrown.expect(IntrospectionException.class);
        BeanUtil.findGetter("error", Dummy.class);
    }

    @Test
    public final void testFindSetter() throws IntrospectionException {
        final Method setter = BeanUtil.findSetter("id", Dummy.class);
        assertNotNull("setter is null", setter);
        assertEquals("setter name is invalid", "setId", setter.getName());
        assertEquals("setter return type is invalid", void.class,
                setter.getReturnType());
        assertArrayEquals("setter parameter types are invalid",
                new Class[] { String.class }, setter.getParameterTypes());
    }

    @Test
    public final void testFindSetterWriteOnly() throws IntrospectionException {
        final Method setter = BeanUtil.findSetterWriteOnly("name", Dummy.class);
        assertNotNull("setter is null", setter);
        assertEquals("setter name is invalid", "setName", setter.getName());
        assertEquals("setter return type is invalid", void.class,
                setter.getReturnType());
        assertArrayEquals("setter parameter types are invalid",
                new Class[] { String.class }, setter.getParameterTypes());
    }

    @Test
    public final void testFindSetterException() throws IntrospectionException {
        thrown.expect(IntrospectionException.class);
        BeanUtil.findSetter("error", Dummy.class);
    }

    @Test
    public final void testInvokeGetter()
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, IntrospectionException {
        final String id = (String) BeanUtil.invokeGetter("id", Dummy.class,
                dummy);
        TestUtils.notBlank(id, "id");
        assertEquals("id value is invalid", "5", dummy.getId());
        assertEquals("id value is invalid", "5", id);
        assertEquals("id value is invalid", dummy.getId(), id);
    }

    @Test
    public final void testInvokeGetterIntrospectionException()
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, IntrospectionException {
        thrown.expect(IntrospectionException.class);
        BeanUtil.invokeGetter("error", Dummy.class, dummy);
    }

    @Test
    public final void testInvokeGetterIllegalArgumentException()
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, IntrospectionException {
        thrown.expect(IllegalArgumentException.class);
        BeanUtil.invokeGetter("count", Dummy.class, dummy, 10);
    }

    @Test
    public final void testInvokeSetter()
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, IntrospectionException {
        BeanUtil.invokeSetter("id", Dummy.class, dummy, "2");
        assertEquals("id value is invalid", "2", dummy.getId());
    }

    @Test
    public final void testInvokeSetterIntrospectionException()
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, IntrospectionException {
        thrown.expect(IntrospectionException.class);
        BeanUtil.invokeSetter("error", Dummy.class, dummy, "error");
    }

    @Test
    public final void testInvokeSetterIllegalArgumentException()
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, IntrospectionException {
        thrown.expect(IllegalArgumentException.class);
        BeanUtil.invokeSetter("count", Dummy.class, dummy, "error");
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
