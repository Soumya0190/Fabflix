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

(3) Navigate to the WebApp directory, in which **pom.xml** is located. 

(4) Run **mvn clean package** to generate the **.war** file, which will be created within the target directory. 

(5) Copy this war file to the **webapps** directory of Tomcat using 
**cp ./target/*.war /PATH_TO_TOMCAT/webapps** (i.e. home/ubuntu/tomcat/webapps).

(6) Run the following links on the browser to connect to the different instances

Project deployed on Master Instance: http://18.191.106.30:8080/cs122b-spring20-team-3/

Project deployed on Slave Instance: http://3.21.240.182:8080/cs122b-spring20-team-3/

Project deployed on AWS Load Balancer: http://18.221.207.68:8080/cs122b-spring20-team-3/ 

Project deployed on GCP Load Balancer: http://35.202.189.23:8080/cs122b-spring20-team-3/  

Project deployed on Original Instance (used on all the other projects): http://3.14.14.154:8080/cs122b-spring20-team-3/  




    - #### Collaborations and Work Distribution: Solo


- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
cs122b-spring20-team-3/WebApp/web/META-INF/context.xml
cs122b-spring20-team-3/WebApp/web/WEB-INF/web.xml
cs122b-spring20-team-3/WebApp/src/main/java/ConfirmationServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/DashboardServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/EmployeeLoginServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/LoginServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/MovieListServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/PaymentServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/SAXParserActor.java
cs122b-spring20-team-3/WebApp/src/main/java/SAXParserStar.java
cs122b-spring20-team-3/WebApp/src/main/java/SAXParserMovie.java
cs122b-spring20-team-3/WebApp/src/main/java/SearchServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/SingleMovieServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/SingleStarServlet.java
cs122b-spring20-team-3/WebApp/src/main/java/UpdateSecurePassword.java
cs122b-spring20-team-3/WebApp/src/main/java/VerifyPassword.java
    
   - #### Explain how Connection Pooling is utilized in the Fabflix code.
1. For Fabflix, I have added added connection pool settings in resources in context.xml  
2. for slaveDB (read operations), I have used resource name = jdbc/moviedb
<Resource name="jdbc/moviedb"
          auth="Container"
          driverClassName="com.mysql.jdbc.Driver"
          factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
          type="javax.sql.DataSource"
          maxTotal="100" maxIdle="30" maxWaitMillis="10000"
          username="mytestuser"
          password="mypassword"
          url="jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&amp;useSSL=false&amp;cachePrepStmts=true" />
3. for masterDB (insert/update operations), I am using jdbc/masterdb 
<Resource name="jdbc/masterdb"
          auth="Container"
          driverClassName="com.mysql.jdbc.Driver"
          factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
          type="javax.sql.DataSource"
          maxTotal="100" maxIdle="30" maxWaitMillis="10000"
          username="mytestuser"
          password="mypassword"
          url="jdbc:mysql://172.31.24.250:3306/moviedb?autoReconnect=true&amp;useSSL=false&amp;cachePrepStmts=true"/>


2. The following changes are made to the servlets where JDBC connection is used
Context initContext = new InitialContext();
Context envContext = (Context) initContext.lookup("java:/comp/env"); 
DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
Connection connection = ds.getConnection();

By adding connection pooling , connections are reused rather than created each time a connection is requested. 
Connection pooling is performed in the background and does not affect how an application is coded; however, 
the application must use a DataSource object to obtain a connection instead of using the DriverManager class. 
The lookup returns a connection from the pool if one is available. in my configuration, there are 100 connection in connection pool, 
max idle connections are 30, i have added autoconnect=true.
The closing event on a pooled connection signals the pooling module to place the connection back in the connection pool for future reuse.



    - #### Explain how Connection Pooling works with two backend SQL.
    
The Master-Slave configuration is implemented in MySQL. 
Master allows read and write operations while the slave aloows only read operation. 
Used two AWS VM Servers with Tomcat and Mysql server running on each of them. 
The Connection pooling helps managing the connections to the database more efficiently. 
    
 
- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
The following below files were changed for read-only slave master
cs122b-spring20-team-3/WebApp/src/main/java/ConfirmationServlet.java

cs122b-spring20-team-3/WebApp/src/main/java/DashboardServlet.java

cs122b-spring20-team-3/WebApp/src/main/java/EmployeeLoginServlet.java

cs122b-spring20-team-3/WebApp/src/main/java/LoginServlet.java

cs122b-spring20-team-3/WebApp/src/main/java/MovieListServlet.java

cs122b-spring20-team-3/WebApp/src/main/java/PaymentServlet.java

cs122b-spring20-team-3/WebApp/src/main/java/SearchServlet.java

cs122b-spring20-team-3/WebApp/src/main/java/SingleMovieServlet.java

cs122b-spring20-team-3/WebApp/src/main/java/SingleStarServlet.java

cs122b-spring20-team-3/WebApp/src/main/java/VerifyPassword.java

for masterdb, I have changed below files
cs122b-spring20-team-3/WebApp/src/main/java/UpdateSecurePassword.java

cs122b-spring20-team-3/WebApp/src/main/java/SAXParserActor.java

cs122b-spring20-team-3/WebApp/src/main/java/SAXParserStar.java

cs122b-spring20-team-3/WebApp/src/main/java/SAXParserMovie.java
 
    - #### How read/write requests were routed to Master/Slave SQL?
  I created two resources, for read only operations, resource for slave is used and for write/update masterdb resource is used
    resource moviedb is for slave DB
    <Resource name="jdbc/moviedb"
          auth="Container"
          driverClassName="com.mysql.jdbc.Driver"
          factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
          type="javax.sql.DataSource"
          maxTotal="100" maxIdle="30" maxWaitMillis="10000"
          username="mytestuser"
          password="mypassword"
          url="jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&amp;useSSL=false&amp;cachePrepStmts=true" />
3. for masterDB (insert/update operations), I am using jdbc/masterdb 
<Resource name="jdbc/masterdb"
          auth="Container"
          driverClassName="com.mysql.jdbc.Driver"
          factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
          type="javax.sql.DataSource"
          maxTotal="100" maxIdle="30" maxWaitMillis="10000"
          username="mytestuser"
          password="mypassword"
          url="jdbc:mysql://172.31.24.250:3306/moviedb?autoReconnect=true&amp;useSSL=false&amp;cachePrepStmts=true"/>
 
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
| Case 1: HTTP/1 thread                          | log_processing_single_cp_1.txt | 100 ms                     | 7,540,362 ms                       | 6,271,671 ms              | In our test scenario, this test case has the best performance, even better than multithreading. This must be because during the initialization time, when the connections are first opened, there is an execution expense of creating the 10 threads. Since the duration of these tests was not enough, multithreading did not benefit much in execution time. |
| Case 2: HTTP/10 threads                        | log_processing_single_cp_10.txt |  235 ms                     | 7,876,373 ms                        | 6,616,776 ms              | HTTP connection pooling was faster than no connection pooling and faster than HTTPS. This must be because connection pooling sped up the execution time. |
| Case 3: HTTPS/10 threads                       | log_processing_https.txt | 203 ms                          | 9,5902,816 ms                        | 6,991,626 ms              | HTTPS with connection pooling is slower than HTTP with connection pooling, both with 10 threads. This is because security enabled, there is a SSL handshake at connection level that results in execution cost and makes it little slower than HTTP.            |
| Case 4: HTTP/10 threads/No connection pooling  | log_processing_single_ncp_10.txt | 611 ms                    | 113,727,073,886 ms                    | 7,987,877 ms                 | This test case with no connection pooling was much slower than the test case with connection pooling. This must be because there is no connection management and reuse of the connections happening costing the increase in execution time operations. |
 
| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1 AWS: HTTP/1 thread                          | log_processing_apache_master_cp_1.txt          | 115 ms                                 | M = 1,520,610; S = 1,370,663 ms      | M = 614,266 ms; S = 624,810 ms        | For both scaled instances, the Jmeter tests produced 2 sets of data, one on master instance, another on slave instance. On the AWS Load Balancer, again 1 thread was faster than 10 threads, this must be because during the initialization time, when the connections are first opened, there is an execution expense of creating the 10 threads. Since the duration of these tests was not enough, multithreading did not benefit much in execution time. In addition, because connection pooling is enabled, it was even faster.          |
| Case 2 AWS: HTTP/10 threads                        | log_processing_apache_master_cp_10.txt         | 109 ms                                  | M = 3,371,767 ms; S = 3,508,806 ms             | M = 2,524,242 ms; S = 2,648,711 ms     | The 10 threads was faster than no connection pooling. This must be because connection pooling sped up the execution time.          |
| Case 3 AWS: HTTP/10 threads/No connection pooling  | log_processing_apache_master_ncp_10.txt        | 124 ms                                 | M = 5,815,637 ms; S = 5,891,018 ms              |  M = 4,552,608 ms; S = 4,519,064 ms  | No connection pooling led to the slowest execution time for the AWS load balancer. This must be because there is no connection management and reuse of the connections happening costing the increase in execution time operations.  |

|**Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1 GCP: HTTP/1 thread                          | log_processing_gcp_master_cp_1.txt  | 108 ms                         | M = 1,348,639 ms; S = 1,475,805 ms   | M = 607,806 ms; S = 642,546 ms     | GCP 1 thread is faster than the other tests, this may be because the duration of the test was not enough for the multithreading to be faster than single thread. |
| Case 2 GCP: HTTP/10 threads                        | log_processing_gcp_master_cp_10.txt    | 100 ms                      | M = 3,662,841 ms; S = 3,671,920 ms      | M = 2,788,663 ms; S = 2,786,886 ms    | The 10 threads was faster than no connection pooling. This must be because connection pooling sped up the execution time.    |
| Case 3 GCP: HTTP/10 threads/No connection pooling  | log_processing_gcp_master_ncp_10.txt   | 117 ms                       | M = 7,151,798,900 ms; S = 6,234,668 ms   | M = 4,601,459 ms; S = 4,281,205 ms | Since the GCP is on an external Google Cloud platform, not on the AWS network platform, there is a network latency for execution time. This is why the time here is slower than the others, in addition to no connection pooling enabled. |
