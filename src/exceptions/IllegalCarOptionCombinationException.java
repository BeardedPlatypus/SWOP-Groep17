package exceptions;

public class IllegalCarOptionCombinationException extends AssemAssistException{

	/**
	 * UID this class uses for serialisability
	 */
	private static final long serialVersionUID = -3166385477991199340L;

	public IllegalCarOptionCombinationException() {
		super();
	}
	
	public IllegalCarOptionCombinationException(String message) {
		super(message);
	}
}
