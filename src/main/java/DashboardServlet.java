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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@WebServlet(name = "DashboardServlet", urlPatterns = "/api/dashboard")
public class DashboardServlet extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    private final String servletName ="DashboardServlet";
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json");
        PrintWriter printer = response.getWriter();
        String jSONResponse = getDBMetaData(request);
        printer.write(jSONResponse);
        printer.close();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        JsonObject responseJsonObject = new JsonObject();
        response.setContentType("application/json");

        String formaction = request.getParameter("formaction");
        String starName = request.getParameter("starName");
        String starDOB = request.getParameter("starDOB");
        User usrObj =null;  String status ="";
        System.out.println("formaction = "+ formaction+", starName = "+starName);
        try {
            if (formaction != null && formaction.equals("addstar")) {
                status = saveActorInfoinDB(request);
            } else if (formaction.equals("addmovie")) {
                status = saveMovieInfoinDB(request);
            }
            responseJsonObject.addProperty("message", status);
            //if (status.equals("success")) responseJsonObject.addProperty("status", "success");
            //else responseJsonObject.addProperty("status", "fail");

            response.getWriter().write(responseJsonObject.toString());
            System.out.println(responseJsonObject.toString());
        }
        catch(Exception ex)
        {
            System.out.println("error in add star ="+ ex.getMessage());
        }
    }

    public String saveActorInfoinDB(HttpServletRequest request ) {
        String movieID="";String regex = "\\d+";
        String starName = request.getParameter("starName");
        String starBirthYear = request.getParameter("starDOB");
        String starQuery ="{CALL addStarMovie(?,?,?,?,?)}"; String status ="";String msg ="";
        try
        {
            if (starBirthYear.length()> 0 && (starBirthYear.matches(regex) == false ) ) starBirthYear ="";
            Connection connection = dataSource.getConnection();
            CallableStatement prepStmt = connection.prepareCall(starQuery);
            prepStmt.setString(1, starName);
            prepStmt.setString(2, starBirthYear);
            prepStmt.setString(3, movieID);
            prepStmt.registerOutParameter(4, java.sql.Types.VARCHAR);
            prepStmt.registerOutParameter(5, java.sql.Types.VARCHAR);
            System.out.println("add star : " + prepStmt.toString());
            boolean hadResults = prepStmt.execute();

            while (hadResults) {
                ResultSet rs = prepStmt.getResultSet();
                System.out.println(rs.toString());
                while (rs.next()) {
                    msg = rs.getString("addedData");
                }
                hadResults = prepStmt.getMoreResults();
            }
            status  = prepStmt.getString("status");
            msg  = prepStmt.getString("addedData");
            prepStmt.close();
            connection.close();

        }
        catch (Exception ex)
        {
            System.out.println("saveActorInfoinDB: Exception=" + ex.getMessage());
            msg =  "Add star '" + starName + "'is not successful";
            return msg;
        }
        return msg;
    }
    public String saveMovieInfoinDB(HttpServletRequest request ) {
        String movieID="";
        String movieTitle = request.getParameter("movieTitle");
        String director = request.getParameter("director");
        String movieYear = request.getParameter("movieYear");
        String genre = request.getParameter("genre");
        String movieStar = request.getParameter("movieStar");
        String starByear = request.getParameter("starByear")!=null? request.getParameter("starByear") :"";
        String regex = "\\d+";


        String query ="{CALL addMovie(?,?,?,?,?,?,?,?)}"; String status ="";String msg="";
        try
        {
            if (starByear.length()> 0 && (starByear.matches(regex) == false ) ) starByear ="";
            Connection connection = dataSource.getConnection();
            CallableStatement prepStmt = connection.prepareCall(query);

            prepStmt.setString(1, null);
            prepStmt.setString(2, movieTitle);
            prepStmt.setString(3, director);
            prepStmt.setString(4, movieYear);
            prepStmt.setString(5, genre);
            prepStmt.setString(6, movieStar);
            prepStmt.setString(7, starByear);
            prepStmt.registerOutParameter(8, java.sql.Types.VARCHAR);
            System.out.println("add movie : " + prepStmt.toString());

            boolean hadResults = prepStmt.execute();
            status  = prepStmt.getString("status");
            while (hadResults) {
                ResultSet rs = prepStmt.getResultSet();
                System.out.println(rs.toString());
                while (rs.next()) {
                    msg = rs.getString("addeddata");
                }
                hadResults = prepStmt.getMoreResults();
            }
            prepStmt.close();
            connection.close();

        }
        catch (Exception ex)
        {
            System.out.println("saveMovieInfoinDB: Exception=" + ex.getMessage());
            msg ="Add movie '"+ movieTitle+"' is not successful";
            return msg;
        }
        return msg;
    }

    public String getDBMetaData(HttpServletRequest request ) {
        String query ="{CALL ShowDBMetaData(?)}"; String status =""; List<String> metaDataLst = new ArrayList<String>();
        JsonArray jsonArray = new JsonArray();
        try
        {
            Connection connection = dataSource.getConnection();
            CallableStatement prepStmt = connection.prepareCall(query);
            prepStmt.setString(1, "moviedb");
            boolean hadResults = prepStmt.execute();

            while (hadResults)
            {
                ResultSet rs = prepStmt.getResultSet();
                System.out.println(rs.toString());
                while (rs.next()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("table_name", rs.getString("table_name"));
                    jsonObject.addProperty("column_name", rs.getString("column_name"));
                    jsonObject.addProperty("column_type", rs.getString("column_type"));
                    jsonObject.addProperty("is_nullable", rs.getString("is_nullable"));
                    jsonObject.addProperty("colkey", rs.getString("colkey"));
                    jsonObject.addProperty("extra", rs.getString("extra"));
                    jsonArray.add(jsonObject);
                }
                // process result set
                hadResults = prepStmt.getMoreResults();
            }
            prepStmt.close();
            connection.close();
            HttpSession session = request.getSession();
            session.setAttribute("databaseMetaData",jsonArray.toString() );

            //String movieLstString = getMovieListFromSession(0, recordsPerPage, session);
            System.out.println("databaseMetaData = " + jsonArray.toString());

        }
        catch (Exception ex)
        {
            System.out.println(servletName + ":"+ ex.getMessage());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", ex.getMessage());
            jsonObject.addProperty("status", "fail");
            return jsonObject.toString();

        }
        return jsonArray.toString();
    }

}