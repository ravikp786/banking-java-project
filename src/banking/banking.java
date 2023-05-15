package banking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class banking {
	public static void main(String args[]) //main class of banking
		throws IOException
	{

		BufferedReader sc = new BufferedReader(
			new InputStreamReader(System.in));
		String customer_secret;
		int customer_id;
		int ch;
		String customer_name;
		int balance;
		
		while (true) {
			System.out.println(
				"\n ->|| Welcome to MyJavaBank ||<- \n");
			System.out.println("1)Login Account");
			System.out.println("2)Register Account");
			System.out.println("3)Exit");

			System.out.print("\n Enter Input:"); //user input
			ch = Integer.parseInt(sc.readLine());

			switch (ch) {
				case 1:
					try {
						System.out.print(
							"Enter CustomerID:");
						customer_id = Integer.parseInt(sc.readLine());
						System.out.print(
							"Enter Password:");
						customer_secret = sc.readLine();
	
						if (bankManagement.loginAccount(
								customer_id, customer_secret)) {
							System.out.println(
								"MSG : Login Successfully!\n");
						}
						else {
							System.out.println(
								"ERR : login Failed!\n");
							System.exit(1);
						}
					}
					catch (Exception e) {
						System.out.println(
							" ERR : Enter Valid Data::Login Failed!\n");
					}
	
					break;
					
				case 2:
					try {
						System.out.print(
							"Enter Customer Name:");
						customer_name = sc.readLine();
						System.out.print(
							"Enter Unique CustomerID:");
						customer_id = Integer.parseInt(sc.readLine());
						System.out.print(
							"Enter New Password:");
						customer_secret = sc.readLine();
						System.out.print(
							"Initial Deposit:");
						balance = Integer.parseInt(sc.readLine());
							
	
						if (bankManagement.createAccount(customer_id, customer_name, customer_secret, balance)) {
							System.out.println(
								"MSG : Account Created Successfully!\n");
							System.exit(0);
						}
						else {
							System.out.println(
								"ERR : Account Creation Failed!\n");
						}
					}
					catch (Exception e) {
						System.out.println(
							" ERR : Enter Valid Data::Insertion Failed!\n");
						System.exit(1);
					}
					break;
				
				case 3:
					System.out.println(
						"Exited Successfully!\n\n Thank You :)");
					System.exit(0);
					
				default:
					System.out.println("Invalid Entry!\n");
					break;
			}
			sc.close();
		}
	}
}
