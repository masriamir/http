package com.akm.http;

import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.rules.ExpectedException;

import com.akm.http.exception.HttpServiceException;

/**
 * Utility class for unit testing.
 *
 * @since 0.1
 * @author Amir
 */
final class TestUtils {
    static final String URL_POST = "https://httpbin.org/post";
    static final String URL_GET = "https://httpbin.org/get";
    static final String URL_STATUS = "https://httpbin.org/status/";
    static final String URL_HEADERS = "https://httpbin.org/response-headers?";

    /**
     * Asserts that the given collection is not null and not empty.
     * 
     * @param c
     *            the collection
     * @param name
     *            variable name used in assert message
     */
    public static void notEmpty(final Collection<?> c, final String name) {
        assertNotNull(String.format("%s is null", name), c);
        assertFalse(String.format("%s is empty", name), c.isEmpty());
    }

    /**
     * Asserts that the given map is not null and not empty.
     * 
     * @param m
     *            the map
     * @param name
     *            variable name used in assert message
     */
    public static void notEmpty(final Map<?, ?> m, final String name) {
        assertNotNull(String.format("%s is null", name), m);
        assertFalse(String.format("%s is empty", name), m.isEmpty());
    }

    /**
     * Asserts that the given string is not null and not empty.
     * 
     * @param s
     *            the string
     * @param name
     *            variable name used in assert message
     */
    public static void notBlank(final String s, final String name) {
        assertNotNull(String.format("%s is null", name), s);
        assertFalse(String.format("%s is blank", name), s.isEmpty());
    }

    /**
     * Asserts the given response is valid.
     * 
     * @param resp
     *            the HttpResponse
     */
    public static void validResponse(final HttpResponse resp) {
        assertNotNull("response is null", resp);
        notEmpty(resp.getHeaders(), "headers");
        notBlank(resp.getProtocol(), "protocol");
        notBlank(resp.getStatusMessage(), "status message");
    }

    /**
     * Asserts the given response represents a successful request.
     * 
     * @param resp
     *            the HttpResponse
     */
    public static void successResponse(final HttpResponse resp) {
        validResponse(resp);
        notBlank(resp.getData(), "data");
    }

    /**
     * Asserts the given response represents a successful request with an
     * appropriate status code.
     * 
     * @param resp
     *            the HttpResponse
     */
    public static void successResponseAndCode(final HttpResponse resp) {
        successResponse(resp);
        successStatusCode(resp.getStatusCode());
    }

    /**
     * Asserts the given response represents a successful request with the given
     * status code.
     * 
     * @param resp
     *            the HttpResponse
     * @param statusCode
     *            the expected status code
     */
    public static void successResponseWithCode(final HttpResponse resp,
            final int statusCode) {
        successResponse(resp);
        statusCode(statusCode, resp.getStatusCode());
    }

    /**
     * Asserts the given response represents a successful request.
     * <p>
     * This method does not make any assertions about the data returned.
     * 
     * @param resp
     *            the HttpResponse
     */
    public static void errorResponse(final HttpResponse resp) {
        validResponse(resp);
        assertNotNull("data is null", resp.getData());
    }

    /**
     * Asserts the given response represents an unsuccessful request with an
     * appropriate status code.
     * 
     * @param resp
     *            the HttpResponse
     */
    public static void errorResponseAndCode(final HttpResponse resp) {
        errorResponse(resp);
        errorStatusCode(resp.getStatusCode());
    }

    /**
     * Asserts the given response represents an unsuccessful request with the
     * given status code.
     * 
     * @param resp
     *            the HttpResponse
     * @param statusCode
     *            the expected status code
     */
    public static void errorResponseWithCode(final HttpResponse resp,
            final int statusCode) {
        errorResponse(resp);
        statusCode(statusCode, resp.getStatusCode());
    }

    /**
     * Asserts the given status codes are equal.
     * 
     * @param expected
     *            the expected status code
     * @param actual
     *            the actual status code
     */
    public static void statusCode(final int expected, final int actual) {
        assertEquals(String.format("status code is not %d", expected), expected,
                actual);
    }

    /**
     * Asserts the given status code represents a successful request.
     * 
     * @param statusCode
     *            the status code
     */
    public static void successStatusCode(final int statusCode) {
        assertTrue("success code expected", statusCode < 400);
    }

    /**
     * Asserts the given status code represents an unsuccessful request.
     * 
     * @param statusCode
     *            the status code
     */
    public static void errorStatusCode(final int statusCode) {
        assertTrue("error code expected", statusCode >= 400);
    }

    /**
     * Asserts the given response contains the given header.
     * 
     * @param resp
     *            the HttpResponse
     * @param header
     *            the name of the header
     */
    public static void containsHeader(final HttpResponse resp,
            final String header) {
        assertTrue(String.format("header %s missing from response", header),
                resp.getHeaders().containsKey(header));
    }

    /**
     * Asserts the given response contains the given header with the specified
     * value.
     * 
     * @param resp
     *            the HttpResponse
     * @param header
     *            the name of the header
     * @param value
     *            the value of the header
     */
    public static void containsHeaderWithValue(final HttpResponse resp,
            final String header, final String value) {
        containsHeader(resp, header);
        assertEquals(String.format("header %s does not have value %s", header,
                value), value, resp.getHeaders().get(header));
    }

    /**
     * Adds the given header name and value to the specified list of headers.
     * 
     * @param headers
     *            the list of headers
     * @param name
     *            the name of the header
     * @param value
     *            the value of the header
     */
    public static void addHeader(final List<Header> headers, final String name,
            final String value) {
        headers.add(new BasicHeader(name, value));
    }

    /**
     * Expected exception for a null url.
     * 
     * @param e
     *            the ExpectedException
     */
    public static void nullUrlException(final ExpectedException e) {
        e.expect(HttpServiceException.class);
        e.expectMessage(startsWith("unable to instantiate class"));
        e.expectCause(isA(InvocationTargetException.class));
    }

    private TestUtils() {}
}
