package com.akm.http;

import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Use this class to send Http PUT requests.
 *
 * @author Amir
 * @see AbstractHttpCallable
 * @since 0.8
 */
final class HttpPutCallable extends AbstractHttpCallable {

  public HttpPutCallable(final String url, final Map<String, String> headers,
      final Map<String, String> parameters) {
    this(url, headers, parameters, null);
  }

  public HttpPutCallable(final String url, final Map<String, String> headers,
      final Map<String, String> parameters, final String body) {
    super(url, headers, parameters, body, HttpPut.METHOD_NAME);
  }

  @Override
  public CloseableHttpResponse execute(final CloseableHttpClient client)
      throws IOException {
    final HttpPut put = new HttpPut(getUrl());
    addHeaders(put);
    addPostParameters(put);

    return client.execute(put);
  }
}
