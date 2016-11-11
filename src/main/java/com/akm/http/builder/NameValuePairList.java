package com.akm.http.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Convenience object for easily generating multiple name value pairs at once.
 *
 * @see NameValuePair
 * @since 0.2
 * @author Amir
 */
public final class NameValuePairList {
    /**
     * The internal list of NameValuePair objects.
     */
    private final List<NameValuePair> nvps;

    /**
     * Returns a new builder instance for adding name value pairs.
     *
     * @return a new Builder object
     */
    public static Builder getInstance() {
        return new Builder();
    }

    public static class Builder implements BuilderPattern<NameValuePairList> {
        private final List<NameValuePair> nvps;

        public Builder() {
            this.nvps = new ArrayList<>();
        }

        @Override
        public NameValuePairList build() {
            return new NameValuePairList(this);
        }

        /**
         * Adds a new name value pair using the given name and value.
         *
         * @param name
         *            the name
         * @param value
         *            the value
         * @return the Builder
         */
        public Builder add(final String name, final String value) {
            nvps.add(new NameValuePair(name, value));
            return this;
        }
    }

    public List<NameValuePair> getNvps() {
        return nvps;
    }

    private NameValuePairList(final Builder builder) {
        this.nvps = builder.nvps;
    }
}
