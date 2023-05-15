package banking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class bankManagement { // these class provides all

	private static final int NULL = 0;

	static Connection con = connection.getConnection();
	static String sql = "";
	public static boolean createAccount(int customer_id, String customer_name, String customer_secret, int balance) {
		try {
			if (customer_name == "" || customer_secret == "") {
				System.out.println("All Field Required!");
				return false;
			}
			// query
			Statement st = con.createStatement();
			sql = "INSERT INTO customer(customer_id,customer_name,customer_secret,balance) values(" +
				customer_id + "," +
				'"' + customer_name + '"' + "," +
				'"' + customer_secret  + '"' + "," +
				balance + ");";
				

			// Execution
			if (st.executeUpdate(sql) == 1) {
				System.out.println(customer_name
								+ ", Now You Login!");
				return true;
			}
			// return
		}
		catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Username Not Available!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean depositMoney(int customer_id, int balance) {
		try {
			if (customer_id == NULL || balance == NULL) {
				System.out.println("All Field Required!");
				return false;
			}
			con.setAutoCommit(false);
			Statement st = con.createStatement();

			con.setSavepoint();
			sql = "update customer set balance=balance+"
				+ balance + " where customer_id=" + customer_id;
			if (st.executeUpdate(sql) == 1) {
				System.out.println("Amount Credited!");
			}
			con.commit();
			return true;
		}
		catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Username Not Available!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean loginAccount(int customer_id, String customer_secret) // login method
	{
		try {
			if (customer_id == NULL || customer_secret == "") {
				System.out.println("All Field Required!");
				return false;
			}
			// query
			sql = "select * from customer where customer_id='"
				+ customer_id + "' and customer_secret=" + customer_secret;
			PreparedStatement st
				= con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			// Execution
			BufferedReader sc = new BufferedReader(
				new InputStreamReader(System.in));

			if (rs.next()) {
				// after login menu driven interface method

				int ch = 5;
				int amt = 0;
				int sender_customer_id = rs.getInt("customer_id");
				int receive_customer_id;
				while (true) {
					try {
						System.out.println(
							"Hallo, "
							+ rs.getString("customer_name"));
						System.out.println(
							"1)Transfer Money");
						System.out.println("2)View Balance");
						System.out.println("3)Deposit");
						System.out.println("5)LogOut");

						System.out.print("Enter Choice:");
						ch = Integer.parseInt(
							sc.readLine());
						if (ch == 1) {
							System.out.print(
								"Enter Receiver A/c No:");
							receive_customer_id = Integer.parseInt(
								sc.readLine());
							System.out.print(
								"Enter Amount:");
							amt = Integer.parseInt(
								sc.readLine());

							if (bankManagement.transferMoney(sender_customer_id, receive_customer_id,amt)) {
								System.out.println(
									"MSG : Money Sent Successfully!\n");
							}
							else {
								System.out.println(
									"ERR : Failed!\n");
							}
						}
						else if (ch == 2) {

							bankManagement.getBalance(
								sender_customer_id);
						}
						else if (ch == 3) {
							System.out.print("Enter Amount to Deposit");
							int balance = Integer.parseInt(sc.readLine());
							bankManagement.depositMoney(customer_id, balance);
						}
						else if (ch == 5) {
							break;
						}
						else {
							System.out.println(
								"Err : Enter Valid input!\n");
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else {
				return false;
			}
			// return
			return true;
		}
		catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Customer Not Available!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void
	getBalance(int acNo) // fetch balance method
	{
		try {

			// query
			sql = "select * from customer where customer_id="
				+ acNo;
			PreparedStatement st
				= con.prepareStatement(sql);

			ResultSet rs = st.executeQuery(sql);
			System.out.println(
				"-----------------------------------------------------------");
			System.out.printf("%12s %10s %10s\n",
							"Account No", "Name",
							"Balance");

			// Execution

			while (rs.next()) {
				System.out.printf("%12d %10s %10d.00\n",
								rs.getInt("customer_id"),
								rs.getString("customer_name"),
								rs.getInt("balance"));
			}
			System.out.println(
				"-----------------------------------------------------------\n");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean transferMoney(int sender_ac,int reveiver_ac,int amount)
		throws SQLException // transfer money method
	{
		// validation
		if (reveiver_ac == NULL || amount == NULL) {
			System.out.println("All Field Required!");
			return false;
		}
		if (reveiver_ac == sender_ac) {
			System.out.println("Sender receiver must be different!");
			return false;
		}
		try {
			
			PreparedStatement rps = con.prepareStatement("select * from customer where customer_id=" + reveiver_ac);
			ResultSet rrs = rps.executeQuery();

			if (rrs.next()) {
				try {
					if(rrs.getInt("customer_id") == NULL) {
						System.out.println(
							"Invalid Receiver!");
						return false;
					}
				}
				catch (Exception e) {
					System.out.println(
						"Invalid Receiver!");
					return false;
				}
			}
			
			con.setAutoCommit(false);
			sql = "select * from customer where customer_id=" + sender_ac;
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				if (rs.getInt("balance") < amount) {
					System.out.println(
						"Insufficient Balance!");
					return false;
				}
			}

			Statement st = con.createStatement();

			// debit
			con.setSavepoint();

			sql = "update customer set balance=balance-"
				+ amount + " where customer_id=" + sender_ac;
			if (st.executeUpdate(sql) == 1) {
				System.out.println("Amount Debited!");
			}

			// credit
			sql = "update customer set balance=balance+"
				+ amount + " where customer_id=" + reveiver_ac;
			st.executeUpdate(sql);

			con.commit();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			con.rollback();
		}
		// return
		return false;
	}
}
