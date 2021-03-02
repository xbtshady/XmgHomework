package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.DatabaseUserRepository;
import org.geektimes.projects.user.sql.DBConnectionManager;
import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * 输出 “Hello,World” Controller
 */
@Path("/page")
public class MyPageController implements PageController {

    @GET
    @POST
    @Path("/world") // /hello/world -> HelloWorldController
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        System.out.println("execute");
        return "login-form.jsp";
    }

    @GET
    @POST
    @Path("/goRegister")
    public String goRegister(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        System.out.println("goRegister");
        return "register.jsp";
    }


    @GET
    @POST
    @Path("/registerSuccess")
    public String register(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        String databaseURL = "jdbc:derby:mydb;create=true";
        Connection connection = DriverManager.getConnection(databaseURL);
        DBConnectionManager dbConnectionManager = new DBConnectionManager();
        dbConnectionManager.setConnection(connection);
        DatabaseUserRepository db = new DatabaseUserRepository(dbConnectionManager);
        User user = new User();
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        db.save(user);
        return "registerSuccess.jsp";
    }
}
