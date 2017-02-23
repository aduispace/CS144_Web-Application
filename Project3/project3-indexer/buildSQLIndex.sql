create table if not exists ItemLocation( ItemID integer not null, Location geometry not null, spatial index(Location)) engine = MyISAM;

insert into ItemLocation (ItemID, Location)
select ItemID, point(Latitude, Longitude) 
from  Item
where Latitude is not null and Longitude is not null;
