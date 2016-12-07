package com.akm.http.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.akm.http.TestUtils;
import com.akm.http.exception.HttpRequestTranslationException;

/**
 * Provides test cases for request parameter translation.
 *
 * @since 0.3
 * @author Amir
 */
public class RequestObjectTest {
    private TestRequestObject tro = null;
    private SuperComplexType sct = null;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        tro = new TestRequestObject("swkj-22984", 5, "help", true, null,
                "take me to your leader", 8, true, TestEnum.DOWN);
        sct = new SuperComplexType("sct message", 10, true);
    }

    @After
    public void tearDown() throws Exception {
        tro = null;
        sct = null;
    }

    @Test
    public final void testTranslate() {
        tro.setErrorField("no error");
        final Map<String, String> map = tro.translate();
        final long expectedMapSize = Arrays
                .stream(TestRequestObject.class.getDeclaredFields())
                .filter(f -> f.getAnnotation(RequestParameter.class) != null)
                .count();
        TestUtils.notEmpty(map, "map");
        assertEquals("map size is invalid", expectedMapSize, map.size());
        assertTrue("api_user_key key is missing",
                map.containsKey("api_user_key"));
        assertTrue("limit key is missing", map.containsKey("limit"));
        assertTrue("delete_on_error key is missing",
                map.containsKey("delete_on_error"));
        assertEquals("api_user_key is invalid", "swkj-22984",
                map.get("api_user_key"));
        assertEquals("limit is invalid", "5", map.get("limit"));
        assertEquals("delete_on_error is invalid", "this is true",
                map.get("delete_on_error"));
        assertEquals("complex is invalid", "8: take me to your leader",
                map.get("complex"));
        assertEquals("super_complex is invalid",
                "8: take me to your leader (fatal=true)",
                map.get("super_complex"));
        assertEquals("direction is invalid", "DOWN", map.get("direction"));
        assertEquals("product1 is invalid", "PRODUCT1", map.get("product1"));
        assertEquals("product2 is invalid", "PRODUCT 3 has 30 in stock",
                map.get("product2"));
    }

    @Test
    public final void testTranslateInheritance() {
        final Map<String, String> map = sct.translate();
        TestUtils.notEmpty(map, "map");
        assertEquals("map size is invalid", 2, map.size());
        assertTrue("message key is missing", map.containsKey("message"));
        assertEquals("message is invalid", "sct message", map.get("message"));
        assertTrue("fatal key is missing", map.containsKey("fatal"));
        assertEquals("fatal is invalid", "true", map.get("fatal"));
    }

    @Test
    public final void testTranslateException() {
        thrown.expect(HttpRequestTranslationException.class);
        tro.translate();
    }

    public static final class TestRequestObject implements RequestObject {
        @RequestParameter("api_user_key")
        private String userId;

        @RequestParameter("limit")
        private int limit;

        private String unused;

        @RequestParameter(value = "delete_on_error", adapter = TestRequestParameterAdapter.class)
        public Boolean deleteOnError;

        @RequestParameter(value = "error_field", required = true)
        private String errorField;

        @RequestParameter(value = "complex", required = true, adapter = ComplexTypeAdapter.class)
        private ComplexType complex;

        @RequestParameter(value = "super_complex", required = true, adapter = SuperComplexTypeAdapter.class)
        private SuperComplexType superComplex;

        @RequestParameter("direction")
        public TestEnum direction;

        @RequestParameter("product1")
        public TestComplexEnum product1;

        @RequestParameter(value = "product2", adapter = TestComplexEnumAdapter.class)
        public TestComplexEnum product2;

        public TestRequestObject(final String userId, final Integer limit,
                final String unused, final Boolean deleteOnError,
                final String errorField, final String message,
                final int severity, final boolean fatal,
                final TestEnum direction) {
            this.userId = userId;
            this.limit = limit;
            this.unused = unused;
            this.deleteOnError = deleteOnError;
            this.errorField = errorField;
            this.complex = new ComplexType(message, severity);
            this.superComplex = new SuperComplexType(message, severity, fatal);
            this.direction = direction;
            this.product1 = TestComplexEnum.PRODUCT1;
            this.product2 = TestComplexEnum.PRODUCT3;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(final String userId) {
            this.userId = userId;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(final Integer limit) {
            this.limit = limit;
        }

        public String getUnused() {
            return unused;
        }

        public void setUnused(final String unused) {
            this.unused = unused;
        }

        public String getErrorField() {
            return errorField;
        }

        public void setErrorField(final String errorField) {
            this.errorField = errorField;
        }

        public ComplexType getComplex() {
            return complex;
        }

        public void setComplex(final ComplexType complex) {
            this.complex = complex;
        }

        public SuperComplexType getSuperComplex() {
            return superComplex;
        }

        public void setSuperComplex(final SuperComplexType superComplex) {
            this.superComplex = superComplex;
        }
    }

    public static class ComplexType implements RequestObject {
        @RequestParameter("message")
        public String message;

        private int severity;

        public ComplexType(final String message, final int severity) {
            this.message = message;
            this.severity = severity;
        }

        public int getSeverity() {
            return severity;
        }

        public void setSeverity(final int severity) {
            this.severity = severity;
        }
    }

    public static final class SuperComplexType extends ComplexType {
        @RequestParameter("fatal")
        final private boolean fatal;

        public SuperComplexType(final String message, final int severity,
                final boolean fatal) {
            super(message, severity);
            this.fatal = fatal;
        }

        public boolean isFatal() {
            return fatal;
        }
    }

    public static enum TestEnum {
        UP, DOWN, LEFT, RIGHT;
    }

    public static enum TestComplexEnum {
        PRODUCT1("PRODUCT 1", 50),
        PRODUCT2("PRODUCT 2", 25),
        PRODUCT3("PRODUCT 3", 30),
        PRODUCT4("PRODUCT 4", 10);

        private final String name;
        private final int quantity;

        private TestComplexEnum(final String name, final int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public static final class TestRequestParameterAdapter
            extends RequestParameterAdapter<Boolean> {
        @Override
        public String convert(final Boolean t)
                throws HttpRequestTranslationException {
            return t ? "this is true" : "this is false";
        }
    }

    public static final class ComplexTypeAdapter
            extends RequestParameterAdapter<ComplexType> {
        @Override
        public String convert(final ComplexType t)
                throws HttpRequestTranslationException {
            return String.format("%d: %s", t.getSeverity(), t.message);
        }
    }

    public static final class SuperComplexTypeAdapter
            extends RequestParameterAdapter<SuperComplexType> {
        @Override
        public String convert(final SuperComplexType t)
                throws HttpRequestTranslationException {
            return String.format("%d: %s (fatal=%s)", t.getSeverity(),
                    t.message, t.isFatal());
        }
    }

    public static final class TestComplexEnumAdapter
            extends RequestParameterAdapter<TestComplexEnum> {
        @Override
        public String convert(final TestComplexEnum t)
                throws HttpRequestTranslationException {
            return String.format("%s has %d in stock", t.getName(),
                    t.getQuantity());
        }
    }
}
