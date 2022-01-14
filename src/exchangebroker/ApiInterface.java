package exchangebroker;

import org.json.simple.JSONObject;

import marketwatch.exception.*;

/** API class to get and post an exchange or a broker. */
public interface ApiInterface {
	
	/** Show all symbols that are in the exchange or the broker.
	 * @return All symbols */
	public String[] showAllSymbols();

	/** Return whether the symbol is in the exchange or the broker. 
	 * @param symbol Symbol
	 * @return True or false */
	public boolean isSymbol(String symbol);

	/** Return whether the time frame is in the exchange or the broker. 
	 * @param timeframe Time frame
	 * @return True or false */
	public boolean isTimeframe(String timeframe);
	
	/** Get the exchage's or the broker's server time in second.
	 * @return Server time */
	public long serverTime();
	
	/** Get map of history of currency.
	 * @param symbol Symbol
	 * @param timeframe Time frame
	 * @param initialDate Initial date and time to get the set
	 * @param fianlDate Final date and time to get the set
	 * @return History of currency
	 * @throws SymbolException
	 * @throws TimeframeException */
	public JSONObject history(String symbol, String timeframe, long initialDate, long finalDate) throws SymbolException, TimeframeException;
	
	/** Get map of the last ticker of currency.
	 * @param symbol Symbol
	 * @return The last ticker of currency
	 * @throws SymbolException */
	public JSONObject ticker(String symbol) throws SymbolException;
}