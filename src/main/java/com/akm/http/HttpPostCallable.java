package com.akm.http;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Use this class to send Http POST requests.
 *
 * @author Amir
 * @see AbstractHttpCallable
 * @since 0.1
 */
final class HttpPostCallable extends AbstractHttpCallable {

  public HttpPostCallable(final String url, final Map<String, String> headers,
      final Map<String, String> parameters) {
    this(url, headers, parameters, null);
  }

  public HttpPostCallable(final String url, final Map<String, String> headers,
      final Map<String, String> parameters, final String body) {
    super(url, headers, parameters, body, HttpPost.METHOD_NAME);
  }

  @Override
  public CloseableHttpResponse execute(final CloseableHttpClient client)
      throws IOException {
    final HttpPost post = new HttpPost(getUrl());
    addHeaders(post);
    addRequestParameters(post);
    addPostParameters(post);

    return client.execute(post);
  }
}
