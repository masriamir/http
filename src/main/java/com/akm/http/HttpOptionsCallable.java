package com.akm.http;

import java.io.IOException;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.impl.client.CloseableHttpClient;

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
    super(url, headers, parameters, HttpOptions.METHOD_NAME);
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
