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


public class SAXParserStar extends DefaultHandler
{
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
        else if (qName.equalsIgnoreCase("f"))
        {
            tempStar.setFilmID(tempVal);
        }
        else if (qName.equalsIgnoreCase("a"))
        {
            tempStar.setDirectorName(tempVal);
        }
        else if (qName.equalsIgnoreCase("p"))
        {
            tempStar.setGenre(tempVal);
        }
        else if (qName.equalsIgnoreCase("n"))
        {
            tempStar.setDirectorName(tempVal);
        }
        else if (qName.equalsIgnoreCase("dirid"))
        {
            tempStar.setDirectorID(tempVal);
        }
    }

    private void printData()
    {
        System.out.println("No of Stars '" + starList.size() + "'.");
        Iterator<Star> it = starList.iterator();
        while (it.hasNext())
        {
            System.out.println(it.next().toString());
        }
    }

    public static void main(String[] args)
    {
        SAXParserActor spe = new SAXParserActor();
        spe.runParser();
    }

    public void runParser()
    {
        parseDocument();
        printData();
    }
}

