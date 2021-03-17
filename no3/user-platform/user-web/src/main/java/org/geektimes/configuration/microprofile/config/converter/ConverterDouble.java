package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class ConverterDouble implements Converter<Double> {

    public ConverterDouble() {
    }

    @Override
    public Double convert(String s) throws IllegalArgumentException, NullPointerException {
        return Double.valueOf(s);
    }
}