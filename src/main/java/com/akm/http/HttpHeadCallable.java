package com.akm.http;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Use this class to send Http HEAD requests.
 *
 * @see AbstractHttpCallable
 * @since 0.8
 * @author Amir
 */
final class HttpHeadCallable extends AbstractHttpCallable {
    public HttpHeadCallable(final String url, final Map<String, String> headers,
            final Map<String, String> parameters) {
        super(url, headers, parameters, HttpHead.METHOD_NAME);
    }

    @Override
    public CloseableHttpResponse execute(final CloseableHttpClient client)
            throws IOException {
        final HttpHead head = new HttpHead(getUrl());
        addHeaders(head);
        addRequestParameters(head);

        return client.execute(head);
    }
}
