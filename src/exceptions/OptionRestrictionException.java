package exceptions;

/**
 * Thrown when a list of options does not conform to the system's restrictions.
 * 
 * @author Frederik Goovaerts
 */
public class OptionRestrictionException extends AssemAssistException {

	/**
	 * UID this class uses for serialisability
	 */
	private static final long serialVersionUID = -7307301041852470507L;

	public OptionRestrictionException(){
		super();
	}
	
	public OptionRestrictionException(String message) {
		super(message);
	}
	
	public OptionRestrictionException(String message, Throwable cause) {
		super(message, cause);
	}
}
