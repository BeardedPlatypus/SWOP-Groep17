package exceptions;

/**
 * Thrown when an operation on an order is requested, but given order is not present
 * in the system.
 * 
 * @author Frederik Goovaerts
 */
public class OrderDoesNotExistException extends AssemAssistException {

	/**
	 * UID this class uses for serialisability
	 */
	private static final long serialVersionUID = -6571694166174960908L;
	
	public OrderDoesNotExistException() {
		super();
	}
	
	public OrderDoesNotExistException(String message) {
		super(message);
	}
	
	public OrderDoesNotExistException(String message, Throwable cause) {
		super(message, cause);
	}
}
