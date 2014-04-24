package ui;

import java.util.List;

import domain.Model;
import domain.Option;
import domain.Specification;
import domain.handlers.CheckOrderDetailsHandler;
import domain.order.OrderContainer;

public class CheckOrderDetailsUIPart {

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
	public CheckOrderDetailsUIPart(CheckOrderDetailsHandler handler, UIHelper helper)
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
	private CheckOrderDetailsHandler getHandler(){
		return this.partHandler;
	}
	
	/** Handler for this part of the UI */
	private CheckOrderDetailsHandler partHandler;
	

	/** UIhelper of this class */
	private final UIHelper helper;
	

	//--------------------------------------------------------------------------
	// Usecase Methods
	//--------------------------------------------------------------------------

	/**
	 * Displays a list of all pending and completed orders and asks the user for
	 * the choice of information about a pending order, a completed order, or
	 * exiting the menu.
	 */
	public void run(){
		System.out.println(helper.SEPERATOR);
		boolean exitMenu = false;
		while(!exitMenu){
			List<OrderContainer> completedOrders = getHandler().getCompletedOrdersContainers();
			List<OrderContainer> pendingOrders = getHandler().getPendingOrdersContainers();
			visualiseCompletedAndPendingOrders(completedOrders,pendingOrders);
			int choice = getMenuChoice();
			System.out.println(helper.SEPERATOR);
			switch(choice){
			case 1: chooseAndDisplayPending(pendingOrders);
					break;
			case 2: chooseAndDisplayComplete(completedOrders);
					break;
			default: exitMenu = true;
			}
		}
	}
	
	/**
	 * Displays the contents of given completed order list as desired
	 */
	private void chooseAndDisplayComplete(List<OrderContainer> orders) {
		System.out.println("Which complete order would you like to check?");
		//Get order choice
		int choice = helper.getIntFromUser(1, orders.size()+1);
		OrderContainer chosenOrder = orders.get(choice);
		//Print order number
		System.out.println("Pending Order Nb. " + choice);
		//Print order Model
		Model currentOrderModel = chosenOrder.getModel();
		System.out.println("Model Name:" + currentOrderModel.getName());
		//Print specs
		Specification orderSpec = chosenOrder.getSpecifications();
		int amountOfOptionsInOrder = orderSpec.getAmountOfOptions();
		System.out.println("Model Name:" + currentOrderModel.getName());
		for(int i = 0; i < amountOfOptionsInOrder; i++){
			Option currentOption = orderSpec.getOption(i);
			System.out.println("\tOption: " + currentOption.getName());
		}
		//Print submission time
		System.out.println("Submission Time: " + chosenOrder.getSubmissionTime().toString());
		//Print ETA
		System.out.println("Completion Time: " + chosenOrder.getCompletionTime().toString());
		System.out.println(helper.SEPERATOR);
	}

	/**
	 * Displays the contents of given pending order list as desired
	 */
	private void chooseAndDisplayPending(List<OrderContainer> orders) {
		System.out.println("Which pending order would you like to check?");
		//Get order choice
		int choice = helper.getIntFromUser(1, orders.size()+1);
		OrderContainer chosenOrder = orders.get(choice);
		//Print order number
		System.out.println("Pending Order Nb. " + choice);
		//Print order Model
		Model currentOrderModel = chosenOrder.getModel();
		System.out.println("Model Name:" + currentOrderModel.getName());
		//Print specs
		Specification orderSpec = chosenOrder.getSpecifications();
		int amountOfOptionsInOrder = orderSpec.getAmountOfOptions();
		System.out.println("Model Name:" + currentOrderModel.getName());
		for(int i = 0; i < amountOfOptionsInOrder; i++){
			Option currentOption = orderSpec.getOption(i);
			System.out.println("\tOption: " + currentOption.getName());
		}
		//Print submission time
		System.out.println("Submission Time: " + chosenOrder.getSubmissionTime().toString());
		//Print ETA
		System.out.println("Estimated Completion Time: " +
				this.getHandler().getEstimatedCompletionTime(chosenOrder).toString());
		System.out.println(helper.SEPERATOR);
		
	}

	/**
	 * query the user for the choice for what to do in the menu
	 */
	private int getMenuChoice() {
		System.out.println("What would you like to do?" + helper.CRLF +
				"1) Check a Pending Order" + helper.CRLF +
				"2) Check a Completed Order" + helper.CRLF +
				"3) Go back to the main menu" + helper.CRLF);
		return helper.getIntFromUser(1, 3);
	}

	/**
	 * Print the completed and pending orders in a nice list.
	 * Orders show a model and specifications.
	 * Pending orders also show an estimated completion time.
	 * 
	 * @param completedOrders
	 * 		The list of completed orders from the system
	 * @param pendingOrders
	 * 		The list of pending orders from the system
	 */
	private void visualiseCompletedAndPendingOrders(List<OrderContainer> completedOrders,
			List<OrderContainer> pendingOrders) {
		
		System.out.println(helper.SEPERATOR);
		System.out.println("Pending orders at this moment:");
		System.out.println(helper.SEPERATOR);
		for(int i = 0; i<pendingOrders.size();i++){
			OrderContainer order = pendingOrders.get(i);
			System.out.println("Pending Order Nb. " + (i+1));
			Model currentOrderModel = order.getModel();
			System.out.println("Model Name:" + currentOrderModel.getName());
			System.out.println("With Estimated Completion Time: " + this.getHandler().getEstimatedCompletionTime(order).toString());
			System.out.println(helper.SEPERATOR);
		}

		System.out.println("Completed orders to date:");
		System.out.println(helper.SEPERATOR);
		for(int i = 0; i<completedOrders.size();i++){
			OrderContainer order = completedOrders.get(i);
			System.out.println("Completed Order Nb. " + (i+1));
			Model currentOrderModel = order.getModel();
			System.out.println("Model Name:" + currentOrderModel.getName());
			System.out.println("With Completion Time: " + order.getCompletionTime().toString());
			System.out.println(helper.SEPERATOR);
		}

	}
}
