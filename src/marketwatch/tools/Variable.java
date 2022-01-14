package marketwatch.tools;

import java.math.*;

import org.json.simple.JSONArray;

public class Variable {

	/** Convert array of byte to JSONArray.
	 * @param arr Array as byte
	 * @return Array as JSONArray */
	@SuppressWarnings("unchecked")	
	public JSONArray arrayToJsonArray(byte[] arr) {
		JSONArray newArr = new JSONArray();
		for (Object element : arr) {
			newArr.add(element);
		}
		return newArr;
	}
	
	/** Convert array of character to JSONArray.
	 * @param arr Array as character
	 * @return Array as JSONArray */
	@SuppressWarnings("unchecked")	
	public JSONArray arrayToJsonArray(char[] arr) {
		JSONArray newArr = new JSONArray();
		for (Object element : arr) {
			newArr.add(element);
		}
		return newArr;
	}
	
	/** Convert array of double to JSONArray.
	 * @param arr Array as double
	 * @return Array as JSONArray */
	@SuppressWarnings("unchecked")
	public JSONArray arrayToJsonArray(double[] arr) {
		JSONArray newArr = new JSONArray();
		for (Object element : arr) {
			newArr.add(element);
		}
		return newArr;
	}
	
	/** Convert array of float to JSONArray.
	 * @param arr Array as float
	 * @return Array as JSONArray */
	@SuppressWarnings("unchecked")	
	public JSONArray arrayToJsonArray(float[] arr) {
		JSONArray newArr = new JSONArray();
		for (Object element : arr) {
			newArr.add(element);
		}
		return newArr;
	}
	
	/** Convert array of integer to JSONArray.
	 * @param arr Array as integer
	 * @return Array as JSONArray */
	@SuppressWarnings("unchecked")
	public JSONArray arrayToJsonArray(int[] arr) {
		JSONArray newArr = new JSONArray();
		for (Object element : arr) {
			newArr.add(element);
		}
		return newArr;
	}
	
	/** Convert array of long to JSONArray.
	 * @param arr Array as long
	 * @return Array as JSONArray */
	@SuppressWarnings("unchecked")	
	public JSONArray arrayToJsonArray(long[] arr) {
		JSONArray newArr = new JSONArray();
		for (Object element : arr) {
			newArr.add(element);
		}
		return newArr;
	}
	
	/** Convert array of object to JSONArray.
	 * @param arr Array as object
	 * @return Array as JSONArray */
	@SuppressWarnings("unchecked")
	public JSONArray arrayToJsonArray(Object[] arr) {
		JSONArray newArr = new JSONArray();
		for (Object element : arr) {
			newArr.add(element);
		}
		return newArr;
	}
	
	/** Convert array of string to JSONArray.
	 * @param arr Array as string
	 * @return Array as JSONArray */
	@SuppressWarnings("unchecked")	
	public JSONArray arrayToJsonArray(String[] arr) {
		JSONArray newArr = new JSONArray();
		for (Object element : arr) {
			newArr.add(element);
		}
		return newArr;
	}
	
	/** Convert double to big decimal.
	 * @param dbl Number as double
	 * @param precision Number of decimal
	 * @return Number as big decimal */
	public BigDecimal doubleToBigDecimal(double dbl) {
		BigDecimal bd = new BigDecimal(dbl);
		return bd;
	}
	
	/** Convert double to big decimal with specific precision and half up rounding mode.
	 * @param dbl Number as double
	 * @param precision Number of decimal
	 * @return Number as big decimal */
	public BigDecimal doubleToBigDecimal(double dbl, int precision) {
		return doubleToBigDecimal(dbl, precision, RoundingMode.HALF_UP);
	}
	
	/** Convert double to big decimal with specific precision and rounding mode.
	 * @param dbl Number as double
	 * @param precision Number of decimal
	 * @param mode Rounding mode
	 * @return Number as big decimal */
	public BigDecimal doubleToBigDecimal(double dbl, int precision, RoundingMode mode) {
		MathContext mc = new MathContext(precision, mode);
		BigDecimal bd = new BigDecimal(dbl, mc);
		return bd;
	}
	
