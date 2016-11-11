package com.akm.http.builder;

import org.apache.http.message.BasicNameValuePair;

/**
 * Class to create a simple name-value pair.
 *
 * @since 0.2
 * @author Amir
 */
public final class NameValuePair extends BasicNameValuePair {
    private static final long serialVersionUID = -6577731301397060528L;

    public NameValuePair(final String name, final String value) {
        super(name, value);
    }
}
