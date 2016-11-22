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
        tro = new TestRequestObject("swkj-22984", 5, "help", true, null);
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
        assertEquals("map size is invalid", 4, map.size());
        assertTrue("api_user_key key is missing",
                map.containsKey("api_user_key"));
        assertTrue("limit key is missing", map.containsKey("limit"));
        assertTrue("delete_on_error key is missing",
                map.containsKey("delete_on_error"));
        assertEquals("api_user_key is invalid", "swkj-22984",
                map.get("api_user_key"));
        assertEquals("limit is invalid", "5", map.get("limit"));
        assertEquals("delete_on_error is invalid", "true",
                map.get("delete_on_error"));
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
        private Integer limit;

        private String unused;

        @RequestParameter("delete_on_error")
        public Boolean deleteOnError;

        @RequestParameter(value = "error_field", required = true)
        private String errorField;

        public TestRequestObject(final String userId, final Integer limit,
                final String unused, final Boolean deleteOnError,
                final String errorField) {
            this.userId = userId;
            this.limit = limit;
            this.unused = unused;
            this.deleteOnError = deleteOnError;
            this.errorField = errorField;
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
    }
}
