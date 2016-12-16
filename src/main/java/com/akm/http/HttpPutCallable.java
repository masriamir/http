package com.akm.http;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Use this class to send Http PUT requests.
 *
 * @see AbstractHttpCallable
 * @since 0.8
 * @author Amir
 */
final class HttpPutCallable extends AbstractHttpCallable {
    public HttpPutCallable(final String url, final Map<String, String> headers,
            final Map<String, String> parameters) {
        super(url, headers, parameters, HttpPut.METHOD_NAME);
    }

    @Override
    public CloseableHttpResponse execute(final CloseableHttpClient client)
            throws IOException {
        final HttpPut put = new HttpPut(getUrl());
        addHeaders(put);
        addPostParameters(put);

        return client.execute(put);
    }
}
