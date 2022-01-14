package marketwatch.demoaccount;

import java.math.*;
import java.sql.*;

import org.json.simple.JSONObject;

import marketwatch.exception.*;
import marketwatch.tools.DatabaseSetting;

/** Stores currency and amount in the wallet. */
public class Wallet extends DatabaseSetting {
	
	//Variable for memory mode
	protected JSONObject memoryWallet;

	/** Constructor.
	 * Sets database mode as memory. */
	public Wallet() {
		super();
	}
	
	/** Constructor
	 * Sets only database mode.
	 * @param databaseMode Database mode */
	public Wallet(DatabaseMode databaseMode) {
		super(databaseMode);
	}
	
	/** Constructor.
	 * Sets database mode. Set specific user name, password, and URL.
	 * @param databaseMode Database mode
	 * @param url Database URL
	 * @param user User name
	 * @param password Password */
	public Wallet(DatabaseMode databaseMode, String url, String user, String password) {
		super(databaseMode, url, user, password);
	}

	/** Clears all currency and amount in the wallet. All currencies will be removed.
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public void reset() throws DatabaseModeException, SQLException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			reset_memory();
			return;
		case MySQL:
			reset_mySql();
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}

	protected void reset_memory() {
		memoryWallet.clear();
	}
	
	protected void reset_mySql() throws SQLException {
		String query = "DELETE FROM demowallet";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
		}
	}
	
	/** Gets amount of every currency in the wallet.
	 * @return Map of amount of currency in the wallet
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public JSONObject getAmount() throws DatabaseModeException, SQLException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return getAmount_memory();
		case MySQL:
			return getAmount_mySql();
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	protected JSONObject getAmount_memory() {
		return memoryWallet;
	}
	
	@SuppressWarnings("unchecked")
	protected JSONObject getAmount_mySql() throws SQLException {
		String query = "SELECT currency, amount FROM demowallet";
		JSONObject wallet = new JSONObject();
		try (Connection conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				wallet.put(rs.getString("currency"), rs.getBigDecimal("amount"));
			}
		}
		return wallet;
	}
	
	/** Gets amount of a specific currency in the wallet.
	 * @param currency Currency in the  wallet
	 * @return Amount of currency in the wallet
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet. */
	public BigDecimal getAmount(String currency) throws DatabaseModeException, SQLException, NotExistCurrencyException {
		currency = currency.toUpperCase();
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return getAmount_memory(currency);
		case MySQL:
			return getAmount_mySql(currency);
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	protected BigDecimal getAmount_memory(String currency) throws NotExistCurrencyException {
		if (memoryWallet.containsKey(currency)) {
			return new BigDecimal(memoryWallet.get(currency).toString());
		}
		else {
			throw new NotExistCurrencyException("\"" + currency + "\" is not found in the wallet.");
		}
	}
	
	protected BigDecimal getAmount_mySql(String currency) throws SQLException, NotExistCurrencyException {
		String query = "SELECT * FROM demowallet WHERE currency = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, currency.toUpperCase());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getBigDecimal("amount");
			}
			else {
				throw new NotExistCurrencyException("\"" + currency + "\" is not found in the wallet.");
			}
		}
	}
	
	/** Check whether or not there is a specific currency in the wallet.
	 * @param currency Currency to be checked
	 * @return If the currency is in the wallet, returns true; if not, returns false.
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed. */
	public boolean containsCurrency(String currency) throws DatabaseModeException, SQLException {
		currency = currency.toUpperCase();
		try {
			getAmount(currency);
			return true;
		}
		catch (NotExistCurrencyException nece) {
			return false;
		}
	}
	
	/** Changes specific amount of a specific currency in the wallet.
	 * @param currency Currency to be changed
	 * @param amount New amount
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public void changeCurrency(String currency, BigDecimal amount) throws DatabaseModeException, SQLException {
		currency = currency.toUpperCase();
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			changeCurrency_memory(currency, amount);
			return;
		case MySQL:
			changeCurrency_mySql(currency, amount);
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void changeCurrency_memory(String currency, BigDecimal amount) {
		memoryWallet.put(currency, amount);
	}
	
	protected void changeCurrency_mySql(String currency, BigDecimal amount) throws DatabaseModeException, SQLException {
		boolean existCurrency;
		existCurrency = containsCurrency(currency);
		
		if (existCurrency) {
			String query = "UPDATE demowallet SET amount = ? WHERE currency = ?";
			try (Connection conn = DriverManager.getConnection(url, user, password);
					PreparedStatement pstmt = conn.prepareStatement(query)) {
				pstmt.setBigDecimal(1, amount);
				pstmt.setString(2, currency.toUpperCase());
				pstmt.executeUpdate();
			}
		}
		else {
			String query = "INSERT INTO demowallet VALUES (?, ?)";
			try (Connection conn = DriverManager.getConnection(url, user, password);
					PreparedStatement pstmt = conn.prepareStatement(query)) {
				pstmt.setString(1, currency.toUpperCase());
				pstmt.setBigDecimal(2, amount);
				pstmt.executeUpdate();
			}
		}
	}
	
	/** Increases amount of a specific currency in the wallet.
	 * @param currency Currency to be increased
	 * @param amount Amount to be increased
	 * @return New amount after increasing
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws AmountFormatException If amount is less than zero. */
	public BigDecimal increaseCurrency(String currency, BigDecimal amount) throws DatabaseModeException, DatabaseModeException, SQLException, AmountFormatException {
		//#### First: Check amount format ####
		if (amount.compareTo(zeroDecimal) < 0) {
			throw new AmountFormatException("Amount is less than zero.");
		}
		
		//#### Second: Get current amount in the wallet ####
		BigDecimal oldAmount;
		try {
			//Currency is found in the wallet
			oldAmount = getAmount(currency);
		}
		catch (NotExistCurrencyException nece) {
			//New currency in the wallet
			oldAmount = zeroDecimal;
		}
		BigDecimal newAmount = oldAmount.add(amount);
		
		//#### Third: Update new amount in the wallet ####
		changeCurrency(currency, newAmount);
		return newAmount;
	}

	/** Decreases amount of currency in wallet.
	 * @param currency Currency to be decreased
	 * @param amount Amount to be decreased
	 * @return New amount after decreasing
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotExistCurrencyException If the currency is not found in the wallet.
	 * @throws NotEnoughAmountException If amount of the currency in the wallet is not enough.
	 * @throws AmountFormatException If amount is less than zero. */
	public BigDecimal decreaseCurrency(String currency, BigDecimal amount) throws DatabaseModeException, SQLException, NotExistCurrencyException, NotEnoughAmountException, AmountFormatException {
		//#### First: Check amount format ####
		if (amount.compareTo(zeroDecimal) < 0) {
			throw new AmountFormatException("Amount is less than zero.");
		}
		
		//#### Second: Get current amount in the wallet ####
		BigDecimal oldAmount;
		oldAmount = getAmount(currency);
		
		//#### Third: Check if current amount is less than decreased amount ####
		BigDecimal newAmount = oldAmount.subtract(amount);
		if (newAmount.compareTo(zeroDecimal) < 0) {
			throw new NotEnoughAmountException("Amount of \"" + currency + "\" is not enough to decrease.");
		}
		
		//#### Forth: Update new amount in the wallet ####		
		changeCurrency(currency, newAmount);
		return newAmount;
	}
}