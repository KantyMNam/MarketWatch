package marketwatch.exception;

public class NotExistCurrencyException extends MarketWatchException {
	
	private static final long serialVersionUID = -8911954330651936301L;

	public NotExistCurrencyException() {
		super();
	}
	
	public NotExistCurrencyException(String message) {
		super(message);
	}
}