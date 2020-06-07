# General
    - #### Team#: Team 3

    - #### Names: Soumya Sharma

    - #### Project 5 Video Demo Link: https://youtu.be/YIim8xjZ8vk

    - #### Instruction of deployment:
    
    ## Deployment Instructions:
    Set-up environments:
    1. created three additional VMs using same security group in AWS and 1 in GCP
    2. on AWS: 
        2.1 Created one instance for mysql and tomcat 
            2.1.1 Set-up Master Database 
                2.1.1.1 created image for sql replication, click images and then click AMIs, name image and launch image/instance
                2.1.1.2 Configured mysql as master database by changing cnf/configuration file (bind ip address of sql server, set server_id=1, enable logs )
                2.1.1.3 restart sql server, set inbound rules in security group
            2.1.2 Set up Tomcat and deploy code
                2.1.2.1 install tomcat and Java JDK
                2.1.2.2 cloned code from github, generated **mvn clean package** to generate the **.war** file on tomcat by running **mvn clean package** 
                2.1.2.3 executed all sql files to create stored procedures and other database objects. Ran sql files to populate data in the tables
                2.1.2. 4 ran all three SAXparser files by executing below commands
                        mvn exec:java -Dexec.mainClass=main.java.SAXParserMovie
                        mvn exec:java -Dexec.mainClass=main.java.SAXParserActor
                        mvn exec:java -Dexec.mainClass=main.java.SAXParserStar
        2.2 Created second instance for mysql and tomcat. 
               2.2.1 Set-up Slave Database set-up
                    2.2.1.1 created image cs122b_image for sql replication, click images and then click AMIs, name image and launch image/instance
                    2.2.1.2 Configured mysql as slave database by changing cnf file (bind ip address of sql server, set server_id=2)
                    2.2.1.3 restart sql server, set inbound rules in security group
                    2.2.1.4 verified that data from master database propagated to slave database
               2.2.2 Set up Tomcat and deploy code
                    2.1.2.1 install tomcat and Java JDK
                    2.1.2.2 cloned code from github, generated **mvn clean package** to generate the **.war** file on tomcat by running **mvn clean package** 
                    2.1.2.3 ran all three SAXparser files by executing below commands
                           mvn exec:java -Dexec.mainClass=main.java.SAXParserMovie
                           mvn exec:java -Dexec.mainClass=main.java.SAXParserActor
                           mvn exec:java -Dexec.mainClass=main.java.SAXParserStar
        2.3 Created instance for Apache webserver and configured as load balancer to balance the traffic for above two instances
    3. Created instance on Google Cloud Platform and configured as load balancer to balance the traffic
        3.2.1 Set configuration to allow external ips of other instances and firewall settings to open port 80 
    4. Install and set-up test cases in JMETER 
       4.2.1 Install JMeter tar file from apache site
       4.2.2 Run ./jmeter.sh to open JMETER gui interface
       4.2.3 Set up test cases as per requirements
       4.2.4 Run the tests and collected log files from all the environemnts


    - #### Collaborations and Work Distribution: Solo

- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
    Configuration Files:
    ***cs122b-spring20-team-3/WebApp/web/META-INF/context.xml

    ***cs122b-spring20-team-3/WebApp/web/WEB-INF/web.xml

    Servlet Files:
    ***cs122b-spring20-team-3/WebApp/src/main/java/ConfirmationServlet.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/DashboardServlet.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/EmployeeLoginServlet.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/LoginServlet.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/MovieListServlet.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/PaymentServlet.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/SAXParserActor.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/SAXParserStar.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/SAXParserMovie.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/SearchServlet.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/SingleMovieServlet.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/SingleStarServlet.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/UpdateSecurePassword.java

    ***cs122b-spring20-team-3/WebApp/src/main/java/VerifyPassword.java
    
   - #### Explain how Connection Pooling is utilized in the Fabflix code.
