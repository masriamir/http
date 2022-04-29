package com.akm.http;

import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.util.Args;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents an HTTP response.
 * <p>
 * The response headers, status line related info, and the response data are all accessible through
 * the object.
 *
 * @author Amir
 * @since 0.1
 */
public final class HttpResponse {

  private final Map<String, String> headers;
  private final int statusCode;
  private final String statusMessage;
  private final String protocol;
  private final String data;

  HttpResponse(final Header[] headers, final StatusLine statusLine,
               final String data) {
    Objects.requireNonNull(headers, "headers");
    Objects.requireNonNull(statusLine, "status line");

    // set header info
    this.headers = new HashMap<>();
    for (final Header header : headers) {
      this.headers.put(header.getName(), header.getValue());
    }

    // set status line info
    this.statusCode = statusLine.getStatusCode();
    this.statusMessage = statusLine.getReasonPhrase();
    this.protocol = statusLine.getProtocolVersion().toString();

    // set data
    this.data = data;
  }

  /**
   * Returns a formatted string of the status line including the protocol. status code, and
   * message.
   *
   * @return the status line
   */
  public String getStatusLine() {
    return String.format("%s %s %s", protocol, statusCode, statusMessage);
  }

  /**
   * Returns the value of the given header.
   *
   * @param name the name of the header
   *
   * @return the value of the header
   */
  public String getHeader(final String name) {
    Args.notBlank(name, "header name");

    return headers.get(name);
  }

  /**
   * Returns the map of response headers.
   *
   * @return the map of headers
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * Returns the status code of the response.
   *
   * @return the status code
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * Returns the status message of the response.
   *
   * @return the status message
   */
  public String getStatusMessage() {
    return statusMessage;
  }

  /**
   * Returns the protocol used.
   *
   * @return the protocol
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * Returns the response data as a string.
   *
   * @return the data
   */
  public String getData() {
    return data;
  }
}
