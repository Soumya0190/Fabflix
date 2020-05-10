package main.java;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserActor extends DefaultHandler
{
    String loginUser = "mytestuser";
    String loginPasswd = "mypassword";
    String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

    Connection connection;

    List<Actor> actorList;
    private String tempVal;
    private Actor tempActor;

    public SAXParserActor()
    {
        actorList = new ArrayList<Actor>();
    }

    private void parseDocument()
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try
        {
            SAXParser sp = spf.newSAXParser();
            sp.parse("stanford-movies/actors63.xml",this );
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
        if (qName.equalsIgnoreCase("Actor"))
        {
            tempActor = new Actor();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException
    {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (qName.equalsIgnoreCase("Actor"))
        {
            actorList.add(tempActor);
        }
        else if (qName.equalsIgnoreCase("stagename"))
        {
            tempActor.setStagename(tempVal);
        }
        else if (qName.equalsIgnoreCase("dob"))
        {
            tempActor.setDob(tempVal);
        }
    }

    private void printData() throws IOException
    {
        System.out.println("No of Actors '" + actorList.size() + "'.");
        Iterator<Actor> it = actorList.iterator();
        FileWriter report = new FileWriter("inconsistency_report_actors.txt");
        while (it.hasNext())
        {
            Actor actorObj = (Actor)it.next();
            Boolean reportIt = false;
            String starName = actorObj.getStagename();
            String starBirthYear = actorObj.getDob();
            if (starName == null || starName.length() <= 0 )
            {
                starName = "Unspecified".trim();
                reportIt = true;
            }
            if (starBirthYear == null || starBirthYear.length() <= 0)
            {
                starBirthYear = "Unspecified".trim();
                reportIt = true;
            }
            try
            {
                int num = Integer.parseInt(starBirthYear);
                if(num < 2020){reportIt = false;}
            }
            catch (NumberFormatException e)
            {
                reportIt = true;
                starBirthYear = "Unspecified".trim();
            }
            if(reportIt)
            {
                report.write("Star Name: " + starName);
                report.write(" Star Birth Year: " + starBirthYear);
                report.write("Movie ID: MovieID is not found in actors63.xml");
            }
            else if(!reportIt)
            {
                saveActorInfoinDB(actorObj.getStagename() , actorObj.getDob(), null);
            }
        }
        report.close();
    }

    public static void main(String[] args) throws IOException
    {
        SAXParserActor spe = new SAXParserActor();
        spe.runParser();
        spe.printData();
    }

    public void runParser()
    {
        parseDocument();
    }

    public String saveActorInfoinDB(String starName, String starBirthYear, String movieID)
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
