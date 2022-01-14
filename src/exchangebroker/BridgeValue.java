package exchangebroker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/** Get and return a value in map or array from bridge. Use this class to get a value easily. */
public class BridgeValue {
	
	/** Get and return an array value of specific map.
	 * @param obj Map
	 * @param key Key
	 * @return Value of map */
	public JSONArray mapValue_array(JSONObject obj, String key) {
		JSONArray value = new JSONArray();
		value = (JSONArray)obj.get(key);
		return value;
	}
	
	/** Get and return a boolean value of specific map.
	 * @param obj Map
	 * @param key Key
	 * @return Value of map */
	public boolean mapValue_boolean(JSONObject obj, String key) {
		boolean value = (boolean)obj.get(key);
		return value;
	}
	
	/** Get and return a double value of specific map.
	 * @param obj Map
	 * @param key Key
	 * @return Value of map */
	public double mapValue_double(JSONObject obj, String key) {
		double value = (double)obj.get(key);
		return value;
	}
	
	/** Get and return a float value of specific map.
	 * @param obj Map
	 * @param key Key
	 * @return Value of map */
	public float mapValue_float(JSONObject obj, String key) {
		float value = (float)obj.get(key);
		return value;
	}
	
	/** Get and return an integer value of specific map.
	 * @param obj Map
	 * @param key Key
	 * @return Value of map */
	public int mapValue_integer(JSONObject obj, String key) {
		int value = (int)obj.get(key);
		return value;
	}
	
	/** Get and return a long value of specific map.
	 * @param obj Map
	 * @param key Key
	 * @return Value of map */
	public long mapValue_long(JSONObject obj, String key) {
		long value = (long)obj.get(key);
		return value;
	}
	
	/** Get and return a map value of specific map.
	 * @param obj Map
	 * @param key Key
	 * @return Value of map */
	public JSONObject mapValue_map(JSONObject obj, String key) {
		JSONObject value = new JSONObject();
		value = (JSONObject)obj.get(key);
		return value;
	}
	
	/** Get and return a string value of specific map.
	 * @param obj Map
	 * @param key Key
	 * @return Value of map */
	public String mapValue_string(JSONObject obj, String key) {
		String value = (String)obj.get(key);
		return value;
	}
		
	/** Get and return "open" value of history.
	 * @param hist History map
	 * @return Value of map */
	public JSONArray history_open(JSONObject hist) {
		return mapValue_array(hist, "open");
	}
	
	/** Get and return "close" value of history.
	 * @param hist History map
	 * @return Value of map */
	public JSONArray history_close(JSONObject hist) {
		return mapValue_array(hist, "close");
	}
	
	/** Get and return "high" value of history.
	 * @param hist History map
	 * @return Value of map */
	public JSONArray history_high(JSONObject hist) {
		return mapValue_array(hist, "high");
	}
	
	/** Get and return "low" value of history.
	 * @param hist History map
	 * @return Value of map */
	public JSONArray history_low(JSONObject hist) {
		return mapValue_array(hist, "low");
	}
	
	/** Get and return "time" value of history.
	 * @param hist History map
	 * @return Value of map */
	public JSONArray history_time(JSONObject hist) {
		return mapValue_array(hist, "time");
	}
	
	/** Get and return "volume" value of history.
	 * @param hist History map
	 * @return Value of map */
	public JSONArray history_volume(JSONObject hist) {
		return mapValue_array(hist, "volume");
	}

	/** Get and return "last" value of ticker.
	 * @param tick Ticker map
	 * @return Value of map */
	public double ticker_last(JSONObject tick) {
		return mapValue_double(tick, "last");
	}
	
	/** Get and return "highestBid" value of ticker.
	 * @param tick Ticker map
	 * @return Value of map */
	public double ticker_highestBid(JSONObject tick) {
		return mapValue_double(tick, "highestBid");
	}
	
	/** Get and return "lowestAsk" value of ticker.
	 * @param tick Ticker map
	 * @return Value of map */
	public double ticker_lowestAsk(JSONObject tick) {
		return mapValue_double(tick, "lowestAsk");
	}
}