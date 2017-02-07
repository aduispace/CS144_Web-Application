-- Team: Yu Cai, Dui Lin

--  13422
SELECT COUNT(*) FROM (SELECT UserID FROM Seller UNION SELECT UserID FROM Bidder) as a;

--  103
SELECT COUNT(*) FROM Item WHERE BINARY Location = 'New York';

--  8365
SELECT COUNT(*) FROM(SELECT ItemID, COUNT(Category) as c FROM Category GROUP BY ItemID HAVING c = 4) as a;

-- 1046740686
SELECT Bid.ItemID FROM Bid INNER JOIN Item ON Bid.ItemID = Item.ItemID WHERE Item.Ends > '2001-12-20 00:00:01'AND Bid.Amount = (SELECT MAX(Amount) FROM Bid);

--  3130
SELECT COUNT(*) FROM Seller WHERE Rating > 1000;

--  6717
SELECT COUNT(*) FROM Seller WHERE UserID IN ( SELECT UserID FROM Bidder);

-- 150
SELECT COUNT(DISTINCT Category) FROM Category WHERE ItemID IN (SELECT ItemID FROM Bid WHERE Amount > 100.00);
