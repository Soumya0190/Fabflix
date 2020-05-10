package main.java;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //logout
        JsonObject responseJsonObject = new JsonObject();
        String logout = request.getParameter("logout");

        if (logout == "Y")
        {
            request.getSession().setAttribute("user", null);
            responseJsonObject.addProperty("status", "fail");

            Cookie[] cookies = request.getCookies();
            if(cookies != null)
            {
               for(Cookie cookie : cookies)
               {
                   if(cookie.getName().equals("JSESSIONID"))
                   {
                       System.out.println("JSESSIONID="+cookie.getValue());
                       break;
                   }
               }
            }
            //invalidate the session if exists
            System.out.println("User="+session.getAttribute("user"));
            if(session != null)
            {
                session.invalidate();
            }
            response.sendRedirect("login.html");

        }

        else {
            //Boolean isUserValid = validateUser(username, password);
            Boolean isUserValid = true;
            User usrObj = validateUser(username, password);
            if (usrObj != null && usrObj.isUserValid == true) {
                session = request.getSession(true);
                request.getSession().setAttribute("user", usrObj);
                request.getSession().setAttribute("authenticated", true);//TBD not needed

                //read session Boolean isUserAuthenticated = (Boolean) session.getAttribute("authenticated");
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");

            }
            else {
                // Login fail
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Incorrect Username or password");

            }
        }
        response.getWriter().write(responseJsonObject.toString());
    }

    protected User validateUser(String username, String password)
    {
        String id =""; String firstName =""; String lastName =""; User userObj;
        Boolean userExists = false;
        String movieInfoQuery = "SELECT id, firstName, lastName FROM customers WHERE email=? AND password =?";

        try
        {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatemenUser = connection.prepareStatement(movieInfoQuery);
            preparedStatemenUser.setString(1, username);
            preparedStatemenUser.setString(2, password);

            ResultSet userResult = preparedStatemenUser.executeQuery();

            while (userResult.next())
            {
                id = userResult.getString("id");
                firstName = userResult.getString("firstName");
                lastName = userResult.getString("lastName");
                userExists = true;
            }

            userResult.close();
            preparedStatemenUser.close();
            connection.close();
            userObj = new User(username, id, firstName, lastName,userExists);
        }
        catch (Exception ex)
        {
            return null;
        }
        return userObj;
    }
}
