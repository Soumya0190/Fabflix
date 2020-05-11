DELIMITER $$
CREATE PROCEDURE `addStarMovie`(IN starName varchar(100), IN starByear varchar(10), IN movieID varchar(10), OUT status varchar(100), OUT addedData varchar(100))
BEGIN
DECLARE maxId varchar(10); DECLARE lenId int; DECLARE newstarID varchar(10);
DECLARE exit handler for SQLEXCEPTION
BEGIN
  GET DIAGNOSTICS CONDITION 1 @text = MESSAGE_TEXT;
  SET status = CONCAT('ERROR ', substring(@text, 1,100));
END;

if NOT Exists (select 1 from stars where name = starName) then
	SELECT max(id) INTO maxId from stars;
	SELECT length(maxId) INTO lenId;
	SELECT  concat(substring(maxId,1,2),LPAD(substring(maxId,3,lenId)+1, lenId-2,'0')) INTO newstarID;
	if (starByear is not null and length(starByear)  <=0) then set starByear =null; end if;
	INSERT INTO stars(id, name, birthYear) Values(newstarID,starName, starByear );
    set addedData = CONCAT('Added Star "',starName,'" (',newstarID,').');
else
	select id INTO newstarID from stars where name = starName;
END IF;
if (movieID is not null and length(movieID)> 0 and newstarID is not null and length(newstarID) >0) then
	if NOT exists(SELECT 1 from stars_in_movies where starID = newstarID and movieId = movieID) then
		INSERT INTO stars_in_movies(starId, movieId) Values(newstarID, movieID);
        set addedData = CONCAT(addedData,' Added "',starName,'" to movie id=', movieID);
    end if;
end if;
if (status is null or length(status) <= 0) then set status = 'success'; end if;
select addedData;
END$$
DELIMITER ;
