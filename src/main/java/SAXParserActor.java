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

public class SAXParserActor extends DefaultHandler
{
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

    private void printData()
    {
        System.out.println("No of Actors '" + actorList.size() + "'.");
        Iterator<Actor> it = actorList.iterator();
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

