package marketwatch.exception;

public class NotEnoughAmountException extends MarketWatchException {
	
	private static final long serialVersionUID = 8632620955795663683L;

	public NotEnoughAmountException() {
		super();
	}
	
	public NotEnoughAmountException(String message) {
		super(message);
	}
}