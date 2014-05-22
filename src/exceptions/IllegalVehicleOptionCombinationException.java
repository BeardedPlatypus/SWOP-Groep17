package exceptions;

/**
 * Thrown when a list of options is used together with a model, and the list
 * of options is not a valid combination for said model.
 * e.g. Duplicate options are present in the list or an option is present in
 * the list, but is not contained in the model.
 * 
 * @author Frederik Goovaerts
 */
public class IllegalVehicleOptionCombinationException extends AssemAssistException{

	/**
	 * UID this class uses for serialisability
	 */
	private static final long serialVersionUID = -3166385477991199340L;

	public IllegalVehicleOptionCombinationException() {
		super();
	}
	
	public IllegalVehicleOptionCombinationException(String message) {
		super(message);
	}
	
	public IllegalVehicleOptionCombinationException(String message, Throwable cause) {
		super(message, cause);
	}
}
