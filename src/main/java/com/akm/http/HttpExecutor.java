package com.akm.http;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import java.io.IOException;

/**
 * Internal interface for executing HTTP requests.
 *
 * @author Amir
 * @see AbstractHttpCallable
 * @since 0.1
 */
interface HttpExecutor {

  /**
   * Execute an HTTP request using the given client and return the response.
   *
   * @param client the provided {@link CloseableHttpClient}
   *
   * @return the {@link CloseableHttpResponse}
   *
   * @throws IOException if there were any issues executing the request
   */
  CloseableHttpResponse execute(final CloseableHttpClient client)
      throws IOException;
}
