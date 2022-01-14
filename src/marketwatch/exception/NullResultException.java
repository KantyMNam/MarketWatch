package marketwatch.exception;

public class NullResultException extends MarketWatchException {

	private static final long serialVersionUID = -4328675428048875389L;

	public NullResultException() {
		super();
	}
	
	public NullResultException(String message) {
		super(message);
	}
}