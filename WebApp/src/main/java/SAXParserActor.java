package main.java;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
    String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
    //String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

    //@Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;

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
        int totalRecords = 0;
        FileWriter report = new FileWriter("inconsistency_report_actors.txt");
        report.write("Inconsistency Report for mains243.xml :");
        String regex = "\\d+";
        String status="";
        while (it.hasNext()) {
            status = "";
            Actor actorObj = (Actor) it.next();
            Boolean reportIt = false;
            String starName = actorObj.getStagename();
            String starBirthYear = actorObj.getDob();

            if (starName == null || (starName != null && (starName.trim().length() <= 0 ||starName.trim().length()>100)))
            {
                report.write("Bad data for Star Name: " + starName + " | ");
                reportIt = true;
            }
            if (starBirthYear != null && starBirthYear.trim().length() > 0) { //starBirthYear is optional, so can't be blank

                if (starBirthYear.matches(regex) == false) {
                    report.write(" starBirthYear is not numeric: " + starBirthYear);
                    reportIt = true;
                } else if (Integer.parseInt(starBirthYear) < 2020) {
                    reportIt = false;
                }
            }


            if(!reportIt) {
                starBirthYear =  starBirthYear != null? starBirthYear :"";
                status = saveActorInfoinDB(actorObj.getStagename(), actorObj.getDob(), null);
                if (status != null && !status.contains("success")) {
                    report.write(" Error in processing xml record " + status + "\n");
                }

            }else{
                totalRecords++;
                report.write("\n");}

        }
        report.write(" Total Records with Bad Data: "+ totalRecords);
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
        String starQuery ="{CALL addStarMovie(?,?,?,?,?)}";
        String status ="";

        try
        {
            //connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection connection = ds.getConnection();
            if (connection == null)
                System.out.println("connection is null.");

            //connection = DriverManager.getConnection(String.valueOf(dataSource));
            CallableStatement  prepStmt = connection.prepareCall(starQuery);
            prepStmt.setString(1, starName);
            prepStmt.setString(2, starBirthYear);
            prepStmt.setString(3, movieID);
            prepStmt.registerOutParameter(4, java.sql.Types.VARCHAR);
            prepStmt.registerOutParameter(5, java.sql.Types.VARCHAR);
            System.out.println(prepStmt);
            boolean hadResults = prepStmt.execute();
            status  = prepStmt.getString("status");
            System.out.println("status: " + status);
            prepStmt.close();
            connection.close();
            return status;
        }
        catch (SQLException | NamingException e)
        {
            e.printStackTrace();
            System.out.println("SAXParserActor:" +" Exception=" + e.getMessage());
            return "error";
        }
    }
}
