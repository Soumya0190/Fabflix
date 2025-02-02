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
import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

@WebServlet(name = "MovieListNoConnPoolServlet", urlPatterns = "/api/moviesNoConPool")
public class MovieListNoConnPoolServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String servletName ="MovieListServlet";
    @Resource(name = "jdbc/moviedbNoConPool")
    private DataSource dataSource;
    final int totalRecordsCashed = 100;
    long elapsedTimeMainQuery;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.nanoTime(); //Start recording time for servlet
        // response.setContentType("application/json");
        HttpSession session = request.getSession();
        PrintWriter printer = response.getWriter();
        SearchCriteria searchCriteria = new SearchCriteria();
        String jSONResponse ="";

        try {
            searchCriteria.genreid = request.getParameter("genreid");
            searchCriteria.titleStartsWith = request.getParameter("browsetitle");
            if (request.getParameter("ftMovieTitle") != null && request.getParameter("ftMovieTitle").length()>0)
                searchCriteria.ftMovieTitle = request.getParameter("ftMovieTitle");
            if (request.getParameter("searchTitle") != null && request.getParameter("searchTitle").length()>0)
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
           // System.out.println(" searchCriteria.ftMovieTitle ="+  searchCriteria.ftMovieTitle );
            if (pagination != null && pagination == "Y") { //initiated from pagination
                SearchCriteria srchCriteria = (SearchCriteria) session.getAttribute("searchCriteria");
                int lastRecord = totalRecordsCashed + firstRecord;
                if (firstRecord >= srchCriteria.pageOffset && firstRecord+srchCriteria.recordsPerPage <= lastRecord) {   //Get Data from Session
                    jSONResponse = getMovieListFromSession(firstRecord, searchCriteria.recordsPerPage, session);
                } else {
                    //Get Data from database
                    jSONResponse = getDataFromDatabase(srchCriteria,firstRecord, request, response);
                  //  System.out.println("srchCriteria="+ srchCriteria.toString());
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
            System.out.println(servletName + ":"+ ex.getMessage());
        }
        printer.close();
        //End recording Time
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        // long totalJDBCExecTime = elapsedTimeMainQuery + elapsedTimeStarQuery + elapsedTimeGenreQuery;elapsedTimeMainQuery includes all jdbc execution time
        writeToFile(elapsedTime,elapsedTimeMainQuery);
    }
    private static final String newLine = System.getProperty("line.separator");

    private synchronized void writeToFile(long elapsedTime, long totalJDBCExecTime)  {
        String fileName = "log_processing_noConnectionPool.txt";
        //String fileName = "/Users/pratyushsharma/Documents/122B/logfiles/log_processing.txt";

        PrintWriter printWriter = null;
        String msg="SearchServletTotalExecutionTime:"+ elapsedTime + ""+",JDBCExecutionTime:" + totalJDBCExecTime;
        File file = new File(fileName);
        try {
            if (!file.exists()) file.createNewFile();
            printWriter = new PrintWriter(new FileOutputStream(fileName, true));
            printWriter.write(newLine + msg);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        PrintWriter printer = response.getWriter();
        SearchCriteria searchCriteria = new SearchCriteria();
        String jSONResponse ="";

        try {
            searchCriteria.genreid = request.getParameter("genreid");
            searchCriteria.titleStartsWith = request.getParameter("browsetitle");
            if (request.getParameter("ftMovieTitle") != null && request.getParameter("ftMovieTitle").length()>0)
                searchCriteria.ftMovieTitle = request.getParameter("ftMovieTitle");
            if (request.getParameter("searchTitle") != null && request.getParameter("searchTitle").length()>0)
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
           // System.out.println(" searchCriteria.ftMovieTitle ="+  searchCriteria.ftMovieTitle );
            if (pagination != null && pagination == "Y") { //initiated from pagination
                SearchCriteria srchCriteria = (SearchCriteria) session.getAttribute("searchCriteria");
                int lastRecord = totalRecordsCashed + firstRecord;
                if (firstRecord >= srchCriteria.pageOffset && firstRecord+srchCriteria.recordsPerPage <= lastRecord) {   //Get Data from Session
                    jSONResponse = getMovieListFromSession(firstRecord, searchCriteria.recordsPerPage, session);
                } else {
                    //Get Data from database
                    jSONResponse = getDataFromDatabase(srchCriteria,firstRecord, request, response);
                 //   System.out.println("srchCriteria="+ srchCriteria.toString());
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
            System.out.println(servletName + ":"+ ex.getMessage());
        }
        printer.close();
    }

    protected String getDataFromDatabase(SearchCriteria searchCriteria, int pgOffset, HttpServletRequest request, HttpServletResponse response){
        String movieStar, movieTitle, movieDirector, movieYear, genreid, titleStartsWith, ftMovieTitle;
        //int pgOffset, recordsPerPage;
        JsonArray jsonArray = new JsonArray();
        String[] arrParams;
        genreid = searchCriteria.genreid;
        titleStartsWith = searchCriteria.titleStartsWith;
        movieTitle = searchCriteria.movieTitle;
        movieDirector = searchCriteria.movieDirector;
        movieYear = searchCriteria.movieYear;
        movieStar = searchCriteria.movieStar;// searchCriteria.recordsPerPage = request.getParameter("recordsPerPage");
        pgOffset = searchCriteria.pageOffset;
        ftMovieTitle = searchCriteria.ftMovieTitle;
        Integer intParams = 0;
        Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>(); //Store search criteria and set paramaters in prepared stmt

        // recordsPerPage = searchCriteria.recordsPerPage;
        //  System.out.println("titleStartsWith=" +titleStartsWith+", movieTitle = "+ movieTitle+", movieDirector="+movieDirector);
        try {
            String commaSeparatedMovieIds = "";


            PreparedStatement preparedStatementMainQuery;

            String query = "SELECT m.id movieId, m.title, m.year, m.director, r.rating , FLOOR(RAND()*(10)+5) price ";
            query =  query + " FROM ratings r RIGHT JOIN movies m ON r.movieId = m.id where 1=1 ";

            if ((movieStar != null && movieStar.length() > 0) || (genreid != null && genreid.length() > 0)) {
                commaSeparatedMovieIds = getMovieList(movieStar, genreid);
                if (commaSeparatedMovieIds != null && commaSeparatedMovieIds.length() > 0) query = query + " AND m.id in (" + commaSeparatedMovieIds + ") ";
            }
            if (titleStartsWith != null && titleStartsWith.length() > 0) {
                if (titleStartsWith.equals("spchr") ) query = query + " AND substring(title,1,1) NOT REGEXP '[A-Za-z0-9]' ";
                    //else query = query + " AND title like '" + titleStartsWith + "%' ";
                else
                {
                    query = query + " AND title like ? ";
                    intParams++; hashtable.put(intParams,titleStartsWith + "%" );
                }
            }
            if (movieTitle != null && movieTitle.length() > 0) {
                query = query + " AND title like ?";
                intParams++;
                hashtable.put(intParams, "%" + movieTitle + "%");
            }
            if (ftMovieTitle != null && ftMovieTitle.length() >0) {
                query = query + " AND MATCH (title) AGAINST (? IN BOOLEAN MODE) ";
                intParams++;
                String[] splited = ftMovieTitle.split("\\s+");
                String temp ="";
                for (int i = 0; i < splited.length; i++)
                    temp = temp + "+"+ splited[i] + "* ";
                hashtable.put(intParams, temp);
            }

            if (movieDirector != null && movieDirector.length() > 0) {
                query = query + " AND director like ? "; intParams++;hashtable.put(intParams,"%" + movieDirector + "%" );
            }
            if (movieYear != null && movieYear.length() > 0) {
                query = query + " AND year = ? " ; intParams++;hashtable.put(intParams,movieYear);
            }
            String mainQuery = query + " LIMIT ? OFFSET ?;";
            //hashtable.put(intParams,String.valueOf(totalRecordsCashed)); intParams++;
            //hashtable.put(intParams,String.valueOf(pgOffset)); intParams++;

            //System.out.println("MovieListServlet DBquery = " + query);
            Enumeration names = hashtable.keys(); Integer key;
            String genreQuery = "SELECT DISTINCT name FROM genres_in_movies gm, genres g WHERE gm.movieId=? AND gm.genreId = g.id LIMIT 3";
            String starsQuery = "SELECT DISTINCT name, id FROM stars_in_movies, stars WHERE movieId=? AND starId = id LIMIT 3";
            long startTimeMainQuery = System.nanoTime();

            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection connection = ds.getConnection();
          //  Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatementGenres = connection.prepareStatement(genreQuery);


            PreparedStatement preparedStatementStars = connection.prepareStatement(starsQuery);


            preparedStatementMainQuery = connection.prepareStatement(mainQuery);
            while(names.hasMoreElements()) {
                key = (Integer) names.nextElement();
                preparedStatementMainQuery.setString(key, hashtable.get(key));
            }
            key = intParams+ 1;

            preparedStatementMainQuery.setInt(key, totalRecordsCashed);
            key = intParams+ 2;

            preparedStatementMainQuery.setInt(key, pgOffset);
           // System.out.println("After : " + preparedStatementMainQuery.toString());

            ResultSet result = preparedStatementMainQuery.executeQuery();
            //ResultSet result = statement.executeQuery(mainQuery);
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
            //System.out.println(servletName +"  After : " + preparedStatementMainQuery.toString());
            preparedStatementMainQuery.close();
            // statement.close();
            preparedStatementGenres.close();
            preparedStatementStars.close();
            connection.close();
            long endTimeMainquery = System.nanoTime();
            elapsedTimeMainQuery = endTimeMainquery - startTimeMainQuery;
            //Store in session
            String numOfRecords = getTotalRecords(query, hashtable);
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
            User userInfo = (User) request.getSession().getAttribute("user");
            if (userInfo!=null) {
                jsnObject.addProperty("usertype", userInfo.role);
            }

            response.setStatus(200);
            return jsnObject.toString();
        } catch (Exception ex) {
            System.out.println(servletName + ":"+ ex.getStackTrace().toString());
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
        User userInfo = (User) session.getAttribute("user");
        if (userInfo!=null) {
            newJSonObj.addProperty("usertype", userInfo.role);
        }
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
        return newJSonObj.toString();
    }

    protected String getMovieList(String starName, String genreID) {
        List<String> movieLst = new ArrayList<String>();
        String query = "";
        String movieList = "";
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection connection = ds.getConnection();

          //  Connection connection = dataSource.getConnection();
            if (starName != null && starName.length() > 0) {
                ResultSet movieResult = null;
                //query = "SELECT  movieId FROM stars_in_movies sm, stars s WHERE starId = id AND s.name like '%" + starName + "%'; ";
                query = "SELECT  movieId FROM stars_in_movies sm, stars s WHERE starId = id AND s.name like ?; ";
                query = "SELECT movieId FROM stars_in_movies sm, stars s WHERE starId = id AND match(s.name) against (? in boolean mode); ";
                // query = "SELECT  movieId FROM stars_in_movies sm, stars s WHERE starId = id AND s.name like ?;
                // Statement statement = connection.createStatement();
                // movieResult = statement.executeQuery(query);
                PreparedStatement preparedStatementStars = connection.prepareStatement(query);
                preparedStatementStars.setString(1, starName);
                movieResult = preparedStatementStars.executeQuery();

                while (movieResult.next()) {
                    movieLst.add(movieResult.getString("movieId"));
                    if (movieList.length() <= 0) movieList = "'" + movieResult.getString("movieId") + "'";
                    else movieList += ",'" + movieResult.getString("movieId") + "'";
                }
                movieResult.close();
                preparedStatementStars.close();
            } else if (genreID != null && genreID.length() > 0) {
                query = "SELECT movieId FROM genres_in_movies, genres WHERE genreId = id AND genreId=? ";
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
            System.out.println(servletName+" : " + ex.getMessage());
            return null;
        }
        String moviesCommaSeparated = String.join(",", movieLst);
        return movieList;
    }
    protected String getTotalRecords(String query, Hashtable<Integer, String> hashtable)
    {
        Integer rowCount = 0;
        try
        {
            Enumeration names = hashtable.keys(); Integer key;
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection connection = ds.getConnection();
           // Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatemenUser = connection.prepareStatement(query+";");
            while(names.hasMoreElements()) {
                key = (Integer) names.nextElement();
                preparedStatemenUser.setString(key, hashtable.get(key));
            }
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
            System.out.println("Error in total records =" +ex.getMessage());
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
        String ftMovieTitle;
    }

}
