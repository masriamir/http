package com.akm.http.builder;

/**
 * Internal interface for implementing the builder pattern.
 *
 * @since 0.2
 * @author Amir
 *
 * @param <T>
 *            the type of object to build
 */
interface BuilderPattern<T> {
    /**
     * Builds the final object of type T.
     *
     * @return the object
     */
    T build();
}
