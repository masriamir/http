package com.akm.http;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Use this class to send Http DELETE requests.
 *
 * @author Amir
 * @see AbstractHttpCallable
 * @since 0.8
 */
final class HttpDeleteCallable extends AbstractHttpCallable {

  public HttpDeleteCallable(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters) {
    super(url, headers, parameters, null, HttpDelete.METHOD_NAME);
  }

  @Override
  public CloseableHttpResponse execute(final CloseableHttpClient client)
      throws IOException {
    final HttpDelete delete = new HttpDelete(getUrl());
    addHeaders(delete);
    addRequestParameters(delete);

    return client.execute(delete);
  }
}
