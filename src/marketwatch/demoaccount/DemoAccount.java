package marketwatch.demoaccount;

import java.math.*;
import java.sql.*;

import marketwatch.exception.*;
import marketwatch.tools.TradeMode;

/** Demo account for back test and real time test. */
public class DemoAccount extends Wallet {
	
	/** Constructor.
	 * Sets database mode as memory. */
	public DemoAccount() {
		super();
	}
	
	/** Constructor
	 * Sets only wallet mode.
	 * @param databaseMode Database mode */
	public DemoAccount(DatabaseMode databaseMode) {
		super(databaseMode);
	}
	
	/** Constructor.
	 * Sets database mode. Set specific user name, password, and URL.
	 * @param databaseMode Database mode
	 * @param url Database URL
	 * @param user User name
	 * @param password Password */
	public DemoAccount(DatabaseMode databaseMode, String url, String user, String password) {
		super(databaseMode, url, user, password);
	}
	
	/** Buys or sells a currency with another currency.
	 * @param currency1 Currency_1
	 * @param currency2 Currency_2
	 * @param amountSold Amount of currency to be sold
	 * @param price Price as currency_1 per currency_2
	 * @param tradeMode Buy or sell currency_1
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException 
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void trade(String currency1, String currency2, BigDecimal amountSold, BigDecimal price, TradeMode tradeMode) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		//Declare new variables as buy mode.
		//Do not use parameters directly. It may be confusing.
		String cr1 = currency1;
		String cr2 = currency2;
		BigDecimal amountBought = amountSold.divide(price, 10, RoundingMode.HALF_UP);
		switch (tradeMode) {
		case Buy:
			//Nothing to do
			//Variables have been set.
			break;
		case Sell:
			//Swap buy and sell variables.
			String temp = cr1;
			cr1 = cr2;
			cr2 = temp;
			amountBought = amountSold;
			amountSold = amountBought.divide(price, 10, RoundingMode.HALF_UP);
			break;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
		
		//Buy cr1 by selling cr2 at prc
		amountSold = amountSold.setScale(10, RoundingMode.HALF_UP);
		decreaseCurrency(cr2, amountSold);
		increaseCurrency(cr1, amountBought);
	}
	
	/** Buys specific amount of currency1 with currency2.
	 * @param amount1 Amount of currency1
	 * @param currency1 Currency to be bought
	 * @param currency2 Currency to be sold
	 * @param price Price as currency1 per currency2
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void tradeSecondToFirst(BigDecimal amount1, String currency1, String currency2, BigDecimal price) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		BigDecimal amount2 = amount1.multiply(price);
		trade(currency1, currency2, amount2, price, TradeMode.Buy);
	}

	/** Buys currency1 with specific amount of currency2.
	 * @param currency1 Currency to be bought
	 * @param currency2 Currency to be sold
	 * @param amount2 Amount of currency2
	 * @param price Price as currency1 per currency2
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void tradeSecondToFirst(String currency1, String currency2, BigDecimal amount2, BigDecimal price) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		trade(currency1, currency2, amount2, price, TradeMode.Buy);
	}
	
	/** Buys currency1 with total amount of currency2.
	 * @param currency1 Currency to be bought
	 * @param currency2 Currency to be sold
	 * @param price Price as currency1 per currency2
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void tradeSecondToFirstAll(String currency1, String currency2, BigDecimal price) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		BigDecimal amount2 = getAmount(currency2);
		trade(currency1, currency2, amount2, price, TradeMode.Buy);
	}
	
	/** Buys specific amount of currency1 with currency2.
	 * @param amount1 Amount of currency1
	 * @param currency1 Currency to be bought
	 * @param currency2 Currency to be sold
	 * @param price Price as currency1 per currency2
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void tradeBuy(BigDecimal amount1, String currency1, String currency2, BigDecimal price) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		tradeSecondToFirst(amount1, currency1, currency2, price);
	}
	
	/** Buys currency1 with specific amount of currency2.
	 * @param currency1 Currency to be bought
	 * @param currency2 Currency to be sold
	 * @param amount2 Amount of currency2
	 * @param price Price as currency1 per currency2
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void tradeBuy(String currency1, String currency2, BigDecimal amount2, BigDecimal price) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		tradeSecondToFirst(currency1, currency2, amount2, price);
	}
	
	/** Buys currency1 with total amount of currency2.
	 * @param currency1 Currency to be bought
	 * @param currency2 Currency to be sold
	 * @param price Price as currency1 per currency2
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void tradeBuyAll(String currency1, String currency2, BigDecimal price) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		tradeSecondToFirstAll(currency1, currency2, price);
	}
	
	/** Sells specific amount of currency1 with currency2.
	 * @param amount1 Amount of currency1
	 * @param currency1 Currency to be sold
	 * @param currency2 Currency to be bought
	 * @param price Price as currency1 per currency2
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void tradeFirstToSeceond(BigDecimal amount1, String currency1, String currency2, BigDecimal price) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		BigDecimal amount2 = amount1.multiply(price);
		trade(currency1, currency2, amount2, price, TradeMode.Sell);
	}
	
	/** Sells currency1 with specific amount of currency2.
	 * @param currency1 Currency to be sold
	 * @param currency2 Currency to be bought
	 * @param amount2 Amount of currency2
	 * @param price Price as currency1 per currency2
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void tradeFirstToSeceond(String currency1, String currency2, BigDecimal amount2, BigDecimal price) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		trade(currency1, currency2, amount2, price, TradeMode.Sell);
	}
	
	/** Sells total amount of currency1 with currency2.
	 * @param currency1 Currency to be sold
	 * @param currency2 Currency to be bought
	 * @param price Price as currency1 per currency2
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void tradeFirstToSeceondAll(String currency1, String currency2, BigDecimal price) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		BigDecimal amount1 = getAmount(currency1);
		BigDecimal amount2 = amount1.multiply(price);
		trade(currency1, currency2, amount2, price, TradeMode.Sell);
	}
	
	/** Sells specific amount of currency1 with currency2.
	 * @param amount1 Amount of currency1
	 * @param currency1 Currency to be sold
	 * @param currency2 Currency to be bought
	 * @param price Price as currency1 per currency2
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void tradeSell(BigDecimal amount1, String currency1, String currency2, BigDecimal price) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		tradeFirstToSeceond(amount1, currency1, currency2, price);
	}
	
	/** Sells currency1 with specific amount of currency2.
	 * @param currency1 Currency to be sold
	 * @param currency2 Currency to be bought
	 * @param amount2 Amount of currency2
	 * @param price Price as currency1 per currency2
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void tradeSell(String currency1, String currency2, BigDecimal amount2, BigDecimal price) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		tradeFirstToSeceond(currency1, currency2, amount2, price);
	}
	
	/** Sells total amount of currency1 with currency2.
	 * @param currency1 Currency to be sold
	 * @param currency2 Currency to be bought
	 * @param price Price as currency1 per currency2
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough. 
	 * @throws AmountFormatException If amount is less than zero. */
	public void tradeSellAll(String currency1, String currency2, BigDecimal price) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		tradeFirstToSeceondAll(currency1, currency2, price);
	}
}