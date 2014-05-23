package domain.initialdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import domain.Manufacturer;
import domain.assembly_line.AssemblyTaskView;
import domain.assembly_line.WorkPostView;
import domain.car.Model;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.clock.ClockManipulator;
import domain.handlers.DomainFacade;
import domain.handlers.OrderSingleTaskHandler;
import domain.order.OrderView;
import domain.order.SingleTaskOrder;
import domain.order.StandardOrder;
import exceptions.OptionRestrictionException;

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
	public InitialDataLoader(DomainFacade domain, Manufacturer manuf, ClockManipulator manip){
		this.domainFacade = domain;
		this.manufacturer = manuf;
		this.clockMan = manip;
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	/**
	 * Get the domainfacade of this loader for internal use
	 * 
	 * @return the domainfacade
	 */
	private DomainFacade getDomainFacade() {
		return this.domainFacade;
	}

	private final DomainFacade domainFacade;
	
	/**
	 * Get the manufacturer of this loader for internal use
	 * 
	 * @return the manufacturer
	 */
	private Manufacturer getManufacturer(){
		return this.manufacturer;
	}
	
	private final Manufacturer manufacturer;
	
	/**
	 * Get the clockManipulator of this loader for internal use
	 * 
	 * @return the ClockManipulator
	 */
	private ClockManipulator getClockManipulator(){
		return this.clockMan;
	}
	
	private final ClockManipulator clockMan;
	
	//--------------------------------------------------------------------------
	// Loading methods
	//--------------------------------------------------------------------------
	
	public void addPendingStandardOrder(StandardOrder order){
		this.getManufacturer().submitStandardOrder(order.getModel(), order.getSpecifications().getOptions());
	}
	
	public void addPendingSingleOrder(SingleTaskOrder order){
		this.getManufacturer().submitSingleTaskOrder(order.getSpecifications().getOption(0), order.getDeadline().get());
	}
	
	public void advanceDay(int amountOfDays){
		for (int i = 0; i < amountOfDays; i++) {
			this.completeAllOrders();
			this.getClockManipulator().advanceDay();
		}
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

		Random rand = new Random();
		//start new order session
		this.getDomainFacade().startNewOrderSession();
		Model chosenModel = this.getDomainFacade().getVehicleModels().get(0);
		this.getDomainFacade().chooseModel(chosenModel);
		
		//select compatible options
		List<Option> options = new ArrayList<Option>();
		boolean accepted = false;
		while(!accepted){
			//start new order session
			this.getDomainFacade().startNewOrderSession();

			//select first car model
			this.getDomainFacade().chooseModel(chosenModel);

			//select compatible options
			while(this.getDomainFacade().orderHasUnfilledOptions()){
				OptionCategory optCat = this.getDomainFacade().getNextOptionCategory();
				Option opt = optCat.getOption(rand.nextInt(optCat.getAmountOfOptions()));
				options.add(opt);
				this.getDomainFacade().selectOption(opt);
			}
			try{
				//submit composed order
				this.getDomainFacade().submitOrder();
				accepted = true;
			} catch (OptionRestrictionException e) {
				accepted = false;
			}
		}

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
		//Random rand = new Random();
		//Setup up an order session for no exceptions
		this.getDomainFacade().getNewOrderSessionHandler().startNewOrderSession();
		for(int i=0;i<numberOfOrders;i++){
			List<Model> models = this.getDomainFacade().getVehicleModels();
			Model chosenModel = models.get(4);
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
			boolean accepted = false;
			while(!accepted){
				//start new order session
				this.getDomainFacade().startNewOrderSession();

				//select first car model
				this.getDomainFacade().chooseModel(model);

				//select compatible options
				List<Option> options = new ArrayList<Option>();
				while(this.getDomainFacade().orderHasUnfilledOptions()){
					OptionCategory optCat = this.getDomainFacade().getNextOptionCategory();
					Option opt = optCat.getOption(rand.nextInt(optCat.getAmountOfOptions()));
					options.add(opt);
					this.getDomainFacade().selectOption(opt);
				}
				try{
					//submit composed order
					this.getDomainFacade().submitOrder();
					accepted = true;
				} catch (OptionRestrictionException e) {
					accepted = false;
				}
			}
		}

	}


	public void placeSingleTaskOrder(int numberOfOrders) {
		Random rand = new Random();


		for(int i = 0; i<numberOfOrders; i++){
			boolean accepted = false;
			while(!accepted){
				//start new order session
				OrderSingleTaskHandler sing = this.getDomainFacade().getOrderSingleTaskHandler();
				sing.startNewOrderSession();
				
				sing.selectOption(sing.getPossibleTasks().get(0).getOption(0));
				sing.specifyDeadline(2, 6, 0);
				sing.submitSingleTaskOrder();
				accepted = true;
			}
		}
	}

	//----- end of order placement methods -----//

	//--------- Assembly line advancement methods ---------//

	
	/**
	 * Simulates completing all pending orders.
	 */
	public void completeAllOrders(){
		while(this.getDomainFacade().getPendingOrders().size() > 0){
			for(OrderView v : this.getDomainFacade().getAssemblyLineStatusHandler().getLineViews().get(0).getActiveOrderContainers())
				System.out.println(v.getOrderNumber());
			for(int i=0; i<this.getDomainFacade().getLineViews().size();i++)
				completeAllTasksOnAssemblyLine(i,1);
		}
	}
	
	/**
	 * Simulates the completion of all tasks at each work post, for a given number of iterations.
	 * Tasks are set to have been completed in 40 minutes each.
	 * 
	 * @param numberOfTimes
	 */
	public void completeAllTasksOnAssemblyLine(int lineNb, int numberOfTimes) {
		completeAllTasksOnAssemblyLine(lineNb, numberOfTimes, 10);
	}
	
	/**
	 * Simulates the completion of all tasks at each work post, for a given number of iterations.
	 * Tasks are set to have been completed in the given number of minutes.
	 * 
	 * @param numberOfTimes
	 */
	public void completeAllTasksOnAssemblyLine(int lineNb, int numberOfTimes,
			int timeSpentPerTask) {
		for(int i = 0; i < numberOfTimes; i++){
			for(WorkPostView wp : this.getDomainFacade().getWorkPosts(lineNb)){
				for(AssemblyTaskView task : wp.getMatchingAssemblyTasks()){
					if(!task.isCompleted()){
						this.getDomainFacade().completeWorkpostTask( lineNb,
								wp.getWorkPostNum(), task.getTaskNumber(),
								timeSpentPerTask);
					}
				}
			}
		}
	}

	//----- end of Assembly line advancement methods -----//

	
	
}
