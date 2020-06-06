package main.java;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.sql.DataSource;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    // private DataSource dataSource;
    private final String servletName ="LoginServlet";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        JsonObject responseJsonObject = new JsonObject();
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String usertype = request.getParameter("usertype");

        String logout = request.getParameter("logout");
        User usrObj =null;
        System.out.println("usrNm="+ username+" , password="+password+",  usertype="+usertype);

        if (usertype != null && usertype.equals("admin")) {
            usrObj = validateEmployee(username, password);
            System.out.println(" admin =" + usrObj.toString());
        }
        else {
            if( usertype != null && usertype.equals("wuser")) {
                String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
                try {
                    RecaptchaVerifyUtils.verify(gRecaptchaResponse);
                } catch (Exception e) {
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", "recaptcha verification error");
                    response.getWriter().write(responseJsonObject.toString());
                    System.out.println(servletName + " Exception=" + e.getMessage());
                    return;
                }
            }
            //Boolean isUserValid = validateUser(username, password);
            usrObj = validateUser(username, password);
            System.out.println(" customer =" + usrObj.toString());
        }

        if (usrObj != null && usrObj.isUserValid == true) {
            System.out.println(" Valid user");
            session = request.getSession(true);
            request.getSession().setAttribute("user", usrObj);

            //read session Boolean isUserAuthenticated = (Boolean) session.getAttribute("authenticated");
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("usertype", usertype);
            responseJsonObject.addProperty("message", "success");
            response.setStatus(200);

        }
        else {
            // Login fail
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "Incorrect Username or password");
            response.setStatus(200);
        }

        response.getWriter().write(responseJsonObject.toString());
        System.out.println(responseJsonObject.toString());
    }

    protected User validateEmployee(String username, String password)
    {
        String role ="";  User userObj;
        Boolean userExists = false;
        String userQuery = "SELECT role FROM employees WHERE username=? AND password =?";

        try
        {
            // Connection connection = dataSource.getConnection();
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection connection = ds.getConnection();
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



    protected User validateUser(String username, String password)
    {
        String id =""; String firstName =""; String lastName =""; User userObj; String pwd;
        Boolean userExists = false;
        Boolean isPasswordValid = false;
        String movieInfoQuery = "SELECT id, firstName, lastName, password FROM customers WHERE email=?";

        try
        {
            // Connection connection = dataSource.getConnection();
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection connection = ds.getConnection();
            PreparedStatement preparedStatemenUser = connection.prepareStatement(movieInfoQuery);
            preparedStatemenUser.setString(1, username);
            //preparedStatemenUser.setString(2, password);
            System.out.println("after="+ preparedStatemenUser.toString() );
            ResultSet userResult = preparedStatemenUser.executeQuery();

            while (userResult.next())
            {
                String encryptedPassword = userResult.getString("password");
                System.out.println("ENCRYPTED PASSWORD?? " + encryptedPassword);
                isPasswordValid = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
                System.out.println("IS PASSWORD VALID?? " + isPasswordValid);
                if(isPasswordValid){
                    userExists = true;
                    id = userResult.getString("id");
                    pwd = userResult.getString("password");
                    firstName = userResult.getString("firstName");
                    lastName = userResult.getString("lastName");
                }
            }
            userResult.close();
            preparedStatemenUser.close();
            connection.close();
            userObj = new User(username, id, firstName, lastName,userExists, "cust");
        }
        catch (Exception ex)
        {
            System.out.println(servletName +" Exception=" + ex.getMessage());
            return null;
        }
        return userObj;
    }
}