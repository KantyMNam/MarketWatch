package marketwatch.tools;

import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PrintClass {
	
	/** Write a text.
	 * @param x Boolean to be print */
	public void print(boolean x) {
		print(Boolean.toString(x));
	}

	/** Write a text.
	 * @param x Character to be print */
	public void print(char x) {
		print(Character.toString(x));
	}
	
	/** Write a text.
	 * @param x Double to be print */
	public void print(double x) {
		print(Double.toString(x));
	}

	/** Write a text.
	 * @param x Float to be print */
	public void print(float x) {
		print(Float.toString(x));
	}
	
	/** Write a text.
	 * @param x Integer to be print */
	public void print(int x) {
		print(Integer.toString(x));
	}
	
	/** Write a text.
	 * @param x Long to be print */
	public void print(long x) {
		print(Long.toString(x));
	}
	
	/** Write a text.
	 * @param x Object to be print */
	public void print(Object x) {
		print((x.toString()));
	}
	
	/** Write a text.
	 * @param x String to be print */
	public void print(String x) {
		System.out.print(x);
	}

	/** Write a nextline character. */
	public void println() {
		print("\n");
	}
	
	/** Write a text and a nextline character at the end.
	 * @param x Boolean to be print */
	public void println(boolean x) {
		print(x);
		println();
	}
	
	/** Write a text and a nextline character at the end.
	 * @param x Character to be print */
	public void println(char x) {
		print(x);
		println();
	}

	/** Write a text and a nextline character at the end.
	 * @param x Double to be print */
	public void println(double x) {
		print(x);
		println();
	}
	
	/** Write a text and a nextline character at the end.
	 * @param x Float to be print */
	public void println(float x) {
		print(x);
		println();
	}
	
	/** Write a text and a nextline character at the end.
	 * @param x Integer to be print */
	public void println(int x) {
		print(x);
		println();
	}
	
	/** Write a text and a nextline character at the end.
	 * @param x Long to be print */
	public void println(long x) {
		print(x);
		println();
	}
	
	/** Write a text and a nextline character at the end.
	 * @param x Object to be print */
	public void println(Object x) {
		print(x);
		println();
	}
	
	/** Write a text and a nextline character at the end.
	 * @param x String to be print */
	public void println(String x) {
		print(x);
		println();
	}
	
		/** Write a JSONArray to text and a nextline character at the end.
	 * @param x JSONArray to be print
	 * @param numberOfColumn Number of columns in a row */
	public void printJsonArray(JSONArray x, int numberOfColumn) {
		print("[");
		int count = 0;
		for (int index = 0; index < x.size(); index++) {
			if (index != 0) {
				print(",");
			}
			if (count >= numberOfColumn) {
				println();
				print(" ");
				count = 0;
			}
			else if (count != 0) {
				print(" ");
			}
			print(x.get(index));
			count++;
		}
		println("]");
	}
	
	/** Write a map as text and a nextline character at the end.
	 * @param x Map to be print */
	public void printMap(JSONObject x) {
		@SuppressWarnings("rawtypes")
		Set set =  x.keySet();
		for (Object element : set) {
			println("\"" + element + "\": " + x.get(element));
		}
	}

	/** Read a line of text, and write a nextline character at the end.
	 * @return Input text by user */
	public String scan() {
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		scanner.close();
		return input;
	}

	/** Write a text, read a line of text, and write a nextline character at the end.
	 * @param text String to be print
	 * @return Input text by user */
	public String scan(String text) {
		print(text);
		return scan();
	}
}