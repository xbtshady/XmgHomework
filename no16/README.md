## 作业：
将 Spring Boot 应用打包 Java Native 应用，再将该应用通过 Dockerfile 构建 Docker 镜像，部署到 Docker 容器中，并且成功运行，Spring Boot 应用的实现复杂度不做要求

实现：
在linux操作，需要先装上Graalvm、Maven、Docker。

首先使用maven命令进行打包
```shell
[root spring-boot-native-demo] mvn package -Pnative
```
之后是DockerFile
```shell
FROM centos:7
LABEL maintainer="xw"
LABEL email = "943217258@qq.com"
COPY target/demo /demo
CMD [ "/demo" ]
```
大意是将taget中生成的Java Native 应用复制到镜像中，然后运行这个应用

```shell
[root spring-boot-native-demo]# docker build -t demo .
[root spring-boot-native-demo]# docker images
REPOSITORY   TAG       IMAGE ID       CREATED        SIZE
demo         latest    8965787af32d   2 days ago     276MB
centos       7         8652b9f0cb4c   7 months ago   204MB

```
docker构建完影像之后可以看到该影像


```shell
[root spring-boot-native-demo]# docker run -p 8080:8080 demo

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.5.1)

2021-06-23 12:35:56.759  INFO 1 --- [           main] com.example.demo.DemoApplication         : Starting DemoApplication v0.0.1-SNAPSHOT using Java 1.8.0_292 on f4609b58d2f4 with PID 1 (/demo started by root in /)
2021-06-23 12:35:56.760  INFO 1 --- [           main] com.example.demo.DemoApplication         : No active profile set, falling back to default profiles: default
2021-06-23 12:35:56.792  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
Jun 23, 2021 12:35:56 PM org.apache.coyote.AbstractProtocol init
INFO: Initializing ProtocolHandler ["http-nio-8080"]
Jun 23, 2021 12:35:56 PM org.apache.catalina.core.StandardService startInternal
INFO: Starting service [Tomcat]
Jun 23, 2021 12:35:56 PM org.apache.catalina.core.StandardEngine startInternal
INFO: Starting Servlet engine: [Apache Tomcat/9.0.46]
Jun 23, 2021 12:35:56 PM org.apache.catalina.core.ApplicationContext log
INFO: Initializing Spring embedded WebApplicationContext
2021-06-23 12:35:56.794  INFO 1 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 34 ms
Jun 23, 2021 12:35:56 PM org.apache.coyote.AbstractProtocol start
INFO: Starting ProtocolHandler ["http-nio-8080"]
2021-06-23 12:35:56.855  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2021-06-23 12:35:56.855  INFO 1 --- [           main] com.example.demo.DemoApplication         : Started DemoApplication in 0.109 seconds (JVM running for 0.125)

```
运行结果，可以看到spring boot正常启动

```shell
[root]# curl http://127.0.0.1:8080
Hello GraalVm
```