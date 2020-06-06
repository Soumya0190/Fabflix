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
| Case 1: HTTP/1 thread                          | /graph_results  | 217                       | 1,302,903 ms                                 | 720,764 ms                        | 1 thread took longer than 10 threads           |
| Case 2: HTTP/10 threads                        | /graph_results   |                          | 8,691,160 ms                                  | 6,454,065 ms                        | 10 threads were faster than having 1 thread, and connection pooling is faster than no connection pooling           |
| Case 3: HTTPS/10 threads                       | /graph_results   | 220                         | 9,309,436 ms                                  | 7879926 ms                        | ??           |
| Case 4: HTTP/10 threads/No connection pooling  | /graph_results  | ??                         | 10,684,810 ms                                  | 8,246,870                        | As can be seen from times, on the single instance, no connection pooling was slower than having 10 threads with connection pooling           |
 
| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1 AWS MASTER: HTTP/1 thread                          | /graph_results   | 108                         | 1,106,077 ms                                  | 573,739 ms                        | Having 1 thread was slower than having 10 threads on the same instance           |
| Case 2 AWS MASTER: HTTP/10 threads                        | /graph_results   | ??                         | 4,025,746 ms                                  | 3,061,537 ms                        | The 10 threads was about 2,000,000 ms faster than having 1 thread on the same instance           |
| Case 3 AWS MASTER: HTTP/10 threads/No connection pooling  | /graph_results   | 117                         | 3,150,529 ms                                   |  2,207,705 ms                      |      No connection pooling is slower than having 10 threads with connection pooling      |
| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1 AWS SLAVE: HTTP/1 thread                          | /graph_results   | 108                       | 1,143,968 ms                                  | 569,903 ms                        | Similar to results of master, having 1 thread is slower compared to 10 threads, but is faster than running the single instance           |
| Case 2 AWS SLAVE: HTTP/10 threads                        | /graph_results   | ??                         | 110,759,871 ms                                  | 3,057,154 ms                        | 10 threads results in faster execution time than 1 thread           |
| Case 3 AWS SLAVE: HTTP/10 threads/No connection pooling  | /graph_results   | 117                        |  2,858,440 ms                                | 2,025,506 ms                        | No connection pooling results in slower execution time than having connection pooling           |
**Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1 GCP MASTER: HTTP/1 thread                          | /graph_results  | 109                         | 1092474 ms                                  | 576,821 ms                        | GCP takes the longest amount of time, and most so with the single thread because there is a netwrok latency           |
| Case 2 GCP MASTER: HTTP/10 threads                        | /graph_results   | ??                         | 4,607,613 ms                                  | 3,551,625 ms                        | 10 threads had faster execution time than single thread           |
| Case 3 GCP MASTER: HTTP/10 threads/No connection pooling  | /graph_results   | 118                       | 2,796,297 ms                                   |  1,955,982 ms                       |           | Without connection pooling and 10 threads, the results were slower than with connection pooling and only 1 thread
| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1 GCP SLAVE: HTTP/1 thread                          | /graph_results  | 109                        | 1123661ms                                  | 569,278 ms                        |  Similar to results of master, single thread results in more execution time than 10 threads on same instance          |
| Case 2 GCP SLAVE: HTTP/10 threads                        | /graph_results   |                          | 5561143 ms                                  | 4,412,899 ms                        | With connection pooling and 10 threads, the execution time was the fastest           |
| Case 3 GCP SLAVE: HTTP/10 threads/No connection pooling  | /graph_results   | 118                       | 2,858,440 ms                                   |  2,025,506 ms                    |   Without connection pooling, the results are seen to be slower than with connection pooling         |


