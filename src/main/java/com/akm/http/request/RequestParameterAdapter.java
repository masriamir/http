package com.akm.http.request;

import com.akm.http.exception.HttpRequestTranslationException;

/**
 * Adapts a type to a String for request parameter mapping.
 *
 * @param <T> the type to be converted
 *
 * @author Amir
 * @see RequestParameter
 * @since 0.5
 */
public abstract class RequestParameterAdapter<T> {

  /**
   * Constructs a new <code>RequestParameterAdapter</code>.
   */
  protected RequestParameterAdapter() {
  }

  /**
   * Convert the given type to a String.
   *
   * @param t the value to be converted
   *
   * @return the value converted to a String
   *
   * @throws HttpRequestTranslationException if there are any errors during the conversion
   */
  public abstract String convert(final T t)
      throws HttpRequestTranslationException;
}
