package com.akm.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akm.http.builder.NameValuePairList;

/**
 * Internal abstract implementation of {@link HttpExecutor} that also implements
 * {@link Callable}.
 * <p>
 * Extended classes must implement
 * {@link HttpExecutor#execute(CloseableHttpClient)}.
 * <p>
 * Headers and parameters are set using a {@link NameValuePairList} that can be
 * built using the following:
 *
 * <pre>
 * NameValuePairList.getInstance().add("name", "value").build();
 * </pre>
 *
 * @see HttpExecutor
 * @see Callable
 * @see NameValuePairList
 * @since 0.1
 * @author Amir
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
     * The list of headers to use when making the request.
     */
    private final NameValuePairList headers;

    /**
     * The list of parameters for the request.
     */
    private final NameValuePairList parameters;

    /**
     * The HTTP method name.
     */
    private final String method;

    public AbstractHttpCallable(final String url,
            final NameValuePairList headers, final NameValuePairList parameters,
            final String method) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("url may not be empty");
        }

        if (method == null || method.isEmpty()) {
            throw new IllegalArgumentException("method may not be empty");
        }

        this.url = url;
        this.headers = headers;
        this.parameters = parameters;
        this.method = method;
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
     * @param request
     *            an {@link HttpRequest} object
     */
    protected void addHeaders(final HttpRequest request) {
        if (validList(headers.getNvps())) {
            for (final NameValuePair nvp : headers.getNvps()) {
                LOGGER.debug("setting the following header: {}={}",
                        nvp.getName(), nvp.getValue());
                request.setHeader(nvp.getName(), nvp.getValue());
            }
        }
    }

    /**
     * Adds all parameters to the given entity enclosing request.
     *
     * @param request
     *            an {@link HttpEntityEnclosingRequest} object
     */
    protected void addPostParameters(final HttpEntityEnclosingRequest request) {
        if (validList(parameters.getNvps())) {
            LOGGER.debug("setting the following entity parameters: {}",
                    parameters.getNvps());
            request.setEntity(new UrlEncodedFormEntity(parameters.getNvps(),
                    StandardCharsets.UTF_8));
        }
    }

    /**
     * Adds all parameters to the given request as url parameters.
     *
     * @param request
     *            an {@link HttpRequestBase} object
     */
    protected void addRequestParameters(final HttpRequestBase request) {
        if (validList(parameters.getNvps())) {
            try {
                final URIBuilder uriBuilder = new URIBuilder(url);

                for (final NameValuePair nvp : parameters.getNvps()) {
                    LOGGER.debug("setting the following url parameter: {}={}",
                            nvp.getName(), nvp.getValue());
                    uriBuilder.setParameter(nvp.getName(), nvp.getValue());
                }

                request.setURI(uriBuilder.build());
            } catch (final URISyntaxException e) {
                LOGGER.error("unable to build uri", e);
            }
        }
    }

    protected String getUrl() {
        return url;
    }

    protected NameValuePairList getHeaders() {
        return headers;
    }

    protected NameValuePairList getParameters() {
        return parameters;
    }

    protected String getMethod() {
        return method;
    }

    /**
     * Checks if the given list is not null and not empty.
     *
     * @param list
     *            the list to check
     * @return true if list is not null and not empty, false otherwise
     */
    private <T> boolean validList(final List<T> list) {
        return list != null && !list.isEmpty();
    }
}
