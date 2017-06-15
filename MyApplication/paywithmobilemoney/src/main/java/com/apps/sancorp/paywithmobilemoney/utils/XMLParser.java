package com.apps.sancorp.paywithmobilemoney.utils;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by norrisboateng on 12/29/16.
 */

public class XMLParser {

    public static String getInvoiceNumber(String response) throws ParserConfigurationException, IOException, SAXException {
        String element;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;
        builder = factory.newDocumentBuilder();
        is = new InputSource(new StringReader(response));
        Document doc = builder.parse(is);
        NodeList list = doc.getElementsByTagName(Constants.INVOICE_NUMBER);
        element = list.item(0).getTextContent();
        return element;
    }

    public static String transactionStatus(String response){
        String element="";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;
        try {
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(response));
            Document doc = builder.parse(is);
            NodeList list = doc.getElementsByTagName(Constants.RESPONSE_CODE);
            element = list.item(0).getTextContent();
        } catch (ParserConfigurationException | SAXException | IOException ignored) {
        } catch (Exception e){
            Log.v(Constants.LOG_TAG,"get is transaction completed failure "+e.toString());
            e.printStackTrace();
        }

        return element;
    }

}
