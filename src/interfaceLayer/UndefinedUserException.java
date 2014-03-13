package interfaceLayer;

public class UndefinedUserException extends Exception {
	
	/**
	 * @author simon
	 * 
	 * 
	 */
	private static final long serialVersionUID = 5656785378572021093L;
	private final String user;
	
	public UndefinedUserException(String user){
		this.user = user;
	}
	
	@Override
	public String toString(){
		return "UndefinedUserException: User type \"" + user + "\" is "
				+ "specified in the user interface, but not implemented.";
	}

}
