package com.akm.http.exception;

/**
 * Thrown when an error occurs while trying to translate a request object's fields to parameters.
 *
 * @author Amir
 * @since 0.3
 */
public class HttpRequestTranslationException extends RuntimeException {

  private static final long serialVersionUID = 5177082922774300995L;

  /**
   * Constructs a new runtime exception with <code>null</code> as its detail message.
   */
  public HttpRequestTranslationException() {
  }

  /**
   * Constructs a new runtime exception with the specified detail message.
   *
   * @param message the detail message
   */
  public HttpRequestTranslationException(final String message) {
    super(message);
  }

  /**
   * Constructs a new runtime exception with the specified cause.
   *
   * @param cause the cause
   */
  public HttpRequestTranslationException(final Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new runtime exception with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public HttpRequestTranslationException(final String message,
      final Throwable cause) {
    super(message, cause);
  }
}
