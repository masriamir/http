package com.akm.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.Args;
import org.apache.hc.core5.util.TextUtils;
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

  private final String body;

  /**
   * The HTTP method name.
   */
  private final String method;

  public AbstractHttpCallable(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters, final String body, final String method) {
    this.url = Args.notBlank(url, "url");
    this.headers = headers;
    this.parameters = parameters;
    this.body = body;
    this.method = Args.notBlank(method, "method");
  }

  @Override
  public HttpResponse call() throws Exception {
    final CloseableHttpClient client = HttpClients.createDefault();
    CloseableHttpResponse resp = null;
    HttpResponse response;
    String data = null;

    // send the request
    try {
      LOGGER.info("attempting to execute http {} request to {}", method,
          url);

      resp = execute(client);
      final StatusLine statusLine = new StatusLine(resp);

      LOGGER.info("execution complete with status {}", statusLine);

      // handle the response
      try {
        final HttpEntity entity = resp.getEntity();

        if (entity != null) {
          data = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        }

        EntityUtils.consume(entity);
        response = new HttpResponse(resp.getHeaders(), statusLine,
            data);
      } catch (final IOException e) {
        LOGGER.error("error handling http response", e);
        throw e;
      }
    } catch (final IOException e) {
      LOGGER.error("error sending http request", e);
      throw e;
    } finally {
      if (resp != null) {
        resp.close();
      }

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
   * @param request an {@link HttpUriRequestBase} object
   */
  protected void addPostParameters(final HttpUriRequestBase request) {
    if (notEmpty(parameters)) {
      request.setEntity(HttpEntities.createUrlEncoded(
          parameters.entrySet().parallelStream()
              .map(e -> new BasicNameValuePair(e.getKey(),
                  e.getValue()))
              .collect(Collectors.toList()),
          StandardCharsets.UTF_8));
    } else if (!TextUtils.isBlank(body)) {
      request.setEntity(HttpEntities.create(body, StandardCharsets.UTF_8));
    }
  }

  /**
   * Adds all parameters to the given request as url parameters.
   *
   * @param request an {@link HttpUriRequestBase} object
   */
  protected void addRequestParameters(final HttpUriRequestBase request) {
    if (notEmpty(parameters)) {
      try {
        final URIBuilder uriBuilder = new URIBuilder(url);
        parameters.forEach((k, v) -> uriBuilder.setParameter(k, v));
        request.setUri(uriBuilder.build());
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

  protected String getBody() {
    return body;
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
