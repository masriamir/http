package com.akm.http.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        tro = new TestRequestObject("swkj-22984", 5, "help", true, null,
                "take me to your leader", 8, true);
    }

    @After
    public void tearDown() throws Exception {
        tro = null;
    }

    @Test
    public final void testTranslate() {
        tro.setErrorField("no error");
        final Map<String, String> map = tro.translate();
        TestUtils.notEmpty(map, "map");
        assertEquals("map size is invalid", 6, map.size());
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

        public TestRequestObject(final String userId, final Integer limit,
                final String unused, final Boolean deleteOnError,
                final String errorField, final String message,
                final int severity, final boolean fatal) {
            this.userId = userId;
            this.limit = limit;
            this.unused = unused;
            this.deleteOnError = deleteOnError;
            this.errorField = errorField;
            this.complex = new ComplexType(message, severity);
            this.superComplex = new SuperComplexType(message, severity, fatal);
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

    public static class ComplexType {
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
        private boolean fatal;

        public SuperComplexType(final String message, final int severity,
                final boolean fatal) {
            super(message, severity);
            this.fatal = fatal;
        }

        public boolean isFatal() {
            return fatal;
        }

        public void setFatal(final boolean fatal) {
            this.fatal = fatal;
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
}
