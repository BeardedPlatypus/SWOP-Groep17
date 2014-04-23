package ui;

import domain.handlers.AdaptSchedulingAlgorithmHandler;

public class AdaptSchedulingAlgorithmUIPart {

	//--------------------------------------------------------------------------
	// constructor
	//--------------------------------------------------------------------------
	/**
	 * Construct this part of the UI with given handler and helper to interface with.
	 * 
	 * @param handler
	 * 		The new handler for this object
	 * @param helper 
	 * 		The UIhelper of this class
	 * 
	 * @throws IllegalArgumentException
	 * 		If either of the parameters is null
	 */
	public AdaptSchedulingAlgorithmUIPart(AdaptSchedulingAlgorithmHandler handler, UIHelper helper)
			throws IllegalArgumentException{
		if(handler == null)
			throw new IllegalArgumentException("Handler can not be null!");
		if(helper == null)
			throw new IllegalArgumentException("Helper can not be null!");
		this.partHandler = handler;
		this.helper = helper;
	}
	
	//--------------------------------------------------------------------------
	// properties
	//--------------------------------------------------------------------------

	/**
	 * Get the handler for this part for internal use.
	 * 
	 * @return the handler
	 */
	private AdaptSchedulingAlgorithmHandler getHandler(){
		return this.partHandler;
	}
	
	/** Handler for this part of the UI */
	private AdaptSchedulingAlgorithmHandler partHandler;
	
	/** UIhelper of this class */
	private final UIHelper helper;

	//--------------------------------------------------------------------------
	// Usecase Methods
	//--------------------------------------------------------------------------

	
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
