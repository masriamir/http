package com.akm.http.exception;

/**
 * Generic exception thrown when any error occurs while executing an HTTP request.
 *
 * @author Amir
 * @since 0.1
 */
public class HttpServiceException extends Exception {

  private static final long serialVersionUID = -6276063712137097923L;

  public HttpServiceException() {
  }

  public HttpServiceException(final String message) {
    super(message);
  }

  public HttpServiceException(final Throwable cause) {
    super(cause);
  }

  public HttpServiceException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
