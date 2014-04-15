package domain;

//TODO: This needs to be updated since the handlers will not be available anymore to the ui
/**
 * The InitialisationHandler is responsible for initialising the system. This
 * includes making all the appropriate objects and setting their associations. 
 * It also provides a way for the interface to ask for the appropriate handlers. 
 * 
 */
public class InitialisationHandler {
	//FIXME: create new constructor
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Construct the initialisationhandler, which in turn sets up a domainfacade,
	 * by constructing all necessary components and putting them together.
	 */
	public InitialisationHandler(){
		//TODO initialise ALL THE THINGS
		domainFacade = new DomainFacade();
	}
	
	/** 
	 * Get the DomainFacade of the system initialised 
	 * by this InitialisationHandler.
	 * 
	 * @return The DomainFacade of the system initialised by this InitialisationHandler
	 */
	public DomainFacade getDomainFacade() {
		return this.domainFacade;
	}
	
	/** The domain facade that is accessible by the UI. */
	private final DomainFacade domainFacade;
}