package marketwatch.activewatcher;

import java.math.*;
import java.sql.SQLException;

import org.json.simple.JSONArray;

import marketwatch.demoaccount.*;
import marketwatch.exception.*;
import marketwatch.indicator.*;

/** Edit expert advisor in this class. */
public class ExpertAdvisor {
	
	private JSONArray price = new JSONArray();
	
	/** Resets variables to null or default value. */
	public void reset() {
		price.clear();
	}
	
	/** Removes array of price, sets a new array of price, and calculates moving average.
	 * @param price Array of price */
	public void setPrice(JSONArray price) {
		reset();
		this.price = price;
	}
	
	/** Adds new price at the last element of array of price, and calculates moving average.
	 * @param price New price */
	@SuppressWarnings("unchecked")
	public void addPrice(BigDecimal price) {
		this.price.add(price);
	}
	
	private DemoAccount demoAccount = new DemoAccount(DemoAccount.DatabaseMode.MySQL, "jdbc:mysql://localhost/marketwatch", "MarketWatch", "MwEa");
	private int i = 0;
	private MACD macd = new MACD(MA.Type.EMA, 5, 20);
	private int currentPosition = 0;
	/** Main function */
	public void run() {
		int lastIndex = price.size() - 1;
		BigDecimal lastPrice = new BigDecimal(price.get(lastIndex).toString()); 
		macd.addPrice(lastPrice);
		
		BigDecimal lastResult = macd.getResultLast();
		System.out.println(i + ": " + lastResult);
		
		if (lastResult.compareTo(new BigDecimal("0.0")) <= 0 & currentPosition == 0) {
			try {
				demoAccount.tradeBuy("BTC", "THB", new BigDecimal("5000"), lastPrice);
				currentPosition = 1;
				System.out.println("BUY BTC, Price = " + lastPrice + " BTC/THB");
			}
			catch (DatabaseModeException | SQLException | NotExistCurrencyException | NotEnoughAmountException | AmountFormatException e) {
				System.out.println("Price =  " + lastPrice + " BTC/THB");
				e.printStackTrace();
			}
			finally {
				try {
					System.out.println("BTC: " + demoAccount.getAmount("BTC"));
					System.out.println("THB: " + demoAccount.getAmount("THB"));
					System.out.println();
				}
				catch (DatabaseModeException | SQLException | NotExistCurrencyException e) {}
			}
		}
		else if (lastResult.compareTo(new BigDecimal("0.0")) > 0 & currentPosition == 1) {
			try {
				if (demoAccount.getAmount("BTC").compareTo(new BigDecimal("0.0")) > 0) {
					demoAccount.tradeSellAll("BTC", "THB", lastPrice);
					currentPosition = 0;
					System.out.println("SELL BTC, Price =  " + lastPrice + " BTC/THB");
					System.out.println("BTC: " + demoAccount.getAmount("BTC"));
					System.out.println("THB: " + demoAccount.getAmount("THB"));
					System.out.println();
				}
			}
			catch (DatabaseModeException | SQLException | NotExistCurrencyException | NotEnoughAmountException | AmountFormatException e) {
				System.out.println("Price =  " + lastPrice + " BTC/THB");
				e.printStackTrace();
			}
			finally {
				try {
					System.out.println("BTC: " + demoAccount.getAmount("BTC"));
					System.out.println("THB: " + demoAccount.getAmount("THB"));
					System.out.println();
				}
				catch (DatabaseModeException | SQLException | NotExistCurrencyException e) {}
			}
		}
		
		i++;
	}
}