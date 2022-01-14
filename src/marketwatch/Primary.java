package marketwatch;

import java.util.Date;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exchangebroker.*;
import marketwatch.activewatcher.ActiveWatcher;
import marketwatch.exception.*;
import marketwatch.tools.*;

/*** The first class to be run. */
public class Primary {
	
	private static Bridge bridge = new Bridge();
	private static PrintClass pc = new PrintClass();

	private static ActiveWatcher[] activeWatcher = new ActiveWatcher[16];   //Maximum Active Watch is 16. Add more code to fix this problem.
	private static int awLength;
	
	private static String exchangeBrokerName;
	private static JSONObject historyRecord;
	private static JSONObject tickerRecord;
	
	/** Resets before main loop. */
	private static void primary_reset() {
		awLength = 0;
		
		pc.println("Market Watch 1.0.0");
		pc.println();
		
		Favorite favorite = new Favorite();
		String[] arr = {"select", favorite.getFavorite_exchangeBrokerName()};
		pc.println("Get exchange or broker name from favorite");
		select(arr);
		
		pc.println();
	}
	
	/** Sets exchange or broker name to exchangebroker.Bridge variable.
	 * @param input Array of input string from user */
	private static void select(String[] input) {
		if (input.length != 2) {
			pc.println("Error_P002: Argument is incorrect.");
			return;
		}
		exchangeBrokerName = input[1];
		try {
			bridge.setExchangeBrokerName(exchangeBrokerName);
		}
		catch (ExchangeBrokerNameException ebne) {
			pc.println("Error_P010: Invalid exchange or broker name.");
		}
		pc.println("Exchange or broker name: " + exchangeBrokerName);
	}
	
	/** Uses Active Watcher.
	 * @param input Array of input string from user */
	private static void watcher(String[] input) {
		if (input.length == 1) {
			pc.println("Error_P102: Active Watcher argument is incorrect.");
			return;
		}
		
		//New Active Watcher
		if (input[1].toLowerCase().equals("new")) {
			Favorite favorite = new Favorite();
			String exchangeBrokerName;
			String symbol;
			String timeframe;
			if (input.length == 2) {
				exchangeBrokerName = favorite.getFavorite_exchangeBrokerName();
				symbol = favorite.getFavorite_symbol();
				timeframe = favorite.getFavorite_timeframe();
			} else if (input.length == 5) {
				// Exchange or broker name
				if (input[2].equals("fav")) {
					exchangeBrokerName = favorite.getFavorite_exchangeBrokerName();
				}
				else {
					if (bridge.isExchangeBrokerName(input[2])) {
						exchangeBrokerName = input[2];
					}
					else {
						pc.println("Error_P110: Exchange or broker name is incorrect.");
						return;
					}
					
				}
				// Symbol
				if (input[3].equals("fav")) {
					symbol = favorite.getFavorite_symbol();
				}
				else {
					if (bridge.isSymbol(input[3])) {
						symbol = input[3];
					}
					else {
						pc.println("Error_P111: Symbol is incorrect.");
						return;
					}
					
				}
				// Time frame
				if (input[4].equals("fav")) {
					timeframe = favorite.getFavorite_timeframe();
				}
				else {
					if (bridge.isTimeframe(input[4])) {
						timeframe = input[4];
					}
					else {
						pc.println("Error_P112: Time frame is incorrect.");
						return;
					}
				}
			}
			else {   //input.length != 2 || input.length != 5
				pc.println("Error_P102: Active Watcher argument is incorrect.");
				return;
			}
			//Sets and starts new Active Watcher
			if (awLength < 16) {
				int newIndex = awLength;
				awLength++;
				activeWatcher[newIndex] = new ActiveWatcher(newIndex);
				activeWatcher[newIndex].setExchangeBrokerName_SymbolTimeframe(exchangeBrokerName, symbol, timeframe);
				activeWatcher[newIndex].start();
				pc.println("Active Watcher| New Active Watcher starts...");
				pc.println("Active Watcher| Identification: " + newIndex);
				pc.println("Active Watcher| Exchange or broker name: " + exchangeBrokerName + ", Symbol: " + symbol + ", Time frame: " + timeframe);
			}
			else {
				pc.println("Error_P101: Active Watcher array is full. Cannot add new Active Watcher.");
				return;
			}
		}
		
		//Exist Active Watcher
		else {
			Variable variable = new Variable();
			int selectedIndex;
			try {
				selectedIndex = variable.stringToInteger(input[1]);
			}
			catch (NumberFormatException nfe) {
				pc.println("Error_P120: Selected Active Watcher is not integer.");
				return;
			}
			switch (input[2].toLowerCase()) {
			case "watchmode":
				if (input.length != 4) {
					pc.println("Error_P102: Active Watcher argument is incorrect.");
					return;
				}
				if (activeWatcher[selectedIndex].setWatchMode(input[3])) {
					pc.println("Active Watcher| Change watch mode");
					pc.println("Active Watcher| Identification: " + selectedIndex);
					pc.println("Active Watcher| Watch mode: " + input[3]);
				}
				else {
					pc.println("Error_P121: Watch mode is incorrect.");
					return;
				}
				break;
				
			case "print":
				if (input.length != 4) {
					pc.println("Error_P102: Active Watcher argument is incorrect.");
					return;
				}
				pc.println("Active Watcher| Print");
				pc.println("Active Watcher| Identification: " + selectedIndex);
				switch (input[3].toLowerCase()) {
				case "watchmode":
					pc.println("Active Watcher| Watch mode: " + activeWatcher[selectedIndex].getWatchMode());
					break;
				case "ebname":
					pc.println("Active Watcher| Exchange or broker name: " + activeWatcher[selectedIndex].getExchangeBrokerName());
					break;
				case "symbol":
					pc.println("Active Watcher| Symbol: " + activeWatcher[selectedIndex].getSymbol());
					break;
				case "timeframe":
					pc.println("Active Watcher| Time frame: " + activeWatcher[selectedIndex].getTimeframe());
					break;
				default:
					pc.println("Error_P102: Active Watcher argument is incorrect.");
					return;
				}
				break;
				
			default:
				pc.println("Error_P102: Active Watcher argument is incorrect.");
				return;
			}
		}
	}
	
