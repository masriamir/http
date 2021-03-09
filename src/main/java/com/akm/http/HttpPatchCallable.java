package com.akm.http;

import java.io.IOException;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Use this class to send Http PATCH requests.
 *
 * @author Amir
 * @see AbstractHttpCallable
 * @since 0.8
 */
final class HttpPatchCallable extends AbstractHttpCallable {

  public HttpPatchCallable(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters) {
    this(url, headers, parameters, null);
  }

  public HttpPatchCallable(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters, final String body) {
    super(url, headers, parameters, body, HttpPatch.METHOD_NAME);
  }

  @Override
  public CloseableHttpResponse execute(final CloseableHttpClient client)
      throws IOException {
    final HttpPatch patch = new HttpPatch(getUrl());
    addHeaders(patch);
    addPostParameters(patch);

    return client.execute(patch);
  }
}
