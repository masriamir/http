package com.akm.http;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.akm.http.exception.HttpServiceException;

/**
 * Provides test cases for HTTP post requests.
 *
 * @since 0.1
 * @author Amir
 */
public class HttpPostTest {
    private HttpService http = null;
    private static Map<String, String> headers = null;
    private static Map<String, String> parameters = null;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        http = new HttpService();
        headers = new HashMap<>();
        parameters = new HashMap<>();
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
        post(null);
    }

    @Test
    public final void testPost() throws HttpServiceException {
        final HttpResponse resp = post();
        TestUtils.successResponseAndCode(resp);
    }

    @Test
    public final void testPostHeaders() throws HttpServiceException {
        final HttpResponse resp = post();
        TestUtils.successResponseAndCode(resp);
        TestUtils.containsHeaderWithValue(resp, "Content-Type",
                "application/json");
    }

    @Test
    public final void testPostError() throws HttpServiceException {
        final HttpResponse resp = post(
                String.format("%st", TestUtils.URL_POST));
        TestUtils.errorResponseAndCode(resp);
    }

    @Test
    public final void testPostStatus() throws HttpServiceException {
        getStatus(202);
        getStatus(404);
        getStatus(502);
    }

    @Test
    public final void testPostParameters() throws HttpServiceException {
        parameters.put("a", "5");
        parameters.put("b", "2");
        final HttpResponse resp = post();
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
        final HttpResponse resp = post(
                String.format("%s%d", TestUtils.URL_STATUS, statusCode));
        TestUtils.errorResponseWithCode(resp, statusCode);
    }

    /**
     * Performs an HTTP post request on the given url and returns the response.
     *
     * @param url
     *            the url
     * @return the HttpResponse
     * @throws HttpServiceException
     *             if any errors occur while executing the request
     */
    private HttpResponse post(final String url) throws HttpServiceException {
        return http.post(url, headers, parameters);
    }

    private HttpResponse post() throws HttpServiceException {
        return post(TestUtils.URL_POST);
    }
}
