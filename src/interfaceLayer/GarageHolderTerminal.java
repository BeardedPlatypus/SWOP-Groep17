package interfaceLayer;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import domain.*;

/**
 * Specialized terminal for the garage holder, used to place new orders.
 * 
 * @author Simon Slangen
 *
 */
public class GarageHolderTerminal {

	/**
	 * Starts the Garageholder terminal. It assumes the user is logged in at this point.
	 * We start by showing an overview and help the user place a new car order.
	 * 
	 * @param 	handler
	 * 			Used to interface with the domain layer. 
	 */
	public static void login(NewOrderSessionHandler handler) {
		showHeader();
		
		while(true){
			//Show overview
			showOrderOverview(handler);
			
			if(userWantsOrder()){
				Model model = selectModel(handler.getNewOrderModels());
				Specification spec = composeOrder(model);
				
				if(userConfirmsOrder(model,spec)){
					handler.chooseModelAndSpecifications(model, spec);
					
					//Easiest way to show estimated completion time at the moment...
					List<OrderContainer> allOrders = new LinkedList<OrderContainer>();
					allOrders.addAll(handler.getPendingOrders());
					Collections.sort(allOrders, new OrderDatesDescendingComparator());
					System.out.print("Order placed. Estimated completion time: ");
					System.out.println(allOrders.get(0).getEstimatedCompletionTime().toString());
					System.out.println();
				}
				
				continue;
			}
			return;
		}
	}

