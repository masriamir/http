package com.akm.http.exception;

/**
 * Generic exception thrown when any error occurs while executing an HTTP request.
 *
 * @author Amir
 * @since 0.1
 */
public class HttpServiceException extends Exception {

  private static final long serialVersionUID = -6276063712137097923L;

  /**
   * Constructs a new exception with <code>null</code> as its detail message.
   */
  public HttpServiceException() {
  }

  /**
   * Constructs a new exception with the specified detail message.
   *
   * @param message the detail message
   */
  public HttpServiceException(final String message) {
    super(message);
  }

  /**
   * Constructs a new exception with the specified cause.
   *
   * @param cause the cause
   */
  public HttpServiceException(final Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new exception with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public HttpServiceException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
