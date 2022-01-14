package marketwatch.indicator;

import java.math.*;

import org.json.simple.JSONArray;

/** Moving Average Convergence Divergence */
public class MACD {

	private JSONArray result = new JSONArray();
	private MA ma1;   //Fast moving average
	private MA ma2;   //Slow moving average
	private int pointer;
	
	/** Constructor.
	 * @param type Type of moving aveage
	 * @param period1 Period of the first moving average
	 * @param period2 Period of the second moving average */
	public MACD(MA.Type type, int period1, int period2) {
		reset();
		
		//perd1 must be less than perd2
		if (period1 > period2) {
			int temp = period2;
			period2 = period1;
			period1 = temp;
		}
		else if (period1 == period2) {
			period2++;
		}
		
		ma1 = new MA(type, period1);
		ma2 = new MA(type, period2);
	}
	
	/** Resets variables to null or default value. */
	private void reset() {
		result.clear();
		pointer = 0;
	}
	
	/** Gets array of result of moving average convergence divergence.
	 * @return Array of result */
	public JSONArray getResult() {
		return result;
	}
	
	/** Gets the last result of moving average convergence divergence.
	 * @return The last result */
	public BigDecimal getResultLast() {
		int lastIndex = result.size() - 1;
		BigDecimal lastResult = new BigDecimal(result.get(lastIndex).toString()); 
		return lastResult;
	}
	
	/** Removes array of price, sets a new array of price, and calculates moving average convergence divergence.
	 * @param prc Array of price */
	public void setPrice(JSONArray prc) {
		reset();
		ma1.setPrice(prc);
		ma2.setPrice(prc);
		calculate();
	}

	/** Adds new price at the last element of array of price, and calculates moving average convergence divergence.
	 * @param prc New price */
	public void addPrice(BigDecimal prc) {
		ma1.addPrice(prc);
		ma2.addPrice(prc);
		calculate();
	}
	
	/** Gets type of moving average convergence divergence.
	 * @return Type of moving average convergence divergence */
	public MA.Type getType() {
		return ma1.getType();
	}
	
	/** Gets array of period.
	 * @return Array of period */
	public int[] getPeriod() {
		int[] perd = new int[2];
		perd[0] = ma1.getPeriod();
		perd[1] = ma2.getPeriod();
		return perd;
	}
	
	/** Gets period of fast moving average.
	 * @return Period of fast moving average */
	public int getPeriod_fast() {
		return ma1.getPeriod();
	}
	
	/** Gets period of slow moving average.
	 * @return Period of slow moving average */
	public int getPeriod_slow() {
		return ma2.getPeriod();
	}
	
	/** Calculates moving average convergence divergence from pointer to the last candle. */
	@SuppressWarnings("unchecked")
	private void calculate() {
		JSONArray result1 = ma1.getResult();
		JSONArray result2 = ma2.getResult();
		while (pointer < result1.size()) {
			//diff = result1.get(pointer) - result2.get(pointer);
			BigDecimal diff1 = new BigDecimal(result1.get(pointer).toString());
			BigDecimal diff2 = new BigDecimal(result2.get(pointer).toString());
			BigDecimal diff = diff1.subtract(diff2);
			diff = diff.setScale(2, RoundingMode.HALF_UP);
			result.add(diff);
			pointer++;
		}
	}
}