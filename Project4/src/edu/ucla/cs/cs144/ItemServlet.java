package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    static class MyErrorHandler implements ErrorHandler {

        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }

        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }

        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }

    }





    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException
    {
        // your codes here

        try {
        String itemID = request.getParameter("itemID");
        String xmlData = AuctionSearch.getXMLDataForItemId(itemID);





        StringReader reader = new StringReader(xmlData);
        InputSource source = new InputSource(reader);
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbfactory.newDocumentBuilder();
        builder.setErrorHandler(new MyErrorHandler());
        Document doc = builder.parse(source);



        Element itemElement =  doc.getDocumentElement();
        String docItemId = itemElement.getAttribute("ItemID");
        String docName = getElementTextByTagNameNR(itemElement, "Name");
        String docCurrently = getElementTextByTagNameNR(itemElement, "Currently");
        String docBuyPrice = getElementTextByTagNameNR(itemElement, "Buy_Price");
        docBuyPrice = docBuyPrice.length() == 0? "N/A" : docBuyPrice;
        String docFirstBid = getElementTextByTagNameNR(itemElement, "First_Bid");
        String docCountry = getElementTextByTagNameNR (itemElement, "Country");
        String docStarted = getElementTextByTagNameNR (itemElement, "Started");
        String docEnds = getElementTextByTagNameNR (itemElement, "Ends");
        String docDescription = getElementTextByTagNameNR (itemElement, "Description");
        String docNumberOfBids = getElementTextByTagNameNR (itemElement, "Number_of_Bids");
        Element[] docCategoryE = getElementsByTagNameNR (itemElement, "Category");
        String docCategory =  getElementText(docCategoryE[0]);
        for (int i = 1; i  < docCategoryE.length; i++) {
            docCategory = docCategory + ", " + getElementText(docCategoryE[i]);
        } 
        Element docSellerE = getElementByTagNameNR(itemElement, "Seller");
        String docSellerID = docSellerE.getAttribute("UserID");
        String docSellerRating = docSellerE.getAttribute("Rating");
        Element docLocationE = getElementByTagNameNR(itemElement, "Location");
        String docLocation = getElementText(docLocationE);
        String docLatitude = docLocationE.getAttribute("Latitude");
  //      docLatitude = docLatitude.length() == 0? "N/A" : docLatitude;
        String docLongitude = docLocationE.getAttribute("Longitude");
  //      docLongitude = docLongitude.length() == 0? "N/A" : docLongitude;

        Element docBids = getElementByTagNameNR(itemElement, "Bids");
        Element[] docBid= getElementsByTagNameNR(docBids, "Bid");
        ArrayList<String> docBidderID = new ArrayList<>();
        ArrayList<String> docBidderRating = new ArrayList<>();
        ArrayList<String> docTime = new ArrayList<>();
        ArrayList<String> docAmount = new ArrayList<>();
        for (Element curr : docBid) {
            Element docBidder = getElementByTagNameNR(curr, "Bidder");
            docBidderID.add(docBidder.getAttribute("UserID"));
            docBidderRating.add(docBidder.getAttribute("Rating"));
            docTime.add(getElementTextByTagNameNR (curr, "Time"));
            docAmount.add(getElementTextByTagNameNR (curr, "Amount"));
        }



        request.setAttribute("docBidderID", docBidderID);
        request.setAttribute("docBidderRating", docBidderRating);
        request.setAttribute("docTime", docTime);
        request.setAttribute("docAmount", docAmount);

        request.setAttribute("docLocation", docLocation);
        request.setAttribute("docNumberOfBids", docNumberOfBids);
        request.setAttribute("docLatitude", docLatitude);
        request.setAttribute("docLongitude", docLongitude);
        request.setAttribute("docSellerRating", docSellerRating);
        request.setAttribute("docSellerID", docSellerID);
        request.setAttribute("docItemId", docItemId);
        request.setAttribute("docName", docName);
        request.setAttribute("docCurrently", docCurrently);
        request.setAttribute("docBuyPrice", docBuyPrice);
        request.setAttribute("docFirstBid", docFirstBid);
        request.setAttribute("docCountry", docCountry);
        request.setAttribute("docStarted", docStarted);
        request.setAttribute("docEnds", docEnds);
        request.setAttribute("docDescription", docDescription);
        request.setAttribute("docCategory", docCategory);

        request.getRequestDispatcher("/indexItem.jsp").forward(request, response);

        }
        catch (SAXException e1 ) {
            System.out.println( "e1");
        }
        catch (ParserConfigurationException e2) {
            System.out.println( "e2");
        }

    }

    //********************************************************************************/

    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }

    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }

    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }

    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
//**********************************************************************************
}
