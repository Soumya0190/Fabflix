package main.java;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.sql.DataSource;

@WebServlet(name = "Confirmation", urlPatterns = "/api/confirmation")
public class ConfirmationServlet extends HttpServlet
{
    final String servletName ="ConfirmationServlet";
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        ArrayList<ShoppingCart> itemsInCart  = (ArrayList<ShoppingCart>) session.getAttribute("moviesinCart");
        JsonArray jsonArray = new JsonArray();
        User userInfo = (User) request.getSession().getAttribute("user");
        // responseJsonObject.addProperty("cardIDNumber", userInfo.get(0).cardNumber);
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("cardFirstName", userInfo.firstName);
        responseJsonObject.addProperty("cardLastName", userInfo.lastName);
        try {
            System.out.println(servletName+" :" + itemsInCart.size());
            if (itemsInCart == null || itemsInCart.size() <= 0 ) {
                responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("errorMessage", "No items in the cart");
                response.setStatus(200);
                response.getWriter().write(responseJsonObject.toString());
                System.out.println(servletName+" :" + jsonArray.toString());
            } else if (itemsInCart != null) {
                System.out.println("ShoppingServlet: itemsInCart is not null  " + itemsInCart.size());
                JsonObject jsonObject;


                synchronized (itemsInCart) {
                    for (ShoppingCart cart : itemsInCart) {
                        jsonObject = new JsonObject();
                        jsonObject.addProperty("id", cart.movieId);
                        jsonObject.addProperty("title", cart.movieTitle);
                        jsonObject.addProperty("price", cart.price);
                        jsonObject.addProperty("quantity", cart.quantity);
                        jsonObject.addProperty("saleId", cart.saleId);
                        jsonArray.add(jsonObject);

                        System.out.println(servletName + " : movieid=" + cart.movieId + "  cart =" + jsonObject.toString());
                    }
                    responseJsonObject.addProperty("allMovies", jsonArray.toString());
                }
            }
            PrintWriter printer = response.getWriter();
            printer.write(responseJsonObject.toString());
            printer.close();
            System.out.println(servletName +" : " + responseJsonObject.toString());
        }
        catch (Exception ex)
        {
            responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("errorMessage", ex.getMessage());
            responseJsonObject.addProperty("status", "fail");
            response.setStatus(500);
            response.getWriter().write(responseJsonObject.toString());
            System.out.println(servletName +" : " + jsonArray.toString());
        }
    }

    //Get Sales id list as couldn;t retrived while inserting record
    protected User getSalesId(String username, String password)
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


