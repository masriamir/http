package com.akm.http.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Provides test cases for the StringUtil class.
 *
 * @since 0.7
 * @author Amir
 */
public class StringUtilTest {
    @Test
    public final void testCapitalize() {
        assertAll("capitalize",
            () -> assertEquals("Dog", StringUtil.capitalize("dog")),
            () -> assertEquals("Dog cat", StringUtil.capitalize("dog cat")),
            () -> assertEquals("  dog cat", StringUtil.capitalize("  dog cat")));
    }

    @Test
    public final void testCapitalizeException() {
        assertThrows(IllegalArgumentException.class, () -> StringUtil.capitalize(""));
    }
}
