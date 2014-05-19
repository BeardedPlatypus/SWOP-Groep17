package domain.initialdata;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nonfunctional.ProductionScheduleTest;
import domain.Manufacturer;
import domain.car.Model;
import domain.car.ModelCatalog;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.handlers.DomainFacade;
import domain.order.Order;
import domain.order.OrderFactory;
import domain.order.SingleTaskCatalog;
import domain.order.SingleTaskOrder;
import domain.order.StandardOrder;
import domain.productionSchedule.ProductionScheduleFacade;
import exceptions.IllegalCarOptionCombinationException;
import exceptions.NoOptionCategoriesRemainingException;
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
		ProductionScheduleFacade prodSched = this.getManufacturer().getProductionSchedule();
		prodSched.submitStandardOrder(order);
	}
	
	public void addPendingSingleOrder(SingleTaskOrder order){
		ProductionScheduleFacade prodSched = this.getManufacturer().getProductionSchedule();
		prodSched.submitSingleTaskOrder(order);
	}
	
	public void scheduleOnLine(int line, Order order){
		//TODO
	}
	
	//--------------------------------------------------------------------------
	// Simulation methods
	//--------------------------------------------------------------------------
	
	/**
	 * Generate a number of the same orders and add them to the system.
	 * 
	 * @param 	numberOfOrders
	 * 			The number of options to be placed.
	 */
	public void simulatePlaceIdenticalOrder(int numberOfOrders) {
		
		//start new order session
		domainFacade.startNewOrderSession();
		
		//select first car model
		Model chosenModel = domainFacade.getCarModels().get(0);
		domainFacade.chooseModel(chosenModel);
		
		//select compatible options
		List<Option> options = new ArrayList<Option>();
		while(domainFacade.orderHasUnfilledOptions()){
			OptionCategory optCat = domainFacade.getNextOptionCategory();
			
			boolean validSelection = false;
			for(int i = 0; i < optCat.getAmountOfOptions() && validSelection; i++){
				Option opt = optCat.getOption(i);
				
				options.add(opt);
				
				if(chosenModel.checkOptionsValidity(options)){
					domainFacade.selectOption(opt);
					validSelection = true;
				} else {
					options.remove(opt);
				}
			}
		}
		//submit composed order
		domainFacade.submitOrder();

		
		//submit order n-1 times
		for(int i = 1; i < numberOfOrders;i++){

			//prepare next order
			domainFacade.startNewOrderSession();
			domainFacade.chooseModel(chosenModel);
			for(Option opt : options){
				domainFacade.selectOption(opt);
			}
			//submit composed order
			domainFacade.submitOrder();
		}

	}
	
}
