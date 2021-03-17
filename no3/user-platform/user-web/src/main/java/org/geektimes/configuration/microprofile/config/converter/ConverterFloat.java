package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class ConverterFloat implements Converter<Float> {

    public ConverterFloat() {
    }

    @Override
    public Float convert(String s) throws IllegalArgumentException, NullPointerException {
        return Float.valueOf(s);
    }
}
