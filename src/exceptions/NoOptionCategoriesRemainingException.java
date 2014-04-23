package exceptions;

/**
 * Thrown when an OptionCategory object is requested for assembling an order,
 * but all categories are already used.
 * 
 * @author Frederik Goovaerts
 */
public class NoOptionCategoriesRemainingException extends AssemAssistException {

	/**
	 * UID this class uses for serialisability
	 */
	private static final long serialVersionUID = 5496370487479320498L;

	public NoOptionCategoriesRemainingException() {
		super();
	}
	
	public NoOptionCategoriesRemainingException(String message) {
		super(message);
	}
}
