package edu.uci.ics.fabflixmobile;

public class MovieItem {
    private String title;
    private String year;
    private String movieid;
    private String director;
    private String rating;
    private String price;
    private String movie_genres;
    private String movie_stars;
    public MovieItem()
    {

    }
    public MovieItem(String title, String year, String movieid, String director, String rating, String price, String movie_genres, String movie_stars) {
        this.title = title;
        this.year = year;
        this.movieid = movieid;
        this.director = director;
        this.rating = rating;
        this.price = price;
        this.movie_genres = movie_genres;
        this.movie_stars = movie_stars;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMovie_genres() {
        return movie_genres;
    }

    public void setMovie_genres(String movie_genres) {
        this.movie_genres = movie_genres;
    }

    public String getMovie_stars() {
        return movie_stars;
    }

    public void setMovie_stars(String movie_stars) {
        this.movie_stars = movie_stars;
    }
}

