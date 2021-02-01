package com.akm.http;

import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

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
