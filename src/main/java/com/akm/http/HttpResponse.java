package com.akm.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.StatusLine;

/**
 * This class represents an HTTP response.
 * <p>The response headers, status line related info, and the response data
 * are all accesible through the object.
 *
 * @since 0.1
 * @author Amir
 */
public final class HttpResponse {
	private final Map<String, String> headers;
	private final int statusCode;
	private final String statusMessage;
	private final String protocol;
	private final String data;

	HttpResponse(final Header[] headers, final StatusLine statusLine, final String data) {
		if (headers == null) {
			throw new IllegalArgumentException("headers may not be null");
		}

		if (statusLine == null) {
			throw new IllegalArgumentException("status line may not be null");
		}

		// set header info
		this.headers = new HashMap<>();
		for (Header header : headers) {
			this.headers.put(header.getName(), header.getValue());
		}

		// set status line info
		this.statusCode = statusLine.getStatusCode();
		this.statusMessage = statusLine.getReasonPhrase();
		this.protocol = statusLine.getProtocolVersion().toString();

		// set data
		this.data = data;
	}

	/**
	 * Returns a formatted string of the status line including the protocol,
	 * status code, and message.
	 * @return the status line
	 */
	public String getStatusLine() {
		return String.format("%s %s %s", protocol, statusCode, statusMessage);
	}

	/**
	 * Returns the value of the given header.
	 * @param name the name of the header
	 * @return the value of the header
	 */
	public String getHeader(final String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("header name may not be blank");
		}

		return headers.get(name);
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getData() {
		return data;
	}
}
