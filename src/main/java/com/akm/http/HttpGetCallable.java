package com.akm.http;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Use this class to send Http GET requests.
 *
 * @see AbstractHttpCallable
 * @since 0.1
 * @author Amir
 */
final class HttpGetCallable extends AbstractHttpCallable {
    public HttpGetCallable(final String url, final Map<String, String> headers,
            final Map<String, String> parameters) {
        super(url, headers, parameters, HttpGet.METHOD_NAME);
    }

    @Override
    public CloseableHttpResponse execute(final CloseableHttpClient client)
            throws IOException {
        final HttpGet get = new HttpGet(getUrl());
        addHeaders(get);
        addRequestParameters(get);

        return client.execute(get);
    }
}
