<%@ page import ="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import ="java.util.*" %>

<html lang="en-US">
<head>
    <title> Item Information </title>
    <style type = "text/css">

		#column-1 {
			width: calc((100% - 3em)/2);
			margin-left: 1em;
			margin-right: 1em;
			position: absolute;
			top:0px;
			left:0;
		}
		#column-2 {
			width: calc((100% - 3em)/2);
			margin-left: 1em;
			margin-right: 1em;
			position: absolute;
			top: 0px;
			height: 100px;
			left: calc((100% - 3em)/2 + 1em);
		}		

		/*#map_canvas { 
			width: calc((100% - 3em)/2);
			margin-left: 1em;
			margin-right: 1em;
			position: absolute;
			height: 100%;
			left: calc((100% - 3em)/2 + 1em);
		} */
		#map_canvas { 
			width: calc((100% - 3em)/2);
			margin-left: 1em;
			margin-right: 1em;
			position: absolute;
			top: 100px;
			height: 100%;
			left: calc((100% - 3em)/2 + 1em);
		}

		table {
			font-family: Times New Roman;
			width: 90%;
			table-layout: auto;
		}

		td, th {
			border: 1px solid #dddddd;
			text-align: center;
			padding: 8px;
		}

		tr:nth-child(even) {
			background-color: #dddddd;
		}






	</style>
</head>
<body>

<div id = "column-1">
	<h2> Item Info </h2>
	<table>
		<tr>
			<th>ID</th>
			<td><%= request.getAttribute("docItemId") %></td>
		</tr>
		<tr>
			<th>Name</th>
			<td><%= request.getAttribute("docName") %></td>
		</tr>
		<tr>
			<th>Category</th>
			<td><%= request.getAttribute("docCategory") %></td>
		</tr>
		<tr>
			<th>Currently</th>
			<td><%= request.getAttribute("docCurrently") %></td>
		</tr>
		<tr>
			<th>Buy Price</th>
			<td><%= request.getAttribute("docBuyPrice") %></td>
		</tr>
		<tr>
			<th>First Bid</th>
			<td><%= request.getAttribute("docFirstBid") %></td>
		</tr>
		<tr>
			<th>Number of Bids</th>
			<td><%= request.getAttribute("docNumberOfBids") %></td>
		</tr>
		<tr>
			<th>Location</th>
			<td><%= request.getAttribute("docLocation") %></td> 
		</tr>
		<tr>
			<th>Latitude</th>
			<td><%= request.getAttribute("docLatitude") %></td>
		</tr>
		<tr>
			<th>Longitude</th>
			<td><%= request.getAttribute("docLongitude") %></td>
		</tr>
		<tr>
			<th>Country</th>
			<td><%= request.getAttribute("docCountry") %></td>
		</tr>
		<tr>
			<th>Started</th>
			<td><%= request.getAttribute("docStarted") %></td>
		</tr>
		<tr>
			<th>Ended</th>
			<td><%= request.getAttribute("docEnds") %></td>
		</tr>
		<tr>
			<th>Seller ID</th>
			<td><%= request.getAttribute("docSellerID") %></td>
		</tr>
		<tr>
			<th>Seller Rating</th>
			<td><%= request.getAttribute("docSellerRating") %></td>
		</tr>
		<tr>
			<th>Description</th>
			<td><%= request.getAttribute("docDescription") %></td>
		</tr>
	</table>



	<% ArrayList<String> bidderList = (ArrayList<String>)(request.getAttribute("docBidderID")); 
		if (bidderList.size() != 0) { %>

	<table>
			<tr>
				<th>Bidder Id</th>
				<th>Bidder Rating</th>
				<th>Time</th>
				<th>Amount</th>
			</tr>
			<%
			
			ArrayList<String> ratingList = (ArrayList<String>)(request.getAttribute("docBidderRating"));
			ArrayList<String> timeList = (ArrayList<String>)(request.getAttribute("docTime"));
			ArrayList<String> amountList = (ArrayList<String>)(request.getAttribute("docAmount"));
			%>

			<% for (int i = 0; i < bidderList.size(); i ++) {%>
			 <tr><td><%= bidderList.get(i) %></td>
			 <td><%= ratingList.get(i) %></td>
			<td><%= timeList.get(i) %></td>
			 <td><%= amountList.get(i) %></td>
			<% } %>

	</table>
	<%
	}
	%>
</div>
<div id ="column-2">
		<h2> Item Location </h2>
</div> 
<div id="map_canvas">
							
		<script type="text/javascript" async defer 
			src="http://maps.google.com/maps/api/js?key=AIzaSyDdyWywxuK3KD3jlwdDXFX2ILgGpi7HD-Y&callback=initialize"> 
		</script> 
		<script type="text/javascript"> 
		 			function isEmpty(s) 
					{
					return (!s || 0 === s.length);
					}

					function initialize() { 
					// var latlng = new google.maps.LatLng(34.063509,-118.44541);
					// var lat = String(<%=request.getAttribute("itemLatitude")%>);
					// var lon = String(<%=request.getAttribute("itemLongitude")%>);
					if (!isEmpty(<%=request.getAttribute("docLatitude")%>) && !isEmpty(<%=request.getAttribute("docLongitude")%>)) {
						/*var latlng = new google.maps.LatLng(parseFloat(<%=request.getAttribute("itemLatitude")%>), parseFloat(<%=request.getAttribute("itemLongitude")%>));*/
						var mylat = parseFloat(<%=request.getAttribute("docLatitude")%>);
						var mylng = parseFloat(<%=request.getAttribute("docLongitude")%>);
						var myLatLng = {lat: mylat, lng: mylng};
						var map = new google.maps.Map(document.getElementById("map_canvas"), {
							zoom: 14,
							center: myLatLng 
						}); 

						var marker = new google.maps.Marker({
							position: myLatLng,
							map: map,
							title: 'Your item is here!'
						});
					} else {
						var mapOptions = {
							center: new google.maps.LatLng(0,0),
							zoom: 1
						};
						var map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
					}
				
					/*var myOptions = { 
      				zoom: 14, // default is 8  
     				center: latlng, 
    				mapTypeId: google.maps.MapTypeId.ROADMAP 
 					}; */
					
				}

		</script> 
</div>



</body>
</html>