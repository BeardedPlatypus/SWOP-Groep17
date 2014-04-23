package ui;

import java.util.List;

import domain.OptionCategory;
import domain.handlers.OrderSingleTaskHandler;

public class OrderSingleTaskUIPart {

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
	public OrderSingleTaskUIPart(OrderSingleTaskHandler handler, UIHelper helper)
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
	private OrderSingleTaskHandler getHandler(){
		return this.partHandler;
	}
	
	/** Handler for this part of the UI */
	private OrderSingleTaskHandler partHandler;

	/** UIhelper of this class */
	private final UIHelper helper;

	//--------------------------------------------------------------------------
	// Usecase Methods
	//--------------------------------------------------------------------------

	/**
	 * Runs the routine for ordering a new car.
	 * Until the exit choice is taken on the ordermenu the possible models are printed,
	 * and after choosing one, the specs are collected and a new model is made if the user
	 * hasn't canceled during the specs.
	 * If both model and specs are chosen, the order is placed and the submenu runs again,
	 * with an updated view of the pending and completed orders.
	 */
	public void run() {
		System.out.println(helper.SEPERATOR);
		boolean exitMenu = false;
		while(!exitMenu){
			System.out.println("What do you want to do?:" + helper.CRLF + 
					"1) Order a single task" + helper.CRLF + "2) Exit this menu");
			int choice = helper.getIntFromUser(1, 2);
			if(choice == 2){
				exitMenu = true;
			} else {
				this.getHandler().startNewOrderSession();
				List<OptionCategory> categories = getHandler().getPossibleTasks();
//				int modelChoice = getModelChoice(orderModels);
//				this.getHandler().chooseModel(orderModels.get(modelChoice));
//				boolean continueOrder = setOptions();
//				if(continueOrder){
//					try{
//						this.getHandler().submitOrder();
//					} catch(OptionRestrictionException e){
//						System.out.println("Order was not conform with system restrictions."
//								+ helper.CRLF + "Try again." + helper.CRLF);
//					}
//				}
			}
		}
	}
}