1. For Fabflix, I have added added connection pool settings in resources in context.xml  
2. For read only operations, I have used resource name = jdbc/moviedb
I have used localhost instead of using Slave database for read operations, as master database also supports read operation and going to local database will reduce network latency.
<Resource name="jdbc/moviedb"
          auth="Container"
          driverClassName="com.mysql.jdbc.Driver"
          factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
          type="javax.sql.DataSource"
          maxTotal="100" maxIdle="30" maxWaitMillis="10000"
          username="mytestuser"
          password="mypassword"
          url="jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&amp;useSSL=false&amp;cachePrepStmts=true" />
          
3. For masterDB (insert/update operations), I am using resource name ="jdbc/masterdb"
 
<Resource name="jdbc/masterdb"
          auth="Container"
          driverClassName="com.mysql.jdbc.Driver"
          factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
          type="javax.sql.DataSource"
          maxTotal="100" maxIdle="30" maxWaitMillis="10000"
          username="mytestuser"
          password="mypassword"
          url="jdbc:mysql://<master IP address>:3306/moviedb?autoReconnect=true&amp;useSSL=false&amp;cachePrepStmts=true"/>
          
maxTotal is maximum number of active database connections that can be allocated from the connection pool.
maxIdle is maximum number of connections that should be kept in the pool at all times
maxwait is maximum number of milliseconds that pool waits for a connection to be returned before throwing exception

2. The following changes are made to the servlets where JDBC connection is used

Context initContext = new InitialContext();
Context envContext = (Context) initContext.lookup("java:/comp/env"); 
DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb"); //changes resource name based on command (insert/update or select )
Connection connection = ds.getConnection();

A connection pool operates by performing the work of creating connections ahead of time. 
By adding connection pooling, connections are reused rather than created each time a connection is requested. 
Connection pooling is performed in the background and does not affect how an application is coded; however, 
the application must use a DataSource object to obtain a connection, instead of using the DriverManager class. 
The lookup returns a connection from the pool if one is available. 
A pool of connection objects is created at the time the application server starts.
The closing event on a pooled connection signals the pooling module to place the connection back in the connection pool for future reuse.

    - #### Explain how Connection Pooling works with two backend SQL.
For two backend SQL databases, two resource names are created in context.xml (one for each database). 
Each resource has configuration for connection pool, so two connection pools are created.
For  Master-Slave configuration, Master allows read and write operations while the slave allows only read operations. 
So for all database queries that need insert/update operation, master database connection pool is used. 
But read(select) operation queries are directed to local database to reduce network latency. 
(The slave database can be used for read operations all the time, but going to local database will avoid network issues and improve network latency).
Master instance of tomcat will create one database connection pool, since master database can serve read and write operations.
Slave instance of tomcat will create two database connection pools, since read/select database queries will be directed to slave/local database 
and write(update/insert) database queries will be directed to master database.
The Connection pooling helps managing the connections to the database more efficiently. 
With two database SQL servers, query throughput is improved.
    
 
- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
The following below files were changed for the read-only slave master

***cs122b-spring20-team-3/WebApp/src/main/java/EmployeeLoginServlet.java

***cs122b-spring20-team-3/WebApp/src/main/java/LoginServlet.java

***cs122b-spring20-team-3/WebApp/src/main/java/MovieListServlet.java

***cs122b-spring20-team-3/WebApp/src/main/java/PaymentServlet.java

***cs122b-spring20-team-3/WebApp/src/main/java/SearchServlet.java

***cs122b-spring20-team-3/WebApp/src/main/java/SingleMovieServlet.java

***cs122b-spring20-team-3/WebApp/src/main/java/SingleStarServlet.java

***cs122b-spring20-team-3/WebApp/src/main/java/VerifyPassword.java

The following files were changed for the read and write master database
***cs122b-spring20-team-3/WebApp/src/main/java/ConfirmationServlet.java

***cs122b-spring20-team-3/WebApp/src/main/java/DashboardServlet.java

***cs122b-spring20-team-3/WebApp/src/main/java/UpdateSecurePassword.java

***cs122b-spring20-team-3/WebApp/src/main/java/SAXParserActor.java

***cs122b-spring20-team-3/WebApp/src/main/java/SAXParserStar.java

