package com.akm.http;

import org.apache.hc.client5.http.classic.methods.HttpOptions;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Use this class to send Http OPTIONS requests.
 *
 * @author Amir
 * @see AbstractHttpCallable
 * @since 0.8
 */
final class HttpOptionsCallable extends AbstractHttpCallable {

  public HttpOptionsCallable(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters) {
    super(url, headers, parameters, null, HttpOptions.METHOD_NAME);
  }

  @Override
  public CloseableHttpResponse execute(final CloseableHttpClient client)
      throws IOException {
    final HttpOptions options = new HttpOptions(getUrl());
    addHeaders(options);
    addRequestParameters(options);

    return client.execute(options);
  }
}
