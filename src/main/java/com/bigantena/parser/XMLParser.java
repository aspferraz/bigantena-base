/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author aspferraz
 */
public abstract class XMLParser {
    
    private final Logger logger = Logger.getLogger(XMLParser.class);
    protected final String baseUri = "C:\\Users\\AntonioSergio\\Downloads\\sitemap - radios.com.br";
    
    protected abstract File[] getFiles();
    
    public List<String> getUrlLocList() {
        List<String> result = new ArrayList<>();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            
            for (File file : getFiles()) {
                Document doc = docBuilder.parse(file);

                // normalize text representation
                doc.getDocumentElement().normalize();
                logger.info("Root element of the doc is " + doc.getDocumentElement().getNodeName());

                NodeList urlsNodeList = doc.getElementsByTagName("url");
                int totalUrls = urlsNodeList.getLength();
                logger.info("Total number of urls: " + totalUrls);

                for (int i = 0; i < urlsNodeList.getLength(); i++) {

                    Node urlNode = urlsNodeList.item(i);
                    if (urlNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element urlElement = (Element) urlNode;
                        NodeList locsNodeList = urlElement.getElementsByTagName("loc");
                        Element locElement = (Element) locsNodeList.item(0);

                        NodeList textLocNList = locElement.getChildNodes();
                        String textLoc = ((Node) textLocNList.item(0)).getNodeValue().trim();
//                    logger.info("loc : " + textLoc);

                        result.add(textLoc);
                    }
                }
            }
        } catch (SAXParseException err) {
            logger.error("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId(), err);
        } catch (SAXException e) {
            Exception x = e.getException();
            logger.error(e.getMessage(), ((x == null) ? e : x));
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }

        return result;
    }

}
