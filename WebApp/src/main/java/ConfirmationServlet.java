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
    private final String servletName ="ConfirmationServlet";
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("application/json");
        JsonObject responseJsonObject = new JsonObject();
        if (makePayment(request)== false ){

            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "Sorry, Transaction failed");
            response.getWriter().write(responseJsonObject.toString());
            return;
        }
        ArrayList<ShoppingCart> itemsInCart  = (ArrayList<ShoppingCart>) session.getAttribute("moviesinCart");
        JsonArray jsonArray = new JsonArray();
        User userInfo = (User) request.getSession().getAttribute("user");
        // responseJsonObject.addProperty("cardIDNumber", userInfo.get(0).cardNumber);
        //JsonObject responseJsonObject = new JsonObject();
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
            ArrayList<ShoppingCart> newCartList = new ArrayList<ShoppingCart>();
            session.setAttribute("moviesinCart", newCartList);
            //
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
            // Connection connection = dataSource.getConnection();
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection connection = ds.getConnection();
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
    protected Boolean makePayment(HttpServletRequest request)
    {
        Connection connection = null; //dataSource.getConnection();
        Statement preparedStatement =null; String paymentQuery;
        ArrayList<ShoppingCart> itemsInCart = (ArrayList<ShoppingCart>) request.getSession().getAttribute("moviesinCart");
        System.out.println("itemsInCart: "+itemsInCart.toString());
        HttpSession session = request.getSession();
        Integer customer_id = (Integer) session.getAttribute("customerID");
        ArrayList<ShoppingCart> updatedItemsinCart = new ArrayList<ShoppingCart>();
        ShoppingCart updatedItemsObj; //need to add Salesid with it
        String sale_time = getCurrentTime();
        User customer = (User)request.getSession().getAttribute("user");
        customer_id = Integer.parseInt(customer.userId);
        ResultSet rs = null;
        Integer rid;
        try
        {
            //connection = dataSource.getConnection();
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            connection = ds.getConnection();
            if (itemsInCart != null)
            {
                int count = 0;
                synchronized (itemsInCart)
                {
                    for (ShoppingCart cart : itemsInCart)
                    {
                        if(cart != null ) {

                            String sale_movie_id = cart.movieId;

                            paymentQuery = "INSERT INTO sales (customerId, movieId, saleDate) VALUES (?,?,?)";
                          /* paymentQuery = "INSERT INTO sales (customerId, movieId, saleDate) VALUES ('"
                                   + customer_id + "','" + sale_movie_id + "','" + sale_time + "');";*/
                            preparedStatement = connection.prepareStatement(paymentQuery, Statement.RETURN_GENERATED_KEYS);


                            ((PreparedStatement) preparedStatement).setInt(1, customer_id );
                            ((PreparedStatement) preparedStatement).setString(2,sale_movie_id);
                            ((PreparedStatement) preparedStatement).setString(3,sale_time);

                            //  int userResult =preparedStatement.executeUpdate(paymentQuery); //int userResult =
                            ((PreparedStatement) preparedStatement).executeUpdate();

                            rs = preparedStatement.getGeneratedKeys();
                            if (rs != null && rs.next()){
                                rid = rs.getInt(1);
                                System.out.println("confirmation: "+rid);
                                updatedItemsObj = new ShoppingCart(cart.movieId, cart.movieTitle, cart.price, cart.quantity, rid.toString());
                                updatedItemsinCart.add(updatedItemsObj);
                            }
                            //  rs.next();
                            // int auto_id = rs.getInt(1);

/*
                           if(rs.next()) {
                               //java.sql.RowId rid=rs.getRowId(1);
                               //TODO create movieid, sales id (rid);, quanity, price
                               //String id, String title, int price, int quantity, String saleId)
                               System.out.println("confirmation: "+rid);
                               updatedItemsObj = new ShoppingCart(cart.movieId, cart.movieTitle, cart.price, cart.quantity, rid.toString());
                               updatedItemsinCart.add(updatedItemsObj);
                           }
                           */


                        }
                    }
                }
            }
            if (rs != null) rs.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();

            //Update session with items in cart
            session = request.getSession();
            session.setAttribute("moviesinCart", updatedItemsinCart);
        }
        catch (Exception ex)
        {
            System.out.println(servletName +" ERROR : " + ex.getMessage());
            return false;
        }
        return true;
    }

    private String getCurrentTime()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime currentTime = LocalDateTime.now();
        String saleDate = dtf.format(currentTime);
        return saleDate;
    }
}