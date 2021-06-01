package com.xw.projects.spring.cloud.config.server;

import org.springframework.core.env.MapPropertySource;

import java.util.Map;

public class MyPropertySource extends MapPropertySource {
    public MyPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }
}
