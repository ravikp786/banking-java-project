package banking;

import java.sql.*;

// Global connection Class
public class connection {
	static Connection con; // Global Connection Object
	public static Connection getConnection()
	{
		try {
			String url = "jdbc:mysql://localhost:3306/bank";
			String user = "root";
			String pass = "ValidPassword";
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
		}
		catch (Exception e) {
			System.out.println("Connection Failed!");
			System.out.println(e);
		}

		return con;
	}
}
