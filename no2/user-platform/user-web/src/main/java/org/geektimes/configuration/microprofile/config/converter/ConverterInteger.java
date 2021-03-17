package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class ConverterInteger implements Converter<Integer> {

    public ConverterInteger() {
    }

    @Override
    public Integer convert(String s) throws IllegalArgumentException, NullPointerException {
        return Integer.valueOf(s);
    }
}