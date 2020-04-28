
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ViewCartServlet", urlPatterns = "/api/viewcart")
public class ViewCart extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    final String servletName ="ViewCartServlet";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        //String newMovieId = request.getParameter("id")
        //read data from http session object and store in array list "?movieId="+ movieid +"&qty="+ qty;
        String newMovieId = request.getParameter("movieId");
        String newQty = request.getParameter("qty");

        ArrayList<ShoppingCart> itemsInCart  = (ArrayList<ShoppingCart>) session.getAttribute("moviesinCart");
        JsonArray jsonArray = new JsonArray();
        try {
            System.out.println(servletName+" :" + itemsInCart.size());
            if (itemsInCart == null || itemsInCart.size() <= 0 ) {
                JsonObject responseJsonObject = new JsonObject();
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
                        if (newMovieId != null && newMovieId.length() > 0) {
                            if (newMovieId.equals(cart.movieId)) {
                                if (Integer.parseInt(newQty) != 0) {
                                    jsonObject.addProperty("quantity", newQty);
                                    jsonArray.add(jsonObject);
                                }
                            }
                        } else {
                            jsonObject.addProperty("quantity", cart.quantity);
                            jsonArray.add(jsonObject);
                        }
                        System.out.println(servletName + " : movieid=" + cart.movieId + "  cart =" + jsonObject.toString());
                    }
                }
                }
                PrintWriter printer = response.getWriter();
                //response.getWriter().write(jsonArray.toString());
                printer.write(jsonArray.toString());
                printer.close();
                System.out.println(servletName +" : " + jsonArray.toString());
        }
        catch (Exception ex)
        {
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("errorMessage", ex.getMessage());
            responseJsonObject.addProperty("status", "fail");
            response.setStatus(500);
            response.getWriter().write(responseJsonObject.toString());
            System.out.println(servletName +" : " + jsonArray.toString());
        }
  }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       /* HttpSession session = request.getSession();
        String newMovieId = request.getParameter("movieId");
        String newQty = request.getParameter("qty");
        //read data from http session object and store in array list
        ArrayList<ShoppingCart> itemsInCart  = (ArrayList<ShoppingCart>) session.getAttribute("moviesinCart");
        JsonArray jsonArray = new JsonArray();
        try {
            if (itemsInCart == null || itemsInCart.size() <= 0 ) {
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("errorMessage", "No items in the cart");
                responseJsonObject.addProperty("status", "fail");
                response.setStatus(200);
                response.getWriter().write(responseJsonObject.toString());
                System.out.println(servletName+" :" + jsonArray.toString());
            } else if (itemsInCart != null) {
                System.out.println("ShoppingServlet: itemsInCart is not null  " + itemsInCart.size());
                synchronized (itemsInCart) {
                    for (ShoppingCart cart : itemsInCart) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("id", cart.movieId);
                        jsonObject.addProperty("title", cart.movieTitle);
                        jsonObject.addProperty("price", cart.price);
                        jsonObject.addProperty("quantity", cart.quantity);
                        System.out.println(servletName+" : movieid=" + cart.movieId + "  cart =" + jsonObject.toString());
                        jsonArray.add(jsonObject);
                    }
                }
                PrintWriter printer = response.getWriter();
                //response.getWriter().write(jsonArray.toString());
                printer.write(jsonArray.toString());
                printer.close();
                System.out.println(servletName +" : " + jsonArray.toString());
            }
        }
        catch (Exception ex)
        {
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("errorMessage", ex.getMessage());
            responseJsonObject.addProperty("status", "fail");
            response.setStatus(500);
            response.getWriter().write(responseJsonObject.toString());
            System.out.println(servletName +" : " + jsonArray.toString());
        }

        */
    }
}

