package marketwatch.account.orderid;

import marketwatch.exception.*;
import marketwatch.tools.Variable;

/** Generates and checks order ID */
public class OrderId {

	/* ID contains 19 digits. Memory ID range is from -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807 (long).
	 * Actual ID range (with correct check digit) is from 000-00000-00000-00001-8 to 922-33720-36854-77579-3
	 * 
	 * The last digit is check digit.
	 * Check digit is generated and validated by Verhoeff algorithm. */
	
	private final long numberOfValidId = 922337203685477579L;
	
	//Verhoeff's tables
	/** d_table[last_c][p_value] */
	final byte[][] d_table = {{0,1,2,3,4,5,6,7,8,9}, {1,2,3,4,0,6,7,8,9,5}, {2,3,4,0,1,7,8,9,5,6}, {3,4,0,1,2,8,9,5,6,7}, {4,0,1,2,3,9,5,6,7,8}, {5,9,8,7,6,0,4,3,2,1}, {6,5,9,8,7,1,0,4,3,2}, {7,6,5,9,8,2,1,0,4,3}, {8,7,6,5,9,3,2,1,0,4}, {9,8,7,6,5,4,3,2,1,0}};
	/** p_table[index mod 8][number] */
	final byte[][] p_table = {{0,1,2,3,4,5,6,7,8,9}, {1,5,7,6,2,8,3,0,9,4}, {5,8,0,3,7,9,6,1,4,2}, {8,9,1,6,0,4,3,5,2,7}, {9,4,5,3,1,2,6,8,7,0}, {4,2,8,6,5,7,3,9,0,1}, {2,7,9,3,8,0,6,4,1,5}, {7,0,4,6,9,1,3,2,5,8}};
	final byte[] inv_table = {0,4,3,2,1,5,6,7,8,9};
	
	/** Gets number of total valid ID. It means how many ID can store in database.
	 * @return Number of total valid ID */
	public long validId() {
		return numberOfValidId;
	}
	
	/** Calculates number of remaining valid ID by the last ID.
	 * @param lastId The last or recent ID in database
	 * @return Number of total valid ID */
	public long remainingId(long lastId) {
		long remaining = numberOfValidId - lastId;
		return remaining;
	}
	
	/** Seperates the first 18 digits and check digit (19th digit).
	 * @param id 19 digits number of ID
	 * @return Array of ID. Index 0 is the first 18 digits. Index 1 is check digit.
	 * @throws IdFormatException If ID is not 18 digits number */
	private long[] splitId(long id) throws IdFormatException {
		//String str_id = String.format("%020d", String.valueOf(id));
		String str_id = String.format("%019d", id);
		if (str_id.length() != 19) {
			throw new IdFormatException("The last ID is not 20 digits number.");
		}
		
		long[] ret = new long[2];
		ret[0] = Long.parseLong(str_id.substring(0, 18));
		ret[1] = Long.parseLong(str_id.substring(18, 19));
		return ret;
	}
	
	/** Calculates check digit from 18 digits number of ID.
	 * @param id 19 digits number of ID
	 * @return check digit
	 * @throws IdFormatException If ID is not 18 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-? */
	private byte generateCheckDigit(long id) throws IdFormatException, OutOfRangeException {
		if (id <= 0 | id > 1844674407370955160L) {
			throw new OutOfRangeException("Id is out of range.");
		}
		String str_id = String.format("%018d", id);
		if (str_id.length() != 18) {
			throw new IdFormatException("ID is not 18 digits number.");
		}
		
		//#### First: Creates an array of the individual digits of the number, taken from right to left ####
		byte[] digit_id = new byte[19];
		digit_id[0] = 0;   //Initializes index 0 to zero
		for (byte i = 17, k = 1; i >= 0; i--, k++) {
			try {
				Variable variable = new Variable();
				digit_id[k] = variable.charToByte(str_id.charAt(i));
			}
			catch (NumberFormatException nfe) {
				//This line cannot be reached because str_id is initialized by number
				throw new OutOfRangeException("Some digits of ID are not numbers.");
			}
		}
		
		//#### Second: Finds the final c ####
		byte c = 0;   //Initializes c to zero
		for (byte i = 0; i < 18; i++) {
			byte pos = (byte)(i % 8);
			byte num = digit_id[i];
			byte p_value = p_table[pos][num];
			c = d_table[c][p_value];
		}
		
		//#### Third: Returns check digit ####
		byte inv_value = inv_table[c];
		return inv_value;
	}
	
	/** Validates check digit.
	 * @param id 19 digits number of ID
	 * @return If check digit is correct, returns true; if not, returns false.
	 * @throws IdFormatException If ID is not 19 digits number
	 * @throws OutOfRangeException If ID is negative number or zero number or greater than 922-33720-36854-77579-3 */
	public boolean valiadateCheckDigit(long id) throws IdFormatException, OutOfRangeException {
		if (id <= 0 | id > 1844674407370955160L) {
			throw new OutOfRangeException("Id is out of range.");
		}
		String str_id = String.format("%019d", id);
		if (str_id.length() != 19) {
			throw new IdFormatException("ID is not 19 digits number.");
		}
		
		//#### First: Creates an array of the individual digits of the number, taken from right to left ####
		byte[] digit_id = new byte[19];
		for (byte i = 18, k = 0; i >= 0; i--, k++) {
			try {
				Variable variable = new Variable();
				digit_id[k] = variable.charToByte(str_id.charAt(i));
			}
			catch (NumberFormatException nfe) {
				//This line cannot be reached because str_id is initialized by number
				throw new OutOfRangeException("Some digits of ID are not numbers.");
			}
		}
		
		//#### Second: Finds the final c ####
		byte c = 0;   //Initializes c to zero
		for (byte i = 0; i < 18; i++) {
			byte pos = (byte)(i % 8);
			byte num = digit_id[i];
			byte p_value = p_table[pos][num];
			c = d_table[c][p_value];
		}
		
		//#### Third: Returns true or false ####
		if (c == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/** Generates a new ID.
	 * @param lastId The last or recent ID in database
	 * @return New ID
	 * @throws IdFormatException If ID is not 19 digits number
	 * @throws OutOfRangeException If all valid ID are used. */
	public long generateId(long lastId) throws IdFormatException, OutOfRangeException {
		long[] split = splitId(lastId);
		long newId = split[0] + 1;
		byte checkDigit;
		try {
			checkDigit = generateCheckDigit(newId);
		}
		catch (IdFormatException ife) {
			throw ife;
		}
		catch (OutOfRangeException oore) {
			throw new OutOfRangeException("All valid ID are used.");
		}
		
		newId = (newId * 10) + checkDigit;   //Shifts 18 digits to left, and adds check digit at 19th digit
		return newId;
	}
}