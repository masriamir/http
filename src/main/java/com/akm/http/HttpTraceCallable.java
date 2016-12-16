package com.akm.http;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Use this class to send Http TRACE requests.
 *
 * @see AbstractHttpCallable
 * @since 0.8
 * @author Amir
 */
final class HttpTraceCallable extends AbstractHttpCallable {
    public HttpTraceCallable(final String url,
            final Map<String, String> headers,
            final Map<String, String> parameters) {
        super(url, headers, parameters, HttpTrace.METHOD_NAME);
    }

    @Override
    public CloseableHttpResponse execute(final CloseableHttpClient client)
            throws IOException {
        final HttpTrace trace = new HttpTrace(getUrl());
        addHeaders(trace);
        addRequestParameters(trace);

        return client.execute(trace);
    }
}
