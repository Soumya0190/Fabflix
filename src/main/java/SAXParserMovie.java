package main.java;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SAXParserMovie extends DefaultHandler
{
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
            tempMovie.setMovieID(tempVal);
        }
        else if (qName.equalsIgnoreCase("t"))
        {
            tempMovie.setTitle(tempVal);
        }
        else if (qName.equalsIgnoreCase("year"))
        {
            tempMovie.setYear(tempVal);
        }
        else if (qName.equalsIgnoreCase("dir"))
        {
            tempMovie.setDirector(tempVal);
        }
        else if (qName.equalsIgnoreCase("cat"))
        {
            tempMovie.setGenre(tempVal);
        }
    }

    private void printData()
    {
        System.out.println("No of Movies '" + movieList.size() + "'.");
        Iterator<Movie> it = movieList.iterator();
        while (it.hasNext())
        {
            System.out.println(it.next().toString());
        }
    }

    public static void main(String[] args)
    {
        SAXParserMovie spe = new SAXParserMovie();
        spe.runParser();
    }

    public void runParser()
    {
        parseDocument();
        printData();
    }
}

