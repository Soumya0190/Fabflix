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


/*
"totalRecords": "5",
	"recordsPerPage": "25",
	"pgOffset": "0",
	"movieArray": "[
  {\"movie_id\":\"tt0321364\",\"movie_title\":\"Bajar es lo peor\",\"movie_year\":\"2002\",\
"movie_director\":\"Leyla Grunberg\",\"movie_rating\":\"5.3\",\"price\":\"7\",
	\"movie_genres\":\"Drama\",\"movie_stars\":\"[\\\"nm0051822\\\",\\\"Cristina Banegas\\\",\\\"nm0222740\\\",\\\"Danilo Devizzia\\\",\\\"nm1188396\\\",\\\"Santiago Fernández Calvete\\\"]\"},{\"movie_id\":\"tt0401561\",\"movie_title\":\"Krokus: As Long as We Live\",\"movie_year\":\"2004\",\"movie_director\":\"Reto Caduff\",\"movie_rating\":\"7.2\",\"price\":\"14\",\"movie_genres\":\"Documentary, Music\",\"movie_stars\":\"[\\\"nm0128307\\\",\\\"Reto Caduff\\\",\\\"nm0202764\\\",\\\"Christian Davi\\\",\\\"nm0679493\\\",\\\"Daniel Pfisterer\\\"]\"},{\"movie_id\":\"tt0403867\",\"movie_title\":\"Barbecue: A Texas Love Story\",\"movie_year\":\"2004\",\"movie_director\":\"Chris Elley\",\"movie_rating\":\"7.1\",\"price\":\"7\",\"movie_genres\":\"Comedy, Documentary\",\"movie_stars\":\"[\\\"nm0018220\\\",\\\"Art Alexakis\\\",\\\"nm0072621\\\",\\\"Ray Benson\\\",\\\"nm0295273\\\",\\\"Kinky Friedman\\\"]\"},{\"movie_id\":\"tt0436238\",\"movie_title\":\"Dios los cría 2\",\"movie_year\":\"2004\",\"movie_director\":\"Jacobo Morales\",\"movie_rating\":\"7.6\",\"price\":\"9\",\"movie_genres\":\"Drama\",\"movie_stars\":\"[\\\"nm0069134\\\",\\\"Jaime Bello\\\",\\\"nm0259749\\\",\\\"Blanca Silvia Eró\\\",\\\"nm0400258\\\",\\\"Bonita Huffman\\\"]\"},{\"movie_id\":\"tt0448020\",\"movie_title\":\"Los locos también piensan\",\"movie_year\":\"2005\",\"movie_director\":\"Che Castellanos\",\"movie_rating\":\"5.6\",\"price\":\"10\",\"movie_genres\":\"Action, Comedy\",\"movie_stars\":\"[\\\"nm0460192\\\",\\\"Victoria Kluge\\\",\\\"nm0551779\\\",\\\"Luisito Martí\\\",\\\"nm0762631\\\",\\\"PengBian Sang\\\"]\"}]",
	"movieStar": "",
	"movieTitle": "s lo",
	"movieDirector": "",
	"movieYear": "",
	"genreid": null,
	"titleStartsWith": null,
	"usertype": "cust"
 */