DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `addStar`(IN starName varchar(100), IN starByear varchar(10), IN movieID varchar(10), OUT status varchar(100))
BEGIN
DECLARE maxId varchar(10); DECLARE lenId int;
DECLARE newstarID varchar(10);

DECLARE exit handler for SQLEXCEPTION
BEGIN
  GET DIAGNOSTICS CONDITION 1 @text = MESSAGE_TEXT;
  SET status = CONCAT('ERROR ', substring(@text, 1,50));
END;

SELECT max(id) INTO maxId from stars;
SELECT length(maxId) INTO lenId;
SELECT  concat(substring(maxId,1,2),LPAD(substring(maxId,3,lenId)+1, lenId-2,'0')) INTO newstarID;
if (starByear is not null and length(starByear)  <=0) then set starByear =null; end if;
INSERT INTO stars(id, name, birthYear) Values(newstarID,starName, starByear );
if (movieID is not null and length(movieID)> 0) then
	INSERT INTO stars_in_movies(starId, movieId) Values(newstarID, movieID);
end if;
if (status is null or length(status) <= 0) then set status = 'success'; end if;
select status;
END$$
DELIMITER ;
