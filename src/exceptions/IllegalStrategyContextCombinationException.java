package exceptions;

public class IllegalStrategyContextCombinationException extends AssemAssistException{

	/**
	 * UID this class uses for serialisability
	 */
	private static final long serialVersionUID = -5266869436461956214L;
	
	public IllegalStrategyContextCombinationException() {
		super();
	}
	
	public IllegalStrategyContextCombinationException(String message) {
		super(message);
	}
}
