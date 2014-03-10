package com.MobShop.app;

/**
 * Created by Segarceanu Calin on 3/10/14.
 */
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLDOMParser {
    //Returns the entire XML document
    public Document getDocument(InputStream inputStream) {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(inputStream);
            document = db.parse(inputSource);
        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        return document;
    }


    public String getValue(Element item, String name) {
        NodeList nodes = item.getElementsByTagName(name);
        return this.getTextNodeValue(nodes.item(0));
    }

    private final String getTextNodeValue(Node node) {
        Node child;
        if (node != null) {
            if (node.hasChildNodes()) {
                child = node.getFirstChild();
                while(child != null) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                    child = child.getNextSibling();
                }
            }
        }
        return "";
    }
}
