package marketwatch.account;

import java.math.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONObject;

import marketwatch.account.orderid.OrderId;
import marketwatch.exception.*;
import marketwatch.tools.DatabaseSetting;
import marketwatch.tools.TradeMode;
import marketwatch.tools.TradeModeConverter;

/** Manages order and cost for every trade. */
public class OrderCost extends DatabaseSetting {
	
	/** Constructor.
	 * Sets database mode as memory. */
	public OrderCost() {
		super();
	}
	
	/** Constructor
	 * Sets only database mode.
	 * @param databaseMode Database mode */
	public OrderCost(DatabaseMode databaseMode) {
		super(databaseMode);
	}
	
	/** Constructor.
	 * Sets database mode. Set specific user name, password, and URL.
	 * @param databaseMode Database mode
	 * @param url Database URL
	 * @param user User name
	 * @param password Password */
	public OrderCost(DatabaseMode databaseMode, String url, String user, String password) {
		super(databaseMode, url, user, password);
	}

	/** Get the last ID in database from orderRecord.
	 * @return The last ID
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	private long getLastId() throws DatabaseModeException, SQLException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return -1;   //TODO
		case MySQL:
			return getLastId_mySql();
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private long getLastId_mySql() throws SQLException {
		String query = "SELECT MAX(id) AS id FROM orderRecord"; 
		try (Connection conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			boolean hasResult = rs.next();
			//Does orderRecord have at least one order?
			if (hasResult) {
				return rs.getLong("id");
			}
			else {
				return 0L;
			}
		}
	}
	
	/** Resets orderRecord to blank table.
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public void resetOrder() throws DatabaseModeException, SQLException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			//TODO
			return;
		case MySQL:
			resetOrder_mySql();
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private void resetOrder_mySql() throws SQLException {
		String query = "DELETE FROM orderRecord";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
		}
	}
	
	/** Adds new order into orderRecord. Fees and taxes are zero.
	 * @param orderTimestamp Date and time
	 * @param position Buy or sell
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-? */
	public void newOrder(long orderTimestamp, TradeMode position, String currency, BigDecimal amount, BigDecimal price) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException {
		newOrder(orderTimestamp, position, currency, amount, price, zeroDecimal, zeroDecimal);
	}
	
