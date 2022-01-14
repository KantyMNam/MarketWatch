package marketwatch;

/** Save and Retrieve favorite settings */
public class Favorite {
	
	private String fav0_exchangeBrokerName = "Bitkub";
	private String fav0_symbol = "BTC_THB";
	private String fav0_timeframe = "5M";
	
	/** Set favorite.
	 * @param name Exchange or broker name
	 * @param symbol Symbol
	 * @param tframe Time frame */
	public void setFavorite_exchangeBrokerName(String name, String symbol, String tframe) {
		fav0_exchangeBrokerName = name;
		fav0_symbol = symbol;
		fav0_timeframe = tframe;
	}
	
	/** Set exchange or broker name to favorite.
	 * @param name Exchange or broker name */
	public void setFavorite_exchangeBrokerName(String name) {
		fav0_exchangeBrokerName = name;
	}
	
	/** Set symbol to favorite.
	 *  @param symbol Symbol */
	public void setFavorite_symbol(String symbol) {
		fav0_symbol = symbol;
	}
	
	/** Set time frame to favorite.
	 * @param tframe Time frame */
	public void setFavorite_timeframe(String tframe) {
		fav0_timeframe = tframe;
	}
	
	/** Get favorite.
	 * @return exchange or broker name, symbol, and time frame */
	public String[] getFavorite() {
		String[] fav = new String[3];
		fav[0] = fav0_exchangeBrokerName;
		fav[1] = fav0_symbol;
		fav[2] = fav0_timeframe;
		return fav;
	}
	
	/** Get exchange or broker name from favorite.
	 * @return Exchange or broker name */
	public String getFavorite_exchangeBrokerName() {
		return fav0_exchangeBrokerName;
	}
	
	/** Get symbol from favorite.
	 * @return Symbol */
	public String getFavorite_symbol() {
		return fav0_symbol;
	}
	
	/** Get time frame from favorite.
	 * @return Time frame */
	public String getFavorite_timeframe() {
		return fav0_timeframe;
	}
} 