### 完善 my dependency-injection 模块
- 脱离 web.xml 配置实现 ComponentContext 自动初始化
- 使用独立模块并且能够在 user-web 中运行成功
### 完善 my-configuration 模块
- Config 对象如何能被 my-web-mvc 使用
- 可能在 ServletContext 获取如何通过 ThreadLocal 获取

### 实现：
- my-configuration
```java
//主要都是通过spi加载一个javax.servlet.ServletContainerInitializer，然后添加listener实现的独立模块

/**
 * 注册ServletContextConfigInitializer
 */
public class ServletConfigInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
        // 增加 ServletContextListener
        servletContext.addListener(ServletContextConfigInitializer.class);
    }
}

/**
 * ServletContextConfigInitializer
 */
@WebListener
public class ServletContextConfigInitializer implements ServletContextListener {

    
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        /**
         * 获取ServletContext，配置好configProviderResolver之后，将其setAttribute到ServletContext
         */
        ServletContext servletContext = servletContextEvent.getServletContext();
        ServletContextConfigSource servletContextConfigSource = new ServletContextConfigSource(servletContext);
        // 获取当前 ClassLoader
        ClassLoader classLoader = servletContext.getClassLoader();
        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
        ConfigBuilder configBuilder = configProviderResolver.getBuilder();
        // 配置 ClassLoader
        configBuilder.forClassLoader(classLoader);
        // 默认配置源（内建的，静态的）
        configBuilder.addDefaultSources();
        // 通过发现配置源（动态的）
        configBuilder.addDiscoveredConverters();
        // 增加扩展配置源（基于 Servlet 引擎）
//        configBuilder.withSources(servletContextConfigSource);
        // 获取 Config
        Config config = configBuilder.build();
        // 注册 Config 关联到当前 ClassLoader
        configProviderResolver.registerConfig(config, classLoader);
        //使用ConfigProviderResolver的name注册到servletContext
        servletContext.setAttribute(ConfigProviderResolver.class.getName(), configProviderResolver);
    }
    //...
}

```

- my dependency-injection
```java
/**
 * ComponentContextInitializerListener
 */
public class ComponentContextInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
// 增加 ServletContextListener
        servletContext.addListener(ComponentContextInitializerListener.class);
    }
}
/**
 * ServletContextConfigInitializer
 */
@WebListener
public class ComponentContextInitializerListener implements ServletContextListener {

    private ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /**
         * 获取ServletContext，对ClassicComponentContext进行初始化
         */
        this.servletContext = sce.getServletContext();
        ClassicComponentContext context = new ClassicComponentContext();
        context.init(servletContext);
    }
    //...
}

```


### 测试

```java
@Path("/hello")
public class HelloWorldController implements PageController {

    @GET
    @Path("/world") // /hello/world -> HelloWorldController
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        ServletContext servletContext = request.getServletContext();
        ClassLoader classLoader = servletContext.getClassLoader();
        ConfigProviderResolver ConfigProviderResolver =
                (ConfigProviderResolver)servletContext.getAttribute(ConfigProviderResolver.class.getName());
        Config config = ConfigProviderResolver.getConfig(classLoader);

        System.out.println("---------- app.name = " + config.getValue("app.name", String.class));
        System.out.println("---------- app.version = " + config.getValue("app.version", Double.class));

        ClassicComponentContext context = ClassicComponentContext.getInstance();
        DBConnectionManager dbConnectionManager = context.getComponent("bean/DBConnectionManager");
        dbConnectionManager.getEntityManager();
        return "index.jsp";
    }
}
```
