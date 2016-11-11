package com.akm.http;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akm.http.exception.HttpServiceException;

/**
 * HTTP request service.
 *
 * @since 0.1
 * @author Amir
 */
public final class HttpService {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(HttpService.class);

    /**
     * Performs an HTTP GET request to the given url using the specified headers
     * and parameters. If the request is successful an {@link HttpResponse} is
     * returned.
     * 
     * @param url
     *            the url to send the request
     * @param headers
     *            the list of headers to set
     * @param parameters
     *            the list of parameters to set
     * @return the HttpResponse
     * @throws HttpServiceException
     *             if any errors occur while executing the request
     */
    public HttpResponse get(final String url, final List<Header> headers,
            final List<NameValuePair> parameters) throws HttpServiceException {
        return doRequest(HttpGetCallable.class, url, headers, parameters);
    }

    /**
     * Performs an HTTP POST request to the given url using the specified
     * headers and parameters. If the request is successful an
     * {@link HttpResponse} is returned.
     * 
     * @param url
     *            the url to send the request
     * @param headers
     *            the list of headers for the request
     * @param parameters
     *            the list of parameters to send
     * @return the HttpResponse
     * @throws HttpServiceException
     *             if any errors occur while executing the request
     */
    public HttpResponse post(final String url, final List<Header> headers,
            final List<NameValuePair> parameters) throws HttpServiceException {
        return doRequest(HttpPostCallable.class, url, headers, parameters);
    }

    /**
     * Executes the given {@link AbstractHttpCallable} class using reflection.
     * 
     * @param clazz
     *            the class of the AbstractHttpCallable implementation
     * @param url
     *            the url to send the request
     * @param headers
     *            the list of headers for the request
     * @param parameters
     *            the list of parameters to send
     * @return the HttpResponse
     * @throws HttpServiceException
     *             if any errors occur while executing the request
     */
    private <T extends AbstractHttpCallable> HttpResponse doRequest(
            final Class<T> clazz, final String url, final List<Header> headers,
            final List<NameValuePair> parameters) throws HttpServiceException {
        final T callable = getHttpCallable(clazz, url, headers, parameters);
        return execute(callable);
    }

    /**
     * Creates and submits a new {@link AbstractHttpCallable}, returning the
     * result.
     * 
     * @param callable
     *            the AbstractHttpCallable to execute
     * @return the HttpResponse
     * @throws HttpServiceException
     *             if any errors occur while executing the request
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
     * Uses reflection to instantiate the appropriate
     * {@link AbstractHttpCallable} using the given class and constructor
     * arguments.
     * 
     * @param clazz
     *            the class of the AbstractHttpCallable implementation
     * @param url
     *            the url to send the request
     * @param headers
     *            the list of headers for the request
     * @param parameters
     *            the list of parameters to send
     * @return the HttpResponse
     * @throws HttpServiceException
     *             if any errors occur while instantiating the class
     */
    private <T extends AbstractHttpCallable> T getHttpCallable(
            final Class<T> clazz, final String url, final List<Header> headers,
            final List<NameValuePair> parameters) throws HttpServiceException {
        try {
            final Constructor<T> constructor = clazz
                    .getConstructor(String.class, List.class, List.class);
            return constructor.newInstance(url, headers, parameters);
        } catch (NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new HttpServiceException(String.format(
                    "unable to instantiate class %s with error %s",
                    clazz.getName(), e.toString()), e);
        }
    }
}
