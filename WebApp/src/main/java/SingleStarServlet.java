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
import javax.xml.transform.Result;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/*
Code used form Professor Chen Li's project1-api-example
*/
@WebServlet(name = "SingleStarServlet", urlPatterns = "/api/stars")
public class SingleStarServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private final String servletName ="SingleStarServlet";
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");

        String starID = request.getParameter("id");

        PrintWriter printer = response.getWriter();

        JsonObject star = new JsonObject();
        User userInfo = (User) request.getSession().getAttribute("user");
        if (userInfo!=null) {
            star.addProperty("usertype", userInfo.role);
        }

        try
        {
            Connection connection = dataSource.getConnection();

            String starInfoQuery = "SELECT name, birthYear FROM stars WHERE id=?";
            PreparedStatement preparedStatementStar = connection.prepareStatement(starInfoQuery);

            String starMovieQuery = "SELECT id, title FROM stars_in_movies, movies " +
                    "WHERE id = movieId AND starId=?";
            PreparedStatement preparedStatementMovies = connection.prepareStatement(starMovieQuery);

            preparedStatementStar.setString(1, starID);
            preparedStatementMovies.setString(1, starID);

            ResultSet starResult = preparedStatementStar.executeQuery();
            ResultSet movieResult = preparedStatementMovies.executeQuery();


            JsonArray movies = new JsonArray();

            while (starResult.next())
            {
                String name = starResult.getString("name");
                String year = starResult.getString("birthYear");

                star.addProperty("name", name);
                star.addProperty("birth", year);
            }

            while (movieResult.next())
            {
                String movieID = movieResult.getString("id");
                String movieTitle = movieResult.getString("title");

                movies.add(movieID);
                movies.add(movieTitle);
            }

            star.addProperty("movies", movies.toString());

            printer.write(star.toString());
            response.setStatus(200);

            starResult.close();
            movieResult.close();
            preparedStatementMovies.close();
            preparedStatementStar.close();
            connection.close();
        }
        catch (Exception ex)
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", ex.getMessage());
            printer.write(jsonObject.toString());
            response.setStatus(500);
        }
        printer.close();
    }
}