package edu.uci.ics.fabflixmobile;

public class Movie
{
    //public boolean movieArray;
    private String totalRecords;
    private String recordsPerPage;
    private String pgOffset;
    private Movie[] movieArray;
    private String movieStar;
    private String movieTitle;
    private String movieDirector;
    private String movieYear;
    private String genreid;
    private String titleStartsWith;
    private String usertype;

    public Movie(String totalRecords, String recordsPerPage, String pgOffset, Movie[] movieArray) {
        this.totalRecords = totalRecords;
        this.recordsPerPage = recordsPerPage;
        this.pgOffset = pgOffset;
        this.movieArray = movieArray;
    }

    public Movie(String totalRecords, String recordsPerPage, String pgOffset, Movie[] movieArray, String movieStar, String movieTitle, String movieDirector, String movieYear, String genreid, String titleStartsWith, String usertype) {
        this.totalRecords = totalRecords;
        this.recordsPerPage = recordsPerPage;
        this.pgOffset = pgOffset;
        this.movieArray = movieArray;
        this.movieStar = movieStar;
        this.movieTitle = movieTitle;
        this.movieDirector = movieDirector;
        this.movieYear = movieYear;
        this.genreid = genreid;
        this.titleStartsWith = titleStartsWith;
        this.usertype = usertype;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(String recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    public String getPgOffset() {
        return pgOffset;
    }

    public void setPgOffset(String pgOffset) {
        this.pgOffset = pgOffset;
    }

    public Movie[] getMovieArray() {
        return movieArray;
    }

    public void setMovieArray(Movie[] movieArray) {
        this.movieArray = movieArray;
    }

    public String getMovieStar() {
        return movieStar;
    }

    public void setMovieStar(String movieStar) {
        this.movieStar = movieStar;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieDirector() {
        return movieDirector;
    }

    public void setMovieDirector(String movieDirector) {
        this.movieDirector = movieDirector;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public String getGenreid() {
        return genreid;
    }

    public void setGenreid(String genreid) {
        this.genreid = genreid;
    }

    public String getTitleStartsWith() {
        return titleStartsWith;
    }

    public void setTitleStartsWith(String titleStartsWith) {
        this.titleStartsWith = titleStartsWith;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}