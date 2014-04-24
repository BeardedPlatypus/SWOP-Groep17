package ui;

import java.util.List;

import domain.Option;
import domain.Specification;
import domain.handlers.AdaptSchedulingAlgorithmHandler;
import domain.productionSchedule.strategy.SchedulingStrategyView;

public class AdaptSchedulingAlgorithmUIPart {

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
	public AdaptSchedulingAlgorithmUIPart(AdaptSchedulingAlgorithmHandler handler, UIHelper helper)
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
	private AdaptSchedulingAlgorithmHandler getHandler(){
		return this.partHandler;
	}
	
	/** Handler for this part of the UI */
	private AdaptSchedulingAlgorithmHandler partHandler;
	
	/** UIhelper of this class */
	private final UIHelper helper;

	//--------------------------------------------------------------------------
	// Usecase Methods
	//--------------------------------------------------------------------------

	
	public void run() {
		System.out.println("Welcome mister manager.");
		boolean exitMenu = false;
		while(!exitMenu){
			System.out.println("The current scheduling algorithm is: " +
					getHandler().getCurrentAlgorithm().getName());
			System.out.println(helper.SEPERATOR);
			String choice = getAlgorithmName();
			switch(choice){
			case "First-in first-out strategy": setFifoAlg();
					break;
			case "Batch strategy": setBatchAlg();
					break;
			default: exitMenu = true;
					break;
			}
		}
	}

	private String getAlgorithmName() {
		System.out.println("What would you like to do?");
		List<SchedulingStrategyView> algs = getHandler().getAlgorithms();
		for (int i = 0; i < algs.size(); i++) {
			System.out.println((i+1) + ") " + algs.get(i).getName());
		}
		System.out.println((algs.size()+1) + ") Exit Menu");
		int choice = helper.getIntFromUser(1, algs.size()+1);
		if(choice == algs.size()+1)
			return "ExitPLS";
		return algs.get(choice).getName();
	}

	private void setFifoAlg() {
		System.out.println("Switching the system to FIFO mode!");
		getHandler().setFifoAlgorithm();
		System.out.println(helper.SEPERATOR);
	}

	private void setBatchAlg() {
		System.out.println("You have selected the Batch algorithm, this needs a specification.");
		System.out.println("Please select the desired specification:");
		List<Specification> batches = getHandler().getCurrentBatches();
		int chosenBatch = chooseBatch(batches);
		if(chosenBatch >0 && chosenBatch<=batches.size()){
			getHandler().setBatchAlgorithm(batches.get(chosenBatch-1));
			System.out.println("Batch algorithm has been set!");
		}
	}

	private int chooseBatch(List<Specification> batches) {
		for (int i = 0; i < batches.size(); i++) {
			System.out.println((i+1) + ") Specification contents:");
			visualiseSpec(batches.get(i));
		}
		System.out.println((batches.size()+1) + ") Stop setting Batch Algorithm");
		return helper.getIntFromUser(1, batches.size()+1);
	}

	private void visualiseSpec(Specification specification) {
		List<Option> options = specification.getOptions();
		for(Option option : options){
			System.out.println("\t" + option.getName());
		}
	}
}
