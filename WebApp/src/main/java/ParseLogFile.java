package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ParseLogFile {


    public static void main(String[] args) {

        readFile("Log Processing Files/log_processing_single_cp_1.txt", "Single Instance HTTP with Connection Pooling - 1 thread");
        readFile("Log Processing Files/log_processing_files/log_processing_single_ncp_10.txt", "Single Instance HTTP without using Connection Pooling - 10 threads");
        readFile("Log Processing Files/log_processing_single_cp_10.txt", "Single Instance HTTP with Connection Pooling - 10 threads");
        readFile("Log Processing Files/log_processing_https.txt", "Single Instance HTTPS with Connection Pooling - 10 threads");

        readFile("Log Processing Files/log_processing_apache_master_ncp_10.txt", "AWS Load Balancer Master without using Connection Pooling -  10 threads");
        readFile("Log Processing Files/log_processing_apache_slave_ncp_10.txt", "AWS Load Balancer Slave without using Connection Pooling -  10 threads");
        readFile("Log Processing Files/log_processing_apache_master_cp_1.txt", "AWS Load Balancer Master with Connection Pooling -  1 thread");
        readFile("Log Processing Files/log_processing_apache_slave_cp_1.txt", "AWS Load Balancer Slave with Connection Pooling -  1 thread");
        readFile("Log Processing Files/log_processing_apache_master_cp_10.txt", "AWS Load Balancer Master with Connection Pooling -  10 threads");
        readFile("Log Processing Files/log_processing_apache_slave_cp_10.txt", "AWS Load Balancer Slave with Connection Pooling -  10 threads");

        readFile("Log Processing Files//log_processing_gcp_master_ncp_10.txt", "GCP Load Balancer Master without using Connection Pooling -  10 threads");
        readFile("Log Processing Files/log_processing_gcp_slave_ncp_10.txt", "GCP Load Balancer Slave without using Connection Pooling -  10 threads");
        readFile("Log Processing Files/log_processing_gcp_master_cp_1.txt", "GCP Load Balancer Master with Connection Pooling -  1 thread");
        readFile("Log Processing Files/log_processing_gcp_slave_cp_1.txt", "GCP Load Balancer Slave with Connection Pooling -  1 thread");
        readFile("Log Processing Files/log_processing_gcp_master_cp_10.txt", "GCP Load Balancer Master with Connection Pooling -  10 threads");
        readFile("Log Processing Files/log_processing_gcp_slave_cp_10.txt", "GCP Load Balancer Slave with Connection Pooling -  10 threads");

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