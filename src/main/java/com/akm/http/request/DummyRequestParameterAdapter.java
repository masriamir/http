package com.akm.http.request;

import com.akm.http.exception.HttpRequestTranslationException;

/**
 * Dummy implementation of {@link RequestParameterAdapter} that just calls the
 * <code>toString()</code> method on the Object.
 *
 * @author Amir
 * @see RequestParameterAdapter
 * @since 0.5
 */
final class DummyRequestParameterAdapter
    extends RequestParameterAdapter<Object> {

  @Override
  public String convert(final Object t)
      throws HttpRequestTranslationException {
    return t.toString();
  }
}
