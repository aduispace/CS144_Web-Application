package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.*;
import javax.xml.parsers.ParserConfigurationException;

import java.io.StringWriter;


public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */


	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!
		ArrayList<SearchResult> result = new ArrayList<>();
		try{
            if (numResultsToReturn <= 0) {
                System.out.println("Basic search result found: 0" );
                return result.toArray(new SearchResult[0]);
            }
            if (numResultsToSkip < 0 ) {
                numResultsToSkip = 0;
            }
            System.out.println("performSearch");
    		SearchEngine se = new SearchEngine();
    		TopDocs topdocs =se.performSearch(query,numResultsToReturn+numResultsToSkip);	
    		ScoreDoc[] hits = topdocs.scoreDocs;
            System.out.println("Basic search result found: " + topdocs.totalHits);
    		for (int i = numResultsToSkip; i < hits.length; i++) {
                Document doc = se.getDocument(hits[i].doc);
                SearchResult newSR = new SearchResult(doc.get("ItemID"), doc.get("Name"));
                result.add(newSR);
            }
        }
        catch (IOException|ParseException e) {
            System.out.println("Exception in BasicSearch.\n");
            e.printStackTrace();
        }
        	System.out.println("BasicSearch done");
            return result.toArray(new SearchResult[0]);
        }


	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		ArrayList<SearchResult> result = new ArrayList<SearchResult>();
		try{

			SearchResult[] basicSearchResult = basicSearch(query, 0, Integer.MAX_VALUE);
			Connection conn = null;
            conn = DbManager.getConnection(true);
            Statement stmt = conn.createStatement();
            String s = "" + region.getLx()+  " " + region.getLy() + ", " + region.getRx() + " " + region.getLy() + ", " + region.getRx() + " " + region.getRy() + ", " + region.getLx() + " " + region.getRy() + ", " + region.getLx() + " " + region.getLy();
            ResultSet rs = stmt.executeQuery("SELECT ItemID FROM ItemLocation WHERE MBRContains(GeomFromText ('Polygon((" + s + "))'), Location)");
            HashSet<String> hashSet = new HashSet<String> (); 
            while (rs.next())
            {
                String theItemID = rs.getString("ItemID");
                hashSet.add (theItemID);
            }
            rs.close();
            conn.close();
            for (int i = 0; i < basicSearchResult.length; i++)
            {
                String itemID_basic = basicSearchResult[i].getItemId();
                if (hashSet.contains(itemID_basic))
                {
                    result.add(basicSearchResult[i]);
                }
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
        if (result.size() < numResultsToSkip)
                return new SearchResult[0];
            else if (numResultsToReturn>result.size()-numResultsToSkip){
                return (result.subList(numResultsToSkip, result.size())).toArray(new SearchResult[0]);
            }
            else  {
                return (result.subList(numResultsToSkip, numResultsToSkip+numResultsToReturn)).toArray(new SearchResult[0]);
            }
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
        String xmlData = "";
        Connection conn = null;
        try{
            conn = DbManager.getConnection(true);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Item WHERE ItemID ="+ itemId);
            if (!rs.first()) return xmlData;
            else
            {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.newDocument();
                String name = rs.getString("Name");
                String sellerID = rs.getString("Seller");
                String currently = rs.getString("Currently");
                String buy_price = rs.getString("Buy_price");
                String first_bid = rs.getString("First_bid");
                String location = rs.getString("location");
                String latitude = rs.getString("Latitude");
                String longitude = rs.getString("Longitude");
                String country = rs.getString("Country");
                String started = rs.getString("Started");
                String ended = rs.getString("Ends");
                String description = rs.getString("Description");
                SimpleDateFormat date_in_format = new SimpleDateFormat ("YYYY-MM-dd HH:mm:ss");
                SimpleDateFormat date_out_format = new SimpleDateFormat ("MMM-dd-yy HH:mm:ss");
                started = timestamp(started);
                ended = timestamp(ended);

                //Seller 
                ResultSet sellerRS = stmt.executeQuery("SELECT * FROM Seller WHERE UserID ='" + sellerID + "'");
                sellerRS.first();
                String rating = sellerRS.getString("Rating");
                //category 
                ResultSet categoryRS = stmt.executeQuery("SELECT Category FROM Category WHERE ItemID = "+itemId);
                ArrayList<String> category = new ArrayList<String>();
                while (categoryRS.next()){
                    category.add(categoryRS.getString("Category"));
                }
                //Bid 
                ResultSet bidCountRS = stmt.executeQuery("SELECT COUNT(*) FROM Bid WHERE ItemID=" +itemId);
                bidCountRS.first();
                String numberofbids = bidCountRS.getString("COUNT(*)");
                


                //reconstruct DTD tree
                Element root = doc.createElement("Item");
                root.setAttribute("ItemID", itemId);
                doc.appendChild(root);

                Element nameE = doc.createElement("Name");
                nameE.appendChild(doc.createTextNode(name));
                root.appendChild(nameE);

                Element sellerE = doc.createElement("Seller");
                sellerE.setAttribute("UserID", sellerID);
                sellerE.setAttribute("Rating", rating);
                root.appendChild(sellerE);

                Element currentlyE = doc.createElement("Currently");
                currentlyE.appendChild(doc.createTextNode(currently));
                root.appendChild(currentlyE);
                Element buyPriceE = doc.createElement("Buy_Price");
                buyPriceE.appendChild(doc.createTextNode(buy_price));
                root.appendChild(buyPriceE);
                Element firstBidE = doc.createElement("First_Bid");
                firstBidE.appendChild(doc.createTextNode(first_bid));
                root.appendChild(firstBidE);
                Element numberOfBidsE = doc.createElement("Number_of_Bids");
                numberOfBidsE.appendChild(doc.createTextNode(numberofbids));
                root.appendChild(numberOfBidsE);

                Element locationE = doc.createElement("location");
                if (!latitude.equals(0))
                    locationE.setAttribute("Latitude", latitude);
                if (!longitude.equals(0))
                    locationE.setAttribute("Longitude", longitude);
                root.appendChild (locationE);

                Element countryE = doc.createElement("Country");
                countryE.appendChild(doc.createTextNode(country));
                root.appendChild(countryE);
                Element startedE = doc.createElement("Started");
                startedE.appendChild(doc.createTextNode(started));
                root.appendChild(startedE);
                Element endsE = doc.createElement("Ends");
                endsE.appendChild(doc.createTextNode(ended));
                root.appendChild(endsE);
                Element descriptionE = doc.createElement("Description");
                descriptionE.appendChild(doc.createTextNode(description));
                root.appendChild(descriptionE);

                //category
                for (int i = 0; i < category.size(); i++) {
                    Element categoryE = doc.createElement("Category");
                    categoryE.appendChild(doc.createTextNode(category.get(i)));
                    root.appendChild(categoryE);
                }
                //bids
                ResultSet bidInfoRS = stmt.executeQuery("SELECT * FROM Bid WHERE ItemID ="+ itemId);
                while (bidInfoRS.next())
                {
                    Element bidE = doc.createElement("Bid");
                    String bidderID = bidInfoRS.getString("BidderID");
                    String time = bidInfoRS.getString("Time");
                    String amount = bidInfoRS.getString("Amount");
                    ResultSet bidderRS = stmt.executeQuery("SELECT * FROM Bidder WHERE BidderID="+ bidderID);
                    String rating_bidder = bidInfoRS.getString("Rating");
                    String location_bidder = bidInfoRS.getString("Location");
                    String country_bidder = bidInfoRS.getString("Country");
                    Element bidderEle = doc.createElement("Bidder");
                    bidderEle.setAttribute("UserID", bidderID);
                    bidderEle.setAttribute("Rating", rating_bidder);
                    if (location_bidder!=""){
                        Element location_bidder_ele = doc.createElement("Location");
                        location_bidder_ele.appendChild(doc.createTextNode(location_bidder));
                        bidderEle.appendChild(location_bidder_ele);
                    }
                    if (country_bidder!=""){
                        Element country_bidder_ele = doc.createElement("Country");
                        country_bidder_ele.appendChild(doc.createTextNode(country_bidder));
                        bidderEle.appendChild(country_bidder_ele);
                    }
                    bidE.appendChild(bidderEle);
                    Element timeEle = doc.createElement("Time");
                    timeEle.appendChild(doc.createTextNode(time));
                    bidE.appendChild(timeEle);
                    Element amountEle = doc.createElement("Amount");
                    amountEle.appendChild(doc.createTextNode(amount));
                    bidE.appendChild(amountEle);
                    
                    root.appendChild(bidE);

                    
                }
                //transform to string
                //System.out.println("In the function **********************");
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                StreamResult result = new StreamResult(new StringWriter());
                DOMSource source = new DOMSource(doc);
                transformer.transform(source, result);
                xmlData = result.getWriter().toString();
                //System.out.println(xmlData);
                //System.out.println("**********************");

            }
        }catch (SQLException|ParserConfigurationException|TransformerException e){
            e.printStackTrace();
        }

		return xmlData;
	}

    private static String timestamp(String date) {
        SimpleDateFormat format_in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format_out = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        StringBuffer buffer = new StringBuffer();
        try {
            Date parsedDate = format_in.parse(date);
            return "" + format_out.format(parsedDate);
        }
        catch(Exception pe) {
            System.err.println("Error in timestamp");
            return "Error in timestamp";
        }
    }
	public String echo(String message) {
		return message;
	}

}
