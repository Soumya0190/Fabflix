# Project 2: Fabflix
By: Soumya Sharma
___
## Demo 
YouTube Video URL: https://youtu.be/-139z_ppkdk

## Deployment Instructions
In order to deploy the project to Tomcat, it is quite simple because no additional framework was used. First, navigate to the **bin** directory of Tomcat and run **./startup.sh**. This ensures that the Tomcat server is running. Then clone the project into your choice of directory and navigate to the directory in which **pom.xml** is located. Run **mvn clean** to ensure the plugin is clean. Run **mvn package** in order to generate the **.war** file which will be located within the target directory. Copy this war file to the **webapps** directory of Tomcat using **cp ./target/*.war /PATH_TO_TOMCAT/webapps** (i.e. home/ubuntu/tomcat/webapps). 

Navigate to the Tomcat manager page on **http://localhost:8080/manager/html** which will now show the newly deployed project. Click on it and you will see the project! 

## Functionalities
This project has several webpages, as listed: Login Page, Search Page, Movie List Page, Single Movie Page, Single Star Page, Shopping Cart Page, Payment Form, Confirmation Page 

The project has the following functionalities:
- logs in user 
- searching for movies based on text input
- searching for movies based in both ascending or descending order of both title or rating
- jumping between different pages using provided buttons
- displays all information about single movie or single star if selected
- browsing through movies by genre or starting letter
- accomodating different number of results on each page
- pagination of results
- adding movies to shopping cart and viewing shopping cart
- updating or deleting order on view cart screen
- allows user to checkout and view cart screen on every page after login
- allows user to add any movie to cart while browsing or searching 
- confirming credit card payment
- confirming payment with message (displays saleID, names of movies, price of each movie, quantity bought)

The project is implemented using Microservice Architecture with CSS, HTML, JavaScript, and Java
