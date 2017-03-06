package edu.ucla.cs.cs144;

import java.util.*;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        String pageTitle = "Search Result";
        String q = request.getParameter("q");
        String skipString = request.getParameter("numResultsToSkip");
        String retString = request.getParameter("numResultsToReturn");

        if (skipString == null) {
            request.setAttribute("title", "Keyword Search");
            request.getRequestDispatcher("/indexFirst.jsp").forward(request, response);
            return;
        }
        int skip = 0;
        int ret = 20;
        try {
             skip = skipString==null? 0 : Integer.parseInt(skipString);
             ret = retString==null? 20 : Integer.parseInt(retString);
        }
        catch (NumberFormatException e){
            skip = 0;
            ret = 20;
        }

        SearchResult[] basicResult = null;
        if (q != null) {
            basicResult = AuctionSearch.basicSearch(q, skip, ret);
        }

        ArrayList<String> IDlist = new ArrayList<String>();
        ArrayList<String> NameList = new ArrayList<String>();
        String s = "not Null";
        if (basicResult != null && basicResult.length != 0) {
            for (SearchResult curr: basicResult) {
                IDlist.add(curr.getItemId());
                NameList.add(curr.getName());
            }
        }
        request.setAttribute("q", q);
        request.setAttribute("numResultsToSkip", "" + skip);
        request.setAttribute("numResultsToReturn", "" + ret);
        request.setAttribute("IDList", IDlist);
        request.setAttribute("NameList", NameList);
        request.setAttribute("title", pageTitle);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
