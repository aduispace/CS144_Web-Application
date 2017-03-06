<%@ page import ="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import ="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="eBay.css">
    <title><%= request.getAttribute("title") %></title>
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
	<h2> Enter Your Search: </h2>
     <form type="text" id="query" name="q">
     	<div class="queryContainer">
     		<div>
			    <input id="queryBox" name="q" type="text" autocomplete="false"></input>
			    <input type="submit" value="search"></input>
			</div>

		</div>
	     <input type="hidden" name="numResultsToSkip" value="0">
	     <input type="hidden" name="numResultsToReturn" value="20">

  	</form>
  </div>
</body>
</html>