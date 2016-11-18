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
        dummy = new Dummy("5", 10, false);
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
        assertEquals("fields has invalid size", 3, fields.size());
        assertEquals("field name is invalid", "id", fields.get(0).getName());
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

    public static final class Dummy {
        private String id;
        private int count;
        private final boolean expired;

        public Dummy(final String id, final int count, final boolean expired) {
            this.id = id;
            this.count = count;
            this.expired = expired;
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
    }
}
