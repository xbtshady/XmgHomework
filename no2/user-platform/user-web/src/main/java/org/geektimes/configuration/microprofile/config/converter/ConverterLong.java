package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class ConverterLong implements Converter<Long> {

    public ConverterLong() {
    }

    @Override
    public Long convert(String s) throws IllegalArgumentException, NullPointerException {
        return Long.valueOf(s);
    }

}