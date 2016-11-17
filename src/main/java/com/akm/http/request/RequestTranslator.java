package com.akm.http.request;

import java.util.Map;

import com.akm.http.exception.HttpRequestTranslationException;

/**
 * Internal interface used to provide a way for objects to translate their
 * fields to request parameters. All fields expected to be translated must be
 * annotated with {@link RequestParameter}.
 *
 * @see RequestParameter
 * @see AbstractRequestTranslator
 * @since 0.3
 * @author Amir
 */
interface RequestTranslator {
    /**
     * Generate a map of request parameters and their values from all annotated
     * fields.
     *
     * @return a map containing all request parameters
     * @throws HttpRequestTranslationException
     *             if any errors occur while translating the fields
     */
    Map<String, String> translate() throws HttpRequestTranslationException;
}
