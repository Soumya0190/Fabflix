package main.java;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class VerifyPassword
{
	public static void main(String[] args) throws Exception
	{
		System.out.println(verifyCredentials("a@email.com", "a2"));
		System.out.println(verifyCredentials("a@email.com", "a3"));
	}

	private static boolean verifyCredentials(String email, String password) throws Exception
	{
		String loginUser = "mytestuser";
		String loginPasswd = "mypassword";
		String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
		Statement statement = connection.createStatement();

		String query = String.format("SELECT * from customers where email='%s'", email);

		ResultSet rs = statement.executeQuery(query);

		boolean success = false;
		if (rs.next())
		{
			String encryptedPassword = rs.getString("password");
			success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
		}

		rs.close();
		statement.close();
		connection.close();
		
		System.out.println("verify " + email + " - " + password);
		return success;
	}

}