	/**
	 * Prints a selected model and corresponding specifications. Asks the user
	 * to confirm an order with those items.
	 * 
	 * @param 	model
	 * 			The car model in the proposed order.
	 * @param 	spec
	 * 			The specifications in the proposed order.
	 * @return	A boolean corresponding with the user response.
	 */
	private static boolean userConfirmsOrder(Model model, Specification spec) {
		
		System.out.println("Please review your choices.");
		System.out.println("Model: " + model.getModelName());
		
		int numberOfOptions = model.getAmountOfOptions();
		
		for(int i = 0; i < numberOfOptions; i++){
			Option option = model.getModelOption(i);
			int choice = spec.getSpec(i);
			System.out.println(option.getOptionName() + ": " + option.getChoiceName(choice));
		}
		
		System.out.println();
		System.out.println("Do you want to place this order?");
		System.out.println("[Y]es");
		System.out.println("[N]o");
		System.out.println();
		
		try {
			while(true){
				// We'll only read in the first character.
				String choice = String.valueOf((char) System.in.read());
				// Don't forget to clear the input buffer.
				System.in.skip(System.in.available());
				if(choice.equalsIgnoreCase("Y")){
					return true;
				}else if (choice.equalsIgnoreCase("N")){
					return false;
				}else{
					System.out.println();
					System.out.println("Sorry, but \"" + choice + "\" is not a valid response. Please try again.");
					System.out.println();
				}
			}
		} catch (IOException e) {
			System.err.println("Critical error: Unable to read user input.");
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Receives a car model and lists the order options, one by one, registering
	 * user choice. Based in these choices, it creates a specification that is
	 * passed back to the caller.
	 * 
	 * @param 	model
	 * 			The car model of the proposed order.
	 * @return	The specifications selected by the user to order.
	 */
	private static Specification composeOrder(Model model) {
		
		int amountOfOptions = model.getAmountOfOptions();
		int[] selection = new int[amountOfOptions];
		
		for(int i = 0; i < amountOfOptions; i++){
			
			Option option = model.getModelOption(i);
			System.out.println("Option " + (i+1) + ": " + option.getOptionName());
			int amountOfChoices = option.getAmountOfChoices();
			
			for(int count = 0; count < amountOfChoices; count++){
				System.out.println("[" + (count+1) + "] " + option.getChoiceName(count));
			}
			System.out.println();
			
			try{
			out:while(true){
					// We'll only read in the first character.
					String choiceStr = String.valueOf((char) System.in.read());
					int choice = 0;
					try{
						choice = Integer.parseInt(choiceStr);
					}catch(Exception e){}
					// Don't forget to clear the input buffer.
					System.in.skip(System.in.available());
					
					if(choice < 1 || choice > amountOfChoices){
						System.out.println();
						System.out.println("Sorry, but \"" + choice + "\" is not a valid choice. Please try again.");
						System.out.println();
					}else{
						selection[i] = (choice-1);
						break out;
					}
				}
			} catch (IOException e) {
				System.err.println("Critical error: Unable to read user input.");
				e.printStackTrace();
			}
			
			System.out.println();
			
		}
		return model.makeSpecification(selection);
		
	}

	/**
	 * Prints a list of available models and polls the user for a specific model.
	 * 
	 * @param 	newOrderModels
	 * 			A list of available car models.
	 * @return	The model selected by the user.
	 */
	private static Model selectModel(List<Model> newOrderModels) {
		
		System.out.println();
		System.out.println("Please select a car model...");
		for(int i = 0; i < newOrderModels.size(); i++){
			System.out.println("[" + (i+1) + "] " + newOrderModels.get(i).getModelName());
		}
		
		System.out.println();
		try{
			while(true){
				// We'll only read in the first character.
				String choiceStr = String.valueOf((char) System.in.read());
				int choice = 0;
				try{
					choice = Integer.parseInt(choiceStr);
				}catch(Exception e){}
				
				// Don't forget to clear the input buffer.
				System.in.skip(System.in.available());
				
				if(choice < 1 || choice > newOrderModels.size()){
					System.out.println();
					System.out.println("Sorry, but \"" + choice + "\" is not a valid option. Please try again.");
					System.out.println();
				}else{
					return newOrderModels.get(choice-1);
				}
			}
		} catch (IOException e) {
			System.err.println("Critical error: Unable to read user input.");
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Asks if the user wants to place an order, or exit the overview.
	 * 
	 * @return	true if the user wants to place an order
	 * 			false if the user wants to exit the overview
	 */
	private static boolean userWantsOrder() {
		
		System.out.println("Do you want to...");
		System.out.println("1. [P]lace new order.");
		System.out.println("2. [E]xit the overview.");
		System.out.println();
		
		try {
			while(true){
				// We'll only read in the first character.
				String choice = String.valueOf((char) System.in.read());
				// Don't forget to clear the input buffer.
				System.in.skip(System.in.available());
				if(choice.equalsIgnoreCase("P")){
					return true;
				}else if (choice.equalsIgnoreCase("E")){
					return false;
				}else{
					System.out.println();
					System.out.println("Sorry, but \"" + choice + "\" is not a valid response. Please try again.");
					System.out.println();
				}
			}
		} catch (IOException e) {
			System.err.println("Critical error: Unable to read user input.");
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Prints an overview of pending and completed orders.
	 * 
	 * @param 	handler
	 */
	private static void showOrderOverview(NewOrderSessionHandler handler) {
		
		System.out.println("ORDER OVERVIEW");
		System.out.println("---------------");
		System.out.println();
		System.out.println("|- PENDING");
		System.out.println();
		
		//pending
		showOrders(handler.getPendingOrders());
		
		System.out.println("---------------");
		System.out.println();
		System.out.println("|- COMPLETED");
		System.out.println();
		
		//completed
		// These are already sorted by completion date, most recent first.
		showOrders(handler.getCompletedOrders());
		
		System.out.println("---------------");
		System.out.println();
	}

	/**
	 * Prints the orders and (if the order is pending) estimated completion time.
	 * Also displays the model and specification for each order.
	 * 
	 * @param 	completedOrders
	 * 			The orders to be printed. 
	 */
	private static void showOrders(List<OrderContainer> completedOrders) {
		
		for(OrderContainer order : completedOrders){
			Model model = order.getModel();
			
			System.out.println("Order number: " + order.getOrderNumber());
			//TODO: or do we need creation date for completed orders?
			if(!order.isCompleted()) {
				System.out.print("Est. completion: ");
			}
			System.out.println(order.getEstimatedCompletionTime().toString());
			System.out.println("    Model: " + model.getModelName());
			
			Specification spec = order.getSpecifications();
			for(int i = 0; i < spec.getAmountofSpecs(); i++){
				int chosenOption = spec.getSpec(i);
				Option option = model.getModelOption(i);
				String optionDescriptor = option.getOptionName();
				String chosenOptionName = option.getChoiceName(chosenOption);
				System.out.println("    " + optionDescriptor + ": " + chosenOptionName);
			}
			
			System.out.println();
		}
		
	}

	/**
	 * Prints a welcome message for the user.
	 * 
	 * @post	The user feels welcome.
	 */
	private static void showHeader(){
		System.out.println("   ==================================");
		System.out.println("   = Aperture Car Assembly Terminal =");
		System.out.println("   ==================================");
		System.out.println("   \\\\   Garage Holder Subsystem    //");
		System.out.println("    ================================");
		System.out.println();
		System.out.println();
	}
	
}