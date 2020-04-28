# Project 1: Fabflix
By: Iqra Abbaali and Soumya Sharma
___

## NOTE
Soumya has 2 different Github accounts and for some reason, *both of her* accounts (soumya-190 AND Soumya0190) made commits to our project. We contacted TA Yicong and he said to make sure to include this detail in this README. Sorry for making this mistake, and we will not do it in future projects. 

## Demo 
YouTube Video URL: https://youtu.be/-139z_ppkdk

## Deployment Instructions
In order to deploy the project to Tomcat, it is quite simple because no additional framework was used. First, navigate to the **bin** directory of Tomcat and run **./startup.sh**. This ensures that the Tomcat server is running. Then clone the project into your choice of directory and navigate to the directory in which **pom.xml** is located. Run **mvn clean** to ensure the plugin is clean. Run **mvn package** in order to generate the **.war** file which will be located within the target directory. Copy this war file to the **webapps** directory of Tomcat using **cp ./target/*.war /PATH_TO_TOMCAT/webapps** (i.e. home/ubuntu/tomcat/webapps). 

Navigate to the Tomcat manager page on **http://localhost:8080/manager/html** which will now show the newly deployed project. Click on it and you will see the project! 

## Member Contribution
Iqra worked on the functionality for the Top 20 Movies List page (HTML, JavaScript, and MovieListServlet). Soumya worked on the functionality for the Single Star page (HTML, JavaScript, and SingleStarServlet). Iqra and Soumya both worked on the Single Movie page together and created the SQL queries for creating tables on the moviedb together. Iqra also did the styling for the pages (styles.css).  
