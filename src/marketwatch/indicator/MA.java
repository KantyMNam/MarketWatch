package marketwatch.indicator;

import java.math.*;

import org.json.simple.JSONArray;

/** Moving Average */
public class MA {
	
	private JSONArray price = new JSONArray();
	private JSONArray result = new JSONArray();
	
	public enum Type{
		/** Exponential Moving Average */
		EMA,
		/** Simple Moving Average */
		SMA
	};
	private Type type;
	
	private int period;
	private int pointer;
	
	/** Constructor.
	 * @param type Type of moving aveage
	 * @param period Period */
	public MA(Type type, int period) {
		reset();
		setType(type);
		if (!setPeriod(period)) {
			this.period = 5;   //Default
		}
	}
	
	/** Resets variables to null or default value. */
	private void reset() {
		result.clear();
		pointer = 0;
	}
	
	/** Gets result of moving average.
	 * @return Result */
	public JSONArray getResult() {
		return result;
	}
	
	/** Gets the last result of moving average.
	 * @return The last result */
	public BigDecimal getResultLast() {
		int lastIndex = result.size() - 1;
		BigDecimal lastResult = new BigDecimal(result.get(lastIndex).toString()); 
		return lastResult;
	}

	/** Removes array of price, sets a new array of price, and calculates moving average.
	 * @param price Array of price */
	public void setPrice(JSONArray price) {
		reset();
		this.price.clear();
		this.price = price;
		calculate();
	}
	
	/** Adds new price at the last element of array of price, and calculates moving average.
	 * @param price New price */
	@SuppressWarnings("unchecked")
	public void addPrice(BigDecimal price) {
		this.price.add(price);
		calculate();
	}
	
	/** Sets type of moving average.
	 * @param type Type of moving average */
	public void setType(Type type) {
		reset();
		this.type = type;
	}
	
	/** Gets type of moving average.
	 * @return Type of moving average */
	public Type getType() {
		return type;
	}
	
	/** Sets period.
	 * @param period Period
	 * @return If process is correct, returns true; if not, return false. */
	public boolean setPeriod(int period) {
		if (period >= 1) {
			reset();
			this.period = period;
			return true;
		}
		else {
			return false;
		}
	}
	
	/** Gets period.
	 * @return Period */
	public int getPeriod() {
		return period;
	}
	
	/** Calculates exponential moving average at a candle.
	 * @param index Index of candle to be calculated
	 * @return Exponential moving average of the candle */
	private BigDecimal calculateCandle_ema(int index) {
		// k = 2 / (period + 1)			
		BigDecimal k = new BigDecimal("2");
		BigDecimal k1 = new BigDecimal(period);
		if (index < period) {k1 = new BigDecimal(index + 1);}
		BigDecimal k2 = new BigDecimal("1");
		BigDecimal k3 = k1.add(k2);   // period + 1
		k = k.divide(k3, 4, RoundingMode.HALF_UP);   // 2 / (period + 1)
		
		// part1 = price.get(index) * k
		BigDecimal part1 = new BigDecimal(price.get(index).toString());
		part1 = part1.multiply(k);
		
		BigDecimal part2 = new BigDecimal("0");
		if (index != 0) {
			// part2 = result.get(index - 1) * (1 - k)
			BigDecimal part2_1 = new BigDecimal(result.get(index - 1).toString());
			BigDecimal part2_2 = new BigDecimal("1");
			BigDecimal part2_3 = part2_2.subtract(k);   // 1 - k
			part2 = part2_1.multiply(part2_3);   // result.get(index - 1) * (1 - k)
		}
		
		// ema = part1 + part2
		BigDecimal ema = part1.add(part2);
		
		ema = ema.setScale(2, RoundingMode.HALF_UP);
		return ema;
	}
	
	/** Calculates simple moving average at a candle.
	 * @param index Index of candle to be calculated
	 * @return Simple moving average of the candle */
	private BigDecimal calculateCandle_sma(int index) {
		BigDecimal sma = new BigDecimal("0");
		
		if (index == 0) {
			//Fix blank result of SMA: Calculate index 0
			// sma = price.get(0)
			sma = new BigDecimal(price.get(0).toString());
		}
		
		else if (index < period - 1) {   //Index begins at 0, but period begins at 1.
			//Fix blank result of SMA: Calculate index 1 to index (period - 2).
			BigDecimal sum = new BigDecimal("0");
			for (int i = 0; i <= index; i++) {
				// sum += price.get(i)
				BigDecimal temp = new BigDecimal(price.get(i).toString());
				sum = sum.add(temp);
			}
			// sma = sum / index
			BigDecimal ind = new BigDecimal(index);
			sma = sum.divide(ind, 4, RoundingMode.HALF_UP);
		}
		
		else {
			//Normal SMA: Calculate index (period - 1) and so on.
			BigDecimal sum = new BigDecimal("0");
			for (int k = 0; k < period; k++) {
				// sum += price.get(index - k)
				BigDecimal temp = new BigDecimal(price.get(index - k).toString());
				sum = sum.add(temp);
			}
			// sma = sum / period
			BigDecimal perd = new BigDecimal(period);
			sma = sum.divide(perd, 4, RoundingMode.HALF_UP);
		}
		
		sma = sma.setScale(2, RoundingMode.HALF_UP);
		return sma;
	}
	
	/** Calculates moving average at a candle.
	 * @param index Index of candle to be calculated
	 * @return Moving average of the candle */
	private BigDecimal calculateCandle(int index) {
		switch (type) {
		case EMA:
			return calculateCandle_ema(index);
		case SMA:
			return calculateCandle_sma(index);
		default:
			return new BigDecimal("-1");
		}
	}
	
	/** Calculates moving average from pointer to the last candle. */
	@SuppressWarnings("unchecked")
	private void calculate() {
		while (pointer < price.size()) {
			result.add(calculateCandle(pointer));
			pointer++;
		}
	}
}