	/** Convert object to big decimal.
	 * @param str Number as object
	 * @return Number as double. Return value may be inaccurate due to floating-point variable. */
	public BigDecimal objectToBigDecimal(Object obj) {
		BigDecimal bd = new BigDecimal(obj.toString());
		return bd;
	}

	/** Convert object to double.
	 * @param str Number as object
	 * @return Number as double. Return value may be inaccurate due to floating-point variable.
	 * @throws NumberFormatException If the string is not integer format */
	public double objectToDouble(Object obj) throws NumberFormatException {
		String str = obj.toString();
		return stringToDouble(str);
	}
	
	/** Convert char to byte.
	 * @param ch Number as char
	 * @return Number as byte
	 * @throws NumberFormatException If the char is not integer format */
	public byte charToByte(char ch) throws NumberFormatException {
		switch (ch) {
		case '0':	return 0;
		case '1':	return 1;
		case '2':	return 2;
		case '3':	return 3;
		case '4':	return 4;
		case '5':	return 5;
		case '6':	return 6;
		case '7':	return 7;
		case '8':	return 8;
		case '9':	return 9;
		default: throw new NumberFormatException("The character is not number.");
		}
	}
	
	/** Convert string to integer.
	 * @param str Number as string
	 * @return Number as integer
	 * @throws NumberFormatException If the string is not integer format */
	public int stringToInteger(String str) {
		int number = 0;
		for (int i = 0; i < str.length(); i++) {
			int digit = (int)charToByte(str.charAt(i));
			int power = (str.length() - 1) - i;
			number += digit * (int)Math.pow(10, power);
		}
		return number;
	}
	
	/** Convert string to long.
	 * @param str Number as string
	 * @return Number as long
	 * @throws NumberFormatException If the string is not integer format */
	public long stringToLong(String str) {
		long number = 0;
		for (int i = 0; i < str.length(); i++) {
			int digit = (int)charToByte(str.charAt(i));
			int power = (str.length() - 1) - i;
			number += digit * (int)Math.pow(10, power);
		}
		return number;
	}
	
	/** Convert string to double.
	 * @param str Number as string
	 * @return Number as double. Return value may be inaccurate due to floating-point variable.
	 * @throws NumberFormatException If the string is not integer format */
	public double stringToDouble(String str) throws NumberFormatException {
		String[] splitStr = str.split("\\.");
		if (splitStr.length > 2) {throw new NumberFormatException();}   //Multiple point in a number is not allowed.
		int beforePoint = 0;
		int afterPoint = 0;
	
		//If splitStr[0].length == 0, this loop will be skiped.		
		for (int i = 0; i < splitStr[0].length(); i++) {
			int digit = (int)charToByte(splitStr[0].charAt(i));
			int power = (splitStr[0].length() - 1) - i;
			beforePoint += digit * (int)Math.pow(10, power);
		}
		double number = beforePoint;
		
		//If splitStr[1].length == 0, this loop will be skiped.
		if (splitStr.length == 2) {
			for (int i = 0; i < splitStr[1].length(); i++) {
				int digit = (int)charToByte(splitStr[1].charAt(i));
				int power = (splitStr[1].length() - 1) - i;
				afterPoint += digit * (int)Math.pow(10, power);
			}
			number += (afterPoint * Math.pow(10, (0 - splitStr[1].length())));
		}
		return number;
	}
	
	/** Check if string is integer format.
	 * @param str number as string
	 * @return If the string is integer format, returns true; if not, returns false. */
	public boolean isInteger(String str) {
		try {
			stringToInteger(str);
			return true;
		}
		catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	/** Check if string is double format.
	 * @param str number as string
	 * @return If the string is double format, returns true; if not, returns false. */
	public boolean isDouble(String str) {
		try {
			stringToDouble(str);
			return true;
		}
		catch (NumberFormatException nfe) {
			return false;
		}
	}
}