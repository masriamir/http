package com.akm.http;

import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Use this class to send Http HEAD requests.
 *
 * @author Amir
 * @see AbstractHttpCallable
 * @since 0.8
 */
final class HttpHeadCallable extends AbstractHttpCallable {

  public HttpHeadCallable(final String url, final Map<String, String> headers,
      final Map<String, String> parameters) {
    super(url, headers, parameters, null, HttpHead.METHOD_NAME);
  }

  @Override
  public CloseableHttpResponse execute(final CloseableHttpClient client)
      throws IOException {
    final HttpHead head = new HttpHead(getUrl());
    addHeaders(head);
    addRequestParameters(head);

    return client.execute(head);
  }
}
