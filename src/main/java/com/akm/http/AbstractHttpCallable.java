package com.akm.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Internal abstract implementation of {@link HttpExecutor} that also implements {@link Callable}.
 * <p>
 * Extended classes must implement {@link HttpExecutor#execute(CloseableHttpClient)}.
 *
 * @author Amir
 * @see HttpExecutor
 * @see Callable
 * @since 0.1
 */
abstract class AbstractHttpCallable
    implements HttpExecutor, Callable<HttpResponse> {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(AbstractHttpCallable.class);

  /**
   * The base request url.
   */
  private final String url;

  /**
   * The map of headers to use when making the request.
   */
  private final Map<String, String> headers;

  /**
   * The map of parameters for the request.
   */
  private final Map<String, String> parameters;

  /**
   * The HTTP method name.
   */
  private final String method;

  public AbstractHttpCallable(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters, final String method) {
    this.url = Args.notBlank(url, "url");
    this.headers = headers;
    this.parameters = parameters;
    this.method = Args.notBlank(method, "method");
  }

  @Override
  public HttpResponse call() throws Exception {
    final CloseableHttpClient client = HttpClients.createDefault();
    CloseableHttpResponse resp = null;
    HttpResponse response = null;
    String data = null;

    // send the request
    try {
      LOGGER.info("attempting to execute http {} request to {}", method,
          url);

      resp = execute(client);
      final StatusLine statusLine = resp.getStatusLine();

      LOGGER.info("execution complete with status {}", statusLine);

      // handle the response
      try {
        final HttpEntity entity = resp.getEntity();

        if (entity != null) {
          data = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        }

        EntityUtils.consume(entity);
        response = new HttpResponse(resp.getAllHeaders(), statusLine,
            data);
      } catch (final IOException e) {
        LOGGER.error("error handling http response", e);
        throw e;
      }
    } catch (final IOException e) {
      LOGGER.error("error sending http request", e);
      throw e;
    } finally {
      resp.close();
      client.close();
    }

    return response;
  }

  /**
   * Adds all headers to the given request.
   *
   * @param request an {@link HttpRequest} object
   */
  protected void addHeaders(final HttpRequest request) {
    if (notEmpty(headers)) {
      headers.forEach((k, v) -> request.setHeader(k, v));
    }
  }

  /**
   * Adds all parameters to the given entity enclosing request.
   *
   * @param request an {@link HttpEntityEnclosingRequest} object
   */
  protected void addPostParameters(final HttpEntityEnclosingRequest request) {
    if (notEmpty(parameters)) {
      request.setEntity(new UrlEncodedFormEntity(
          parameters.entrySet().parallelStream()
              .map(e -> new BasicNameValuePair(e.getKey(),
                  e.getValue()))
              .collect(Collectors.toList()),
          StandardCharsets.UTF_8));
    }
  }

  /**
   * Adds all parameters to the given request as url parameters.
   *
   * @param request an {@link HttpRequestBase} object
   */
  protected void addRequestParameters(final HttpRequestBase request) {
    if (notEmpty(parameters)) {
      try {
        final URIBuilder uriBuilder = new URIBuilder(url);
        parameters.forEach((k, v) -> uriBuilder.setParameter(k, v));
        request.setURI(uriBuilder.build());
      } catch (final URISyntaxException e) {
        LOGGER.error("unable to build uri", e);
      }
    }
  }

  protected String getUrl() {
    return url;
  }

  protected Map<String, String> getHeaders() {
    return headers;
  }

  protected Map<String, String> getParameters() {
    return parameters;
  }

  protected String getMethod() {
    return method;
  }

  /**
   * Checks if the given map is not null and not empty.
   *
   * @param map the map to check
   *
   * @return true if the map is not null and not empty, false otherwise
   */
  private <T extends Map<K, V>, K, V> boolean notEmpty(final T map) {
    return map != null && !map.isEmpty();
  }
}
