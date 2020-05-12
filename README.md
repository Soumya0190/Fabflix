# Project 3: Fabflix
By: Soumya Sharma
___

##Note
I had to delete all previous commits because there were many conflicts with merging latest files

##Prepared Statements
Changed Prepared Statements for the Following Files:
Confirmation Servlet -- for updating Sales Data
Movie-List Servlet
Payment Servlet

#Inconsistencies
- Extracted starName and movieID from casts.xml
- Generated id for starName and saved data in record
- If movieID is invalid (doesn't exist in movie table), then did not add star to the movie

#Parsing Optimization Startegies
- Removed old indexes before loading and then recreated indexes after
- Executed batch statements 

## Demo 
YouTube Video URL: https://youtu.be/C9vt3wOCDhk

##Assumptions
Movie is not added to database if genre name is not provided in xml file, and reported to inconsistency_report_movie.txt

## Deployment Instructions
In order to deploy the project to Tomcat. First, navigate to the **bin** directory of Tomcat and run **./startup.sh**. This ensures that the Tomcat server is running. Then clone the project into your choice of directory and navigate to the directory in which **pom.xml** is located. Run **mvn clean** to ensure the plugin is clean. Run **mvn package** in order to generate the **.war** file which will be located within the target directory. Copy this war file to the **webapps** directory of Tomcat using **cp ./target/*.war /PATH_TO_TOMCAT/webapps** (i.e. home/ubuntu/tomcat/webapps). 

Navigate to the Tomcat manager page on **http://localhost:8080/manager/html** which will now show the newly deployed project. Click on it and you will see the project! 

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
