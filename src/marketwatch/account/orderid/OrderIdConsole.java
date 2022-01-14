package marketwatch.account.orderid;

import java.sql.*;
import java.util.Scanner;

import marketwatch.exception.IdFormatException;
import marketwatch.exception.OutOfRangeException;

public class OrderIdConsole {

	//DO NOT use external package (especially marketwatch.tools.PrintClass)
	//Use only standard libraries
	
	private static final String url = "jdbc:mysql://localhost/account";
	private static final String user = "OrderId";
	private static final String password = "OiConsole";
	
	private static OrderId orderId = new OrderId();
	
	private static void newId(String[] input) {
		if (input.length == 1) {
			String query = "SELECT MAX(id) AS id FROM cryptoOrder";
			ResultSet rs = null;
			try (Connection conn = DriverManager.getConnection(url, user, password);
					Statement stmt = conn.createStatement()) {
				rs = stmt.executeQuery(query);
				long lastId;
				if (rs.next()) {
					lastId = rs.getLong("id");
				}
				else {
					lastId = 0L;			
				}
				long id = orderId.generateId(lastId);
				System.out.println("New ID: " + id);
			}
			
			catch (SQLException | IdFormatException | OutOfRangeException e) {
				System.out.println(e);
			}
			finally {
				try {
					rs.close();
				}
				catch (SQLException se) {
					System.out.println(se);
				}
			}
		}
		
		else if (input.length == 2) {
			try {
				long lastId = Long.parseLong(input[1]);
				long id = orderId.generateId(lastId);
				System.out.println("New ID: " + id);
			}
			catch (NumberFormatException nfe) {
				System.out.println("Error_OIC010: \"New\" argument is not number format.");
			}
			catch (IdFormatException | OutOfRangeException e) {
				System.out.println(e);
			}
		}
		
		else {
			System.out.println("Error_OIC002: Argument is incorrect.");
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Order ID Console 1.0.0");
		System.out.println();
		
		Scanner scan = new Scanner(System.in);
		boolean nextLoop = true;
		while (nextLoop) {
			System.out.print("> ");
			String[] input = scan.nextLine().split(" ");
			switch (input[0].toLowerCase()) {
			case "new":
				newId(input);
				break;
			
			case "exit":
				System.out.println("Confirm terminate (Y/N): ");
				String temp = scan.nextLine().toLowerCase();
				if (temp.equals("y")) {
					System.out.println("-----Terminate-----");
					nextLoop = false;
				}
				break;
			default:
				System.out.println("Error_OIC001: Function is incorrect.");
			}
			System.out.println();
		}
		
		scan.close();
	}
}