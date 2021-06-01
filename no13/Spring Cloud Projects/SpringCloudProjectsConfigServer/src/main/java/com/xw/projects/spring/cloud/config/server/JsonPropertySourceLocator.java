package com.xw.projects.spring.cloud.config.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Order(0)
public class JsonPropertySourceLocator implements PropertySourceLocator {

    public JsonPropertySourceLocator() {
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        return new MyPropertySource("myJson", mapPropertySource());
    }

    /**
     * Resource转Map
     * @return
     */
    private Map<String, Object> mapPropertySource() {

        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 获取json格式的Map
            Map<String, Object> fileMap = objectMapper.readValue(readFile(), Map.class);
            // 组装嵌套json
            processorMap("", result, fileMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private void processorMap(String prefix, Map<String, Object> result, Map<String, Object> fileMap) {
        if (prefix.length() > 0) {
            prefix += ".";
        }
        for (Map.Entry<String, Object> entrySet : fileMap.entrySet()) {
            if (entrySet.getValue() instanceof Map) {
                processorMap(prefix + entrySet.getKey(), result, (Map<String, Object>) entrySet.getValue());
            } else {
                result.put(prefix + entrySet.getKey(), entrySet.getValue());
            }
        }
    }
    private String readFile() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(System.getProperty("user.dir")+"/SpringCloudProjectsConfigServer/src/main/resources/config-repo/my.json");
            String str = "";
            byte[] readByte = new byte[1024];
            int length;
            while ((length = fileInputStream.read(readByte)) > 0) {
                str += new String(readByte, 0, length, "UTF-8");
            }

            return str;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
