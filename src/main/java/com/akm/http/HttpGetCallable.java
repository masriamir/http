package com.akm.http;

import java.io.IOException;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Use this class to send Http GET requests.
 *
 * @author Amir
 * @see AbstractHttpCallable
 * @since 0.1
 */
final class HttpGetCallable extends AbstractHttpCallable {

  public HttpGetCallable(final String url, final Map<String, String> headers,
      final Map<String, String> parameters) {
    super(url, headers, parameters, null, HttpGet.METHOD_NAME);
  }

  @Override
  public CloseableHttpResponse execute(final CloseableHttpClient client)
      throws IOException {
    final HttpGet get = new HttpGet(getUrl());
    addHeaders(get);
    addRequestParameters(get);

    return client.execute(get);
  }
}
