package marketwatch.tools;

import java.math.BigDecimal;

public class DatabaseSetting {
	
	public enum DatabaseMode {
		/** Temporary wallet. Easy to use. */
		Memory,
		/** Permanent wallet. MySQL is required in localhost. */
		MySQL
	}
	protected DatabaseMode databaseMode;
	
	public final BigDecimal zeroDecimal = new BigDecimal("0.0000000000");
	public final int decimalScale = 10;
	
	//Variables for dababase modes
	protected String url;
	protected String user;
	protected String password;
	/* this.url = "jdbc:mysql://localhost/marketwatch";
	 * this.user = "MarketWatch";
	this.password = "MwEa";*/
	
	/** Constructor.
	 * Sets database mode as memory. */
	public DatabaseSetting() {
		setDatabaseMode(DatabaseMode.Memory);
	}
	
	/** Constructor
	 * Sets only database mode.
	 * @param databaseMode Database mode */
	public DatabaseSetting(DatabaseMode databaseMode) {
		setDatabaseMode(databaseMode);
	}
	
	/** Constructor.
	 * Sets database mode. Set specific user name, password, and URL.
	 * @param databaseMode Database mode
	 * @param url Database URL
	 * @param user User name
	 * @param password Password */
	public DatabaseSetting(DatabaseMode databaseMode, String url, String user, String password) {
		setDatabaseMode(databaseMode);
		setLogIn(url, user, password);
	}
	
	/** Sets database mode.
	 * @param databaseMode Database mode */
	public void setDatabaseMode(DatabaseMode databaseMode) {
		this.databaseMode = databaseMode;
	}
	
	/** Gets database mode.
	 * @return Database mode */
	public DatabaseMode getDatabaseMode() {
		return databaseMode;
	}
	
	/** Sets specific user name, password, and URL.
	 * Unable to get these parameters via function due to security reason.
	 * @param url Database URL
	 * @param user User name
	 * @param password Password*/
	public void setLogIn(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}
}