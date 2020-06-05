package main.java;

import jdk.nashorn.internal.codegen.CompilerConstants;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
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


public class SAXParserStar extends DefaultHandler {
    String loginUser = "mytestuser";
    String loginPasswd = "mypassword";
    String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
    //String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
    Connection connection;

    List<Star> starList;
    private String tempVal;
    private Star tempStar;

    public SAXParserStar() {
        starList = new ArrayList<Star>();
    }

    private void parseDocument() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser sp = spf.newSAXParser();
            sp.parse("stanford-movies/casts124.xml", this);
        } catch (SAXException se) {
            se.getMessage();
        } catch (ParserConfigurationException pce) {
            pce.getMessage();
        } catch (IOException ie) {
            ie.getMessage();
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tempVal = "";
        if (qName.equalsIgnoreCase("m")) {
            tempStar = new Star();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("m")) {
            starList.add(tempStar);
        } else if (qName.equalsIgnoreCase("f")) //MovieID
        {
            tempStar.setFilmID(tempVal);
        } else if (qName.equalsIgnoreCase("a")) //Star Name
        {
            tempStar.setActor(tempVal);
        } else if (qName.equalsIgnoreCase("p")) //Genre
        {
            tempStar.setGenre(tempVal);
        } else if (qName.equalsIgnoreCase("n")) //Director
        {
            tempStar.setDirectorName(tempVal);
        }
    }

    public String saveStarMovieInfoinDBMYVERSION(String starName, String starBirthYear, String movieID) {
        String starQuery = "{CALL addStarMovie(?,?,?,?,?)}";
        String status = "";
        try {
            //connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection connection = ds.getConnection();
            if (connection == null)
                System.out.println("connection is null.");

            CallableStatement prepStmt = connection.prepareCall(starQuery);
            prepStmt.setString(1, starName);
            prepStmt.setString(2, starBirthYear);
            prepStmt.setString(3, movieID);
            prepStmt.registerOutParameter(4, java.sql.Types.VARCHAR);
            prepStmt.registerOutParameter(5, java.sql.Types.VARCHAR);
            System.out.println(prepStmt);
            boolean hadResults = prepStmt.execute();
            status = prepStmt.getString("status");
            System.out.println("status: " + status);
            prepStmt.close();
            connection.close();
            return status;
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            System.out.println("SAXParserActor:" + " Exception=" + e.getMessage());
            return "error";
        }
    }

    private void printData() throws IOException, SQLException, NamingException {
        int batchCount = 0;
        boolean badData;
        int totalRecords = 0;
        int[] result = null;
        String status = "";

        System.out.println("No of Stars '" + starList.size() + "'.");
        Iterator<Star> it = starList.iterator();

        FileWriter report = new FileWriter("inconsistency_report_stars.txt");
        report.write("Inconsistency report for casts124.xml :");

        //connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource ds = (DataSource) envContext.lookup("jdbc/moviedbexample");
        Connection connection = ds.getConnection();
        if (connection == null)
            System.out.println("connection is null.");

        connection.setAutoCommit(false);
        String starQuery = "{CALL addStarInBatch(?,?)}";
        CallableStatement prepStmt = connection.prepareCall(starQuery);

        while (it.hasNext()) //Iterate All Stars in StarList
        {
            Star starObj = (Star) it.next(); //Star Object
            String starName = starObj.getActor(); //Get Star Actor
            String starMovieID = starObj.getFilmID(); //Get Film ID
            badData = checkStar(starName, starMovieID, report);
            totalRecords++;


            if (!badData) {
                batchCount = batchCount + 1;
                try
                {
                    if (batchCount <=100 ) //|| !it.hasNext())
                    {
                        prepStmt.setString(1, starName);
                        prepStmt.setString(2, starMovieID);
                        prepStmt.addBatch();
                    }
                    else {
                        result = prepStmt.executeBatch();
                        connection.commit();
                        batchCount =0;
                    }

                }
                catch (SQLException e) {
                    // e.printStackTrace();
                    report.write("starName = "+starName+", starMovieID="+ starMovieID+", "+ e.getMessage() +"\n");
                }

            }
        }
        prepStmt.close();
        connection.close();
        report.write(" Total Records with Bad Data: " + totalRecords);
        report.close();
    }

    public static void main(String[] args) throws IOException, SQLException, NamingException {
        SAXParserStar spe = new SAXParserStar();
        spe.runParser();
    }

    public void runParser() throws IOException, SQLException, NamingException {
        parseDocument();
        printData();
    }

    private boolean checkStar(String starName, String starMovieID, FileWriter report) {
        Boolean reportIt = false; String temp ="";

        if (starName == null || (starName != null && starName.trim().length() <= 0 )) {
            reportIt = true;
            temp = "Blank or null data for Star Name: " + starName + " | ";
        }
        else if (starName != null && starName.trim().length() >100 ){
            reportIt = true;
            temp = " starName "+ starName + " too long for star table";
        }
        if (starMovieID == null || (starMovieID != null && (starMovieID.trim().length() <= 0 ) ))
        { //|| starMovieID.trim().length() > 10))) {
            reportIt = true;
            temp = temp + "Blank or null data for Star Name: " + starMovieID + " | ";
        }
        else if (starMovieID != null && starMovieID.trim().length() >100 ){
            reportIt = true;
            temp = temp + " starMovieID "+ starMovieID + " too long for Movie table";
        }
        try {
            report.write(temp+ "\n");
        } catch (IOException e) {
            e.getMessage();
        }

        return reportIt;
    }
}