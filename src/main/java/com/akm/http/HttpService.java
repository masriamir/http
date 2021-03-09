package com.akm.http;

import com.akm.http.exception.HttpServiceException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP request service.
 *
 * @author Amir
 * @since 0.1
 */
public final class HttpService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(HttpService.class);

  /**
   * Performs an HTTP GET request to the given url using the specified headers and parameters. If
   * the request is successful an {@link HttpResponse} is returned.
   *
   * @param url        the url to send the request
   * @param headers    the map of headers to set
   * @param parameters the map of parameters to set
   *
   * @return the HttpResponse
   *
   * @throws HttpServiceException if any errors occur while executing the request
   */
  public HttpResponse get(final String url, final Map<String, String> headers,
      final Map<String, String> parameters) throws HttpServiceException {
    return doRequest(HttpGetCallable.class, url, headers, parameters, null);
  }

  /**
   * Performs an HTTP DELETE request to the given url using the specified headers and parameters. If
   * the request is successful an {@link HttpResponse} is returned.
   *
   * @param url        the url to send the request
   * @param headers    the map of headers to set
   * @param parameters the map of parameters to set
   *
   * @return the HttpResponse
   *
   * @throws HttpServiceException if any errors occur while executing the request
   */
  public HttpResponse delete(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters) throws HttpServiceException {
    return doRequest(HttpDeleteCallable.class, url, headers, parameters, null);
  }

  /**
   * Performs an HTTP HEAD request to the given url using the specified headers and parameters. If
   * the request is successful an {@link HttpResponse} is returned.
   *
   * @param url        the url to send the request
   * @param headers    the map of headers to set
   * @param parameters the map of parameters to set
   *
   * @return the HttpResponse
   *
   * @throws HttpServiceException if any errors occur while executing the request
   */
  public HttpResponse head(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters) throws HttpServiceException {
    return doRequest(HttpHeadCallable.class, url, headers, parameters, null);
  }

  /**
   * Performs an HTTP OPTIONS request to the given url using the specified headers and parameters.
   * If the request is successful an {@link HttpResponse} is returned.
   *
   * @param url        the url to send the request
   * @param headers    the map of headers to set
   * @param parameters the map of parameters to set
   *
   * @return the HttpResponse
   *
   * @throws HttpServiceException if any errors occur while executing the request
   */
  public HttpResponse options(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters) throws HttpServiceException {
    return doRequest(HttpOptionsCallable.class, url, headers, parameters, null);
  }

  /**
   * Performs an HTTP TRACE request to the given url using the specified headers and parameters. If
   * the request is successful an {@link HttpResponse} is returned.
   *
   * @param url        the url to send the request
   * @param headers    the map of headers to set
   * @param parameters the map of parameters to set
   *
   * @return the HttpResponse
   *
   * @throws HttpServiceException if any errors occur while executing the request
   */
  public HttpResponse trace(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters) throws HttpServiceException {
    return doRequest(HttpTraceCallable.class, url, headers, parameters, null);
  }

  /**
   * Performs an HTTP POST request to the given url using the specified headers and parameters. If
   * the request is successful an {@link HttpResponse} is returned.
   *
   * @param url        the url to send the request
   * @param headers    the map of headers for the request
   * @param parameters the map of parameters to send
   *
   * @return the HttpResponse
   *
   * @throws HttpServiceException if any errors occur while executing the request
   */
  public HttpResponse post(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters, final String body) throws HttpServiceException {
    return doRequest(HttpPostCallable.class, url, headers, parameters, body);
  }

  /**
   * Performs an HTTP PUT request to the given url using the specified headers and parameters. If
   * the request is successful an {@link HttpResponse} is returned.
   *
   * @param url        the url to send the request
   * @param headers    the map of headers for the request
   * @param parameters the map of parameters to send
   *
   * @return the HttpResponse
   *
   * @throws HttpServiceException if any errors occur while executing the request
   */
  public HttpResponse put(final String url, final Map<String, String> headers,
      final Map<String, String> parameters, final String body) throws HttpServiceException {
    return doRequest(HttpPutCallable.class, url, headers, parameters, body);
  }

  /**
   * Performs an HTTP PATCH request to the given url using the specified headers and parameters. If
   * the request is successful an {@link HttpResponse} is returned.
   *
   * @param url        the url to send the request
   * @param headers    the map of headers for the request
   * @param parameters the map of parameters to send
   *
   * @return the HttpResponse
   *
   * @throws HttpServiceException if any errors occur while executing the request
   */
  public HttpResponse patch(final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters, final String body) throws HttpServiceException {
    return doRequest(HttpPatchCallable.class, url, headers, parameters, body);
  }

  /**
   * Executes the given {@link AbstractHttpCallable} class using reflection.
   *
   * @param clazz      the class of the AbstractHttpCallable implementation
   * @param url        the url to send the request
   * @param headers    the map of headers for the request
   * @param parameters the map of parameters to send
   *
   * @return the HttpResponse
   *
   * @throws HttpServiceException if any errors occur while executing the request
   */
  private <T extends AbstractHttpCallable> HttpResponse doRequest(
      final Class<T> clazz, final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters, final String body) throws HttpServiceException {
    final T callable = getHttpCallable(clazz, url, headers, parameters, body);
    return execute(callable);
  }

  /**
   * Creates and submits a new {@link AbstractHttpCallable}, returning the result.
   *
   * @param callable the AbstractHttpCallable to execute
   *
   * @return the HttpResponse
   *
   * @throws HttpServiceException if any errors occur while executing the request
   */
  private HttpResponse execute(final AbstractHttpCallable callable)
      throws HttpServiceException {
    final ExecutorService executor = Executors.newSingleThreadExecutor();
    final Future<HttpResponse> future = executor.submit(callable);
    executor.shutdown();

    HttpResponse resp = null;

    try {
      resp = future.get();
    } catch (InterruptedException | ExecutionException e) {
      final StringBuilder sb = new StringBuilder(
          "unable to execute http request");
      final Throwable t = e.getCause();

      if (t != null) {
        sb.append(" with cause ").append(t);
      }

      LOGGER.error(sb.toString(), e);
      throw new HttpServiceException(sb.toString(), e);
    }

    return resp;
  }

  /**
   * Uses reflection to instantiate the appropriate {@link AbstractHttpCallable} using the given
   * class and constructor arguments.
   *
   * @param clazz      the class of the AbstractHttpCallable implementation
   * @param url        the url to send the request
   * @param headers    the map of headers for the request
   * @param parameters the map of parameters to send
   *
   * @return the HttpResponse
   *
   * @throws HttpServiceException if any errors occur while instantiating the class
   */
  private <T extends AbstractHttpCallable> T getHttpCallable(
      final Class<T> clazz, final String url,
      final Map<String, String> headers,
      final Map<String, String> parameters, final String body) throws HttpServiceException {
    try {
      if (body == null) {
        final Constructor<T> constructor = clazz
            .getConstructor(String.class, Map.class, Map.class);
        return constructor.newInstance(url, headers, parameters);
      } else {
        final Constructor<T> constructor = clazz
            .getConstructor(String.class, Map.class, Map.class, String.class);
        return constructor.newInstance(url, headers, parameters, body);
      }
    } catch (NoSuchMethodException | SecurityException
        | InstantiationException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException e) {
      throw new HttpServiceException(String.format(
          "unable to instantiate class %s with error %s",
          clazz.getName(), e.toString()), e);
    }
  }
}
