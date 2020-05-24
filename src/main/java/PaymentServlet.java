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
    private final String servletName ="PaymentServlet";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json");
        PrintWriter printer = response.getWriter();

        String cardNum = request.getParameter("cardNum");
        String cardExpDate = request.getParameter("txtExpDate");
        String customerFirstName = request.getParameter("txtFirstName");
        String customerLastName = request.getParameter("txtLastName");
        JsonObject responseJsonObject = new JsonObject();

        Boolean isCardValid = isCardValid(cardNum, cardExpDate, request);
        //Boolean isPaymentSuccessful = false;
        if (isCardValid)
        {
            // ArrayList<Payment> paymentList = new ArrayList<Payment>();
            // request.getSession().setAttribute("paymentInfo", paymentList);

            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "Congratulations " + customerFirstName + " " + customerLastName + "! Your order has been processed");
            response.setStatus(200);
        } else {
            String msg = "";
            if (isCardValid == false ) msg = " Card is not valid";
            else msg ="Sorry, Transaction failed";
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", msg);
        }
        printer.write(responseJsonObject.toString());
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
                }
            }
        }
        catch (Exception ex)
        {
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("errorMessage", ex.getMessage());
            response.setStatus(500);
            System.out.println(servletName+ " : " + ex.getMessage());
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
            String paymentQuery = "SELECT customer.ccId FROM customers customer, creditcards card " +
                    "WHERE customer.ccId = card.id AND card.expiration =? AND card.id =?;";

            PreparedStatement preparedStatement = connection.prepareStatement(paymentQuery);
            preparedStatement.setString(1, cardExpDate);
            preparedStatement.setString(2, cardNum);

            //System.out.println("PAYMENT PREPARED STATEMENT: " + preparedStatement);

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
            System.out.println(servletName + " : "+ ex.getMessage());
            return false;
        }
        return isCardValid;
    }
}