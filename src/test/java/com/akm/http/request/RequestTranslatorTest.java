package com.akm.http.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.akm.http.TestUtils;

/**
 * Provides test cases for request parameter translation.
 *
 * @since 0.3
 * @author Amir
 */
public class RequestTranslatorTest {
    private TestRequestObject tro = null;

    @Before
    public void setUp() throws Exception {
        tro = new TestRequestObject("swkj-22984", 5, "help");
    }

    @After
    public void tearDown() throws Exception {
        tro = null;
    }

    @Test
    public final void testTranslate() {
        final Map<String, String> map = tro.translate();
        TestUtils.notEmpty(map, "map");
        assertEquals("map size is not 2", 2, map.size());
        assertTrue("api_user_key key is missing",
                map.containsKey("api_user_key"));
        assertTrue("limit key is missing", map.containsKey("limit"));
        assertEquals("api_user_key is invalid", "swkj-22984",
                map.get("api_user_key"));
        assertEquals("limit is invalid", "5", map.get("limit"));
    }

    public static final class TestRequestObject
            extends AbstractRequestTranslator {
        @RequestParameter("api_user_key")
        private String userId;

        @RequestParameter("limit")
        private Integer limit;

        private String unused;

        public TestRequestObject(final String userId, final Integer limit,
                final String unused) {
            this.userId = userId;
            this.limit = limit;
            this.unused = unused;
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
    }
}
