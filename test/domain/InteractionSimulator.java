package domain;

import java.util.LinkedList;
import java.util.List;

import domain.assemblyLine.AssemblyTaskContainer;
import domain.assemblyLine.WorkPostContainer;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.car.Model;
import domain.handlers.DomainFacade;
import exceptions.IllegalCarOptionCombinationException;
import exceptions.NoOptionCategoriesRemainingException;
import exceptions.OptionRestrictionException;

/**
 * Simulates user input to create a initial valid system state while testing.
 * 
 * @author Simon Slangen
 *
 */
public class InteractionSimulator {
	
	private final DomainFacade facade;
	
	public InteractionSimulator(DomainFacade facade){
		this.facade = facade;
	}
	
	/**
	 * Simulates places a given number of identical orders.
	 * 
	 * @param 	numberOfOrders
	 * 			The number of options to be placed.
	 */
	public void simulatePlaceOrder(int numberOfOrders) {
		
		//start new order session
		facade.startNewOrderSession();
		
		//select first car model
		Model chosenModel = facade.getCarModels().get(0);
		facade.chooseModel(chosenModel);
		
		//select compatible options
		LinkedList<Option> options = new LinkedList<Option>();
		while(facade.orderHasUnfilledOptions()){
			OptionCategory optCat = null;
			try {
				optCat = facade.getNextOptionCategory();
			} catch (IllegalStateException
					| NoOptionCategoriesRemainingException e1) {
				e1.printStackTrace();
			}
			boolean validSelection = false;
			
			for(int i = 0; i < optCat.getAmountOfOptions() && validSelection; i++){
				Option opt = optCat.getOption(i);
				
				options.add(opt);
				
				if(chosenModel.checkOptionsValidity(options)){
					try {
						facade.selectOption(opt);
					} catch (IllegalStateException
							| NoOptionCategoriesRemainingException e) {
						e.printStackTrace();
					}
					validSelection = true;
				} else {
					options.removeLast();
				}
			}
		}
		
		//submit order n times
		do{
			numberOfOrders--;
			
			//submit composed order
			try {
				facade.submitOrder();
			} catch (IllegalStateException | IllegalArgumentException
					| IllegalCarOptionCombinationException
					| OptionRestrictionException e) {
				e.printStackTrace();
			}
			
			//prepare next order
			facade.startNewOrderSession();
			facade.chooseModel(chosenModel);
			for(Option opt : options){
				try {
					facade.getNextOptionCategory();
					facade.selectOption(opt);
				} catch (NoOptionCategoriesRemainingException
						| IllegalStateException e) {
					e.printStackTrace();
				}
			}
		} while(numberOfOrders > 0);
		
		//throw away unsubmitted last order
		facade.startNewOrderSession();
	}
	
	/**
	 * Simulates the completion of all tasks at each work post, for a given number of iterations.
	 * Tasks are set to have been completed in 50 minutes each.
	 * 
	 * @param numberOfTimes
	 */
	public void simulateCompleteAllTasksOnAssemblyLine(int numberOfTimes) {
		simulateCompleteAllTasksOnAssemblyLine(numberOfTimes, 50);
	}
	
	/**
	 * Simulates the completion of all tasks at each work post, for a given number of iterations.
	 * Tasks are set to have been completed in the given number of minutes.
	 * 
	 * @param numberOfTimes
	 */
	public void simulateCompleteAllTasksOnAssemblyLine(int numberOfTimes, int timeSpentPerTask) {
		//do numberOfTimes
		for(int i = 0; i < numberOfTimes; i++){
			//for each work post
			for(WorkPostContainer wp : facade.getWorkPosts()){
				//and each task at that work post
				for(AssemblyTaskContainer task : wp.getMatchingAssemblyTasks()){
					//set uncompleted tasks to have been completed in 50 minutes
					if(!task.isCompleted()){
						facade.completeWorkpostTask(wp.getWorkPostNum(), task.getTaskNumber(), timeSpentPerTask);
					}
				}
			}
		}
	}
	
	/**
	 * Completes a given number of unfinished tasks at a given work post, or the maximum number of
	 * tasks if there not enough unfinished tasks. Tasks are set to have been completed in the given number of minutes.
	 */
	public void simulateCompleteTasksOnWorkPost(int numberOfTasks, int workPostNumber, int timeSpentPerTask){
		List<AssemblyTaskContainer> tasks = facade.getWorkPost(workPostNumber).getMatchingAssemblyTasks();
		int finished = 0;
		for(int i = 0; i < tasks.size() && finished < numberOfTasks; i++){
			if(!tasks.get(i).isCompleted()){
				finished++;
				facade.completeWorkpostTask(workPostNumber, tasks.get(i).getTaskNumber(), timeSpentPerTask);
			}
		}
	}
	
	/**
	 * Completes a given number of unfinished tasks at a given work post, or the maximum number of
	 * tasks if there not enough unfinished tasks. Tasks are set to have been completed in the given number of minutes.
	 */
	public void simulateCompleteTasksOnWorkPost(int numberOfTasks, int workPostNumber){
		simulateCompleteTasksOnWorkPost(numberOfTasks, workPostNumber, 50);
	}
	
	/**
	 * Simulates completing all pending orders.
	 */
	public void simulateCompleteAllOrders(){
		while(facade.getPendingOrders().size() > 0){
			simulateCompleteAllTasksOnAssemblyLine(1);
		}
	}
}
