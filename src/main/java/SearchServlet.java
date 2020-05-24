package main.java;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpSession;

/*
Code used form Professor Chen Li's project1-api-example
*/
@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private final String servletName ="SearchServlet";
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //Check if user is authenticated
        //HttpSession session = request.getSession(true);
        // Boolean isUserAuthenticated = (Boolean) session.getAttribute("authenticated");

     /*  if (isUserAuthenticated == false) {
           JsonObject jsonObject = new JsonObject();
           jsonObject.addProperty("status", "fail");
           jsonObject.addProperty("errorMessage", "Login Failed");
       }*/
        response.setContentType("application/json");
        PrintWriter printer = response.getWriter();
      /* String searchScrData = (String) request.getSession().getAttribute("searchScrData");
       if (searchScrData.length() > 0 ){
           System.out.println("get genre from session "+searchScrData.length() );
           printer.write(searchScrData);
           response.setStatus(200);
       }*/
        //else {
        HttpSession session = request.getSession();
        JsonObject jsonTObject = new JsonObject();
        User userInfo = (User) request.getSession().getAttribute("user");
        if (userInfo!=null) {
            jsonTObject.addProperty("usertype", userInfo.role);
        }
        try {
            Connection connection = dataSource.getConnection();
            // Statement statement = connection.createStatement();
            String query = "select id, name from genres ORDER BY name;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            JsonArray jsonArray = new JsonArray();

            while (result.next()) {
                String genreId = result.getString("id");
                String name = result.getString("name");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("genreID", genreId);
                jsonObject.addProperty("name", name);
                jsonArray.add(jsonObject);
            }
            jsonTObject.addProperty("genre", jsonArray.toString());
            printer.write(jsonTObject.toString());
            // printer.write(jsonArray.toString());
            response.setStatus(200);

            result.close();
            preparedStatement.close();
            connection.close();
            request.getSession().setAttribute("searchScrData", jsonTObject.toString());
        } catch (Exception ex) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "fail");
            jsonObject.addProperty("errorMessage", ex.getMessage());
            printer.write(jsonObject.toString());
            response.setStatus(500);

        }
        printer.close();


        //  }
    }
}