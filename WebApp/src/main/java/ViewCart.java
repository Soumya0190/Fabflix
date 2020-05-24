
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
        JsonArray jsonArray = new JsonArray();
        String newMovieId = request.getParameter("movieid");
        String newQty = request.getParameter("qty");
        String action = request.getParameter("action");
        try {
        //System.out.println(servletName +" : movieId ="+ newMovieId+", qty="+ newQty);
            ArrayList<ShoppingCart> itemsInCart  = (ArrayList<ShoppingCart>) session.getAttribute("moviesinCart");

            ArrayList<ShoppingCart> newCartList = new ArrayList<ShoppingCart>();
           //System.out.println(servletName+" :" + itemsInCart.size());
            if (itemsInCart == null || itemsInCart.size() <= 0 ) {
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("errorMessage", "No items in the cart");
                response.setStatus(200);
                response.getWriter().write(responseJsonObject.toString());
                System.out.println(servletName+" :" + jsonArray.toString());
            } else if (itemsInCart != null) {
                System.out.println("ShoppingServlet: itemsInCart is not null  " + itemsInCart.size());
                JsonObject jsonObject; Boolean deleted = false; Integer movieQty =0; ShoppingCart newCart = null;
                synchronized (itemsInCart) {
                    for (ShoppingCart cart : itemsInCart) {
                        deleted = false;
                        jsonObject = new JsonObject();
                        jsonObject.addProperty("id", cart.movieId);
                        jsonObject.addProperty("title", cart.movieTitle);
                        jsonObject.addProperty("price", cart.price);
                        movieQty = cart.quantity;
                        if (newMovieId != null && newMovieId.length() > 0) {
                            if (newMovieId.equals(cart.movieId)) {
                                if (newQty != null && newQty.length() >0) {
                                    if (Integer.parseInt(newQty) == 0) {
                                        deleted = true;
                                    } else if (Integer.parseInt(newQty) >= 0) {
                                        movieQty = Integer.parseInt(newQty);
                                        cart.quantity = movieQty;
                                    }
                                }
                            }
                        }
                        if ( deleted == false ) {
                            jsonObject.addProperty("quantity", movieQty);

                            newCart = new ShoppingCart(cart.movieId, cart.movieTitle, cart.price, movieQty, "");
                            jsonArray.add(jsonObject);
                            newCartList.add(newCart);
                        }

                        System.out.println(servletName + " : movieid=" + cart.movieId + "  cart =" + jsonObject.toString());
                    }
                }
                }
                PrintWriter printer = response.getWriter();
                //response.getWriter().write(jsonArray.toString());
                printer.write(jsonArray.toString());
                printer.close();
                session.setAttribute("moviesinCart", newCartList);
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
            System.out.println(servletName +" : " + ex.getMessage());
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

