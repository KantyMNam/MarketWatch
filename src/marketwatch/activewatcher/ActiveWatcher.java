package marketwatch.activewatcher;

import java.math.BigDecimal;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exchangebroker.*;
import marketwatch.exception.*;
import marketwatch.tools.DateTime;
import marketwatch.tools.PrintClass;
import marketwatch.tools.TradingView;

public class ActiveWatcher extends Thread {
	
	private PrintClass pc = new PrintClass();
	private Bridge bridge = new Bridge();
	ExpertAdvisor expertAdvisor = new ExpertAdvisor();

	private final int identification;
	private String exchangeBrokerName;
	private String symbol;
	private String timeframe;
	private JSONObject historyRecord;
	//private JSONObject tickerRecord;
	
	public enum WatchMode {
		/** Stops this Active Watcher when the current loop ends. */
		Off,
		/** Works only the beginning of every candle. */
		NewCandle,
		/** Works every second. */
		RealTime,
		/** Simulates only actual data from historyRecord, then stops this Active Watcher. */
		BackTest_Actual,
		/** Simulates white noise (random) by using data from historyRecord, then stops this Active Watcher. */
		BackTest_WhiteNoise
	};
	private WatchMode watchMode;
	
	/** long: Millisecond */
	private long waitingTime_nextCandle;
	
	/** Constructor.
	 * @param index Active Watcher index */
	public ActiveWatcher(int index) {
		identification = index;   //Set Active Watcher identification. The identification cannot be changed later.
		reset();
		exchangeBrokerName = null;
		symbol = null;
		timeframe = null;
	}
		
	/** Gets Active Watcher index.
	 * @return Active Watcher index */
	public int getIdentification() {
		return identification;
	}
	
	/** Resets all variables to null or default value */
	private void reset() {
		historyRecord = null;
		//tickerRecord = null;
		watchMode = WatchMode.RealTime;
	}
	
	/** Waits by wach mode until next loop.
	 * @return If watch mode is newcandle or realtime, returns true; if not, returns false. */
	private boolean waitUntilNext() {
		try {
			switch (watchMode) {
			case NewCandle:
				Thread.sleep(waitingTime_nextCandle);
				break;
			case RealTime:
				Thread.sleep(1000);   // 1 second
				break;
			default:
				return false;
			}	
		}
		catch (InterruptedException ie) {
			return false;
		}
		return true;
	}
	
	/** Sets watch mode by string.
	 * @param mode Watch mode as string
	 * @return If what mode is correct, returns true; if not, returns false. */
	public boolean setWatchMode(String mode) {
		switch (mode.toLowerCase()) {
		case "off":
			watchMode = WatchMode.Off;
			break;
		case "newcandle":
			watchMode = WatchMode.NewCandle;
			break;
		case "realtime":
			watchMode = WatchMode.RealTime;
			break;
		case "backtest_actual":
			watchMode = WatchMode.BackTest_Actual;
			break;
		case "backtest_whitenoise":
			watchMode = WatchMode.BackTest_WhiteNoise;
			break;
		default:
			return false;
		}
		return true;
	}
	
	/** Sets watch mode by WatchMode enum.
	 * @param mode Watch mode as WatchMode enum
	 * @return If what mode is correct, returns true; if not, returns false. */
	public void setWatchMode(WatchMode mode) {
		watchMode = mode;
	}

	/** Gets watch mode.
	 * @return Watch mode */
	public WatchMode getWatchMode() {
		return watchMode;
	}
	
	/** Sets exchange or broker name to exchangebroker.Bridge variable.
	 * @param name Array of input string from user
	 * @return If exchange or broker name is correct, returns true; if not, returns false. */
	public boolean setExchangeBrokerName(String name) {
		try {
			bridge.setExchangeBrokerName(name);

		}
		catch (ExchangeBrokerNameException ebne) {
			return false;
		}
		exchangeBrokerName = name;
		return true;
	}
	
