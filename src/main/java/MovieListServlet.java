package main.java;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "MovieListServlet", urlPatterns = "/api/movies")
public class MovieListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    final int totalRecordsCashed = 100;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        PrintWriter printer = response.getWriter();
        SearchCriteria searchCriteria = new SearchCriteria();
        String jSONResponse ="";

        try {
            searchCriteria.genreid = request.getParameter("genreid");
            searchCriteria.titleStartsWith = request.getParameter("browsetitle");
            searchCriteria.movieTitle = request.getParameter("searchTitle");
            searchCriteria.movieDirector = request.getParameter("searchDirector");
            searchCriteria.movieYear = request.getParameter("searchYear");
            searchCriteria.movieStar = request.getParameter("searchStar");
            String rec = request.getParameter("recordsPerPage");
            searchCriteria.recordsPerPage = rec == null ||rec.length()<0 ? 25 : Integer.parseInt(rec);
            String firstRec = request.getParameter("pgOffset");
            int firstRecord = firstRec == null || firstRec.length() <= 0 ? 0 : Integer.parseInt(firstRec);
            searchCriteria.pageOffset = firstRecord;
            String pageNum = request.getParameter("pageNum");///Still used??
            String pagination = request.getParameter("pagination");

            if (pagination != null && pagination == "Y") { //initiated from pagination
                SearchCriteria srchCriteria = (SearchCriteria) session.getAttribute("searchCriteria");
                int lastRecord = totalRecordsCashed + firstRecord;
                if (firstRecord >= srchCriteria.pageOffset && firstRecord+srchCriteria.recordsPerPage <= lastRecord) {   //Get Data from Session
                    jSONResponse = getMovieListFromSession(firstRecord, searchCriteria.recordsPerPage, session);
                } else {
                    //Get Data from database
                    jSONResponse = getDataFromDatabase(srchCriteria,firstRecord, request, response);
                    System.out.println("srchCriteria="+ srchCriteria.toString());
                }
            } else //get data from database
            {
                jSONResponse = getDataFromDatabase(searchCriteria, firstRecord, request, response);
                printer.write(jSONResponse);
            }
        }
        catch(Exception ex)
        {
            //System.out.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
        }
        printer.close();
    }

    protected String getDataFromDatabase(SearchCriteria searchCriteria, int pgOffset, HttpServletRequest request, HttpServletResponse response){
    String movieStar, movieTitle, movieDirector, movieYear, genreid, titleStartsWith;
    //int pgOffset, recordsPerPage;
        JsonArray jsonArray = new JsonArray();
        genreid = searchCriteria.genreid;
        titleStartsWith = searchCriteria.titleStartsWith;
        movieTitle = searchCriteria.movieTitle;
        movieDirector = searchCriteria.movieDirector;
        movieYear = searchCriteria.movieYear;
        movieStar = searchCriteria.movieStar;// searchCriteria.recordsPerPage = request.getParameter("recordsPerPage");
        pgOffset = searchCriteria.pageOffset;
       // recordsPerPage = searchCriteria.recordsPerPage;
        System.out.println("titleStartsWith=" +titleStartsWith+", movieTitle = "+ movieTitle+", movieDirector="+movieDirector);
        try {
            String commaSeparatedMovieIds = "";
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT r.movieid, m.title, m.year, m.director, r.rating , FLOOR(RAND()*(10)+5) price ";
            query =  query + " FROM ratings r, movies m WHERE r.movieId = m.id ";

            if ((movieStar != null && movieStar.length() > 0) || (genreid != null && genreid.length() > 0)) {
                commaSeparatedMovieIds = getMovieList(movieStar, genreid);
                if (commaSeparatedMovieIds != null && commaSeparatedMovieIds.length() > 0) query = query + " AND m.id in (" + commaSeparatedMovieIds + ") ";
            }
            if (titleStartsWith != null && titleStartsWith.length() > 0) {
                if (titleStartsWith == "spchr") query = query + " AND title substr(title,1,1)  NOT REGEXP '[A-Za-z0-9]'; ";
                else query = query + " AND title like '" + titleStartsWith + "%' ";
            }
            if (movieTitle != null && movieTitle.length() > 0) query = query + " AND title like '%" + movieTitle + "%' ";
            if (movieDirector != null && movieDirector.length() > 0) query = query + " AND director like '%" + movieDirector + "%' ";
            if (movieYear != null && movieYear.length() > 0) query = query + " AND year = " + movieYear;
            String mainQuery = query + " LIMIT "+ String.valueOf(totalRecordsCashed) + " OFFSET "+ String.valueOf(pgOffset) + ";";


           // jsnObject.addProperty("total");
           // query = query1 + query; //Create full query
            System.out.println("MovieListServlet DBquery = " + query);
            String genreQuery = "SELECT DISTINCT name FROM genres_in_movies gm, genres g WHERE gm.movieId=? AND gm.genreId = g.id LIMIT 3";
            PreparedStatement preparedStatementGenres = connection.prepareStatement(genreQuery);

            String starsQuery = "SELECT DISTINCT name, id FROM stars_in_movies, stars WHERE movieId=? AND starId = id LIMIT 3";
            PreparedStatement preparedStatementStars = connection.prepareStatement(starsQuery);

            ResultSet result = statement.executeQuery(mainQuery);
            while (result.next()) {
                String movie_id =result.getString("movieId") ;
                String genres = ""; //result.getString("genre");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", result.getString("title"));
                jsonObject.addProperty("movie_year", result.getString("year"));
                jsonObject.addProperty("movie_director", result.getString("director"));
                jsonObject.addProperty("movie_rating", result.getString("rating"));
                jsonObject.addProperty("price", result.getString("price"));
                //jsonObject.addProperty("genre", _genres);
                preparedStatementGenres.setString(1, movie_id);
                ResultSet genresResult = preparedStatementGenres.executeQuery();
                while (genresResult.next()) {
                    if (!genres.equals("")) genres += ", ";
                    genres += genresResult.getString("name");
                }
                jsonObject.addProperty("movie_genres", genres);
                genresResult.close();

                preparedStatementStars.setString(1, movie_id);
                ResultSet starsResult = preparedStatementStars.executeQuery();

                JsonArray starsArray = new JsonArray();
                while (starsResult.next()) {
                    starsArray.add(starsResult.getString("id"));
                    starsArray.add(starsResult.getString("name"));
                }
                jsonObject.addProperty("movie_stars", starsArray.toString());
                starsResult.close();
                jsonArray.add(jsonObject);
            }
            result.close();
            statement.close();
            preparedStatementGenres.close();
            preparedStatementStars.close();
            connection.close();

            //Store in session
            String numOfRecords = getTotalRecords(query);
            if (numOfRecords != null && (Integer.parseInt(numOfRecords) <0 ))
            {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("errorMessage", "No Data Found");
                jsonObject.addProperty("totalRecords", 0);
                jsonObject.addProperty("status", "fail");
                return jsonObject.toString();
            }
            JsonObject jsnObject = new JsonObject();
            jsnObject.addProperty("totalRecords",numOfRecords);
            jsnObject.addProperty("recordsPerPage",String.valueOf(searchCriteria.recordsPerPage));
            jsnObject.addProperty("pgOffset", String.valueOf(searchCriteria.pageOffset));
            jsnObject.addProperty("movieArray", jsonArray.toString());
            jsnObject.addProperty("movieStar", movieStar);
            jsnObject.addProperty("movieTitle", movieTitle);
            jsnObject.addProperty("movieDirector", movieDirector);
            jsnObject.addProperty("movieYear", movieYear);
            jsnObject.addProperty("genreid", genreid);
            jsnObject.addProperty("titleStartsWith", titleStartsWith);

            HttpSession session = request.getSession();
           session.setAttribute("moviesData", jsnObject);
         //   session.setAttribute("moviesData", jsonArray);
            session.setAttribute("searchCriteria", searchCriteria);

            //String movieLstString = getMovieListFromSession(0, recordsPerPage, session);
            System.out.println("MovieListServlet = " + jsnObject.toString());
            //response.setStatus(200);
            return jsnObject.toString();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", ex.getMessage());
            jsonObject.addProperty("totalRecords", 0);
            jsonObject.addProperty("status", "fail");
            return jsonObject.toString();
            //response.setStatus(500);
        }
    }

    //Get data from session
    protected String getMovieListFromSession(Integer startPg, Integer recPerPg, HttpSession session) {
        JsonObject jsonObj = (JsonObject) session.getAttribute("moviesData");
        SearchCriteria srchCriteria = (SearchCriteria) session.getAttribute("searchCriteria");
        JsonObject newJSonObj = new JsonObject();
        newJSonObj.addProperty("totalRecords",jsonObj.get("totalRecords").toString());
        newJSonObj.addProperty("recordsPerPage", recPerPg.toString());
        newJSonObj.addProperty("pgOffset", startPg.toString());
        newJSonObj.addProperty("movieArray", jsonObj.get("movieArray").toString());
        newJSonObj.addProperty("movieStar", jsonObj.get("movieStar").toString());
        newJSonObj.addProperty("movieTitle", jsonObj.get("movieTitle").toString());
        newJSonObj.addProperty("movieDirector", jsonObj.get("movieDirector").toString());
        newJSonObj.addProperty("movieYear", jsonObj.get("movieYear").toString());
        newJSonObj.addProperty("genreid", jsonObj.get("genreid").toString());
        newJSonObj.addProperty("titleStartsWith", jsonObj.get("titleStartsWith").toString());
        session.setAttribute("moviesData", newJSonObj.toString());

        SearchCriteria newSrchCriteria = new SearchCriteria();
        newSrchCriteria.movieDirector = srchCriteria.movieDirector;
        newSrchCriteria.genreid = srchCriteria.genreid;
        newSrchCriteria.pageOffset = startPg;
        newSrchCriteria.recordsPerPage = recPerPg;
        newSrchCriteria.movieStar = srchCriteria.movieStar;
        newSrchCriteria.movieYear = srchCriteria.movieYear;
        newSrchCriteria.titleStartsWith = srchCriteria.titleStartsWith;
        newSrchCriteria.movieTitle = srchCriteria.movieTitle;
        session.setAttribute("searchCriteria", newSrchCriteria);

       /* JsonArray newJSONarray = new JsonArray();
        JsonObject newJsonObj = new JsonObject();
        String arr=(jsonObj.get("movieArray")).getAsJsonPrimitive().toString();
        int totalRecords = pgRecord + startPg;
        if (jsonObj.isJsonPrimitive()) {
            newJsonObj.addProperty("recordsPerPage", String.valueOf(jsonObj.get("recordsPerPage")));
            newJsonObj.addProperty("pgOffset", String.valueOf(jsonObj.get("pgOffset")));
            newJsonObj.addProperty("totalRecords", String.valueOf(jsonObj.get("totalRecords")));
         }
        try {
            if (jsonObj.getAsJsonArray("movieArray").isJsonArray()) {
                JsonArray jArr = jsonObj.getAsJsonArray("movieArray");
            }
        }
        catch( Exception ex)
        {
            System.out.println(ex.getMessage());
            return jsonObj.toString();
        }
        /*
        //Create small subset of json array ...only for number of  records needed on one page
       // JsonArray jsnArray = (JsonArray)jsonObj.get("movieArray");
        String movieArray = jsonObj.get("movieArray").toString();
        JsonArray array = gson.fromJson(lang, JsonArray.class);
        JsonArray jsnArray = new JsonArray(movieArray);
        JsonElement je = (JsonElement)jsonObj.get("movieArray");
        if (je.isJsonArray())
        for (int i = startPg; i <= totalRecords; i++) {
            jObj = (JsonObject) jsnArray.get(i);
            newJSONarray.add(jObj);
        }
        //create main json object and add jsonarray to it
        JsonObject newJsonObj = new JsonObject();
        newJsonObj.addProperty("recordsPerPage", String.valueOf(jsonObj.get("recordsPerPage")));
        newJsonObj.addProperty("pgOffset", String.valueOf(jsonObj.get("pgOffset")));
        newJsonObj.addProperty("totalRecords", String.valueOf(jsonObj.get("totalRecords")));
        newJsonObj.addProperty("movieArray", newJSONarray.toString());

        System.out.println("MovieList startPg =" + (String.valueOf(startPg)));
        System.out.println("MovieList pgRecord =" + (String.valueOf(pgRecord)));
        System.out.println("MovieList totalRecords =" + String.valueOf(jsonObj.get("totalRecords")));
        /*JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("totalRecords", (String.valueOf(newJSONarray.size())));
        newJSONarray.add(jsonObject);*/
        return newJSonObj.toString();
    }

    protected String getMovieList(String starName, String genreID) {
        List<String> movieLst = new ArrayList<String>();
        String query = "";
        String movieList = "";
        try {
            Connection connection = dataSource.getConnection();
            if (starName != null && starName.length() > 0) {
                query = "SELECT  movieId FROM stars_in_movies sm, stars s WHERE starId = id AND s.name like '%" + starName + "%'; ";
                Statement statement = connection.createStatement();
                ResultSet movieResult = null;
                movieResult = statement.executeQuery(query);
                while (movieResult.next()) {
                    movieLst.add(movieResult.getString("movieId"));
                    if (movieList.length() <= 0) movieList = "'" + movieResult.getString("movieId") + "'";
                    else movieList += ",'" + movieResult.getString("movieId") + "'";
                }
                movieResult.close();
                statement.close();
            } else if (genreID != null && genreID.length() > 0) {
                query = "SELECT movieId FROM genres_in_movies, genres WHERE genreId = id AND genreId=? "; // LIMIT ?";
                PreparedStatement preparedStatementGenre = connection.prepareStatement(query);
                preparedStatementGenre.setString(1, genreID);
                ResultSet movieResult = preparedStatementGenre.executeQuery();

                while (movieResult.next()) {
                    movieLst.add(movieResult.getString("movieId"));
                    if (movieList.length() <= 0) movieList = "'" + movieResult.getString("movieId") + "'";
                    else movieList += ",'" + movieResult.getString("movieId") + "'";
                }
                movieResult.close();
                preparedStatementGenre.close();
            }
            connection.close();
        } catch (Exception ex) {
            return null;
        }
        String moviesCommaSeparated = String.join(",", movieLst);
        return movieList;
    }
    protected String getTotalRecords(String query)
    {
        Integer rowCount = 0;
        try
        {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatemenUser = connection.prepareStatement(query+";");
            ResultSet userResult = preparedStatemenUser.executeQuery();
            if (userResult.last()){
                rowCount= userResult.getRow();
                userResult.beforeFirst(); //point cursor to teh first row  is this needed?
            }
            userResult.close();
            preparedStatemenUser.close();
            connection.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            return "0";
        }
        return rowCount.toString();
    }
    class SearchCriteria {
        String movieStar;
        String movieTitle;
        String movieDirector;
        String movieYear;
        String genreid;
        String titleStartsWith;
        int recordsPerPage;
        int pageOffset;
    }

}

