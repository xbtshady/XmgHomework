package org.geektimes.projects.user.web.controller;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.geektimes.context.ClassicComponentContext;
import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.sql.DBConnectionManager;
import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 输出 “Hello,World” Controller
 */
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