	/** Gets exchange or broker name.
	 * @return Exchange or broker name */
	public String getExchangeBrokerName() {
		return exchangeBrokerName;
	}
	
	/** Sets symbol.
	 * @param symbol Symbol
	 * @return If symbol is correct, returns true; if not, returns false. */
	public boolean setSymbol(String symbol) {
		if (bridge.isSymbol(symbol)) {
			this.symbol = symbol;
			return true;
		}
		else {
			return false;
		}
	}
	
	/** Gets symbol.
	 * @return Symbol */
	public String getSymbol() {
		return symbol;
	}
	
	/** Sets time frame.
	 * @param timeframe Time frame
	 * @return If time frame is correct, returns true; if not, returns false. */
	public boolean setTimeframe(String timeframe) {
		if (bridge.isTimeframe(timeframe)) {
			this.timeframe = timeframe;
			TradingView tradingView = new TradingView();
			waitingTime_nextCandle = tradingView.TimeframeToMillisecond(timeframe);
			return true;
		}
		else {
			return false;
		}
	}
	
	/** Gets time frame.
	 * @return Time frame */
	public String getTimeframe() {
		return timeframe;
	}
	
	/** Sets symbol and time frame.
	 * @param symbol Symbol
	 * @param timeframe Time frame
	 * @return If symbol and time frame is correct, returns true; if not, returns false. */
	public boolean setSymbolTimeframe(String symbol, String timeframe) {
		if (!bridge.isSymbol(symbol) || !bridge.isTimeframe(timeframe)) {
			return false;
		}
		setSymbol(symbol);
		setTimeframe(timeframe);
		return true;
	}
	
	/** Sets symbol and time frame.
	 * @param name Exchange or broker name
	 * @param symbol Symbol
	 * @param timeframe Time frame
	 * @return If exchange or broker name, symbol, and time frame is correct, returns true; if not, returns false. */
	public boolean setExchangeBrokerName_SymbolTimeframe(String name, String symbol, String timeframe) {
		if (!bridge.isExchangeBrokerName(name) || !bridge.isSymbol(symbol) || !bridge.isTimeframe(timeframe)) {
			return false;
		}
		setExchangeBrokerName(name);
		setSymbol(symbol);
		setTimeframe(timeframe);
		return true;
	}
	
	/** Saves history to historyRecord. Initial date is 50 previous candle before current. Final date is current.
	 * @return If process is correct, returns true; if not, returns false. 
	 * @throws TimeframeAwException 
	 * @throws SymbolAwException 
	 * @throws ExchangeBrokerNameAwException */
	public void history() throws ExchangeBrokerNameException, SymbolException, TimeframeException {
		history_previousCandle(50);
	}
	
	/** Saves history to historyRecord. Final date is current.
	 * @param initialDate Initial date
	 * @return If process is correct, returns true; if not, returns false. 
	 * @throws TimeframeAwException 
	 * @throws SymbolAwException 
	 * @throws ExchangeBrokerNameAwException */
	public void history(long initialDate) throws ExchangeBrokerNameException, SymbolException, TimeframeException {
		DateTime dateTime = new DateTime();
		long finalDate = dateTime.dateToSecond(new Date());
		history(initialDate, finalDate);
	}
	
	/** Saves history to historyRecord.
	 * @param initialDate Initial date
	 * @param finalDate Final date 
	 * @throws ExchangeBrokerNameAwException 
	 * @throws SymbolAwException 
	 * @throws TimeframeAwException */
	public void history(long initialDate, long finalDate) throws ExchangeBrokerNameException, SymbolException, TimeframeException {
		if (exchangeBrokerName == null)	{throw new ExchangeBrokerNameException("Enchange or broker name is null.");}
		if (symbol == null)				{throw new SymbolException("Symbol is null.");}
		if (timeframe == null)			{throw new TimeframeException("Timeframe is null.");}
		
		try {
			historyRecord = bridge.history(symbol, timeframe, initialDate, finalDate);
		}
		//Catch a child exception of BridgeException
		//Then, throw a new child exception of ActiveWatcherException
		catch (SymbolException se) {
			throw new SymbolException("\"" + symbol + "\" symbol is not exist in \"" + exchangeBrokerName + "\" exchange or broker.");
		}
		catch (TimeframeException te) {
			throw new TimeframeException("\"" + timeframe + "\" time frame is not exist in \"" + exchangeBrokerName + "\" exchange or broker.");
		}
	}
	
