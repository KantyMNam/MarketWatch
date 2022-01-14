package exchangebroker.Bitkub;

import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exchangebroker.ApiInterface;
import marketwatch.tools.GetPost;
import marketwatch.tools.TradingView;

public class Api implements ApiInterface {
	
	private GetPost gp = new GetPost();
	
	public String[] showAllSymbols() {
		String url = "https://api.bitkub.com/api/market/symbols";
		JSONObject obj = gp.getUrlMap(url);
		JSONArray result = (JSONArray)obj.get("result");
		String[] allSymbols = new String[result.size()];
		for (int i = 0; i < result.size(); i++) {
			JSONObject currency = (JSONObject)result.get(i);
			TradingView tradingView = new TradingView(); 
			allSymbols[i] = tradingView.swapSymbol((String)currency.get("symbol"));
		}
		Arrays.sort(allSymbols);
		return allSymbols;
	}

	public boolean isSymbol(String symbol) {
		String[] allSymbols = showAllSymbols();
		for (String element : allSymbols) {
			if (element.equals(symbol)) {
				return true;
			}
		}
		return false;
	}

	public boolean isTimeframe(String timeframe) {
		String[] allTimeframe = {"1", "5", "15", "30", "60", "240", "1D"};
		for (String element : allTimeframe) {
			if (timeframe.equals(element)) {
				return true;
			}
		}
		return false;
	}
	
	public long serverTime() {
		String url = "https://api.bitkub.com/api/servertime";
		String str = gp.getUrlString(url);
		long time;
		try {
			time = Long.parseLong(str);
			return time;
		}
		catch (NumberFormatException nfe) {
			return -1;
		}
	}
	
	public JSONObject history(String symbol, String timeframe, long initialDate, long finalDate) {
		String url = "https://api.bitkub.com/tradingview/history";
		url += "?symbol=" + symbol + "&resolution=" + timeframe;
		url += "&from=" + initialDate + "&to=" + finalDate;
		JSONObject obj = gp.getUrlMap(url);
		return obj;
	}
	
	public JSONObject ticker(String symbol) {
		TradingView tradingView = new TradingView();
		String reverseSymbol = tradingView.swapSymbol(symbol);
		String url = "https://api.bitkub.com/api/market/ticker";
		url += "?sym=" + reverseSymbol;
		JSONObject obj = gp.getUrlMap(url);
		obj = (JSONObject)obj.get(reverseSymbol);
		return obj;
	}
}