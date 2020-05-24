package main.java;

public class Movie
{
    private String director;

    private String movieID;

    private String title;

    private String year;

    private String genre;

    public Movie(){

    }

    public Movie(String director, String movieID, String title, String year, String genre) {
        this.director = director;
        this.movieID = movieID;
        this.title  = title;
        this.year = year;
        this.genre = genre;
    }

    public void setDirector(String director) { this.director = director; }
    public void setMovieID(String id) {
        this.movieID = id;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public void setGenre(String category) { this.genre = category; }

    public String getDirector() {
        return this.director;
    }
    public String getMovieID() {
        return this.movieID;
    }
    public String getTitle() {
        return this.title;
    }
    public String getYear() {
        return this.year;
    }
    public String getGenre() { return this.genre; }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Movie Details - ");
        sb.append("Name:" + getTitle());
        sb.append(", ");
        sb.append("ID:" + getMovieID());
        sb.append(", ");
        sb.append("Director:" + getDirector());
        sb.append(", ");
        sb.append("Year:" + getYear());
        sb.append(", ");
        sb.append("Genre:" + getGenre());
        sb.append(".");

        return sb.toString();
    }
}