***cs122b-spring20-team-3/WebApp/src/main/java/SAXParserMovie.java
 
    - #### How read/write requests were routed to Master/Slave SQL?
I created two resources - the "moviedb" resource for slave is used for read only (select) operations, and "masterdb" resource is used for insert/update operations

resource moviedb is for slave DB ; Resource name="jdbc/moviedb" auth="Container" driverClassName="com.mysql.jdbc.Driver" 
factory="org.apache.tomcat.jdbc.pool.DataSourceFactory" type="javax.sql.DataSource" maxTotal="100" maxIdle="30" maxWaitMillis="10000"
username="mytestuser" password="mypassword" url="jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&amp;useSSL=false&amp;cachePrepStmts=true" />


for masterDB (insert/update operations), I am using jdbc/masterdb ; Resource name="jdbc/masterdb" auth="Container"
driverClassName="com.mysql.jdbc.Driver" factory="org.apache.tomcat.jdbc.pool.DataSourceFactory" type="javax.sql.DataSource"
maxTotal="100" maxIdle="30" maxWaitMillis="10000" username="mytestuser" password="mypassword"
url="jdbc:mysql://<master database IP> :3306/moviedb?autoReconnect=true&amp;useSSL=false&amp;cachePrepStmts=true"/>
          
For all the database queries that need insert/update operations, master database resource is used. 
But all the read(select) operation queries are directed to local database to reduce network latency.
 
- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.
    - All the Jmeter log_processing files are created in the /tmp folder for master and slave instance
    - Each file is named according to the test, which is recorded in the README table under the graph results for each test case
    - These files are moved from instance to local machine after executing the following
       - sudo scp -i SSH-Key.pem ubuntu@<instance connection string>:/tmp/<log processing file name> . 
       - Example: sudo scp -i SSH-Key.pem ubuntu@ec2-3-14-14-154.us-east-2.compute.amazonaws.com:/tmp/log_processing_https.txt .
    - All Jmeter logs are stored in the folder titled "Log Processing Files" under "WebApp" folder
    - Run the "ParseLogFile" Servlet on IntelliJ to process the log processing files already stored under Log Processing Files folder
    - The "ParseLogFile" Servlet reads all the files from the folder where all log files are saved and parses through it to output the average times, as shown in the demo video. 
    - This servlet reads each log file and extracts below data
    	1. servlet execution time for getting movie results for each request (start-time is added as first line of servlet code(doGet) and endTime is recorded in the end of servlet code 
   	    2. JDBC execution time(start-time is recorded when database connection is opened and end-time is recorded when connection is closed (for each request))
    After extracting above data, the below data is calculated:
   	 1. Average TS : added  servlet execution time of all the records/requests and divided by total number of requests
   	 2. Average TJ : added JDBC execution time of all the records/requests and divided by total number of requests
   	 
 Alternate method of executing "ParseLogFile" Servlet is to execute the below command
 The parsing script was executed by running the command ** mvn exec:java -Dexec.mainClass=main.java.ParseLogFile ** 

- # JMeter TS/TJ Time Measurement Report
 
| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | graph_results/single__HTTP_cp_1.png; WebApp/Log Processing Files/log_processing_single_cp_1.txt | 100 ms                     | 7,540,362 ms                       | 6,271,671 ms              | In our test scenario, this test case has the best performance, even better than multithreading. This must be because during the initialization time, when the connections are first opened, there is an execution expense of creating the 10 threads. Since the duration of these tests was not enough, multithreading did not benefit much in execution time. |
| Case 2: HTTP/10 threads                        | graph_results/Single_HTTP_noCP_10.png ; WebApp/Log Processing Files/log_processing_single_cp_10.txt |  235 ms                     | 7,876,373 ms                        | 6,616,776 ms              | HTTP connection pooling was faster than no connection pooling and faster than HTTPS. This must be because connection pooling sped up the execution time. |
| Case 3: HTTPS/10 threads                       | graph_results/Single_HTTPS_10_cp.png ; WebApp/Log Processing Files/log_processing_https.txt | 203 ms                          | 9,5902,816 ms                        | 6,991,626 ms              | HTTPS with connection pooling is slower than HTTP with connection pooling, both with 10 threads. This is because security enabled, there is a SSL handshake at connection level that results in execution cost and makes it little slower than HTTP.            |
| Case 4: HTTP/10 threads/No connection pooling  | graph_results/Single_HTTP_noCP_10.png ; lWebApp/Log Processing Files/og_processing_single_ncp_10.txt | 611 ms                    | 113,727,073,886 ms                    | 7,987,877 ms                 | This test case with no connection pooling was much slower than the test case with connection pooling. This must be because there is no connection management and reuse of the connections happening costing the increase in execution time operations. |
 
| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1 AWS: HTTP/1 thread                          | graph_results/AWS-cp-single-thread.png ; WebApp/Log Processing Files/log_processing_apache_master_cp_1.txt ; log_processing_apache_slave_cp_1.txt          | 115 ms                                 | M = 1,520,610; S = 1,370,663 ms      | M = 614,266 ms; S = 624,810 ms        | For both scaled instances, the Jmeter tests produced 2 sets of data, one on master instance, another on slave instance. On the AWS Load Balancer, again 1 thread was faster than 10 threads, this must be because during the initialization time, when the connections are first opened, there is an execution expense of creating the 10 threads. Since the duration of these tests was not enough, multithreading did not benefit much in execution time. In addition, because connection pooling is enabled, it was even faster.          |
| Case 2 AWS: HTTP/10 threads                        | graph_results/AWS_CP_10.png ; WebApp/Log Processing Files/log_processing_apache_master_cp_10.txt ; log_processing_apache_slave_cp_10.txt        | 109 ms                                  | M = 3,371,767 ms; S = 3,508,806 ms             | M = 2,524,242 ms; S = 2,648,711 ms     | The 10 threads was faster than no connection pooling. This must be because connection pooling sped up the execution time.          |
| Case 3 AWS: HTTP/10 threads/No connection pooling  | graph_results/AWS_noCP_10.png ; WebApp/Log Processing Files/log_processing_apache_master_ncp_10.txt ; log_processing_apache_slave_ncp_10.txt        | 124 ms                                 | M = 5,815,637 ms; S = 5,891,018 ms              |  M = 4,552,608 ms; S = 4,519,064 ms  | No connection pooling led to the slowest execution time for the AWS load balancer. This must be because there is no connection management and reuse of the connections happening costing the increase in execution time operations.  |

|**Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1 GCP: HTTP/1 thread                          | graph_results/GCP_CP_1.png; WebApp/Log Processing Files/log_processing_gcp_master_cp_1.txt ; log_processing_gcp_slave_cp_1.txt  | 108 ms                         | M = 1,348,639 ms; S = 1,475,805 ms   | M = 607,806 ms; S = 642,546 ms     | GCP 1 thread is faster than the other tests, this may be because the duration of the test was not enough for the multithreading to be faster than single thread. |
| Case 2 GCP: HTTP/10 threads                        | graph_results/GCP_CP_10.png ; WebApp/Log Processing Files/log_processing_gcp_master_cp_10.txt ; log_processing_gcp_slave_cp_10.txt    | 100 ms                      | M = 3,662,841 ms; S = 3,671,920 ms      | M = 2,788,663 ms; S = 2,786,886 ms    | The 10 threads was faster than no connection pooling. This must be because connection pooling sped up the execution time.    |
| Case 3 GCP: HTTP/10 threads/No connection pooling  | graph_results/GCP_noCP_10.png ; WebApp/Log Processing Files/log_processing_gcp_master_ncp_10.txt ; log_processing_gcp_slave_ncp_10.txt   | 117 ms                       | M = 7,151,798,900 ms; S = 6,234,668 ms   | M = 4,601,459 ms; S = 4,281,205 ms | Since the GCP is on an external Google Cloud platform, not on the AWS network platform, there is a network latency for execution time. This is why the time here is slower than the others, in addition to no connection pooling enabled. |
