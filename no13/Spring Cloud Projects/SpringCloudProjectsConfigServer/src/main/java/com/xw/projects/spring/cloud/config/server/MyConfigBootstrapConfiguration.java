package com.xw.projects.spring.cloud.config.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration(proxyBeanMethods = false)
public class MyConfigBootstrapConfiguration {
    @Bean
    public JsonPropertySourceLocator jsonPropertySourceLocator(){
        return new JsonPropertySourceLocator();
    }
}
