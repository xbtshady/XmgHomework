package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.LogManager;

public class LocalFilePropertiesConfigSource  implements ConfigSource {
    private final Map<String, String> properties;

    public LocalFilePropertiesConfigSource() throws IOException {
        //获取本地文件
        Properties p = new Properties();;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("META-INF/system.properties")) {
            p.load(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        Map map = new HashMap();
        Set<Entry<Object, Object>> entrySet = p.entrySet();
        for (Entry<Object, Object> entry : entrySet) {
            map.put(entry.getKey(),entry.getValue());
        }
        this.properties = map;
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
        return 300;
    }
}
