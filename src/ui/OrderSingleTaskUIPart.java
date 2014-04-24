package ui;

import java.util.List;

import domain.OptionCategory;
import domain.handlers.OrderSingleTaskHandler;
import domain.order.OrderContainer;

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
	 * Allows the user to order a single task.
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
				System.out.println(helper.SEPERATOR);
				this.getHandler().startNewOrderSession();
				List<OptionCategory> categories = getHandler().getPossibleTasks();
				int categoryChoice = getCategoryChoice(categories);
				System.out.println(helper.SEPERATOR);
				boolean continueOrder = selectOption(categories.get(categoryChoice));
				if(continueOrder){
					System.out.println("Please specify the deadline for this order.");
					System.out.println("On what day should the order be finished?");
					int day = helper.getIntFromUser(0, Integer.MAX_VALUE);
					System.out.println("On what hour should the order be finished?");
					int hours = helper.getIntFromUser(0, 23);
					System.out.println("On how many minutes past that hour should the order be finished?");
					int minutes = helper.getIntFromUser(0, 59);
					getHandler().specifyDeadline(day, hours, minutes);
					OrderContainer order = getHandler().submitSingleTaskOrder();
					System.out.println("Estimated Completion Time for this order is:");
					System.out.println(getHandler().getEstimatedCompletionTime(order).toString());
					helper.getEnter();
					System.out.println(helper.SEPERATOR);
				}
			}
		}
	}


	private int getCategoryChoice(List<OptionCategory> categories) {
		System.out.println("Please choose the type of task you want to order:");
		for(int i = 0; i<categories.size(); i++){
			System.out.println(i + ") " + categories.get(i).getName());
		}
		return helper.getIntFromUser(1, categories.size());
	}
	

	private boolean selectOption(OptionCategory optionCategory) {
		System.out.println("Please choose the option you want to order:");
		for(int i = 0; i<optionCategory.getAmountOfOptions(); i++){
			System.out.println((i+1) + ") " + optionCategory.getOption(i).getName());
		}
		System.out.println((optionCategory.getAmountOfOptions()+1) + ") Cancel this order");
		int choice = helper.getIntFromUser(1, optionCategory.getAmountOfOptions()+1);
		if(choice == (optionCategory.getAmountOfOptions()+1))
			return false;
		getHandler().selectOption(optionCategory.getOption(choice));
		return true;
	}
}
