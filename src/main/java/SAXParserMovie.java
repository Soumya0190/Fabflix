package main.java;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SAXParserMovie extends DefaultHandler
{
    String loginUser = "mytestuser";
    String loginPasswd = "mypassword";
    String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
    Connection connection;

    List<Movie> movieList;
    private String tempVal;
    private Movie tempMovie;
    public SAXParserMovie()
    {
        movieList = new ArrayList<Movie>();
    }
    private void parseDocument()
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try
        {
            SAXParser sp = spf.newSAXParser();
            sp.parse("stanford-movies/mains243.xml",this );
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ie)
        {
            ie.printStackTrace();
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        tempVal = "";
        if (qName.equalsIgnoreCase("film"))
        {
            tempMovie = new Movie();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException
    {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (qName.equalsIgnoreCase("film"))
        {
            movieList.add(tempMovie);
        }
        else if (qName.equalsIgnoreCase("fid"))
        {
            if(tempVal == null){tempVal = "";}
            tempMovie.setMovieID(tempVal);
        }
        else if (qName.equalsIgnoreCase("t"))
        {
            if(tempVal == null){tempVal = "";}
            tempMovie.setTitle(tempVal);
        }
        else if (qName.equalsIgnoreCase("year"))
        {
            if(tempVal == null){tempVal = "";}
            tempMovie.setYear(tempVal);
        }
        else if (qName.equalsIgnoreCase("dirn"))
        {
            if(tempVal == null){tempVal = "";}
            tempMovie.setDirector(tempVal);
        }
        else if (qName.equalsIgnoreCase("cat"))
        {
            System.out.println("THIS IS THE CATEGORY" + tempVal);
            if(tempVal == null){tempVal = "";}
            tempMovie.setGenre(tempVal);
        }
    }

    private void printData() throws SQLException, IOException
    {
        System.out.println("No of Movies '" + movieList.size() + "'.");
        Iterator<Movie> it = movieList.iterator();
        FileWriter report = new FileWriter("inconsistency_report_movies.txt");
        while (it.hasNext())
        {
            Movie movieObj = (Movie)it.next();

            String movieID = movieObj.getMovieID();
            String movieTitle = movieObj.getTitle();
            String movieDirector = movieObj.getDirector();
            String movieReleaseDate = movieObj.getYear();
            String movieGenre = movieObj.getGenre();

            Boolean reportIt = false;
            if(movieID.length() >= 11)
            {
                reportIt = true;
            }
            if (movieTitle == null || movieTitle.length() <= 0 || movieTitle.length() > 100) //100 NOT NULL
            {
                movieTitle = "Unspecified Movie Title".trim();
                reportIt = true;
            }
            if (movieDirector == null || movieDirector.length() <= 0 || movieDirector.length() > 100) //0 - 100 NOT NULL
            {
                movieDirector = "Unspecified Movie Director".trim();
                reportIt = true;
            }
            if (movieReleaseDate == null || movieReleaseDate.length() <= 0 || movieReleaseDate.length() > 4) //4 digit
            {
                movieReleaseDate = "Unspecified Movie Release Date".trim();
                reportIt = true;
            }
            if (movieGenre == null || movieGenre.length() <= 0 || movieGenre.length() > 32) //NOT NULL  0-32
            {
                movieGenre = "Unspecified".trim();
                reportIt = true;
            }
            if(reportIt)
            {
                report.write("Movie ID: " + movieID + "\n");
                report.write(" Movie Name: " + movieTitle + "\n");
                report.write("Movie Director: " + movieDirector + "\n");
                report.write("Movie Release Date: " + movieReleaseDate + "\n");
                report.write("Movie Genre: " + movieGenre + "\n");
            }
            else if(!reportIt)
            {
                saveMovieInfoinDB(movieObj.getMovieID(), movieObj.getTitle(), movieObj.getDirector(), movieObj.getYear(), movieObj.getGenre());
            }
        }
        report.close();
    }

    public static void main(String[] args) throws SQLException, IOException
    {
        SAXParserMovie spe = new SAXParserMovie();
        spe.runParser();
        spe.printData();
    }

    public void runParser()
    {
        parseDocument();
    }

    public String saveMovieInfoinDB(String movie_id, String movieTitle, String director, String year, String genre) throws SQLException
    {
        String movieQuery ="{CALL addMovie(?,?,?,?,?,?,?,?)}";
        String status ="";
        //CALL `moviedb`.`addMovie`(<{IN newMovieID varchar(10)}>, <{IN movieTitle varchar(100)}>, <{IN movieDirector varchar(100)}>,
        // //<{IN movieYear int(11)}>, <{IN genreName varchar(32)}>,
        // //<{IN starName varchar(100)}>, <{IN starByear varchar(10)}>, <{OUT status varchar(100)}>);
        //String regex = "\\d+";
        try
        {
            connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            CallableStatement prepStmt = connection.prepareCall(movieQuery);
            prepStmt.setString(1, movie_id.trim());
            prepStmt.setString(2, movieTitle.trim());
            prepStmt.setString(3, director.trim());
            prepStmt.setString(4, year.trim());
            prepStmt.setString(5, genre.trim());
            prepStmt.setString(6, "");
            prepStmt.setString(7, "");
            prepStmt.registerOutParameter(8, java.sql.Types.VARCHAR);

            System.out.println("PREPARED STATEMENT: " + prepStmt.toString());
            boolean hadResults = prepStmt.execute();
            status  = prepStmt.getString("status");
            System.out.println("status: " + status);
            System.out.println(genre);
            prepStmt.close();
            connection.close();
            return status;
        }
        catch (Exception ex)
        {
            System.out.println("SAXParserActor:" + " Exception=" + ex.getMessage());
            System.out.println(genre);
            return "error";
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }
    }
}

