package ek.solr;

import java.io.FileReader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class SolrDocParser
{
    public static interface Callback
    {
        public void onDocStart();
        public boolean onDocEnd();
        public void onField(String name, String value);
    }

    ///////////////////////////////////////////////////////////////////////////
    
    private static final QName ATTR_NAME = new QName("name");
    private XMLEventReader reader;
    private Callback cb;

    
    public SolrDocParser(Callback cb) throws Exception
    {
        this.cb = cb;
    }

    
    public void parse(String filePath) throws Exception
    {
        XMLInputFactory fac = XMLInputFactory.newFactory();
        reader = fac.createXMLEventReader(new FileReader(filePath));
        
        while(nextDoc())
        {
        }

        reader.close();
    }

    
    private boolean nextDoc() throws Exception
    {
        if(goToTag(reader, "doc") == false)
        {
            return false;
        }

        cb.onDocStart();

        return parseDoc();
    }

    
    private boolean parseDoc() throws Exception
    {
        while (reader.hasNext())
        {
            XMLEvent event = reader.nextEvent();
            if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("doc"))
            {
                return cb.onDocEnd();
            }

            if (event.isStartElement())
            {
                StartElement el = event.asStartElement();
                String elName = el.getName().getLocalPart();
                if (elName.equals("field"))
                {
                    Attribute attr = el.getAttributeByName(ATTR_NAME);
                    String fieldName = attr.getValue();
                    String fieldValue = reader.getElementText().trim();

                    cb.onField(fieldName, fieldValue);
                }
            }
        }

        return false;
    }

    
    private static boolean goToTag(XMLEventReader reader, String tag) throws Exception
    {
        while (reader.hasNext())
        {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals(tag))
            {
                return true;
            }
        }

        return false;
    }

}
