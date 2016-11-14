package com.akm.http.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Provides test cases for NameValuePairList functionality
 *
 * @since 0.2
 * @author Amir
 */
public class NameValuePairListTest {
    private static Map<String, String> nvps = null;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        nvps = new HashMap<>();
    }

    @After
    public void tearDown() throws Exception {
        nvps = null;
    }

    @Test
    public final void testFromMap() {
        nvps.put("a", "5");
        nvps.put("b", "2");

        final NameValuePairList nvpl = NameValuePairList.fromMap(nvps);

        assertNotNull("name value pair list is null", nvpl);
        assertNotNull("name value pairs are null", nvpl.getNvps());
        assertFalse("name value pairs are empty", nvpl.getNvps().isEmpty());
        assertEquals("name value pair size is not 2", 2, nvpl.getNvps().size());
        assertEquals("name is not 'b'", "b", nvpl.getNvps().get(1).getName());
        assertEquals("value is not '2'", "2", nvpl.getNvps().get(1).getValue());
    }

    @Test
    public final void testFromMapNull() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("map may not be null");

        NameValuePairList.fromMap(null);
    }

    @Test
    public final void testFromMapEmpty() {
        final NameValuePairList nvpl = NameValuePairList.fromMap(nvps);

        assertNotNull("name value pair list is null", nvpl);
        assertNotNull("name value pairs are null", nvpl.getNvps());
        assertTrue("name value pairs are not empty", nvpl.getNvps().isEmpty());
        assertEquals("name value pair size is not 0", 0, nvpl.getNvps().size());
    }
}
