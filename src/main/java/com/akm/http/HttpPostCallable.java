package com.akm.http;

import java.io.IOException;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

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
    super(url, headers, parameters, HttpPost.METHOD_NAME);
  }

  @Override
  public CloseableHttpResponse execute(final CloseableHttpClient client)
      throws IOException {
    final HttpPost post = new HttpPost(getUrl());
    addHeaders(post);
    addPostParameters(post);

    return client.execute(post);
  }
}
