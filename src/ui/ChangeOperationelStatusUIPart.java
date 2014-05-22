package ui;

import java.util.List;

import domain.assembly_line.AssemblyLineStateView;
import domain.handlers.ChangeOperationalStatusHandler;

public class ChangeOperationelStatusUIPart {

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
	public ChangeOperationelStatusUIPart(ChangeOperationalStatusHandler handler,
			UIHelper helper) throws IllegalArgumentException{
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
	private ChangeOperationalStatusHandler getHandler(){
		return this.partHandler;
	}
	
	/** Handler for this part of the UI */
	private ChangeOperationalStatusHandler partHandler;

	/** UIhelper of this class */
	private final UIHelper helper;
	
	//--------------------------------------------------------------------------
	// Usecase Methods
	//--------------------------------------------------------------------------

	/**
	 * Shows the user all workposts on the assemblyline with their assemblyprocedures
	 * and then asks the user to press enter.
	 */
	public void run(){
		System.out.println("Welcome mister manager.");
		boolean exitMenu = false;
		while(!exitMenu){
			System.out.println("The current statuses of the assembly lines are:");
			printCurrentStates();
			System.out.println(helper.SEPERATOR);
			printQuitOrChange();
			int choice = this.helper.getIntFromUser(1, 2);
			if(choice==1){
				int amountOfLines = this.partHandler.getAssemblyLineStates().size();
				System.out.println("Which line would you like to change the "
						+ "status for? (1 to " + amountOfLines
						+ ")");
				int lineChoice = this.helper.getIntFromUser(1, amountOfLines);
				System.out.println(this.helper.SEPERATOR);
				changeLineStatus(lineChoice);
			} else {
				exitMenu = true;
			}
		}
	}


	private void printQuitOrChange() {
		System.out.println("What do you want to do?");
		System.out.println("\t1) Change the status of an assembly line");
		System.out.println("\t2) Quit and return to the main menu");
	}

	private void printCurrentStates() {
		List<AssemblyLineStateView> states = this.getHandler().getAssemblyLineStates();
		for(int i = 1; i<= states.size(); i++){
			System.out.println("Assemblyline " + i + ": " + states.get(i).getName());
		}
	}
	

	private void changeLineStatus(int lineChoice) {
		System.out.println("The current status of assembly line" + lineChoice +" is:");
		System.out.println("\t" + this.getHandler().getAssemblyLineStates().get(lineChoice));
		System.out.println("What should be the new status for assembly line "
				+ lineChoice + "?");
		List<AssemblyLineStateView> states = this.getHandler().getAvailableStates();
		for(int i = 1; i<= states.size(); i++){
			System.out.println(i + ") " + states.get(i).getName());
		}
		int stateChoice = this.helper.getIntFromUser(1, states.size());
		System.out.println("The new status of assembly line " + lineChoice + " is:");
		System.out.println("\t"+states.get(stateChoice));
		this.getHandler().setAssemblyLineState(lineChoice, stateChoice);
	}
}
