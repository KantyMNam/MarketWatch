package marketwatch.tools;

/** Converts from TradeMode to byte, and vice versa. */
public class TradeModeConverter {
	
	/** Converts from byte to trade mode.
	 * @param num Mode as byte
	 * @return Mode as TradeMode */
	public TradeMode byteToMode(byte num) {
		switch (num) {
		case 1:
			return TradeMode.Buy;
		case 2:
			return TradeMode.Sell;
		default:
			return null;
		}
	}
	
	/** Converts from trade mode to byte.
	 * @param mode Mode as TradeMode
	 * @return Mode as byte */
	public byte modeToByte(TradeMode mode) {
		if (mode == null) {return 0;}
		switch (mode) {
		case Buy:
			return 1;
		case Sell:
			return 2;
		default:
			return 0;
		}
	}

	/** Converts from object to byte.
	 * @param obj Mode as object
	 * @return Mode as byte */
	public byte objectToByte(Object obj) {
		TradeMode mode = objectToMode(obj);
		return modeToByte(mode);
	}
	
	/** Converts from string to byte.
	 * @param str Mode as string
	 * @return Mode as byte */
	public byte stringToByte(String str) {
		TradeMode mode = stringToMode(str);
		return modeToByte(mode);
	}	
	
	/** Converts from object to trade mode.
	 * @param obj Mode as object
	 * @return Mode as TradeMode */
	public TradeMode objectToMode(Object obj) {
		String str;
		if (obj == null) {
			str = "$null";   //Special value to call stringToMode function properly
		}
		else {
			str = obj.toString();
		}
		return stringToMode(str);
	}
	
	/** Converts from string to trade mode.
	 * @param str Mode as string
	 * @return Mode as TradeMode */
	public TradeMode stringToMode(String str) {
		if (str == null) {return null;}
		switch (str.toLowerCase()) {
		case "buy":
			return TradeMode.Buy;
		case "sell":
			return TradeMode.Sell;
		case "$null":   //Special value for null object from objectToMode function
			return null;
		default:
			return null;
		}
	}
}