	/** Saves history to historyRecord. Final date is current.
	 * @param previousCandle Number of previous candles
	 * @return If process is correct, returns true; if not, returns false. 
	 * @throws TimeframeAwException 
	 * @throws SymbolAwException 
	 * @throws ExchangeBrokerNameAwException */
	public void history_previousCandle(int previousCandle) throws ExchangeBrokerNameException, SymbolException, TimeframeException {
		DateTime dateTime = new DateTime();
		long finalDate = dateTime.dateToSecond(new Date());
		long initialDate = finalDate - ((waitingTime_nextCandle / 1000) * previousCandle);
		history(initialDate, finalDate);
	}
	
	/** Saves ticker to tickerRecord.
	 * @throws ExchangeBrokerNameAwException 
	 * @throws SymbolAwException */
	public void ticker() throws ExchangeBrokerNameException, SymbolException {
		if (exchangeBrokerName == null)	{throw new ExchangeBrokerNameException("Enchange or broker name is null.");}
		if (symbol == null)				{throw new SymbolException("Symbol is null.");}
		
		/*try {
		tickerRecord = bridge.ticker(symbol);
		}
		
		//Catch a child exception of BridgeException
		//Then, throw a new child exception of ActiveWatcherException
		catch (SymbolException se) {
			throw new SymbolException("\"" + symbol + "\" symbol is not exist in \"" + exchangeBrokerName + "\" exchange or broker.");
		}*/
	}
	
	/** Simulates only actual data from historyRecord. */
	private void backTest_actual() {
		JSONArray price = (JSONArray)historyRecord.get("close");
		expertAdvisor.reset();
		for (int i = 0; i < price.size(); i++) {
			BigDecimal currentPrice = new BigDecimal(price.get(i).toString());
			expertAdvisor.addPrice(currentPrice);
			expertAdvisor.run();
		}
	}
	
	/** Simulates white noise (random) by using data from historyRecord. */
	private void backTest_whiteNoise() {
		//TODO
	}
	
	/** Active Watcher loop. */
	public void run() {
		watchMode = WatchMode.BackTest_Actual;
		
		boolean nextLoop = true;
		while (nextLoop) {
			//#### First: Request history and ticker ####
			try {
				history_previousCandle(10000);
			}
			catch (ExchangeBrokerNameException | SymbolException | TimeframeException e) {
				//TODO
			}
			
			switch (watchMode) {
			case NewCandle:
			case RealTime:
				try {
					ticker();
				}
				catch (ExchangeBrokerNameException | SymbolException e) {
					//TODO
				}
				break;
			default:
				//Nothing to do
			}
			
			//#### Second: Call strategy ####
			switch (watchMode) {
			case BackTest_Actual:
				backTest_actual();
				break;
			case BackTest_WhiteNoise:
				backTest_whiteNoise();
				break;
			default:
				expertAdvisor.run();
			}
			
			//#### Last: Wait until next loop or break loop ####
			switch (watchMode) {
			case Off:
			case BackTest_Actual:
			case BackTest_WhiteNoise:
				nextLoop = false;
				break;
			case NewCandle:
			case RealTime:
				waitUntilNext();
				break;
			}	
		}
		pc.println();
		pc.println("Active Watcher| This Active Watcher ends...");
		pc.println("Active Watcher| Identification: " + identification);
		pc.println();
		pc.print("> ");
	}
}