package marketwatch.exception;

public class ExchangeBrokerNameException extends MarketWatchException {

	private static final long serialVersionUID = 399299236985190474L;

	public ExchangeBrokerNameException() {
		super();
	}
	
	public ExchangeBrokerNameException(String message) {
		super(message);
	}
}