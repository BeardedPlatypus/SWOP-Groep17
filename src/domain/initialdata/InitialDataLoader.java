package domain.initialdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import domain.assembly_line.AssemblyTaskView;
import domain.assembly_line.WorkPostView;
import domain.car.Model;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.handlers.DomainFacade;
import domain.order.Order;
import domain.order.SingleTaskOrder;
import domain.order.StandardOrder;

/**
 * Class which can manipulate the domainfacade of a system to add initial data
 * for a quick setup.
 * 
 * This class offers methods to place random order, to place composed orders,
 * and to complete parts or all of 
 * 
 * @author Frederik Goovaerts, Simon Slangen
 *
 */
public class InitialDataLoader {

	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	
	/**
	 * Create a new initialDataLoader for given manufacturer
	 * 
	 * @param manu
	 * 		The manufacturer to load the data into
	 */
	public InitialDataLoader(DomainFacade domain){
		this.domainFacade = domain;
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	/**
	 * Get the manufacturer of this loader for internal use
	 * 
	 * @return the manufacturer
	 */
	private DomainFacade getDomainFacade() {
		return domainFacade;
	}

	private final DomainFacade domainFacade;
	
	//--------------------------------------------------------------------------
	// Loading methods
	//--------------------------------------------------------------------------
	
	public void advanceDay(int amountOfDays){
		//TODO
	}
	
	public void addCompletedStandardOrder(StandardOrder order){
		//TODO
	}
	
	public void addCompletedSingleOrder(SingleTaskOrder order){
		//TODO
	}
	
	public void addPendingStandardOrder(StandardOrder order){
		
	}
	
	public void addPendingSingleOrder(SingleTaskOrder order){
		
	}
	
	public void scheduleOnLine(int line, Order order){
		//TODO
	}
	
	//--------------------------------------------------------------------------
	// Simulation methods
	//--------------------------------------------------------------------------
	
	
	//--------- order placement methods ---------//

	/**
	 * Generate a number of the same orders and add them to the system.
	 * This method will generate with each choice the first possible one.
	 * For example, the first model is always used. Out of each optioncategory,
	 * the first option is tried first. If it doesn't fit in the current specification
	 * the second one is tried and so on.
	 * 
	 * @param 	numberOfOrders
	 * 			The number of orders to be placed.
	 */
	public void placeIdenticalStandardOrder(int numberOfOrders) {
		
		//start new order session
		this.getDomainFacade().startNewOrderSession();
		Model chosenModel = this.getDomainFacade().getVehicleModels().get(0);
		this.getDomainFacade().chooseModel(chosenModel);
		
		//select compatible options
		List<Option> options = new ArrayList<Option>();
		while(this.getDomainFacade().orderHasUnfilledOptions()){
			OptionCategory optCat = this.getDomainFacade().getNextOptionCategory();
			boolean validSelection = false;
			for(int i = 0; i < optCat.getAmountOfOptions() && validSelection; i++){
				Option opt = optCat.getOption(i);
				options.add(opt);
				if(this.getDomainFacade().isFullyValidOptionSet(chosenModel, options)){
					this.getDomainFacade().selectOption(opt);
					validSelection = true;
				} else {
					options.remove(opt);
				}
			}
		}
		//submit composed order
		this.getDomainFacade().submitOrder();

		//submit order n-1 times again
		for(int i = 1; i < numberOfOrders;i++){
			this.getDomainFacade().startNewOrderSession();
			this.getDomainFacade().chooseModel(chosenModel);
			for(Option opt : options){
				this.getDomainFacade().selectOption(opt);
			}
			this.getDomainFacade().submitOrder();
		}

	}
	
	/**
	 * Generate a number of the random, probably different, orders and add them
	 * to the system.
	 * 
	 * @param 	numberOfOrders
	 * 			The number of orders to be placed.
	 */
	public void placeRandomStandardOrder(int numberOfOrders) {
		Random rand = new Random();
		
		for(int i=0;i<numberOfOrders;i++){
			List<Model> models = this.getDomainFacade().getVehicleModels();
			Model chosenModel = models.get(rand.nextInt(models.size()));
			placeRandomStandardOrderOfModel(1, chosenModel);
		}

	}
	
	/**
	 * Generate a number of the random, probably different, orders and add them
	 * to the system.
	 * 
	 * @param 	numberOfOrders
	 * 			The number of orders to be placed.
	 */
	public void placeRandomStandardOrderOfModel(int numberOfOrders, Model model) {
		Random rand = new Random();
		
		for(int i = 0; i<numberOfOrders; i++){
			//start new order session
			this.getDomainFacade().startNewOrderSession();

			//select first car model
			this.getDomainFacade().chooseModel(model);

			//select compatible options
			List<Option> options = new ArrayList<Option>();
			while(this.getDomainFacade().orderHasUnfilledOptions()){
				OptionCategory optCat = this.getDomainFacade().getNextOptionCategory();

				boolean validSelection = false;
				while(!validSelection){
					Option opt = optCat.getOption(rand.nextInt(optCat.getAmountOfOptions()));

					options.add(opt);

					if(this.getDomainFacade().isFullyValidOptionSet(model, options)){
						this.getDomainFacade().selectOption(opt);
						validSelection = true;
					} else {
						options.remove(opt);
					}
				}
			}
			//submit composed order
			this.getDomainFacade().submitOrder();
		}

	}

	//----- end of order placement methods -----//

	//--------- Assembly line advancement methods ---------//

	
	/**
	 * Simulates completing all pending orders.
	 */
	public void simulateCompleteAllOrders(){
		while(this.getDomainFacade().getPendingOrders().size() > 0){
			simulateCompleteAllTasksOnAssemblyLine(1);
		}
	}
	
	/**
	 * Simulates the completion of all tasks at each work post, for a given number of iterations.
	 * Tasks are set to have been completed in 40 minutes each.
	 * 
	 * @param numberOfTimes
	 */
	public void simulateCompleteAllTasksOnAssemblyLine(int numberOfTimes) {
		simulateCompleteAllTasksOnAssemblyLine(numberOfTimes, 40);
	}
	
	/**
	 * Simulates the completion of all tasks at each work post, for a given number of iterations.
	 * Tasks are set to have been completed in the given number of minutes.
	 * 
	 * @param numberOfTimes
	 */
	public void simulateCompleteAllTasksOnAssemblyLine(int numberOfTimes,
			int timeSpentPerTask) {
		for(int i = 0; i < numberOfTimes; i++){
			for(WorkPostView wp : this.getDomainFacade().getWorkPosts()){
				for(AssemblyTaskView task : wp.getMatchingAssemblyTasks()){
					if(!task.isCompleted()){
						this.getDomainFacade().completeWorkpostTask(
								wp.getWorkPostNum(), task.getTaskNumber(),
								timeSpentPerTask);
					}
				}
			}
		}
	}

	//----- end of Assembly line advancement methods -----//

	
	
}