	/** Saves history to historyRecord.
	 * @param input Array of input string from user */
	private static void history(String[] input) {
		String symbol = null;
		String timeframe = null;
		for (int i = 1; i < input.length; i++) {
			switch (input[i].toLowerCase()) {
			case "1m":
			case "5m":
			case "15m":
			case "30m":
			case "60m":
			case "1h":
			case "4h":
			case "1d":
				if (timeframe != null) {
					pc.println("Error_P021: Two or more timeframes are in a function.");
					return;
				}
				timeframe = input[i];
				break;
			default:
				if (bridge.isSymbol(input[i])) {
					if (symbol != null) {
						pc.println("Error_P020: Two or more symbol are in a function.");
						return;
					}
					symbol = input[i].toUpperCase();
				}
				else {
					pc.println("Error_P002: Argument is incorrect.");
					return;
				}
			}
		}
		Favorite favorite = new Favorite();
		if (symbol == null) {symbol = favorite.getFavorite_symbol();}
		if (timeframe == null) {timeframe = favorite.getFavorite_timeframe();}
		
		try {
			historyRecord = bridge.history(symbol, timeframe);
		}
		catch (SymbolException se) {
			pc.println("Error_P011: \"" + symbol + "\" symbol is not exist in \"" + exchangeBrokerName + "\" exchange or broker.");
		}
		catch (TimeframeException te) {
			pc.println("Error_P012: \"" + timeframe + "\" time frame is not exist in \"" + exchangeBrokerName + "\" exchange or broker.");
		}
		pc.println("Symbol: " + symbol + ", Time frame: " + timeframe);
		pc.println("Update history");
	}
	
	/** Saves ticket to tickerRecord.
	 * @param input Array of input string from user */
	private static void ticker(String[] input) {
		String symbol = null;
		if (input.length == 1) {
			Favorite favorite = new Favorite();
			symbol = favorite.getFavorite_symbol();
		}
		else if (input.length == 2) {
			if (bridge.isSymbol(input[1])) {
				symbol = input[1];
			}
			else {
				pc.println("Error_P002: Argument is incorrect.");
				return;
			}
		}
		else {
			pc.println("Error_P002: Argument is incorrect.");
			return;
		}
		
		try {
			tickerRecord = bridge.ticker(symbol);
		}
		catch (SymbolException se) {
			pc.println("Error_P011: \"" + symbol + "\" symbol is not exist in \"" + exchangeBrokerName + "\" exchange or broker.");
		}
		pc.println("Symbol: " + symbol);
		pc.println("Update ticker");
	}
	
