package marketwatch.exception;

public class MarketWatchException extends Exception {

	private static final long serialVersionUID = 3589476510392169741L;

	public MarketWatchException() {
		super();
	}
	
	public MarketWatchException(String message) {
		super(message);
	}
}