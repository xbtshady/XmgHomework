package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;


public class ConverterBoolean implements Converter<Boolean> {

    public ConverterBoolean() {
    }

    @Override
    public Boolean convert(String s) throws IllegalArgumentException, NullPointerException {
        return Boolean.valueOf(s);
    }
}
