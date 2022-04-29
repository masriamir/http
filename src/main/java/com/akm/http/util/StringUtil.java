package com.akm.http.util;

import org.apache.hc.core5.util.TextUtils;

import java.util.Locale;

/**
 * String utility functions.
 *
 * @author Amir
 * @since 0.7
 */
public final class StringUtil {

  /**
   * Returns a copy of the given string with the first letter capitalized.
   *
   * @param str the string to capitalize
   *
   * @return the capitalized string
   */
  public static String capitalize(final String str) {
    if (TextUtils.isBlank(str)) {
      throw new IllegalArgumentException(
          "cannot capitalize blank string");
    }

    return str.substring(0, 1).toUpperCase(Locale.ENGLISH) +
        str.substring(1);
  }

  private StringUtil() {
  }
}
