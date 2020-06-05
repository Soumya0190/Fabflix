package main.java;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;

@WebServlet(name = "EmployeeLoginServlet", urlPatterns = "/api/employeelogin")
public class EmployeeLoginServlet extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    private final String servletName ="EmployeeLoginServlet";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        JsonObject responseJsonObject = new JsonObject();
        response.setContentType("application/json");

        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //logout

        String logout = request.getParameter("logout");
        if (logout == "Y") {
            request.getSession().setAttribute("user", null);
            responseJsonObject.addProperty("status", "fail");
        }

        else {
            //Boolean isUserValid = validateUser(username, password);
            Boolean isUserValid = true;
            User usrObj = validateUser(username, password);
            if (usrObj != null && usrObj.isUserValid == true) {
                //if (isUserValid){
                session = request.getSession(true);
                request.getSession().setAttribute("user", usrObj);
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");
            }
            else {
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Incorrect Username or password");

            }
        }
        response.getWriter().write(responseJsonObject.toString());
        System.out.println(responseJsonObject.toString());
    }

    protected User validateUser(String username, String password)
    {
        String role ="";  User userObj;
        Boolean userExists = false;
        String userQuery = "SELECT role FROM employees WHERE username=? AND password =?";

        try
        {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatemenUser = connection.prepareStatement(userQuery);
            preparedStatemenUser.setString(1, username);
            preparedStatemenUser.setString(2, password);

            ResultSet userResult = preparedStatemenUser.executeQuery();

            while (userResult.next())
            {
                role = userResult.getString("role");
                userExists = true;
            }

            userResult.close();
            preparedStatemenUser.close();
            connection.close();
            //User(String username, String userId, String firstName, String lastName, Boolean isUserValid, String role)
            userObj = new User(username, username, "", "",userExists, "admin");
        }
        catch (Exception ex)
        {
            System.out.println(servletName +" Exception=" + ex.getMessage());
            return null;
        }
        return userObj;
    }
}

