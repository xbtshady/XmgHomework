package com.xw.projects.spring.cloud.config.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySources;

@SpringBootApplication
//@EnableConfigServer // 激活 Config Server
//@EnableDiscoveryClient // 激活服务注册与发现
//@Import(MyConfigBootstrapConfiguration.class)
public class ConfigServer {


    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ConfigServer.class, args);

        ConfigurableEnvironment environment = (ConfigurableEnvironment)applicationContext.getEnvironment();
        PropertySources sources = environment.getPropertySources();

        System.out.printf("ok");
    }

    @Value("${name}")
    private String name;

    @Value("${version}")
    private int version = 1;

    @Bean
    public ApplicationRunner runner() {
        return args -> System.out.printf("name = %s, version = %d %n", name, version);
    }

    @EventListener(WebServerInitializedEvent.class)
    public void onWebServerInitialized(WebServerInitializedEvent event) {
        WebServer webServer = event.getWebServer();
        System.out.println("当前 Web 服务器端口：" + webServer.getPort());
    }
}