	/** Shows output text.
	 * @param input Array of input string from user */
	private static void print(String[] input) {
		if (input.length == 1) {
			pc.println("Error_P002: Argument is incorrect.");
			return;
		}
		for (int i = 1; i < input.length; i++) {
			if (input[i].startsWith("$") ) {
				String text = input[i].substring(1, input[i].length());
				pc.println(text);
			}
			else {
				PrintClass printClass = new PrintClass();
				String[] subInput = input[i].split("\\.");
				switch (subInput[0]) {
				case "history":
					if (subInput.length == 1) {
						printClass.printMap(historyRecord);
					}
					else if (subInput.length > 3) {
						pc.println("Error_P002: Argument is incorrect.");
						return;
					}
					else {
						if (subInput.length == 2) {
							pc.println(historyRecord.get(subInput[1]));
						}
						else {
							JSONArray arr = (JSONArray)historyRecord.get(subInput[1]);
							int index = -1;
							try {
								index = Integer.parseInt(subInput[2]);
								if (index >= arr.size()) {
									throw new ArrayIndexOutOfBoundsException();
								}
							}
							catch (NumberFormatException nfe) {
								pc.println("Error_P002: Argument is incorrect.");
								return;
							}
							catch (ArrayIndexOutOfBoundsException aob) {
								pc.println("Error_P031: Array index is out of bounds.");
								return;
							}
							pc.println(arr.get(index));
						}
					}
					break;
					
				case "ticker":
					if (subInput.length == 1) {
						printClass.printMap(tickerRecord);
					}
					else if (subInput.length == 2) {
						pc.println(tickerRecord.get(subInput[1]));
					}
					else {
						pc.println("Error_P002: Argument is incorrect.");
						return;
					}
					break;
					
				default:
					pc.println("Error_P002: Argument is incorrect.");
					return;
				}
			}
		}
	}
	
	/** Gets server time.
	 * @param input Array of input string from user */
	private static void serverTime(String[] input) {
		DateTime dateTime = new DateTime();
		long second = bridge.serverTime();
		Date date = dateTime.secondToDate(second);
		String str;
		if (input.length == 1) {
			str = dateTime.dateToString_dateTime(date);
			pc.println(str);
		}
		else if (input.length == 2) {
			switch (input[1].toLowerCase()) {
			case "datetime":
				str = dateTime.dateToString_dateTime(date);
				pc.println(str);
				break;
			case "timedate":
				str = dateTime.dateToString_timeDate(date);
				pc.println(str);
				break;
			case "date":
				str = dateTime.dateToString_date(date);
				pc.println(str);
				break;
			case "time":
				str = dateTime.dateToString_time(date);
				pc.println(str);
				break;
			case "fulldatetime":
				pc.println(date);
				break;
			case "second":
				pc.println(second);
				break;
			default:
				pc.println("Error_P002: Argument is incorrect.");
				return;
			}
		}
		else {
			pc.println("Error_P002: Argument is incorrect.");
			return;
		}
	}
	
	/** Main loop. */
	public static void main(String[] args) {
		primary_reset();
		bridge.showAllSymbols();
		Scanner scan = new Scanner(System.in);
		boolean nextLoop = true;
		while (nextLoop) {
			pc.print("> ");
			String[] input = scan.nextLine().split(" ");
			switch (input[0].toLowerCase()) {
			case "select":
				select(input);
				break;
			case "watcher":
				watcher(input);
				break;
			case "history":
				history(input);
				break;
			case "ticker":
				ticker(input);
				break;
			case "print":
				print(input);
				break;
			case "servertime":
				serverTime(input);
				break;
			case "exit":
				pc.print("Confirm terminate (Y/N): ");
				String temp = scan.nextLine().toLowerCase();
				if (temp.equals("y")) {
					pc.println("-----Terminate-----");
					nextLoop = false;
				}
				break;
			default:
				pc.println("Error_P001: Function is incorrect.");
			}
			pc.println();
		}
		
		//Outside main loop
		scan.close();
	}
}