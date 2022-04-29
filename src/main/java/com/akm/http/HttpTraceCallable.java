package com.akm.http;

import org.apache.hc.client5.http.classic.methods.HttpTrace;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Use this class to send Http TRACE requests.
 *
 * @author Amir
 * @see AbstractHttpCallable
 * @since 0.8
 */
final class HttpTraceCallable extends AbstractHttpCallable {

  public HttpTraceCallable(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters) {
    super(url, headers, parameters, null, HttpTrace.METHOD_NAME);
  }

  @Override
  public CloseableHttpResponse execute(final CloseableHttpClient client)
      throws IOException {
    final HttpTrace trace = new HttpTrace(getUrl());
    addHeaders(trace);
    addRequestParameters(trace);

    return client.execute(trace);
  }
}
