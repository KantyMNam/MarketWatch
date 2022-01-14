package exchangebroker;

import java.util.Date;

import org.json.simple.JSONObject;

import marketwatch.exception.*;
import marketwatch.tools.DateTime;

/** Declare a variable as this class, and call any exchangebroker functions indirectly by this class.
 * The class will call target functions. Using this class to call functions is recommanded. */
public class Bridge implements ApiInterface {
	
	private static String exchangeBrokerName;
	
	/** Check if exchange or broker name is exist.
	 * @param name Exchange or broker name
	 * @return If exchange or broker name is exist, returns true; if not, returns false. */
	public boolean isExchangeBrokerName(String name) {
		switch (name.toLowerCase()) {
		case "bitkub":
			exchangeBrokerName = name;
			return true;
		default:
			return false;
		}
	}
	
	/** Set exchange or broker name.
	 * @param name Exchange or broker name
	 * @throws ExchangeBrokerNameException */
	public void setExchangeBrokerName(String name) throws ExchangeBrokerNameException {
		if (isExchangeBrokerName(name)) {
			exchangeBrokerName = name;
		}
		else {
			throw new ExchangeBrokerNameException("Exchange or broker name is not exist.");
		}
	}
	
	/** Get exchange or broker name.
	 * @return Exchange or broker name */
	public String getExchangeBrokerName() {
		return exchangeBrokerName;
	}
	
	public String[] showAllSymbols() {
		switch (exchangeBrokerName.toLowerCase()) {
		case "bitkub":
			exchangebroker.Bitkub.Api api = new exchangebroker.Bitkub.Api();
			return api.showAllSymbols();
		default:
			return null;
		}
	}

	public boolean isSymbol(String symbol) {
		switch (exchangeBrokerName.toLowerCase()) {
		case "bitkub":
			exchangebroker.Bitkub.Api api = new exchangebroker.Bitkub.Api();
			return api.isSymbol(symbol);
		default:
			return false;
		}
	}
	
	/** Change time frame to correct format for the exchange or broker.
	 * @param timeframe Time frame to be changed
	 * @return New time frame */
	private String correctTimeframe(String timeframe) {
		switch (exchangeBrokerName.toLowerCase()) {
		case "bitkub":
			switch (timeframe.toLowerCase()) {
			case "1m":	return "1";
			case "5m":	return "5";
			case "15m":	return "15";
			case "30m":	return "30";
			case "60m":	return "60";
			case "1h":	return "60";
			case "4h":	return "240";
			case "1d":	return "1D";
			default:	return timeframe;
			}
		default:
			return timeframe;
		}
	}

	public boolean isTimeframe(String timeframe) {
		switch (exchangeBrokerName.toLowerCase()) {
		case "bitkub":
			exchangebroker.Bitkub.Api api = new exchangebroker.Bitkub.Api();
			return api.isTimeframe(correctTimeframe(timeframe));
		default:
			return false;
		}
	}
	
	public long serverTime() {
		switch (exchangeBrokerName.toLowerCase()) {
		case "bitkub":
			exchangebroker.Bitkub.Api api = new exchangebroker.Bitkub.Api();
			return api.serverTime();
		default:
			return -1;
		}
	}
	
	/** Get a map of history of currency.
	 * @param symbol Symbol
	 * @param timeframe Time frame
	 * @return History of currency 
	 * @throws SymbolException
	 * @throws TimeframeException */
	public JSONObject history(String symbol, String timeframe) throws SymbolException, TimeframeException {
		DateTime dateTime = new DateTime();
		long finalDate = dateTime.dateToSecond(new Date());
		long initialDate = finalDate - (60 * 60 * 24);   // 1 day before current
		JSONObject ret = new JSONObject();
		ret = history(symbol, timeframe, initialDate, finalDate);
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject history(String symbol, String timeframe, long initialDate, long finalDate) throws SymbolException, TimeframeException {
		if (!isSymbol(symbol)) {
			throw new SymbolException("\"" + symbol + "\" symbol is not exist.");
		}
		if (!isTimeframe(timeframe)) {
			throw new TimeframeException("\"" + timeframe + "\" timeframe is not exist.");
		}
			
		JSONObject ret = new JSONObject();
		switch (exchangeBrokerName.toLowerCase()) {
		case "bitkub":
			exchangebroker.Bitkub.Api api = new exchangebroker.Bitkub.Api();
			JSONObject temp = api.history(symbol, correctTimeframe(timeframe), initialDate, finalDate);
			ret.put("open", temp.get("o"));
			ret.put("close", temp.get("c"));
			ret.put("high", temp.get("h"));
			ret.put("low", temp.get("l"));
			ret.put("time", temp.get("t"));
			ret.put("volume", temp.get("v"));
			return ret;
		default:
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject ticker(String symbol) throws SymbolException {
		if (!isSymbol(symbol)) {
			throw new SymbolException("\"" + symbol + "\" symbol is not exist.");
		}
		JSONObject ret = new JSONObject();
		switch (exchangeBrokerName.toLowerCase()) {
		case "bitkub":
			exchangebroker.Bitkub.Api api = new exchangebroker.Bitkub.Api();
			JSONObject temp = api.ticker(symbol);
			ret.put("last", temp.get("last"));
			ret.put("highestBid", temp.get("highestBid"));
			ret.put("lowestAsk", temp.get("lowestAsk"));
			return ret;
		default:
			return null;
		}
	}
}