package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OSEnvPropertiesConfigSource implements ConfigSource {

    private final Map<String, String> properties;

    public OSEnvPropertiesConfigSource() {
        //获取系统环境变量
        Map systemProperties = System.getenv();
        this.properties = new HashMap<>(systemProperties);
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public String getName() {
        return "Java System Properties";
    }

    @Override
    public int getOrdinal() {
        return 200;
    }
}
