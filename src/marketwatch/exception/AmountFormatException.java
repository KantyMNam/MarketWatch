package marketwatch.exception;

public class AmountFormatException extends MarketWatchException {

	private static final long serialVersionUID = 2626930351273126303L;

	public AmountFormatException() {
		super();
	}
	
	public AmountFormatException(String message) {
		super(message);
	}
}