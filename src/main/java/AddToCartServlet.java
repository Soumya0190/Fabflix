package main.java;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;


@WebServlet(name = "ShoppingServlet", urlPatterns = "/api/addtocart")
public class AddToCartServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    final String servletName ="ViewCartServlet";
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String newMovieId = request.getParameter("id");
        newMovieId = newMovieId.trim();
        String newMovieTitle = request.getParameter("title");
        String newMoviePrice = request.getParameter("price");

        ArrayList<ShoppingCart> itemsInCart = (ArrayList<ShoppingCart>) session.getAttribute("moviesinCart");
        ArrayList<ShoppingCart> newCartList = new ArrayList<ShoppingCart>();
Boolean isExistingMovie = false;
        try {

            if (itemsInCart != null ) {
                synchronized (itemsInCart) {
                    int i = 0;
                    ShoppingCart newItem = null;
                    for (ShoppingCart cart : itemsInCart) {
                        System.out.println(servletName + " : cart= " + cart.toString());
                        i = cart.quantity;
                        System.out.println("cart.movieId=" + cart.movieId + ", newmovieId=" + newMovieId);
                        newMovieId = newMovieId.trim();
                        if (newMovieId.equals(cart.movieId.trim())) {
                            System.out.println(servletName + " : equal= " + cart.movieId + "==" + newMovieId);
                            i++;
                            isExistingMovie = true;

                            //cart.quantity = cart.quantity + 1;
                        }
                        newItem = new ShoppingCart(cart.movieId, cart.movieTitle, cart.price, i, "");
                        newCartList.add(newItem);

                        System.out.println(servletName + " : new item = " + (newItem.toString()));
                        System.out.println(servletName + " : newCartList = " + (newCartList.toString()));
                    }
                }
            }
            if (isExistingMovie == false) {
                ShoppingCart cart = new ShoppingCart(newMovieId, newMovieTitle, Integer.parseInt(newMoviePrice),1, "");
                newCartList.add(cart);
            }

//Store cart data in the session with attribute moviesinCart
         //   session.setAttribute("moviesinCart", newCartList);
            session.setAttribute("moviesinCart", newCartList);
            ArrayList<ShoppingCart> itemstmoInCart  = (ArrayList<ShoppingCart>) session.getAttribute("moviesinCart");
            System.out.println(servletName+" : newCartList = "+ itemstmoInCart.toString());

            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "Movie '"+ newMovieTitle + "' is added to cart");
            responseJsonObject.addProperty("totalItems", newCartList.size());
            response.getWriter().write(responseJsonObject.toString());
            System.out.println(servletName + " : " + responseJsonObject.toString());
        }
        catch (Exception ex)
        {
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("errorMessage", ex.getMessage());
            response.setStatus(500);
            response.getWriter().write(responseJsonObject.toString());
            System.out.println(responseJsonObject.toString());
        }

    }
}