	/** Adds new order into orderRecord.
	 * @param orderTimestamp Date and time
	 * @param position Buy or sell
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @param fee Fees
	 * @param tax Taxes
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-? */
	public void newOrder(long orderTimestamp, TradeMode position, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException {
		currency = currency.toUpperCase();
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			//TODO
			return;
		case MySQL:
			newOrder_mySql(orderTimestamp, position, currency, amount, price, fee, tax);
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private void newOrder_mySql(long orderTimestamp, TradeMode position, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException {
		long newId;
		OrderId orderId = new OrderId();
		newId = orderId.generateId(getLastId());
		
		String query = "INSERT INTO orderRecord VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setLong(1, newId);
			
			java.sql.Timestamp timestamp = new java.sql.Timestamp(orderTimestamp);
			pstmt.setTimestamp(2, timestamp);
			
			TradeModeConverter tmc = new TradeModeConverter();
			byte byte_position = tmc.modeToByte(position);
			pstmt.setByte(3, byte_position);
			pstmt.setString(4, currency);
			pstmt.setBigDecimal(5, amount);
			pstmt.setBigDecimal(6, price);
			pstmt.setBigDecimal(7, fee);
			pstmt.setBigDecimal(8, tax);
			pstmt.executeUpdate();
		}
	}

	/** Gets an order from orderRecord.
	 * @param id Order ID
	 * @return Map of Order
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NullResultException If ID is not found. */
	public JSONObject getOrder(long id) throws DatabaseModeException, SQLException, NullResultException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return null;   //TODO
		case MySQL:
			return getOrder_mySql(id);
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getOrder_mySql(long id) throws SQLException, NullResultException {
		String query = "SELECT * FROM orderRecord WHERE id = ?";
		ResultSet rs = null;
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setLong(1, id);
			rs = pstmt.executeQuery();
			boolean hasResult = rs.next();
			if (!hasResult) {
				throw new NullResultException("ID is not found.");
			}
			
			JSONObject obj = new JSONObject();
			obj.put("id", rs.getLong("id"));
			obj.put("orderTimestamp", rs.getTimestamp("orderTimestamp").getTime());
			TradeModeConverter tmc = new TradeModeConverter();
			obj.put("position", tmc.byteToMode(rs.getByte("position")));
			obj.put("currency", rs.getString("currency"));
			obj.put("amount", rs.getBigDecimal("amount"));
			obj.put("price", rs.getBigDecimal("price"));
			obj.put("fee", rs.getBigDecimal("fee"));
			obj.put("tax", rs.getBigDecimal("tax"));
			return obj;
		}
		finally {
			rs.close();
		}
	}

	/** Changes values of specific order ID in orderRecord.
	 * fifoCost and averageCost are not changed by calling this function. Users have to update fifoCost and averageCost manually.
	 * @param id Order ID. This parameter must be valid.
	 * @param orderTimestamp Date and time. Type negative number to not change date and time.
	 * @param position Buy or sell. Type null to not change position.
	 * @param currency Currency. Type null to not change currency.
	 * @param amount Bought amount or sold amount. Type null to not change amount.
	 * @param price Actual price. Type null to not change actual price.
	 * @param fee Fees. Type null to not change fees.
	 * @param tax Taxes. Type null to not change taxes.
	 * @return Previous values
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NullResultException If ID is not found. */
	public JSONObject editOrder(long id, long orderTimestamp, TradeMode position, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, NullResultException {
		currency = currency.toUpperCase();
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return null;   //TODO
		case MySQL:
			return editOrder_mySql(id, orderTimestamp, position, currency, amount, price, fee, tax);
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private JSONObject editOrder_mySql(long id, long orderTimestamp, TradeMode position, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, NullResultException {
		//#### First: Gets previous values ####
		JSONObject previous = getOrder(id);
		
		//#### Second: Declares new variables to store new values ####
		java.sql.Timestamp newOrderTimestamp;
		if (orderTimestamp <= 0) {
			long time = Long.parseLong(previous.get("orderTimestamp").toString());
			newOrderTimestamp = new java.sql.Timestamp(time);
		}
		else {
			newOrderTimestamp = new java.sql.Timestamp(orderTimestamp);
		}
		
		byte newPosition;
		TradeModeConverter tmc = new TradeModeConverter();
		if (position == null) {
			newPosition = tmc.objectToByte(previous.get("position"));
		}
		else {
			newPosition = tmc.modeToByte(position);
		}
		
		String newCurrency;
		if (currency == null) {
			newCurrency = previous.get("currency").toString();
		}
		else {
			newCurrency = currency;
		}
		
		BigDecimal newAmount;
		if (amount == null) {
			newAmount = new BigDecimal(previous.get("amount").toString());
		}
		else {
			newAmount = amount;
		}
		
		BigDecimal newPrice;
		if (price == null) {
			newPrice = new BigDecimal(previous.get("price").toString());
		}
		else {
			newPrice = price;
		}
		
		BigDecimal newFee;
		if (fee == null) {
			newFee = new BigDecimal(previous.get("fee").toString());
		}
		else {
			newFee = fee;
		}
		
		BigDecimal newTax;
		if (tax == null) {
			newTax = new BigDecimal(previous.get("tax").toString());
		}
		else {
			newTax = tax;
		}		
		
		//#### Third: Updates values into fifoCost ####
		String query = "UPDATE fifoCost SET orderTimestamp = ?, position = ?, currency = ?, amount = ?, price = ?, fee = ?. tax = ? WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setTimestamp(1, newOrderTimestamp);
			pstmt.setByte(2, newPosition);
			pstmt.setString(3, newCurrency);
			pstmt.setBigDecimal(4, newAmount);
			pstmt.setBigDecimal(5, newPrice);
			pstmt.setBigDecimal(6, newFee);
			pstmt.setBigDecimal(7, newTax);
			pstmt.setLong(8, id);
			pstmt.executeUpdate();
		}
		
		//#### Forth: Returns previous values ####
		return previous;
	}
	
	/** Calculates sum of total cost.
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @param fee Fees
	 * @param tax Taxes
	 * @return Total cost */
	public BigDecimal sumCost(BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) {
		BigDecimal cost = amount.multiply(price);
		cost = cost.add(fee);
		cost = cost.add(tax);
		return cost;
	}
	
	/** Resets fifoCost to blank table.
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public void resetFifo() throws DatabaseModeException, SQLException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			//TODO
			return;
		case MySQL:
			resetFifo_mySql();
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private void resetFifo_mySql() throws SQLException {
		String query = "DELETE FROM fifoCost";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
		}
	}
	
	/** Adds new cost into fifoCost.
	 * @param currency Currency
	 * @param orderTimestamp Date and time
	 * @param cost New cost
	 * @param amount Amount to be added
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public void addFifo(String currency, long orderTimestamp, BigDecimal cost, BigDecimal amount) throws DatabaseModeException, SQLException {
		currency = currency.toUpperCase();
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			//TODO
			return;
		case MySQL:
			addFifo_mySql(currency, orderTimestamp, cost, amount);
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private void addFifo_mySql(String currency, long orderTimestamp, BigDecimal cost, BigDecimal amount) throws SQLException {
		String query = "INSERT INTO fifoCost VALUES (?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, currency);
			java.sql.Timestamp timestamp = new java.sql.Timestamp(orderTimestamp);
			pstmt.setTimestamp(2, timestamp);
			pstmt.setBigDecimal(3, cost);
			pstmt.setBigDecimal(4, amount);
			pstmt.executeUpdate();
		}
	}
	
	/** Gets all exist currencies from fifoCost.
	 * @return Exist currencies
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public ArrayList<String> getExistCurrenciesFifo() throws DatabaseModeException, SQLException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return null;   //TODO
		case MySQL:
			return getExistCurrenciesFifo_mySql();
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private ArrayList<String> getExistCurrenciesFifo_mySql() throws SQLException {
		String query = "SELECT DISTINCT currency FROM fifoCost";
		ResultSet rs = null;
		try (Connection conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement()) {
			rs = stmt.executeQuery(query);
			ArrayList<String> currencies = new ArrayList<String>();
			while (rs.next()) {
				currencies.add(rs.getString("currency"));
			}
			return currencies;
		}
		finally {
			rs.close();
		}
	}
	
	/** Gets cost from fifoCost.
	 * @param currency Currency
	 * @return Cost and amount. Returned map is {"orderTimestamp0": {"cost": c0, "amount": a0}, "orderTimestamp1": {"cost": c1, "amount": a1}, ...}
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException 
	 * @throws NullResultException If currency is not found. */
	public TreeMap<Long, JSONObject> getFifo(String currency) throws DatabaseModeException, SQLException, NullResultException {
		currency = currency.toUpperCase();
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return null;   //TODO
		case MySQL:
			return getFifo_mySql(currency);
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	@SuppressWarnings("unchecked")
	private TreeMap<Long, JSONObject> getFifo_mySql(String currency) throws SQLException, NullResultException {
		String query = "SELECT * FROM fifoCost WHERE currency = ?";
		ResultSet rs = null;
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, currency);
			rs = pstmt.executeQuery();
			
			//Creates a nested map
			TreeMap<Long, JSONObject> returnedMap = new TreeMap<Long, JSONObject>();
			while (rs.next()) {
				long timestamp = rs.getTimestamp("orderTimestamp").getTime();
				JSONObject element = new JSONObject();
				element.put("cost", rs.getBigDecimal("cost"));
				element.put("amount", rs.getBigDecimal("amount"));
				returnedMap.put(timestamp, element);
			}
			
			//Returns a map or throws an exception.
			if (returnedMap.isEmpty()) {
				throw new NullResultException("Currency is not found.");
			}
			else {
				return returnedMap;
			}
		}
		finally {
			rs.close();
		}
	}
	
	/** Gets total amount from fifoCost.
	 * @param fifoMap Tree map of remaining amount
	 * @return Total amount
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	private BigDecimal getTotalAmountFifo(TreeMap<Long, JSONObject> fifoMap) throws DatabaseModeException, SQLException {
		BigDecimal sum = zeroDecimal;
		for (Map.Entry<Long, JSONObject> element : fifoMap.entrySet()) {
			JSONObject obj = element.getValue();
			BigDecimal remainingAmount = new BigDecimal(obj.get("amount").toString()); 
			sum = sum.add(remainingAmount);
		}
		return sum;
	}
	
	/** Gets total amount from fifoCost.
	 * @param currency Currency
	 * @return Total amount
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public BigDecimal getTotalAmountFifo(String currency) throws DatabaseModeException, SQLException {
		currency = currency.toUpperCase();
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return null;   //TODO
		case MySQL:
			return getTotalAmountFifo_mySql(currency);
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private BigDecimal getTotalAmountFifo_mySql(String currency) throws DatabaseModeException, SQLException {
		//Gets FIFO map with try-catch
		try {
			TreeMap<Long, JSONObject> fifoMap = new TreeMap<Long, JSONObject>();
			fifoMap = getFifo(currency);
			return getTotalAmountFifo(fifoMap);
		}
		catch (NullResultException nre) {
			return zeroDecimal;   //Currency is not found
		}
	}
	
	/** Removes cost from fifoCost.
	 * @param currency Currency
	 * @param amount Amount to be removed
	 * @return Removed cost and amount. Returned map is {"orderTimestamp0": {"cost": c0, "amount": a0}, "orderTimestamp1": {"cost": c1, "amount": a1}, ...}
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotEnoughAmountException Amount is not enough to be removed */
	public TreeMap<Long, JSONObject> removeFifo(String currency, BigDecimal amount) throws DatabaseModeException, SQLException, NotEnoughAmountException {
		currency = currency.toUpperCase();
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return null;   //TODO
		case MySQL:
			return removeFifo_mySql(currency, amount);
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	@SuppressWarnings("unchecked")
	private TreeMap<Long, JSONObject> removeFifo_mySql(String currency, BigDecimal amount) throws DatabaseModeException, SQLException, NotEnoughAmountException {
		//#### First: Declares current FIFO map ####
		TreeMap<Long, JSONObject> fifoMap = new TreeMap<Long, JSONObject>();
		try {
			fifoMap = getFifo(currency);
		}
		catch (NullResultException nre) {
			throw new NotEnoughAmountException("Amount of \"" + currency + "\" is not enough to decrease.");
		}
		
		//#### Second: Checks if decreasing amount is greater than remaining amount #### 
		BigDecimal totalAmount = getTotalAmountFifo(fifoMap);
		if (amount.compareTo(totalAmount) > 0) {
			throw new NotEnoughAmountException("Amount of \"" + currency + "\" is not enough to decrease.");
		}
		
		//#### Third: Decreases amount, and returns removed cost and amount ####
		TreeMap<Long, JSONObject> removedMap = new TreeMap<Long, JSONObject>();
		BigDecimal decreasedAmount = zeroDecimal;
		while (decreasedAmount.compareTo(amount) < 0) {
			//Gets current key and values
			Map.Entry<Long, JSONObject> currentEntry = fifoMap.firstEntry();
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentEntry.getKey());
			JSONObject currentValue = currentEntry.getValue();
			BigDecimal currentAmount = new BigDecimal(currentValue.get("amount").toString());
			
			//Deletes or updates fifoCost, and returns removed FIFO map
			BigDecimal nextDecreased = decreasedAmount.add(currentAmount);
			if (nextDecreased.compareTo(amount) <= 0) {
				//Choice 1: Decreases total amount in a current row
				String query = "DELETE FROM fifoCost WHERE currency = ? and orderTimestamp = ?";
				try (Connection conn = DriverManager.getConnection(url, user, password);
						PreparedStatement pstmt = conn.prepareStatement(query)) {
					pstmt.setString(1, currency);
					pstmt.setTimestamp(2, currentTimestamp);
					pstmt.executeUpdate();
				}
				
				JSONObject obj = new JSONObject();
				obj.put("cost", currentValue.get("cost"));
				obj.put("amount", currentValue.get("amount"));
				removedMap.put(currentEntry.getKey(), obj);
				fifoMap.remove(currentEntry.getKey());
			}
			else {
				//Choice 2: Decreases some amount in a current row. This choice only occurs in the last loop
				BigDecimal decreasing = amount.subtract(decreasedAmount);
				BigDecimal newAmount = currentAmount.subtract(decreasing);
				String query = "UPDATE fifoCost SET remainingAmount = ? WHERE currency = ? and orderTimestamp = ?";
				try (Connection conn = DriverManager.getConnection(url, user, password);
						PreparedStatement pstmt = conn.prepareStatement(query)) {
					pstmt.setBigDecimal(1, newAmount);
					pstmt.setString(2, currency);
					pstmt.setTimestamp(3, currentTimestamp);
					pstmt.executeUpdate();
				}
				
				JSONObject obj = new JSONObject();
				obj.put("cost", currentValue.get("cost"));
				obj.put("amount", decreasing);
				removedMap.put(currentEntry.getKey(), obj);
			}
			
			decreasedAmount = nextDecreased;   //Updates decreasedAmount before beginning next loop
		}   //End of while
		
		return removedMap;
	}
		
	/** Adds new bought order into orderRecord, and adds new cost into fifoCost. Fees and taxes are zero.
	 * @param orderTimestamp Date and time
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-? */
	public void buyFifo(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException {
		buyFifo(orderTimestamp, currency, amount, price, zeroDecimal, zeroDecimal);
	}
	
	/** Adds new bought order into orderRecord, and adds new cost into fifoCost.
	 * @param orderTimestamp Date and time
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @param fee Fees
	 * @param tax Taxes
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-? */
	public void buyFifo(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			//TODO
			return;
		case MySQL:
			buyFifo_mySql(orderTimestamp, currency, amount, price, fee, tax);
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private void buyFifo_mySql(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException {
		BigDecimal cost = sumCost(amount, price, fee, tax);
		addFifo(currency, orderTimestamp, cost, amount);
		newOrder(orderTimestamp, TradeMode.Buy, currency, amount, price, fee, tax);
	}

	/** Adds new sold order into orderRecord, and adds new cost into fifoCost. Fees and taxes are zero.
	 * @param orderTimestamp Date and time
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-?
	 * @throws NotEnoughAmountException Amount is not enough to be removed */
	public void sellFifo(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException, NotEnoughAmountException {
		sellFifo(orderTimestamp, currency, amount, price, zeroDecimal, zeroDecimal);
	}
	
	/** Adds new sold order into orderRecord, and adds new cost into fifoCost.
	 * @param orderTimestamp Date and time
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @param fee Fees
	 * @param tax Taxes
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-?
	 * @throws NotEnoughAmountException Amount is not enough to be removed */
	public void sellFifo(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException, NotEnoughAmountException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			//TODO
			return;
		case MySQL:
			sellFifo_mySql(orderTimestamp, currency, amount, price, fee, tax);
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private void sellFifo_mySql(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException, NotEnoughAmountException {
		removeFifo(currency, amount);
		newOrder(orderTimestamp, TradeMode.Sell, currency, amount, price, fee, tax);
	}
	
	/** Resets current fifoCost to blank table, then calculates FIFO and adds total orders from orderRecord into fifoCost.
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public void updateFifo() throws DatabaseModeException, SQLException {
		resetFifo();
		//FIXME
	}
	
	/** Resets averageCost to blank table.
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public void resetAverage() throws DatabaseModeException, SQLException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			//TODO
			return;
		case MySQL:
			resetAverage_mySql();
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private void resetAverage_mySql() throws SQLException {
		String query = "DELETE FROM averageCost";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
		}
	}
	
	/** Adds new cost into averageCost.
	 * @param currency Currency
	 * @param cost New cost
	 * @param amount Amount to be added
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public void addAverage(String currency, BigDecimal cost, BigDecimal amount) throws DatabaseModeException, SQLException {
		currency = currency.toUpperCase();
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			//TODO
			return;
		case MySQL:
			addAverage_mySql(currency, cost, amount);
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private void addAverage_mySql(String currency, BigDecimal cost, BigDecimal amount) throws DatabaseModeException, SQLException {
		//#### First: Gets cost from averageCost ####
		boolean existCurrency;
		BigDecimal oldCost;
		BigDecimal oldAmount;
		try {
			JSONObject oldMap = getAverage(currency);
			existCurrency = true;
			oldCost = new BigDecimal(oldMap.get("cost").toString());
			oldAmount = new BigDecimal(oldMap.get("amount").toString());
		}
		catch (NullResultException nre) {
			existCurrency = false;
			oldCost = zeroDecimal;
			oldAmount = zeroDecimal;
		}
		
		//#### Second: Caculates new cost and amount, and updates averageCost
		// newCost = ((oldCost * oldAmount) + (cost * amount)) / (oldAmount + amount)
		BigDecimal nc1 = oldCost.multiply(oldAmount);
		BigDecimal nc2 = cost.multiply(amount);
		BigDecimal nc3 = nc1.add(nc2);   //((oldCost * oldAmount) + (cost * amount))
		BigDecimal newAmount = oldAmount.add(amount);
		BigDecimal newCost = nc3.divide(newAmount, decimalScale, RoundingMode.HALF_UP);
		
		if (existCurrency) {
			//Choice 1: Exist currency
			String query = "UPDATE averageCost SET cost = ?, amount = ? WHERE currency = ?";
			try (Connection conn = DriverManager.getConnection(url, user, password);
					PreparedStatement pstmt = conn.prepareStatement(query)) {
				pstmt.setBigDecimal(1, newCost);
				pstmt.setBigDecimal(2, newAmount);
				pstmt.setString(3, currency);
				pstmt.executeUpdate();
			}
		}
		else {
			//Choice 2: New currency
			String query = "INSERT INTO averageCost VALUES (?, ?, ?)";
			try (Connection conn = DriverManager.getConnection(url, user, password);
					PreparedStatement pstmt = conn.prepareStatement(query)) {
				pstmt.setString(1, currency);
				pstmt.setBigDecimal(2, newCost);
				pstmt.setBigDecimal(3, newAmount);
				pstmt.executeUpdate();
			}
		}
	}
	
	/** Gets all exist currencies from averageCost.
	 * @return Exist currencies
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public ArrayList<String> getExistCurrenciesAverage() throws DatabaseModeException, SQLException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return null;   //TODO
		case MySQL:
			return getExistCurrenciesAverage_mySql();
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private ArrayList<String> getExistCurrenciesAverage_mySql() throws SQLException {
		String query = "SELECT currency FROM averageCost";
		ResultSet rs = null;
		try (Connection conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement()) {
			rs = stmt.executeQuery(query);
			ArrayList<String> currencies = new ArrayList<String>();
			while (rs.next()) {
				currencies.add(rs.getString("currency"));
			}
			return currencies;
		}
		finally {
			rs.close();
		}
	}
	
	/** Gets cost from averageCost.
	 * @param currency Currency
	 * @return Cost and amount. Returned map is {"cost": c, "amount": a}
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException 
	 * @throws NullResultException If currency is not found. */
	public JSONObject getAverage(String currency) throws DatabaseModeException, SQLException, NullResultException {
		currency = currency.toUpperCase();
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return null;   //TODO
		case MySQL:
			return getAverage_mySql(currency);
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getAverage_mySql(String currency) throws SQLException, NullResultException {
		String query = "SELECT * FROM averageCost WHERE currency = ?";
		ResultSet rs = null;
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, currency);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				JSONObject map = new JSONObject();
				map.put("cost", rs.getBigDecimal("cost"));
				map.put("amount", rs.getBigDecimal("amount"));
				return map;
			}
			else {
				throw new NullResultException("Currency is not found.");
			}
		}
		finally {
			rs.close();
		}
	}

	/** Gets total amount from averageCost.
	 * @param currency Currency
	 * @return Total amount
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException */
	public BigDecimal getTotalAmountAverage(String currency) throws DatabaseModeException, SQLException {
		currency = currency.toUpperCase();
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return null;   //TODO
		case MySQL:
			return getTotalAmountAverage_mySql(currency);
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private BigDecimal getTotalAmountAverage_mySql(String currency) throws DatabaseModeException, SQLException {
		try {
			JSONObject averageMap = getAverage(currency);
			BigDecimal amount = new BigDecimal(averageMap.get("amount").toString());
			return amount;
		}
		catch (NullResultException nre) {
			return zeroDecimal;   //Currency is not found
		}
	}
	
	/** Removes cost from averageCost.
	 * @param currency Currency
	 * @param amount Amount to be removed
	 * @return Removed cost and amount. Returned map is {"cost": c, "amount": a}
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws NotEnoughAmountException Amount is not enough to be removed */
	public JSONObject removeAverage(String currency, BigDecimal amount) throws DatabaseModeException, SQLException, NotEnoughAmountException {
		currency = currency.toUpperCase();
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			return null;   //TODO
		case MySQL:
			return removeAverage_mySql(currency, amount);
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject removeAverage_mySql(String currency, BigDecimal amount) throws DatabaseModeException, SQLException, NotEnoughAmountException {
		//#### First: Declares current FIFO map ####
		BigDecimal oldCost;
		BigDecimal oldAmount;
		try {
			JSONObject averageMap = getAverage(currency);
			oldCost = new BigDecimal(averageMap.get("cost").toString());
			oldAmount = new BigDecimal(averageMap.get("amount").toString());
		}
		catch (NullResultException nre) {
			throw new NotEnoughAmountException("Amount of \"" + currency + "\" is not enough to decrease.");
		}
		
		//#### Second: Checks if decreasing amount is greater than remaining amount #### 
		if (amount.compareTo(oldAmount) > 0) {
			throw new NotEnoughAmountException("Amount of \"" + currency + "\" is not enough to decrease.");
		}
		
		//#### Third: Decreases amount ####
		BigDecimal newAmount = oldAmount.subtract(amount);
		if (newAmount.compareTo(zeroDecimal) > 0) {
			//Choice 1: There is remaining amount
			String query = "UPDATE averageCost SET amount = ? WHERE currency = ?";
			try (Connection conn = DriverManager.getConnection(url, user, password);
					PreparedStatement pstmt = conn.prepareStatement(query)) {
				pstmt.setBigDecimal(1, newAmount);
				pstmt.setString(2, currency);
				pstmt.executeUpdate();
			}
		}
		else {
			//Choice 2: Removes total amount
			String query = "DELETE FROM averageCost WHERE currency = ?";
			try (Connection conn = DriverManager.getConnection(url, user, password);
					PreparedStatement pstmt = conn.prepareStatement(query)) {
				pstmt.setString(1, currency);
				pstmt.executeUpdate();
			}
		}
		
		//#### Forth: Returns removed cost and amount ####
		JSONObject removedMap = new JSONObject();
		removedMap.put("cost", oldCost);
		removedMap.put("amount", amount);
		return removedMap;
	}

	/** Adds new bought order into orderRecord, and adds new cost into averageCost. Fees and taxes are zero.
	 * @param orderTimestamp Date and time
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-? */
	public void buyAverage(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException {
		buyAverage(orderTimestamp, currency, amount, price, zeroDecimal, zeroDecimal);
	}
	
	/** Adds new bought order into orderRecord, and adds new cost into averageCost.
	 * @param orderTimestamp Date and time
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @param fee Fees
	 * @param tax Taxes
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-? */
	public void buyAverage(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			//TODO
			return;
		case MySQL:
			buyAverage_mySql(orderTimestamp, currency, amount, price, fee, tax);
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private void buyAverage_mySql(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException {
		BigDecimal cost = sumCost(amount, price, fee, tax);
		addAverage(currency, cost, amount);
		newOrder(orderTimestamp, TradeMode.Buy, currency, amount, price, fee, tax);
	}
	
	/** Adds new sold order into orderRecord, and adds new cost into averageCost. Fees and taxes are zero.
	 * @param orderTimestamp Date and time
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-?
	 * @throws NotEnoughAmountException Amount is not enough to be removed */
	public void sellAverage(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException, NotEnoughAmountException {
		sellAverage(orderTimestamp, currency, amount, price, zeroDecimal, zeroDecimal);
	}
	
	/** Adds new sold order into orderRecord, and adds new cost into averageCost.
	 * @param orderTimestamp Date and time
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @param fee Fees
	 * @param tax Taxes
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-?
	 * @throws NotEnoughAmountException Amount is not enough to be removed */
	public void sellAverage(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException, NotEnoughAmountException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			//TODO
			return;
		case MySQL:
			sellAverage_mySql(orderTimestamp, currency, amount, price, fee, tax);
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private void sellAverage_mySql(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException, NotEnoughAmountException {
		removeAverage(currency, amount);
		newOrder(orderTimestamp, TradeMode.Sell, currency, amount, price, fee, tax);
	}

	/** Adds new bought order into orderRecord, and adds new cost into fifoCost and averageCost. Fees and taxes are zero.
	 * @param orderTimestamp Date and time
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-? */
	public void buyFifoAverage(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException {
		buyFifoAverage(orderTimestamp, currency, amount, price, zeroDecimal, zeroDecimal);
	}
	
	/** Adds new bought order into orderRecord, and adds new cost into fifoCost and averageCost.
	 * @param orderTimestamp Date and time
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @param fee Fees
	 * @param tax Taxes
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-? */
	public void buyFifoAverage(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			//TODO
			return;
		case MySQL:
			buyFifoAverage_mySql(orderTimestamp, currency, amount, price, fee, tax);
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private void buyFifoAverage_mySql(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException {
		BigDecimal cost = sumCost(amount, price, fee, tax);
		addFifo(currency, orderTimestamp, cost, amount);
		addAverage(currency, cost, amount);
		newOrder(orderTimestamp, TradeMode.Buy, currency, amount, price, fee, tax);
	}
	
	/** Adds new sold order into orderRecord, and adds new cost into fifoCost and averageCost. Fees and taxes are zero.
	 * @param orderTimestamp Date and time
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-?
	 * @throws NotEnoughAmountException Amount is not enough to be removed */
	public void sellFifoAverage(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException, NotEnoughAmountException {
		sellFifoAverage(orderTimestamp, currency, amount, price, zeroDecimal, zeroDecimal);
	}
	
	/** Adds new sold order into orderRecord, and adds new cost into fifoCost and averageCost.
	 * @param orderTimestamp Date and time
	 * @param currency Currency
	 * @param amount Bought amount or sold amount
	 * @param price Actual price
	 * @param fee Fees
	 * @param tax Taxes
	 * @throws DatabaseModeException Database mode is unsuitable. Null is not allowed.
	 * @throws SQLException
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-?
	 * @throws NotEnoughAmountException Amount is not enough to be removed */
	public void sellFifoAverage(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException, NotEnoughAmountException {
		if (databaseMode == null) {throw new DatabaseModeException("Database mode is unsuitable.");}
		switch (databaseMode) {
		case Memory:
			//TODO
			return;
		case MySQL:
			sellFifoAverage_mySql(orderTimestamp, currency, amount, price, fee, tax);
			return;
		default:
			throw new DatabaseModeException("Database mode is unsuitable.");
		}
	}
	
	private void sellFifoAverage_mySql(long orderTimestamp, String currency, BigDecimal amount, BigDecimal price, BigDecimal fee, BigDecimal tax) throws DatabaseModeException, SQLException, IdFormatException, OutOfRangeException, NotEnoughAmountException {
		removeFifo(currency, amount);
		removeAverage(currency, amount);
		newOrder(orderTimestamp, TradeMode.Sell, currency, amount, price, fee, tax);
	}
}