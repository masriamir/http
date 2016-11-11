package com.akm.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.akm.http.builder.NameValuePairList;
import com.akm.http.exception.HttpServiceException;

/**
 * Provides test cases for HTTP get requests.
 *
 * @since 0.1
 * @author Amir
 */
public class HttpGetTest {
    private HttpService http = null;
    private static NameValuePairList headers = null;
    private static NameValuePairList parameters = null;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        http = new HttpService();
        headers = NameValuePairList.getInstance().build();
        parameters = NameValuePairList.getInstance().build();
    }

    @After
    public void tearDown() throws Exception {
        http = null;
        headers = null;
        parameters = null;
    }

    @Test
    public final void testNullUrl() throws HttpServiceException {
        TestUtils.nullUrlException(thrown);
        get(null);
    }

    @Test
    public final void testGet() throws HttpServiceException {
        final HttpResponse resp = get();
        TestUtils.successResponseAndCode(resp);
    }

    @Test
    public final void testGetHeaders() throws HttpServiceException {
        final HttpResponse resp = get();
        TestUtils.successResponseAndCode(resp);
        TestUtils.containsHeaderWithValue(resp, "Content-Type",
                "application/json");
    }

    @Test
    public final void testGetResponseHeaders() throws HttpServiceException {
        final HttpResponse resp = get(
                String.format("%sTest=custom+header", TestUtils.URL_HEADERS));
        TestUtils.successResponseAndCode(resp);
        TestUtils.containsHeaderWithValue(resp, "Test", "custom header");
    }

    @Test
    public final void testGetError() throws HttpServiceException {
        final HttpResponse resp = get(String.format("%st", TestUtils.URL_GET));
        TestUtils.errorResponseAndCode(resp);
    }

    @Test
    public final void testGetStatus() throws HttpServiceException {
        getStatus(202);
        getStatus(404);
        getStatus(502);
    }

    @Test
    public final void testGetParameters() throws HttpServiceException {
        parameters = NameValuePairList.getInstance().add("a", "5").add("b", "2")
                .build();
        final HttpResponse resp = get();
        TestUtils.successResponseAndCode(resp);
    }

    /**
     * Makes a request guaranteed to generate the given status code and verifies
     * the response.
     *
     * @param statusCode
     *            the status code to generate
     * @throws HttpServiceException
     *             if any errors occur while executing the request
     */
    private void getStatus(final int statusCode) throws HttpServiceException {
        final HttpResponse resp = get(
                String.format("%s%d", TestUtils.URL_STATUS, statusCode));
        TestUtils.errorResponseWithCode(resp, statusCode);
    }

    /**
     * Performs an HTTP get request on the given url and returns the response.
     *
     * @param url
     *            the url
     * @return the HttpResponse
     * @throws HttpServiceException
     *             if any errors occur while executing the request
     */
    private HttpResponse get(final String url) throws HttpServiceException {
        return http.get(url, headers, parameters);
    }

    private HttpResponse get() throws HttpServiceException {
        return get(TestUtils.URL_GET);
    }
}
