package marketwatch.exception;

public class OutOfRangeException extends MarketWatchException {

	private static final long serialVersionUID = -4710414150432634720L;

	public OutOfRangeException() {
		super();
	}
	
	public OutOfRangeException(String message) {
		super(message);
	}
}