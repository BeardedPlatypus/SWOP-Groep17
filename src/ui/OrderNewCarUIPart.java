package ui;

import java.util.ArrayList;
import java.util.List;

import domain.Model;
import domain.Option;
import domain.OptionCategory;
import domain.Specification;
import domain.handlers.NewOrderSessionHandler;
import domain.order.OrderContainer;
import exceptions.OptionRestrictionException;

public class OrderNewCarUIPart {


	//--------------------------------------------------------------------------
	// constructor
	//--------------------------------------------------------------------------
	/**
	 * Construct this part of the UI with given handler to interface with.
	 * 
	 * @param handler
	 * 		The new handler for this object
	 * @param helper 
	 * 
	 * @throws IllegalArgumentException
	 * 		If either of the parameters is null
	 */
	public OrderNewCarUIPart(NewOrderSessionHandler handler, UIHelper helper)
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
	private NewOrderSessionHandler getHandler(){
		return this.partHandler;
	}

	/** Handler for this part of the UI */
	private final NewOrderSessionHandler partHandler;
	

	/**
	 * Get the helper for internal use.
	 * 
	 * @return the helper
	 */
	private UIHelper getHelper(){
		return this.helper;
	}
	
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
			List<OrderContainer> completedOrders = getHandler().getCompletedOrders();
			List<OrderContainer> pendingOrders = getHandler().getPendingOrders();
			visualiseCompletedAndPendingOrders(completedOrders,pendingOrders);
			System.out.println("What do you want to do?:" + helper.CRLF + 
					"1) Order a new car" + helper.CRLF + "2) Exit this menu");
			int choice = helper.getIntFromUser(1, 2);
			if(choice == 2){
				exitMenu = true;
			} else {
				this.getHandler().startNewOrderSession();
				List<Model> orderModels = getHandler().getCarModels();
				int modelChoice = getModelChoice(orderModels);
				this.getHandler().chooseModel(orderModels.get(modelChoice));
				boolean continueOrder = setOptions();
				if(continueOrder){
					try{
						this.getHandler().submitOrder();
					} catch(OptionRestrictionException e){
						System.out.println("Order was not conform with system restrictions."
								+ helper.CRLF + "Try again." + helper.CRLF);
					}
				}
			}
		}
	}


	/**
	 * Prints the given list of models as an option list, and lets the user choose one.
	 * A choice int is returned, which symbolizes the chosen model.
	 * The order to evaluate the chosen int in is the order of the list.
	 * 
	 * @param orderModels
	 * 		The list of orders to display and choose from
	 * @return
	 * 		The choice number for the model the user selected
	 */
	private int getModelChoice(List<Model> orderModels) {
		int amountOfChoices = orderModels.size();
		System.out.println("Choose a model please");
		System.out.println(helper.SEPERATOR);
		int modelCounter = 1;
		for(Model model: orderModels){
			System.out.println( modelCounter + ") " + model.getName());
			modelCounter++;
		}
		return helper.getIntFromUser(1, amountOfChoices)-1;
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
		for(OrderContainer order: pendingOrders){
			Model currentOrderModel = order.getModel();
			Specification orderSpec = order.getSpecifications();
			int amountOfOptionsInOrder = orderSpec.getAmountOfOptions();
			System.out.println("Model Name:" + currentOrderModel.getName());
			for(int i = 0; i < amountOfOptionsInOrder; i++){
				Option currentOption = orderSpec.getOption(i);
				System.out.println("\tOption: " + currentOption.getName());
			}
			System.out.println("With Estimated Completion Time: " + this.getHandler().getEstimatedCompletionTime(order).toString());
			System.out.println(helper.SEPERATOR);
		}

		System.out.println("Completed orders to date:");
		System.out.println(helper.SEPERATOR);
		for(OrderContainer order: completedOrders){
			Model currentOrderModel = order.getModel();
			Specification orderSpec = order.getSpecifications();
			int amountOfOptionsInOrder = orderSpec.getAmountOfOptions();
			System.out.println("Model Name:" + currentOrderModel.getName());
			for(int i = 0; i < amountOfOptionsInOrder; i++){
				Option currentOption = orderSpec.getOption(i);
				System.out.println("\tOption: " + currentOption.getName());
			}
			System.out.println("With Completion Time: " + order.getCompletionTime().toString());
			System.out.println(helper.SEPERATOR);
		}

	}

	/**
	 * 
	 */
	private boolean setOptions() {

		boolean cancelOrder = false;
		System.out.println("Which options would you like?" + helper.CRLF);
		while(this.getHandler().hasUnfilledOptions() && !cancelOrder){
			OptionCategory cat = this.getHandler().getNextOptionCategory();
			for(int i = 0; i<cat.getAmountOfOptions();i++){
				System.out.println((i+1) + ") " + cat.getOption(i).getName());
			}
			System.out.println((cat.getAmountOfOptions() + 1 ) + ") Cancel placing an order");
			int choice = helper.getIntFromUser(1, cat.getAmountOfOptions() + 1);
			if(choice == cat.getAmountOfOptions()){
				cancelOrder = true;
			} else {
				this.getHandler().selectOption(cat.getOption(choice));
			}
		}
		if(cancelOrder)
			return false;
		return true;
	}


}
