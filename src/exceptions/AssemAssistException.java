package exceptions;

/**
 * General Exception class for the AssemAssist system.
 * 
 * @author Frederik Goovaerts
 */
public abstract class AssemAssistException extends Exception {

	/**
	 * UID this class uses for serialisability
	 */
	private static final long serialVersionUID = -5823728053968010809L;
	
	public AssemAssistException(){
		super();
	}
	
	public AssemAssistException(String message){
		super(message);
	}
	
}
