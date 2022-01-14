package marketwatch.tools;

public class TradingView {
	
	/** Replace the first currency and the second currency of symbol.
	 * @param symbol Symbol that contains the first currency and the second currency
	 * @return New symbol */
	public String swapSymbol(String symbol) {
		String[] currency = symbol.split("_");
		if (currency.length != 2) {return "";}
		return currency[1] + "_" + currency[0];
	}
	
	/** Change Time frame to millisecond, and return millisecond.
	 * @param timeframe Time frame
	 * @return Millisecond */
	public long TimeframeToMillisecond(String timeframe) {
		switch (timeframe.toLowerCase()) {
		case "1m":		{return 1000 * 60 * 1;}
		case "5m":		{return 1000 * 60 * 5;}
		case "15m":		{return 1000 * 60 * 15;}
		case "30m":		{return 1000 * 60 * 30;}
		case "60m":		{return 1000 * 60 * 60;}
		case "1h":		{return 1000 * 60 * 60 * 1;}
		case "4h":		{return 1000 * 60 * 60 * 4;}
		case "1d":		{return 1000 * 60 * 60 * 24;}
		default:		{return -1;}
		}
	}
}