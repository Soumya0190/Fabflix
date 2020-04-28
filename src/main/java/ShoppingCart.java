package main.java;

public class ShoppingCart {
    String movieId;
    String movieTitle;
    int price;
    int quantity;
    String saleId;
    public ShoppingCart(String id, String title, int price, int quantity, String saleId){
        this.movieId = id;
        this.movieTitle = title;
        this.price = price;
        this.quantity = quantity ;
        this.saleId = saleId;
    }
/*
    public ShoppingCart(String id, String title, int price){
        this(id,title, price,1);
    }

 */


}

