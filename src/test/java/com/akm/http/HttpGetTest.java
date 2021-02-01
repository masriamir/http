package com.akm.http;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.akm.http.exception.HttpServiceException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Provides test cases for HTTP get requests.
 *
 * @author Amir
 * @since 0.1
 */
public class HttpGetTest {

  private HttpService http = null;
  private static Map<String, String> headers = null;
  private static Map<String, String> parameters = null;

  @BeforeEach
  public void setUp() {
    http = new HttpService();
    headers = new HashMap<>();
    parameters = new HashMap<>();
  }

  @AfterEach
  public void tearDown() {
    http = null;
    headers = null;
    parameters = null;
  }

  @Test
  public final void testNullUrl() {
    Exception exception = assertThrows(HttpServiceException.class, () -> get(null));
    TestUtils.nullUrlException(exception);
  }

  @Test
  public final void testGet() throws HttpServiceException {
    final HttpResponse resp = get();
    TestUtils.successResponseAndCode(resp);
  }

  @Test
  public final void testGetHeaders() throws HttpServiceException {
    final HttpResponse resp = get();
    TestUtils.successResponseAndCode(resp);
    TestUtils.containsHeaderWithValue(resp, "Content-Type",
        "application/json");
  }

  @Test
  public final void testGetResponseHeaders() throws HttpServiceException {
    final HttpResponse resp = get(
        String.format("%sTest=custom+header", TestUtils.URL_HEADERS));
    TestUtils.successResponseAndCode(resp);
    TestUtils.containsHeaderWithValue(resp, "Test", "custom header");
  }

  @Test
  public final void testGetError() throws HttpServiceException {
    final HttpResponse resp = get(String.format("%st", TestUtils.URL_GET));
    TestUtils.errorResponseAndCode(resp);
  }

  @Test
  public final void testGetStatus() throws HttpServiceException {
    getStatus(202);
    getStatus(404);
    getStatus(502);
  }

  @Test
  public final void testGetParameters() throws HttpServiceException {
    parameters.put("a", "5");
    parameters.put("b", "2");
    final HttpResponse resp = get();
    TestUtils.successResponseAndCode(resp);
  }

  /**
   * Makes a request guaranteed to generate the given status code and verifies the response.
   *
   * @param statusCode the status code to generate
   *
   * @throws HttpServiceException if any errors occur while executing the request
   */
  private void getStatus(final int statusCode) throws HttpServiceException {
    final HttpResponse resp = get(
        String.format("%s%d", TestUtils.URL_STATUS, statusCode));
    TestUtils.errorResponseWithCode(resp, statusCode);
  }

  /**
   * Performs an HTTP get request on the given url and returns the response.
   *
   * @param url the url
   *
   * @return the HttpResponse
   *
   * @throws HttpServiceException if any errors occur while executing the request
   */
  private HttpResponse get(final String url) throws HttpServiceException {
    return http.get(url, headers, parameters);
  }

  private HttpResponse get() throws HttpServiceException {
    return get(TestUtils.URL_GET);
  }
}
