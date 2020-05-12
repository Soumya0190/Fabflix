DELIMITER $$
CREATE PROCEDURE `addStarInBatch`(starName varchar(100), movieID varchar(10))
BEGIN
DECLARE maxId varchar(10); DECLARE lenId int; DECLARE newstarID varchar(10);

if NOT Exists (select 1 from stars where name = starName) then
	insert into numbers (n) select FLOOR(RAND()*(100)+500);
	select  concat('fabflix',LAST_INSERT_ID()) into newstarID;

	INSERT INTO stars(id, name) Values(newstarID,starName );
   else
	select id INTO newstarID from stars where name = starName;
    END IF;
INSERT INTO stars_in_movies(starId, movieId) Values(newstarID, movieID);


END$$
DELIMITER ;
