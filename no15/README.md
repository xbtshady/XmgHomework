## 作业：
通过 GraalVM 将一个简单 Spring Boot 工程构建为 Native Image，要求:

- 代码要自己手写 @Controller @RequestMapping("/helloworld")
- 相关插件可以参考 Spring Native Samples
- (可选) 理解 Hint 注解的使用

实现：
先装上GraalVM，然后再装上vs2019,vs2019要注意的是选择C++的桌面开发，在Native cmd执行编译命令
```shell
mvn package -Pnative
```
spring boot项目是通过start spring io生成的只添加了依赖web，后续自行在pom.xml添加了依赖
```xml
<!--添加此依赖，可以通过在编译时创建候选对象的静态列表来提高大型应用程序的启动性能 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context-indexer</artifactId>
</dependency>

<!--Graalvm-native 依赖 -->
<dependency>
<groupId>org.springframework.experimental</groupId>
<artifactId>spring-graalvm-native</artifactId>
<version>0.8.5</version>
</dependency>
```
添加了插件
```xml
 <build>
    <plugins>
        <plugin>
            <groupId>org.graalvm.nativeimage</groupId>
            <artifactId>native-image-maven-plugin</artifactId>
            <version>21.0.0.2</version>
            <configuration>
                <skip>false</skip>
                <imageName>${project.artifactId}</imageName>
                <buildArgs>
                    --no-fallback
                </buildArgs>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>native-image</goal>
                    </goals>
                    <phase>package</phase>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>3.1.0</version>
            <configuration>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
                <archive>
                    <manifest>
                        <mainClass>com.example.demo.DemoApplication</mainClass>
                    </manifest>
                </archive>
            </configuration>
            <executions>
                <execution>
                    <id>make-assembly</id>
                    <phase>package</phase>
                    <goals>
                        <goal>single</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

添加profile
```xml
<profiles>
        <profile>
            <id>native</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.graalvm.nativeimage</groupId>
                        <artifactId>native-image-maven-plugin</artifactId>
                        <version>21.0.0.2</version>
                        <configuration>
                            <skip>false</skip>
                            <imageName>${project.artifactId}</imageName>
                            <buildArgs>
                                --no-fallback
                                --initialize-at-build-time=org.springframework.util.unit.DataSize
                                --initialize-at-build-time=org.slf4j.MDC
                                --initialize-at-build-time=ch.qos.logback.classic.Level
                                --initialize-at-build-time=ch.qos.logback.classic.Logger
                                --initialize-at-build-time=ch.qos.logback.core.util.StatusPrinter
                                --initialize-at-build-time=ch.qos.logback.core.status.StatusBase
                                --initialize-at-build-time=ch.qos.logback.core.status.InfoStatus
                                --initialize-at-build-time=ch.qos.logback.core.spi.AppenderAttachableImpl
                                --initialize-at-build-time=org.slf4j.LoggerFactory
                                --initialize-at-build-time=ch.qos.logback.core.util.Loader
                                --initialize-at-build-time=org.slf4j.impl.StaticLoggerBinder
                                --initialize-at-build-time=ch.qos.logback.classic.spi.ThrowableProxy
                                --initialize-at-build-time=ch.qos.logback.core.CoreConstants
                                --initialize-at-build-time=org.springframework.boot.context.config.StandardConfigDataLoader
                                --initialize-at-build-time=org.springframework.boot.context.config.ConfigData$PropertySourceOptions
                                --initialize-at-build-time=org.springframework.boot.context.config.ConfigData$Options
                                --trace-class-initialization=org.springframework.boot.context.config.StandardConfigDataLoader
                                --report-unsupported-elements-at-runtime
                                --allow-incomplete-classpath
                                -H:+TraceClassInitialization
                            </buildArgs>
                        </configuration>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>native-image</goal>
                                </goals>
                                <phase>package</phase>
                            </execution>
                        </executions>
                    </plugin>

                    <plugin>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-maven-plugin</artifactId>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
```

最后得到demo.exe,直接执行在浏览器中打开http://localhost:8080/就可以看到响应