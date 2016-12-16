package com.akm.http;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Use this class to send Http DELETE requests.
 *
 * @see AbstractHttpCallable
 * @since 0.8
 * @author Amir
 */
final class HttpDeleteCallable extends AbstractHttpCallable {
    public HttpDeleteCallable(final String url,
            final Map<String, String> headers,
            final Map<String, String> parameters) {
        super(url, headers, parameters, HttpDelete.METHOD_NAME);
    }

    @Override
    public CloseableHttpResponse execute(final CloseableHttpClient client)
            throws IOException {
        final HttpDelete delete = new HttpDelete(getUrl());
        addHeaders(delete);
        addRequestParameters(delete);

        return client.execute(delete);
    }
}
