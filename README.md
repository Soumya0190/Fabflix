# General
    - #### Team#: Team 3

    - #### Names: Soumya Sharma

    - #### Project 5 Video Demo Link: https://youtu.be/ZvpoAU0Pw5c

    - #### Instruction of deployment:
    
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

    - #### Collaborations and Work Distribution:


- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
cs122b-spring20-team-3/WebApp/web/META-INF/context.xml
cs122b-spring20-team-3/WebApp/web/WEB-INF/web.xml
cs122b-spring20-team-3/WebApp/src/main/java/ConfirmationServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CConfirmationServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CDashboardServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CEmployeeLoginServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CLoginServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CMovieListServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CPaymentServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CSAXParserActor.java
cs122b-spring20-team-3/WebApp/src/main/java/CSAXParserStar.java
cs122b-spring20-team-3/WebApp/src/main/java/CSAXParserMovie.java
cs122b-spring20-team-3/WebApp/src/main/java/CSearchServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CSingleMovieServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CSingleStarServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CUpdateSecurePassword.java
cs122b-spring20-team-3/WebApp/src/main/java/CVerifyPassword.java
    
    - #### Explain how Connection Pooling is utilized in the Fabflix code.
    
    - #### Explain how Connection Pooling works with two backend SQL.
    
 
- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
    cs122b-spring20-team-3/WebApp/web/META-INF/context.xml
cs122b-spring20-team-3/WebApp/web/WEB-INF/web.xml
cs122b-spring20-team-3/WebApp/src/main/java/ConfirmationServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CConfirmationServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CDashboardServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CEmployeeLoginServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CLoginServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CMovieListServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CPaymentServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CSAXParserActor.java
cs122b-spring20-team-3/WebApp/src/main/java/CSAXParserStar.java
cs122b-spring20-team-3/WebApp/src/main/java/CSAXParserMovie.java
cs122b-spring20-team-3/WebApp/src/main/java/CSearchServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CSingleMovieServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CSingleStarServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/CUpdateSecurePassword.java
cs122b-spring20-team-3/WebApp/src/main/java/CVerifyPassword.java
 
    - #### How read/write requests were routed to Master/Slave SQL?
    We update value of parameter called "read_only" with value of "1" in a configuration file called /etc/mysql/my.cnf on Slave node. This enforces "read only" operation in the  slave node.
    
    
 
- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.
    I have copied all Jmeter logs and renamed files for each case
    I have created "ParseLogFile" Servlet that read all the files from the folder where all log files are saved. 
    I have written code to read each file and extracted below data
    	1. servlet execution time for getting movie results for each request (start-time is added as first line of servlet code(doGet) and endTime is recorded in the end of servlet code 
   	2. JDBC execution time(start-time is recorded when database connection is opened and end-time is recorded when connection is closed (for each request))
    After extracting above data, I have added calculated below data:
   	 1. Average TS : added  servlet execution time of all the records/requests and divided by total number of requests
   	 2. Average TJ : added JDBC execution time of all the records/requests and divided by total number of requests
 
 The parsing script was exceuted by running the command ** mvn exec:java -Dexec.mainClass=main.java.ParseLogFile **
 And alternate way of running the ParseLogFile is on IntelliJ 

- # JMeter TS/TJ Time Measurement Report
 
| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | 217                       | 1302903 ms                                 | 720764 ms                        | 1 thread took longer than 10 threads           |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | ??                         | 8691160 ms                                  | 6454065 ms                        | ??           |
| Case 3: HTTPS/10 threads                       | ![](path to image in img/)   | ??                         | 9309436 ms                                  | 7879926 ms                        | ??           |
| Case 4: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | ??                         | 10684810 ms                                  | 8246870                        | ??           |
 
| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1 AWS MASTER: HTTP/1 thread                          | ![](path to image in img/)   | 108                         | 1106077 ms                                  | 573739 ms                        | ??           |
| Case 2 AWS MASTER: HTTP/10 threads                        | ![](path to image in img/)   | ??                         | 4025746 ms                                  | 3061537 ms                        | ??           |
| Case 3 AWS MASTER: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | 117                         | ??                                  | 3150529 ms                         | 2207705 ms           |
| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1 AWS SLAVE: HTTP/1 thread                          | ![](path to image in img/)   | 108                       | 1143968 ms                                  | 569903 ms                        | ??           |
| Case 2 AWS SLAVE: HTTP/10 threads                        | ![](path to image in img/)   | ??                         | 110759871 ms                                  | 3057154 ms                        | ??           |
| Case 3 AWS SLAVE: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | 117                        |  2858440 ms                                | 2025506 ms                        | ??           |
**Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1 GCP MASTER: HTTP/1 thread                          | ![](path to image in img/)   | 109                         | 1092474 ms                                  | 576821 ms                        | GCP takes the longest amount of time because there is a netwrok latency           |
| Case 2 GCP MASTER: HTTP/10 threads                        | ![](path to image in img/)   | ??                         | 4607613 ms                                  | 3551625 ms                        | ??           |
| Case 3 GCP MASTER: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | 118                       | ??                                  | 2796297 ms                        | 1955982 ms           |
| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1 GCP SLAVE: HTTP/1 thread                          | ![](path to image in img/)   | 109                        | 1123661ms                                  | 569278 ms                        |            |
| Case 2 GCP SLAVE: HTTP/10 threads                        | ![](path to image in img/)   |                          | 5561143 ms                                  | 4412899 ms                        | ??           |
| Case 3 GCP SLAVE: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | 118                       | ??                                  | 2858440 ms                        | 2025506 ms           |


