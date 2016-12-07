package com.akm.http.util;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Provides test cases for the StringUtil class.
 *
 * @since 0.7
 * @author Amir
 */
public class StringUtilTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public final void testCapitalize() {
        assertEquals("Dog", StringUtil.capitalize("dog"));
        assertEquals("Dog cat", StringUtil.capitalize("dog cat"));
        assertEquals("  dog cat", StringUtil.capitalize("  dog cat"));
    }

    @Test
    public final void testCapitalizeException() {
        thrown.expect(IllegalArgumentException.class);
        StringUtil.capitalize("");
    }
}
