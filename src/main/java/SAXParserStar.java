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


public class SAXParserStar extends DefaultHandler
{
    String loginUser = "mytestuser";
    String loginPasswd = "mypassword";
    String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
    Connection connection;

    List<Star> starList;
    private String tempVal;
    private Star tempStar;
    public SAXParserStar()
    {
        starList = new ArrayList<Star>();
    }
    private void parseDocument()
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try
        {
            SAXParser sp = spf.newSAXParser();
            sp.parse("stanford-movies/casts124.xml",this );
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
        if (qName.equalsIgnoreCase("m"))
        {
            tempStar = new Star();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException
    {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (qName.equalsIgnoreCase("m"))
        {
            starList.add(tempStar);
        }
        else if (qName.equalsIgnoreCase("f")) //MovieID
        {
            tempStar.setFilmID(tempVal);
        }
        else if (qName.equalsIgnoreCase("a")) //Star Name
        {
            tempStar.setActor(tempVal);
        }
        else if (qName.equalsIgnoreCase("p")) //Genre
        {
            tempStar.setGenre(tempVal);
        }
        else if (qName.equalsIgnoreCase("n")) //Director
        {
            tempStar.setDirectorName(tempVal);
        }
    }

    private void printData() throws IOException
    {
        System.out.println("No of Stars '" + starList.size() + "'.");
        Iterator<Star> it = starList.iterator();

        FileWriter report = new FileWriter("inconsistency_report_stars.txt");
        while (it.hasNext())
        {
            Star starObj = (Star)it.next();
            Boolean reportIt = false;
            String starName = starObj.getActor();
            String starMovieID = starObj.getFilmID();
            if (starName == null || starName.length() <= 0 )
            {
                starName = "Unspecified".trim();
                reportIt = true;
            }
            if (starMovieID.length() > 11 )
            {
                reportIt = true;
            }
            if(reportIt)
            {
                report.write("Star Name: " + starName + "\n");
                report.write(" Star Birth Year: Not Found in casts124.xml" + "\n");
                report.write("Movie ID: " + starMovieID + "\n");
            }
            else if(!reportIt)
            {
                saveStarMovieInfoinDB(starObj.getActor(), "", starObj.getFilmID());
            }
        }
        report.close();
    }



    public static void main(String[] args) throws IOException
    {
        SAXParserStar spe = new SAXParserStar();
        spe.runParser();
    }

    public void runParser() throws IOException
    {
        parseDocument();
        printData();
    }
    //starName varchar(100), IN starByear varchar(10), IN movieID varchar(10), OUT status varchar(100)
    public String saveStarMovieInfoinDB(String starName, String starBirthYear, String movieID)
    {
        String starQuery ="{CALL addStarMovie(?,?,?,?)}";
        String status ="";
        try
        {
            connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            CallableStatement  prepStmt = connection.prepareCall(starQuery);
            prepStmt.setString(1, starName);
            prepStmt.setString(2, starBirthYear);
            prepStmt.setString(3, movieID);
            prepStmt.registerOutParameter(4, java.sql.Types.VARCHAR);
            System.out.println(prepStmt);
            boolean hadResults = prepStmt.execute();
            status  = prepStmt.getString("status");
            System.out.println("status: " + status);
            prepStmt.close();
            connection.close();
            return status;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("SAXParserActor:" +" Exception=" + e.getMessage());
            return "error";
        }
    }

}


