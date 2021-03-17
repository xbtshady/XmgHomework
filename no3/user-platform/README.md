## 整合 https://jolokia.org/实现一个自定义 JMX MBean，通过 Jolokia 做 Servlet 代理
自定义 JMX MBean: [代码链接](https://github.com/xbtshady/XmgHomework/tree/main/no3/user-platform/user-web/src/main/java/org/geektimes/projects/user/management)
```java
public class MyManager implements MyManagerMBean{

    public  String appName;
    @Override
    public String getAppName() {
        return appName;
    }
}
```
在ComponentContextInitializerListener中进行MBeanServer注册: [代码链接](https://github.com/xbtshady/XmgHomework/blob/main/no3/user-platform/user-web/src/main/java/org/geektimes/projects/user/web/listener/ComponentContextInitializerListener.java)
```java
// 获取平台 MBean Server
MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = null;
        try {
        objectName = new ObjectName("org.geektimes.projects.user.management:type=MyManager");
        // 创建 UserMBean 实例
        MyManager myManager = new MyManager();
        myManager.appName = config.getValue("application.name",String.class);
mBeanServer.registerMBean(myManager, objectName);
}catch (Exception e) {
e.printStackTrace();
}
```
通过http://localhost:8080/jolokia/read/org.geektimes.projects.user.management:type=MyManager获取MBean信息
```json
// 获取平台 MBean Server
{"request":{"mbean":"org.geektimes.projects.user.management:type=MyManager","type":"read"},"value":{"AppName":"xmg"},"timestamp":1615994286,"status":200}
}
```

## 扩展 org.eclipse.microprofile.config.spi.ConfigSource 实现，包括 OS 环境变量，以及本地配置文件

实现的类有[代码链接](https://github.com/xbtshady/XmgHomework/tree/main/no3/user-platform/user-web/src/main/java/org/geektimes/configuration/microprofile/config/source)

- LocalFilePropertiesConfigSource
   
配置文件是META-INF/system.properties

```
application.name=xmg
myIntValue=4
myBooleanValue=true
myFloatValue=4.0
myLongValue=4
myDoubleValue=4.0
```
- OSEnvPropertiesConfigSource
  


## 扩展 org.eclipse.microprofile.config.spi.Converter 实现，提供 String 类型到简单类型

实现的类有[代码链接](https://github.com/xbtshady/XmgHomework/tree/main/no3/user-platform/user-web/src/main/java/org/geektimes/configuration/microprofile/config/converter)

- ConverterBoolean
- ConverterDouble
- ConverterFloat
- ConverterInteger
- ConverterLong
- ConverterString

实际获取Converter通过SPI实现,存储到JavaConfig中的converterMap[代码链接](https://github.com/xbtshady/XmgHomework/blob/main/no3/user-platform/user-web/src/main/java/org/geektimes/configuration/microprofile/config/JavaConfig.java)
```java
//获取所有的Converter 存到Converter中
ServiceLoader<Converter> converterLoader = ServiceLoader.load(Converter.class, classLoader);
converterLoader.forEach(converter -> {
    try {
      converterMap.put(converter.getClass().getDeclaredMethod("convert",String.class).getReturnType(),converter);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
      }
    }
);
```
具体使用[代码链接](https://github.com/xbtshady/XmgHomework/blob/main/no3/user-platform/user-web/src/main/java/org/geektimes/configuration/microprofile/config/JavaConfig.java)
```java
@Override
public <T> T getValue(String propertyName, Class<T> propertyType) {
    String propertyValue = getPropertyValue(propertyName);
    // String 转换成目标类型
    return (T)converterMap.get(propertyType).convert(propertyValue);
}
```

## 测试配置

在ComponentContextInitializerListener中进行测试: [代码链接](https://github.com/xbtshady/XmgHomework/blob/main/no3/user-platform/user-web/src/main/java/org/geektimes/projects/user/web/listener/ComponentContextInitializerListener.java)
```java
DefaultConfigProviderResolver defaultConfigProviderResolver = new DefaultConfigProviderResolver();
JavaConfig config = (JavaConfig)defaultConfigProviderResolver.getConfig();
logger.info("application.name " + config.getValue("application.name",String.class));
logger.info("myIntValue " + config.getValue("myIntValue",Integer.class));
logger.info("myBooleanValue " + config.getValue("myBooleanValue",Boolean.class));
logger.info("myFloatValue " + config.getValue("myFloatValue",Float.class));
logger.info("myLongValue " + config.getValue("myLongValue",Long.class));
logger.info("myDoubleValue " + config.getValue("myDoubleValue",Double.class));
```

日志输出：
```
17-Mar-2021...contextInitialized application.name xmg
17-Mar-2021...contextInitialized myIntValue 4
17-Mar-2021...contextInitialized myBooleanValue true
17-Mar-2021...contextInitialized myFloatValue 4.0
17-Mar-2021...contextInitialized myLongValue 4
17-Mar-2021...contextInitialized myDoubleValue 4.0
```