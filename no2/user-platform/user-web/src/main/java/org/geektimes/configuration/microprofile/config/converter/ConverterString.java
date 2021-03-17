package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class ConverterString implements Converter<String> {

    public ConverterString() {
    }

    @Override
    public String convert(String s) throws IllegalArgumentException, NullPointerException {
        return s;
    }
}
