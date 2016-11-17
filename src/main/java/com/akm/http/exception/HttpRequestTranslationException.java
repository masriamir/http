package com.akm.http.exception;

/**
 * Thrown when an error occurs while trying to translate a request object's
 * fields to parameters.
 *
 * @since 0.3
 * @author Amir
 */
public class HttpRequestTranslationException extends RuntimeException {
    private static final long serialVersionUID = 5177082922774300995L;

    public HttpRequestTranslationException() {}

    public HttpRequestTranslationException(final String message) {
        super(message);
    }

    public HttpRequestTranslationException(final Throwable cause) {
        super(cause);
    }

    public HttpRequestTranslationException(final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
