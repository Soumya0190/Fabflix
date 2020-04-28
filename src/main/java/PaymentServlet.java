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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.sql.DataSource;

@WebServlet(name = "Payment", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json");
        PrintWriter printer = response.getWriter();

        String cardNum = request.getParameter("cardNum");
        String cardExpDate = request.getParameter("txtExpDate");
        String customerFirstName = request.getParameter("txtFirstName");
        String customerLastName = request.getParameter("txtLastName");


        JsonObject responseJsonObject = new JsonObject();
        System.out.println("CREATED JSON OBJECT");
        Boolean isCardValid = isCardValid(cardNum, cardExpDate, request);
        //Boolean isPaymentSuccessful = false;
        if (isCardValid)
        {
            ArrayList<Payment> paymentList = new ArrayList<Payment>();
            //Payment payment = new Payment(cardNum, customerFirstName, customerLastName, cardExpDate, customerId);
            //paymentList.add(payment);
            request.getSession().setAttribute("paymentInfo", paymentList);
            //viewCart(request, response);
           // if (makePayment(request)) {
            if (isCardValid){
                System.out.println("PAYMENT SUCCESSFUL");
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "Congratulations " + customerFirstName + " " + customerLastName + "! Your order has been processed");
            } else {
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Sorry, Transaction failed");
            }
        } else {
            String msg = "";
            if (isCardValid == false ) msg = " Card is not valid";
            else msg ="Sorry, Transaction failed";
            System.out.println("NOT SUCCESSFUL PAYMENT");
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", msg);
        }
        printer.write(responseJsonObject.toString());
        response.setStatus(200);
        System.out.println("STRING" + responseJsonObject.toString());
        printer.close();
    }

    protected Boolean viewCart(HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("Payment Servlet View Cart");
        HttpSession session = request.getSession();

        ArrayList<ShoppingCart> itemsInCart  = (ArrayList<ShoppingCart>) session.getAttribute("moviesinCart");
        // insert into sales random
        // get movie id from session
        try
        {
            if (itemsInCart != null && itemsInCart.size() > 0)
            {
                System.out.println("PaymentServlet: itemsInCart is not null  " + itemsInCart.size());
                synchronized (itemsInCart) //itemsInCart = set of items
                {
                    for (ShoppingCart cart : itemsInCart) {
                        System.out.println("CART: movieid" + cart.movieId);
                        System.out.println("CART: movieTitle" + cart.movieTitle);
                        System.out.println("CART: price" + cart.price);
                        System.out.println("CART: quantity" + cart.quantity);
                    }
                    return true;
                       /*
                       JsonObject jsonObject = new JsonObject();
                       if (newMovieId != null && newMovieId.length() > 0)
                       {
                           if (newMovieId == cart.movieId)
                           {
                               newQty = newQty !=null?newQty: "1";
                               jsonObject.addProperty("quantity", newQty);
                           }
                       }
                       jsonObject.addProperty("id", cart.movieId);
                       jsonObject.addProperty("title", cart.movieTitle);
                       jsonObject.addProperty("price", cart.price);
                       jsonObject.addProperty("quantity", newQty != null? newQty :String.valueOf(cart.quantity));
                       jsonObject.addProperty("quantity", newQty != null? newQty.toString():String.valueOf(cart.quantity));
                       System.out.println(servletName+" : movieid=" + cart.movieId + "  cart =" + jsonObject.toString());
                       jsonArray.add(jsonObject);
                        */
                }
            }
        }
        catch (Exception ex)
        {
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("errorMessage", ex.getMessage());
            response.setStatus(500);
            System.out.println("ERROR CAUGHT AN ERROR" + ex);
            return false;
        }
        return true;
    }


    protected Boolean isCardValid(String cardNum, String cardExpDate, HttpServletRequest request)
    {
        int count = 0;
        Boolean isCardValid = false;
        try
        {

            Connection connection = dataSource.getConnection();
            // no need to match customer name as per instructions
            String paymentQuery = "SELECT customer.ccId " +
                    "FROM customers customer, creditcards card " +
                    "WHERE customer.ccId = card.id " +
                    "AND card.expiration =? " +
                    "AND card.id =?;";
            System.out.println("PAYMENT QUERY: " + paymentQuery);

            PreparedStatement preparedStatement = connection.prepareStatement(paymentQuery);
            preparedStatement.setString(1, cardExpDate);
            preparedStatement.setString(2, cardNum);

            System.out.println("PAYMENT PREPARED STATEMENT: " + preparedStatement);

            ResultSet userResult = preparedStatement.executeQuery();
            Integer rowCount = 0;
            if (userResult.last()){
                rowCount= userResult.getRow();
                userResult.beforeFirst(); //point cursor to teh first row  is this needed?
            }
            if (rowCount >0) isCardValid = true;
            preparedStatement.close();
            connection.close();
        }
        catch (Exception ex)
        {
            return false;
        }
        return isCardValid;
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
                connection = dataSource.getConnection();
            if (itemsInCart != null)
            {
                int count = 0;
                synchronized (itemsInCart)
                {
                    for (ShoppingCart cart : itemsInCart)
                    {
                        if(cart != null ) {

                            String sale_movie_id = cart.movieId;


                            paymentQuery = "INSERT INTO sales (customerId, movieId, saleDate) VALUES ('"
                                    + customer_id + "','" + sale_movie_id + "','" + sale_time + "');";
                            preparedStatement = connection.prepareStatement(paymentQuery, Statement.RETURN_GENERATED_KEYS);
                            /*paymentQuery = "INSERT INTO sales (customerId, movieId, saleDate) VALUES (?,?,?)";

                            ((PreparedStatement) preparedStatement).setInt(1, customer_id );
                            ((PreparedStatement) preparedStatement).setString(2,sale_movie_id);
                            ((PreparedStatement) preparedStatement).setString(3,sale_time);
*/
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
            session = request.getSession();
            session.setAttribute("moviesinCart", updatedItemsinCart);
        }
        catch (Exception ex)
        {
            System.out.println("CONFIRMATION ERROR" + ex.getMessage());
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


