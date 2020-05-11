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
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private final String servletName ="SingleMovieServlet";
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");

        String movieID = request.getParameter("id");

        PrintWriter printer = response.getWriter();
        JsonObject movie = new JsonObject();
        User userInfo = (User) request.getSession().getAttribute("user");
        if (userInfo!=null) {
            movie.addProperty("usertype", userInfo.role);
        }
        try
        {
            Connection connection = dataSource.getConnection();

            String movieInfoQuery = "SELECT title, year, director FROM movies WHERE id=?";
            PreparedStatement preparedStatementMovie = connection.prepareStatement(movieInfoQuery);

            String starMovieQuery = "SELECT id, name FROM stars_in_movies, stars WHERE id = starId AND movieId=?";
            PreparedStatement preparedStatementStars = connection.prepareStatement(starMovieQuery);

            String genreMovieQuery = "SELECT name FROM genres, genres_in_movies WHERE genreId = id AND movieId=?";
            PreparedStatement preparedStatementGenres = connection.prepareStatement(genreMovieQuery);

            String movieRatingQuery = "SELECT rating FROM ratings WHERE movieId=?";
            PreparedStatement preparedStatementRatings = connection.prepareStatement(movieRatingQuery);


            preparedStatementMovie.setString(1, movieID);
            preparedStatementStars.setString(1, movieID);
            preparedStatementGenres.setString(1, movieID);
            preparedStatementRatings.setString(1, movieID);

            ResultSet movieResult = preparedStatementMovie.executeQuery();
            ResultSet genreResult = preparedStatementGenres.executeQuery();
            ResultSet starResult = preparedStatementStars.executeQuery();
            ResultSet ratingResult = preparedStatementRatings.executeQuery();



            JsonArray stars = new JsonArray();
            JsonArray genres = new JsonArray();

            while (movieResult.next())
            {
                String title = movieResult.getString("title");
                String year = movieResult.getString("year");
                String director = movieResult.getString("director");

                movie.addProperty("title", title);
                movie.addProperty("year", year);
                movie.addProperty("director", director);
            }

            while (starResult.next())
            {
                String starID = starResult.getString("id");
                String starName = starResult.getString("name");

                stars.add(starID);
                stars.add(starName);
            }

            movie.addProperty("stars", stars.toString());

            while (genreResult.next())
            {
                String genre = genreResult.getString("name");
                genres.add(genre);
            }

            movie.addProperty("genres", genres.toString());

            while (ratingResult.next())
            {
                String rating = ratingResult.getString("rating");
                movie.addProperty("rating", rating);
            }

            printer.write(movie.toString());
            response.setStatus(200);

            movieResult.close();
            genreResult.close();
            starResult.close();
            ratingResult.close();

            preparedStatementMovie.close();
            preparedStatementStars.close();
            preparedStatementGenres.close();
            preparedStatementRatings.close();

            connection.close();
        }
        catch (Exception ex)
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage","Unknown Data Retrieval Error");
            System.out.println(servletName + " : "+ ex.getMessage());
            printer.write(jsonObject.toString());
            response.setStatus(500);
        }
        printer.close();
    }
}