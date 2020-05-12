DELIMITER $$
CREATE PROCEDURE `addMovie`(IN newMovieID varchar(10), IN movieTitle varchar(100),IN movieDirector varchar(100), IN movieYear int(11),IN genreName varchar(32),IN starName varchar(100), IN starByear varchar(10), OUT status varchar(100)  )
BEGIN
DECLARE genre_id int(11);DECLARE addeddata varchar(500);
DECLARE exit handler for SQLEXCEPTION
BEGIN
  GET DIAGNOSTICS CONDITION 1 @text = MESSAGE_TEXT;
  SET status = CONCAT('ERROR ', substring(@text, 1,50));
END;
set @addeddata ='';
#check for duplicate movie
Select count(*) into @cnt from movies where title = movieTitle and year = movieYear and director = movieDirector ;
if (@cnt >0 ) then SET status ='DuplicateMovie';  set addeddata ='Duplicate Movie';
else
	if (newMovieID is null or length(newMovieID) <= 0) then
	/*	select max(id) INTO @maxID from movies;
		Select length(@maxID) INTO @maxLen;
		#Select concat(substring(@maxID, 1,2) , lpad(substring(@maxID, 3,@maxLen)+1,@maxLen-2,'0')) INTO newMovieID;
 		Select concat(substring(@maxID, 1,@maxLen-2) , lpad(substring(@maxID, @maxLen-1,@maxLen)+1,2,'0')) INTO newMovieID;
        */
        insert into numbers (n) select FLOOR(RAND()*(100)+500);
		select  concat('fabflix',LAST_INSERT_ID()) into newMovieID;
	END IF;
    if NOT EXISTS( select 1 from movies where id = newMovieID) then
		insert into movies(id, title, year, director) values(newMovieID, movieTitle, movieYear, movieDirector);
		set addeddata = CONCAT('Added movie ',movieTitle,'(',newMovieID,').');
		set @genreNm = '%' + genreName +'%';

       		select id into genre_id from genres where name = genreName;
 		if NOT EXISTS (select 1 from genres where name = genreName) then # add genre if new
			select max(id) into genre_id from genres; set genre_id = genre_id+1;
			insert into genres(id, name) values(genre_id,  genreName);
			set addeddata = CONCAT(addeddata,' Added Genre ', genreName,'(',genre_id,').');
		else
        set addeddata = CONCAT(addeddata, 'Found ',  genreName,'(',genre_id,').');
        end if;

        if NOT exists( select 1 from genres_in_movies where genreid =genre_id and movieid =newMovieID ) then
			INSERT INTO genres_in_movies(genreid,movieid) values (genre_id,newMovieID );
			set addeddata = CONCAT(addeddata,'Added movieid = ',newMovieID,' to genre id"',genre_id,'".');
        end if;
        if (starName is not null and length(starName) > 0 ) then
			call addStarMovie(starName,starByear, newMovieID, status, @newData);
            set addeddata = CONCAT(addeddata,@newData);
        end if;
	else
    set status = 'Duplicate Movie'; set addeddata ='Duplicate Movie';
	end if;
    if (status is null or length(status) <= 0) then set status = 'success'; end if;
	select addeddata;
 END IF;
END$$
DELIMITER ;