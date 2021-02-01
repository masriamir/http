package com.akm.http;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import org.apache.http.util.TextUtils;

/**
 * Utility class for unit testing.
 *
 * @author Amir
 * @since 0.1
 */
public final class TestUtils {

  static final String URL_POST = "https://httpbin.org/post";
  static final String URL_GET = "https://httpbin.org/get";
  static final String URL_STATUS = "https://httpbin.org/status/";
  static final String URL_HEADERS = "https://httpbin.org/response-headers?";

  /**
   * Asserts that the given collection is not null and not empty.
   *
   * @param c    the collection
   * @param name variable name used in assert message
   */
  public static void notEmpty(final Collection<?> c, final String name) {
    assertAll(name,
        () -> assertNotNull(c, () -> String.format("%s is null", name)),
        () -> assertFalse(c.isEmpty(), () -> String.format("%s is empty", name)));
  }

  /**
   * Asserts that the given map is not null and not empty.
   *
   * @param m    the map
   * @param name variable name used in assert message
   */
  public static void notEmpty(final Map<?, ?> m, final String name) {
    assertAll(name,
        () -> assertNotNull(m, () -> String.format("%s is null", name)),
        () -> assertFalse(m.isEmpty(), () -> String.format("%s is empty", name)));
  }

  /**
   * Asserts that the given string is not null and not empty.
   *
   * @param s    the string
   * @param name variable name used in assert message
   */
  public static void notBlank(final String s, final String name) {
    assertFalse(TextUtils.isBlank(s), () -> String.format("%s is blank", name));
  }

  /**
   * Asserts the given response is valid.
   *
   * @param resp the HttpResponse
   */
  public static void validResponse(final HttpResponse resp) {
    assertAll("response",
        () -> {
          assertNotNull(resp, "response is null");
          assertAll("valid response",
              () -> notEmpty(resp.getHeaders(), "headers"),
              () -> notBlank(resp.getProtocol(), "protocol"),
              () -> notBlank(resp.getStatusMessage(), "status message"));
        });
  }

  /**
   * Asserts the given response represents a successful request.
   *
   * @param resp the HttpResponse
   */
  public static void successResponse(final HttpResponse resp) {
    assertAll("success response",
        () -> validResponse(resp),
        () -> notBlank(resp.getData(), "data"));
  }

  /**
   * Asserts the given response represents a successful request with an appropriate status code.
   *
   * @param resp the HttpResponse
   */
  public static void successResponseAndCode(final HttpResponse resp) {
    assertAll("success response and code",
        () -> successResponse(resp),
        () -> successStatusCode(resp.getStatusCode()));
  }

  /**
   * Asserts the given response represents a successful request with the given status code.
   *
   * @param resp       the HttpResponse
   * @param statusCode the expected status code
   */
  public static void successResponseWithCode(final HttpResponse resp,
      final int statusCode) {
    assertAll("success response with code",
        () -> successResponse(resp),
        () -> statusCode(statusCode, resp.getStatusCode()));
  }

  /**
   * Asserts the given response represents a successful request.
   * <p>
   * This method does not make any assertions about the data returned.
   *
   * @param resp the HttpResponse
   */
  public static void errorResponse(final HttpResponse resp) {
    assertAll("error response",
        () -> validResponse(resp),
        () -> assertNotNull(resp.getData(), "data is null"));
  }

  /**
   * Asserts the given response represents an unsuccessful request with an appropriate status code.
   *
   * @param resp the HttpResponse
   */
  public static void errorResponseAndCode(final HttpResponse resp) {
    assertAll("error response and code",
        () -> errorResponse(resp),
        () -> errorStatusCode(resp.getStatusCode()));
  }

  /**
   * Asserts the given response represents an unsuccessful request with the given status code.
   *
   * @param resp       the HttpResponse
   * @param statusCode the expected status code
   */
  public static void errorResponseWithCode(final HttpResponse resp,
      final int statusCode) {
    assertAll("error response with code",
        () -> errorResponse(resp),
        () -> statusCode(statusCode, resp.getStatusCode()));
  }

  /**
   * Asserts the given status codes are equal.
   *
   * @param expected the expected status code
   * @param actual   the actual status code
   */
  public static void statusCode(final int expected, final int actual) {
    assertEquals(expected, actual, () -> String.format("status code is not %d", expected));
  }

  /**
   * Asserts the given status code represents a successful request.
   *
   * @param statusCode the status code
   */
  public static void successStatusCode(final int statusCode) {
    assertTrue(statusCode < 400, "success code expected");
  }

  /**
   * Asserts the given status code represents an unsuccessful request.
   *
   * @param statusCode the status code
   */
  public static void errorStatusCode(final int statusCode) {
    assertTrue(statusCode >= 400, "error code expected");
  }

  /**
   * Asserts the given response contains the given header.
   *
   * @param resp   the HttpResponse
   * @param header the name of the header
   */
  public static void containsHeader(final HttpResponse resp,
      final String header) {
    assertTrue(resp.getHeaders().containsKey(header),
        () -> String.format("header %s missing from response", header));
  }

  /**
   * Asserts the given response contains the given header with the specified value.
   *
   * @param resp   the HttpResponse
   * @param header the name of the header
   * @param value  the value of the header
   */
  public static void containsHeaderWithValue(final HttpResponse resp,
      final String header, final String value) {
    assertAll("header with value",
        () -> containsHeader(resp, header),
        () -> assertEquals(value, resp.getHeaders().get(header),
            () -> String.format("header %s does not have value %s", header, value)));
  }

  /**
   * Expected exception for a null url.
   *
   * @param e the ExpectedException
   */
  public static void nullUrlException(final Exception e) {
    assertAll("null url exception",
        () -> assertTrue(e.getMessage().startsWith("unable to instantiate class")),
        () -> assertEquals(InvocationTargetException.class, e.getCause().getClass()));
  }

  private TestUtils() {
  }
}
