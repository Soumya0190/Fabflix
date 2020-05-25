# Project 4: Fabflix
By: Soumya Sharma
___

##Demo 
YouTube Video URL (Project 3): https://youtu.be/x9ZtjQJrLzQ


##Full Text Search on Title
CREATE FULLTEXT INDEX idx ON movies(title);

##Fuzzy Search
Not yet implemented 

##Prepared Statements
Changed Prepared Statements for the Following Files: 

Confirmation Servlet (for updating Sales Data): https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-3/blob/master/src/main/java/ConfirmationServlet.java 

Movie-List Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-3/blob/master/src/main/java/MovieListServlet.java

Payment Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-3/blob/master/src/main/java/PaymentServlet.java

##Inconsistencies
- Extracted starName and movieID from casts.xml
- Generated id for starName and saved data in record
- If movieID is invalid (doesn't exist in movie table), then did not add star to the movie


##Parsing Optimization Strategies
- Removed old indexes before loading and then recreated indexes after
- Attempted to execute batch statements 
- Done analysis of indexes on the table, there are already primary indexes on the tables, but stars table can have index on name that might help for large data sets because we are searching with name
- In Movies, index can be created on title, year, and director to improve the performance because these are the used search criterias
- As per my research, indexes are only effective if there are lot of records
- SAX Parser is more efficient than DOM Parser for larger files


##Assumptions
Movie is not added to database if genre name is not provided in xml file, and reported to inconsistency_report_movie.txt


## Deployment Instructions
In order to deploy the project to Tomcat. 
First, navigate to the **bin** directory of Tomcat and run **./startup.sh**. 
This ensures that the Tomcat server is running. 
Next, clone the project into your choice of directory. 
This project has 2 directories inside, the AndroidApp directory for android project, and the 
WebApp directory for the web application. 
To run the website, navigate to the WebApp directory in which **pom.xml** is located. 
Run **mvn clean package** in order to generate the **.war** file which will be located within the target directory. 
Copy this war file to the **webapps** directory of Tomcat using 
**cp ./target/*.war /PATH_TO_TOMCAT/webapps** (i.e. home/ubuntu/tomcat/webapps).
Before running website, ensure database is updated with stored procedures.
This can be done with **sudo mysql moviedb < "filename"** where filename
will be "addStarMovie_SP.sql", "addStar_SP.sql", "addMovie_SP.sql", "showDBMetaData_SP.sql", and "employeeTable.sql".
Lastly, the parsers will need to be executed, which can be done with the commands -
**mvn exec:java -Dexec.mainClass=main.java.SAXParserMovie**, **mvn exec:java -Dexec.mainClass=main.java.SAXParserActor**,
and **mvn exec:java -Dexec.mainClass=main.java.SAXParserStar**  
Navigate to the Tomcat manager page on **http://localhost:8080/manager/html** which will now show the newly deployed project. 
These permission needed to be given to user:

GRANT EXECUTE ON PROCEDURE moviedb.addStar TO mytestuser;
 
GRANT EXECUTE ON PROCEDURE moviedb.addStarMovie TO mytestuser; 

GRANT EXECUTE ON PROCEDURE moviedb.addStarInBatch TO mytestuser; 

Click on it and you will see the project! 

## Functionalities
This project has several webpages, as listed: Login Page, Search Page, Movie List Page, Single Movie Page, Single Star Page, Shopping Cart Page, Payment Form, Confirmation Page 

The project has the following functionalities:
- Redirect any other pages to Login Page if not logged in
- Show error message with incorrect user name or password
- Use HTTP POST for user login, as this is more secire than HTTP GET method
- Login with correct user name and password
- Access to searching and browsing pages/functionalities
    - Search by title
    - Search by year
    - Search by director
    - Search by star's name
    - Substring Matching is implemented for searching title/director/star using the LIKE % predicate for fuzzy searches, in format: %keyword%. If keyword is anywhere in the text input, it will show up in the results
- Jump among movie list page, single movie page, and single star page without using browser history or changing url
- Show at least (title(hyperlinked), year, director, list of genres(hyperlinked), list of stars(hyperlinked)), rating
- Implemented Pagination. Frontend cache and backend cache both can not exceed 100 records 
- Created pagination buttons. it shows maximum 10 page numbers at a time, based on click on the page, it recalculates number of pages shown e.g. if there are 15 pages. It will show first 10 , on clicking page number 4 will show 4-14 in the pagincation section.
- Change number of listings N per page
- Sort on title by clicking header (clicking on title header column will toggle ascending and decensding of the title, it will rearrange rest of the data accordingly.
- Sort on rating - implemented same logic for title for rating (this is numeric sorting instead of alpha)
- Browse by movie title first alphanumeric letter - click on each letter jumps to movie list page only consists of movies starting with this letter
- Browse by genres - click on each genre jumps to Movie List Page which only consists of movies of this genre
- Display information about current shopping cart
 - Modify quantity using an input box and only allow valid values (numbers that >= 0)
- Modify quantity using an input box and only allow valid values (numbers that >= 0). Ask for basic customer transactional information
- Show error message with incorrect input
- Insert sale record in ''moviedb.sales" table
- Show confirmation message, containing order details - sale ID, movies purchased and the quantity
 - "Check Out" button for every page after login - jump to shopping cart page
 - Modify quantity using an input box and only allow valid values (numbers that >= 0) 
""Add to Cart"" button for each movie in ""Movie List"" page and ""Single Movie"" page"
- Show efforts to beautify the page and table using CSS and JavaScript - i have implemneted css consistently , but due to shortage of time, model pops are not implemented everywhere and added alerts for faster delivery. 
-Update and Delete items from shopping cart
- Due to shortage of time logout and about is not imeplemneted, but will add next time. (it is optional, and not part of rubric score)
Use Microservices Architecture (front-end back-end separation)

The project is implemented using Microservice Architecture with CSS, HTML, JavaScript, and Java
