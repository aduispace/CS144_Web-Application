—team: Yu Cai, Dui lin

1. 
Item: {ItemID, Name, SellerID, Currently, Buy_Price, First_Bid, Location, Latitude,Longitude, Country, Started, Ends and Description}
Primary Key: ItemID

Seller: {UserID, Rating}
Primary Key: UserID

Bidder: {UserID, Rating, Location, Country}
Primary Key: UserID

Bid: {ItemID, BidderID, Time, Amount}
Primary Key: ItemID, BidderID, Time

Category: {ItemID, Category}
Primary Key: ItemID, Category

2.
Item:
ItemID —> Name
ItemID —> SellerID
ItemID —> Currently
ItemID —> Buy_Price
ItemID —> First_Bid
ItemID —> Location
ItemID —> Latitude
ItemID —> Longitude
ItemID —> Country
ItemID —> Started
ItemID —> Ends
ItemID —> Description
Seller:
UserID —> Rating
Bidder:
UserID —> Rating
UserID —> Location
UserID —> Country
Bid:
ItemID, BidderID, Time —> Amount
Category:
(None)

3.
Yes, my relations are in BCDF.

4.
Yes, my relations are in 4NF. 
