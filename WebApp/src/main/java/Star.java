package main.java;

public class Star
{
    private String filmID;

    private String actor;

    private String directorID;

    private String directorName;

    private String genre;

    public Star(){

    }

    public Star(String filmID, String actor, String directorID, String directorName, String genre) {
        this.filmID = filmID;
        this.actor = actor;
        this.directorID  = directorID;
        this.directorName = directorName;
        this.genre = genre;
    }

    public void setFilmID(String movieID) { this.filmID = movieID; }
    public void setActor(String actorName) {
        this.actor = actorName;
    }
    public void setDirectorID(String directorID) {
        this.directorID = directorID;
    }
    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }
    public void setGenre(String category) { this.genre = category; }

    public String getFilmID() { return this.filmID; }
    public String getActor() {
        return this.actor;
    }
    public String getDirectorID() {
        return this.directorID;
    }
    public String getDirectorName() {
        return this.directorName;
    }
    public String getGenre() { return this.genre; }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Star Details - ");
        sb.append("MovieID:" + getFilmID());
        sb.append(", ");
        sb.append("Actor Name:" + getActor());
        sb.append(", ");
        sb.append("DirectorID:" + getDirectorID());
        sb.append(", ");
        sb.append("Director Name:" + getDirectorName());
        sb.append(", ");
        sb.append("Genre:" + getGenre());
        sb.append(".");

        return sb.toString();
    }
}
