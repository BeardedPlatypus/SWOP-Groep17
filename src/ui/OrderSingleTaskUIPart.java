package ui;

import domain.handlers.NewOrderSessionHandler;

public class OrderSingleTaskUIPart {

	//--------------------------------------------------------------------------
	// constructor
	//--------------------------------------------------------------------------
	/**
	 * Construct this part of the UI with given handler to interface with.
	 * 
	 * @param handler
	 * 		The new handler for this object
	 * 
	 * @throws IllegalArgumentException
	 * 		If given handler is null
	 */
	public OrderSingleTaskUIPart(NewOrderSessionHandler handler) throws IllegalArgumentException{
		if(handler == null)
				throw new IllegalArgumentException("Handler can not be null!");
		this.partHandler = handler;
	}
	
	//--------------------------------------------------------------------------
	// properties
	//--------------------------------------------------------------------------
	
	/**
	 * Get the handler for this part for internal use.
	 * 
	 * @return the handler
	 */
	private NewOrderSessionHandler getHandler(){
		return this.partHandler;
	}
	
	/** Handler for this part of the UI */
	private NewOrderSessionHandler partHandler;
}
