<%@ page import ="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import ="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title><%= request.getAttribute("title") %></title>
<link rel="stylesheet" type="text/css" href="eBay.css">


        <script src="autosuggest.js"></script>
        <script src="suggestions.js"></script>
        <script type="text/javascript">
            window.onload = function () {
                var oTextbox = 
                    new AutoSuggestControl(document.getElementById("queryBox"),
                new GoogleSuggestions());
            }
        </script>

</head>
<body>
	<div>
	<h1>	Enter Your Search:</h1>
     <form action="search" method="GET">
     	<div class="queryContainer">
     		<div>
		     <input id="queryBox" type="text"  name="q" autocomplete="false"></input>
		     <input type="submit" value = "search"></input>
		    </div>
		    <div class="suggestions"></div>
	    </div>

	     <input type="hidden" name="numResultsToSkip" value="0">
	     <input type="hidden" name="numResultsToReturn" value="20">
  	</form>
    </div>
	<div >
		<p>Search Result:</p>
		<%ArrayList<SearchResult> IDList = (ArrayList<SearchResult>)request.getAttribute("IDList");
				ArrayList<SearchResult> NameList = (ArrayList<SearchResult>)request.getAttribute("NameList");
			%>
			<% for (int i=0; i<IDList.size(); i++) { %>
			    <p> <a href="item?itemID=<%= IDList.get(i) %>"> <%= IDList.get(i) %> <%= NameList.get(i) %>.</p>
		<% } %>
	</div>
	<a href="search?q=<%= request.getAttribute("q") %>&amp;numResultsToSkip=<%= Integer.parseInt(request.getAttribute("numResultsToSkip").toString())==0?0: Integer.parseInt(request.getAttribute("numResultsToSkip").toString())-20 %>&amp;numResultsToReturn=<%=request.getAttribute("numResultsToReturn").toString()%>">Prev</a>
    <a href="search?q=<%= request.getAttribute("q") %>&amp;numResultsToSkip=<%= IDList.size() != 20 ? Integer.parseInt(request.getAttribute("numResultsToSkip").toString()) : Integer.parseInt(request.getAttribute("numResultsToSkip").toString()) + 20 %>&amp;	  				numResultsToReturn=<%= request.getAttribute("numResultsToReturn").toString() %>">Next</a>

</body>
</html>