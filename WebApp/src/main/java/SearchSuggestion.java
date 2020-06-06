package main.java;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

// server endpoint URL
@WebServlet("/search-suggestion")
public class SearchSuggestion extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    private static final long serialVersionUID = 1L;

    /*
     * populate the Super hero hash map.
     * Key is hero ID. Value is hero name.
     */
    public static HashMap<Integer, String> superHeroMap = new HashMap<>();
    public HashMap<String, String> moviemMap = new HashMap<>();
    public HashMap<String, String> starMap = new HashMap<>();


    public SearchSuggestion() {
        super();
    }

    /*
     *
     * Match the query against superheroes and return a JSON response.
     *
     * For example, if the query is "super":
     * The JSON response look like this:
     * [
     *     { "value": "Superman", "data": { "heroID": 101 } },
     *     { "value": "Supergirl", "data": { "heroID": 113 } }
     * ]
     *
     * The format is like this because it can be directly used by the
     *   JSON auto complete library this example is using. So that you don't have to convert the format.
     *
     * The response contains a list of suggestions.
     * In each suggestion object, the "value" is the item string shown in the dropdown list,
     *   the "data" object can contain any additional information.
     *
     *
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JsonArray jsonArray = new JsonArray();
            String query = request.getParameter("query");
            String action = request.getParameter("action");
            //String starName = request.getParameter("searchStar");
            // return the empty json array if query is null or empty
            if (query == null || (query != null && query.trim().isEmpty())) {//&& (action == null || (action !=null && action.trim().isEmpty()))){
                response.getWriter().write(jsonArray.toString());
                return;
            }

            // search on superheroes and add the results to JSON Array
            // this example only does a substring match
            // TODO: in project 4, you should do full text search with MySQL to find the matches on movies and stars
            //if (action.equals("searchmTitle"))
            jsonArray = checkinHashMap ( query,action, moviemMap,  jsonArray );
            // else
            //    jsonArray = checkinHashMap ( query,action, starMap,  jsonArray );


            response.getWriter().write(jsonArray.toString());
            return;
        } catch (Exception e) {
            System.out.println(e);
            response.sendError(500, e.getMessage());
        }
    }


    private JsonArray checkinHashMap (String query, String action, HashMap<String, String> hMap, JsonArray jsonArray ){
        for (String id : hMap.keySet()) {
            String name = hMap.get(id);
            if (name.toLowerCase().contains(query.toLowerCase())) {
                jsonArray.add(generateJsonObject(id, name));
            }
        }
        if (jsonArray.size() <= 0) {
            //  if (action.equals("searchmTitle")) {
            jsonArray = getMovieList(query);
         /*  }
           else
               jsonArray = getStarList(query, jsonArray);
*/
        }
        return jsonArray;
    }
    protected JsonArray  getMovieList(String searchStr  )
    {
        JsonArray jsonArray = new JsonArray();
        try {

            //Connection connection = dataSource.getConnection();
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection connection = ds.getConnection();
            String query =" Select id, title from movies where MATCH (title) AGAINST (? IN BOOLEAN MODE);";
            String id; String name;

            PreparedStatement preparedStatemenUser = connection.prepareStatement(query);
            searchStr = queryParam(searchStr);
            preparedStatemenUser.setString(1, searchStr);
            ResultSet userResult = preparedStatemenUser.executeQuery();
            while (userResult.next())
            {
                id = userResult.getString("id"); name = userResult.getString("title");
                moviemMap.put(id ,name );
                jsonArray.add(generateJsonObject(id, name));
            }
            System.out.println("After  vvv: " + preparedStatemenUser.toString());
            userResult.close(); preparedStatemenUser.close(); connection.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error in total records =" +ex.getStackTrace());
            jsonArray.add(generateJsonObject("error", ex.getMessage()));
            // return jsonArray;
        }
        return jsonArray;
    }

    protected JsonArray  getStarList(String searchStr,  JsonArray jsonArray )
    {
        try {
            // Connection connection = dataSource.getConnection(); String id; String name;
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection connection = ds.getConnection();
            String query =" Select id, name from stars where MATCH (name) AGAINST (? IN BOOLEAN MODE);";
            PreparedStatement preparedStatemenUser = connection.prepareStatement(query);
            searchStr = queryParam(searchStr);
            preparedStatemenUser.setString(1, searchStr);
            ResultSet userResult = preparedStatemenUser.executeQuery();
            while (userResult.next()) {
                id = userResult.getString("id"); name = userResult.getString("name");
                starMap.put(id ,name );
                jsonArray.add(generateJsonObject(id, name));
            }
            userResult.close(); preparedStatemenUser.close(); connection.close();
        }
        catch (Exception ex) {
            System.out.println("Error in total records =" +ex.getMessage());
            return null;
        }
        return jsonArray;
    }

    private static JsonObject generateJsonObject(String ID, String Name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", Name);

        JsonObject additionalDataJsonObject = new JsonObject();
        additionalDataJsonObject.addProperty("ID", ID);

        jsonObject.add("data", additionalDataJsonObject);
        return jsonObject;
    }

    private String queryParam(String query){
        String[] splited = query.split("\\s+");
        query = "";
        for (int i=0; i< splited.length ; i++){
            query = query + "+" + splited[i] + "* ";
        }
        return query;
    }

}