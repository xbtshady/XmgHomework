package org.geektimes.projects.user.web.listener;

import org.geektimes.configuration.microprofile.config.DefaultConfigProviderResolver;
import org.geektimes.configuration.microprofile.config.JavaConfig;
import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.management.MyManager;
import org.geektimes.projects.user.management.UserManager;

import javax.management.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

/**
 * {@link ComponentContext} 初始化器
 * ContextLoaderListener
 */
public class ComponentContextInitializerListener implements ServletContextListener {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
        ComponentContext context = new ComponentContext();
        context.init(servletContext);

        DefaultConfigProviderResolver defaultConfigProviderResolver = new DefaultConfigProviderResolver();
        JavaConfig config = (JavaConfig)defaultConfigProviderResolver.getConfig();
        logger.info("application.name " + config.getValue("application.name",String.class));
        logger.info("myIntValue " + config.getValue("myIntValue",Integer.class));
        logger.info("myBooleanValue " + config.getValue("myBooleanValue",Boolean.class));
        logger.info("myFloatValue " + config.getValue("myFloatValue",Float.class));
        logger.info("myLongValue " + config.getValue("myLongValue",Long.class));
        logger.info("myDoubleValue " + config.getValue("myDoubleValue",Double.class));
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
    }


    private static Object createUserMBean(User user) throws Exception {
        return new UserManager(user);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        ComponentContext context = ComponentContext.getInstance();
//        context.destroy();
    }

}
