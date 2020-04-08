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

/*
Code used form Professor Chen Li's project1-api-example
 */
@WebServlet(name = "MovieListServlet", urlPatterns = "/api/movies")
public class MovieListServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");

        PrintWriter printer = response.getWriter();

        try
        {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT movieid, title, year, director, rating " +
                    "FROM ratings, movies " +
                    "WHERE ratings.movieId = movies.id " +
                    "ORDER BY rating DESC " +
                    "LIMIT 20";

            String genreQuery = "SELECT DISTINCT name FROM genres_in_movies, genres " +
                    "WHERE movieId=? AND genreId = id LIMIT 3";
            PreparedStatement preparedStatementGenres = connection.prepareStatement(genreQuery);

            String starsQuery = "SELECT DISTINCT name, id FROM stars_in_movies, stars " +
                    "WHERE movieId=? AND starId = id LIMIT 3";
            PreparedStatement preparedStatementStars = connection.prepareStatement(starsQuery);

            ResultSet result = statement.executeQuery(query);
            JsonArray jsonArray = new JsonArray();

            while (result.next())
            {
                String movie_id = result.getString("movieid");
                String movie_title = result.getString("title");
                String movie_year = result.getString("year");
                String movie_director = result.getString("director");
                String movie_rating = result.getString("rating");
                String genres = "";

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);

                preparedStatementGenres.setString(1, movie_id);

                ResultSet genresResult = preparedStatementGenres.executeQuery();
                while (genresResult.next())
                {
                    if (!genres.equals(""))
                    {
                        genres += ", ";
                    }
                    genres += genresResult.getString("name");
                }
                jsonObject.addProperty("movie_genres", genres);
                genresResult.close();

                preparedStatementStars.setString(1, movie_id);
                ResultSet starsResult = preparedStatementStars.executeQuery();

                JsonArray starsArray = new JsonArray();
                while (starsResult.next())
                {
                    starsArray.add(starsResult.getString("id"));
                    starsArray.add(starsResult.getString("name"));
                }
                jsonObject.addProperty("movie_stars", starsArray.toString());
                starsResult.close();
                jsonArray.add(jsonObject);
            }

            printer.write(jsonArray.toString());
            response.setStatus(200);

            result.close();
            statement.close();
            preparedStatementGenres.close();
            preparedStatementStars.close();
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

