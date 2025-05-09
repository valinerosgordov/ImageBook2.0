package ru.imagebook.server.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtils {
	private static final String EMPTY = "";
	
    public static Document parseXml(byte[] content)
            throws ParserConfigurationException, SAXException, IOException {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();            
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(new ByteArrayInputStream(content));
    }
    
	public static String getTagValue(String sTag, Element eElement) {
		NodeList list = eElement.getElementsByTagName(sTag);
		if (list != null && list.getLength() > 0) {
			NodeList subList = list.item(0).getChildNodes();
			if (subList != null && subList.getLength() > 0) {
				return subList.item(0).getNodeValue();
			} else {
				return EMPTY;
			}
		}

		return null;
	}    
}
