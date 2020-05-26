# Project 4: Fabflix
By: Soumya Sharma
___

## Demo
YouTube Video URL (Project 3): https://youtu.be/x9ZtjQJrLzQ

YouTube Video URL (Project 4) : https://youtu.be/RUuZjOnDiBI


## Full Text Search on Title
Created full text indexes on title of movies from moviedb database

Database command : CREATE FULLTEXT INDEX idx ON movies(title);

## Fuzzy Search
Implemented in separate search box on search page

Backed out at last minute due to it causing confusion with full text search

Was calulating edit distance for half the length of the search query


## Deployment Instructions
In order to deploy the project to Tomcat, follow below 

(1) Navigate to the **bin** directory of Tomcat and run **./startup.sh**. 
This ensures that the Tomcat server is running. 

(2) Clone the project into your choice of directory. 
This project has 2 directories inside, the AndroidApp directory for android project, and the 
WebApp directory for the web application. 

**----------- To Run The Web Application ---------**

(3) Navigate to the WebApp directory in which **pom.xml** is located. 

(4) Run **mvn clean package** in order to generate the **.war** file which will be located within the target directory. 

(5) Copy this war file to the **webapps** directory of Tomcat using 
**cp ./target/*.war /PATH_TO_TOMCAT/webapps** (i.e. home/ubuntu/tomcat/webapps).



(8) Navigate to the Tomcat manager page on **https://localhost:8443/manager/html** 
which will now show the newly deployed project. 

(9) Click on the deployed project and you will see the web app project! 


**----------- To Run The Android Application ---------**

(10) Navigate to the AndroidApp directory in which **.gradle** file is located on IntelliJ

(11) Build the APK file on IntelliJ under **Build** **Build Bundles(s) / APK(s)** **Build APK(s)**

(12) Drag the APK file to Local Emulator to install app

(13) Run the Android application

(14) Click on the local emulator to see the android app project!


## From Project #3

## Functionalities
This project has several webpages, as listed: Login Page, Search Page, Movie List Page, Single Movie Page, Single Star Page, Shopping Cart Page, Payment Form, Confirmation Page 

The following functionalities capabilities are added under project 4:
- Implemented full-text search on the movie title field, Jump to the corresponding Movie List Page and show correct results.

The project is implemented using Microservice Architecture with CSS, HTML, JavaScript, and Java



## Prepared Statements
Changed Prepared Statements for the Following Files: 

Confirmation Servlet (for updating Sales Data): https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-3/blob/master/src/main/java/ConfirmationServlet.java 

Movie-List Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-3/blob/master/src/main/java/MovieListServlet.java

Payment Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-3/blob/master/src/main/java/PaymentServlet.java

## Inconsistencies
- Extracted starName and movieID from casts.xml
- Generated id for starName and saved data in record
- If movieID is invalid (doesn't exist in movie table), then did not add star to the movie


## Parsing Optimization Strategies
- Removed old indexes before loading and then recreated indexes after
- Attempted to execute batch statements 
- Done analysis of indexes on the table, there are already primary indexes on the tables, but stars table can have index on name that might help for large data sets because we are searching with name
- In Movies, index can be created on title, year, and director to improve the performance because these are the used search criterias
- As per my research, indexes are only effective if there are lot of records
- SAX Parser is more efficient than DOM Parser for larger files


## Assumptions
Movie is not added to database if genre name is not provided in xml file, and reported to inconsistency_report_movie.txt
