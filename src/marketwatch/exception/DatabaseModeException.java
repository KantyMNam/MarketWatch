package marketwatch.exception;

public class DatabaseModeException extends MarketWatchException {

	private static final long serialVersionUID = 3568252499457814338L;

	public DatabaseModeException() {
		super();
	}
	
	public DatabaseModeException(String message) {
		super(message);
	}
}