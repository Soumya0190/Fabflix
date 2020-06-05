package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ParseLogFile {


        public static void main(String[] args) {

            //readFile("/Users/pratyushsharma/documents/122B/logfiles/log_processing1thread.txt", "Single instance with connection Pool - Single thread");
            readFile("/Users/pratyushsharma/documents/122B/logfiles/log_processingsingle.txt", "Single instance with connection Pool -  10 threads");
            readFile("/Users/pratyushsharma/documents/122B/logfiles/log_processingAWSmaster.txt", "AWS Master with connection Pool -  10 threads");
            readFile("/Users/pratyushsharma/documents/122B/logfiles/log_processingAWSslave.txt", "AWS Slave with connection Pool -  10 threads");
            readFile("/Users/pratyushsharma/documents/122B/logfiles/log_processingAWSLB.txt", "AWS Load Balancer with connection Pool -  10 threads");
          //  readFile("/Users/pratyushsharma/documents/122B/logfiles/log_processing_noConnectionPool.txt", "Single instance with no connection Pool - 10 threads");
            readFile("documents/122B/logfiles/GCP/log_processing_gcp.txt", "GCP with connection pool: 10 threads");

        }
private static void readFile(String filename, String mName){
    try {
        File myObj = new File(filename);
        Scanner myReader = new Scanner(myObj);
        int count=0; long totalservLetTime=0; long JDBCservletTime =0;
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if (data != null && data.length() >0 ) {
                String[] temp = data.split(",");
                String servletTime = temp[0].toString().split(":")[1];
                String jdbcTime = temp[1].toString().split(":")[1];
                totalservLetTime = totalservLetTime + Long.parseLong(servletTime);
                JDBCservletTime = JDBCservletTime + Long.parseLong(jdbcTime);
                count++;
            }
        }
        myReader.close();
        long avgServletTime = totalservLetTime/count;
        long avgJDBCServletTime= JDBCservletTime/count;
        System.out.println(mName + "  - Average ServletTime = "+ avgServletTime + ",  Average JDBC Execution Time=" + avgJDBCServletTime);
    } catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}
}


