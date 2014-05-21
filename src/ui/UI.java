package ui;

import java.util.Scanner;

import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;

public class UI {
	
	//--------------------------------------------------------------------------
	// Fields and Constants

	private final DomainFacade facade;
	private UIHelper helper;

	//--------------------------------------------------------------------------


	//--------------------------------------------------------------------------
	// Main system method
	//--------------------------------------------------------------------------
	
	/**
	 * The main method of the class. Set up the system with the initialisation handler and
	 * set up a UI with the handlers from the initialisation.
	 * 
	 * The UI is then started.
	 * 
	 * @param args
	 * 		Program input, which isn't used anyway
	 */
	public static void main(String[] args){
		InitialisationHandler initHandler = new InitialisationHandler();
		UI ui = new UI(initHandler.getDomainFacade());
		ui.run();
	}

	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	
	/**
	 * Instantiates a new UI object with given handlers for interfacing with the system.
	 * 
	 * @param advanceHandler
	 * 		The AdvanceAssemblyLineHandler of the system
	 * @param newOrderHandler
	 * 		The NewOrderSessionHandler of the system
	 * @param performTaskHandler
	 * 		The PerformAssemblyTaskHandler of the system
	 */
	public UI(DomainFacade facade){
		if(facade == null)
			throw new IllegalArgumentException("DomainFacade should not be null.");
		this.facade = facade;
		Scanner input = new Scanner(System.in);
		this.helper = new UIHelper(input);
	}
	
	//--------------------------------------------------------------------------
	// UI methods
	//--------------------------------------------------------------------------
	
	/**
	 * Runs the UI, displaying the main menu and giving the user the choice to log in or exit.
	 * This is repeated when exiting a submenu, until the exit option is chosen.
	 * After exiting the main menu the system quits, and all data is lost.
	 */
	public void run(){
		boolean exitRequest = false;
		while(!exitRequest){
			showMainMenu();
			int choice = this.helper.getIntFromUser(1, 9);
			switch(choice){
			case 1:
				orderNewCarRoutine();
				break;
			case 2:
				checkOrderRoutine();
				break;
			case 3:
				performAssemblyTaskRoutine();
				break;
			case 4:
				checkAssemblyLineStatusRoutine();
				break;
			case 5:
				checkProductionStatisticsRoutine();
				break;
			case 6:
				adaptSchedulingAgorithmRoutine();
				break;
			case 7:
				orderSingleTaskRoutine();
				break;
			case 8:
				changeOperationalStatusRoutine();
				break;
			case 9:
				exitRequest = true;
				break;
			default:
				System.out.println("You entered a faulty choice.");
			}
		}

		System.out.println("Shutting down the system, goodbye.");
			
	}

	/**
	 * Prints a basic main menu with all options.
	 */
	private void showMainMenu() {
		System.out.println(helper.SEPERATOR);
		System.out.println("Welcome to the assembly system.");
		System.out.println(helper.SEPERATOR);
		System.out.println("Choose one of the options below:");
		System.out.println("(1) I would like to log in as Garage Holder to order a new car");
		System.out.println("(2) I would like to log in as Garage Holder to check one of the orders");
		System.out.println("(3) I would like to log in as Mechanic to perform a task at my workpost");
		System.out.println("(4) I would like to log in as Mechanic to check the status of the AssemblyLine");
		System.out.println("(5) I would like to log in as Manager to check the production statistics");
		System.out.println("(6) I would like to log in as Manager to adapt the scheduling algorithm");
		System.out.println("(7) I would like to log in as Customs Shop Owner to order custom task");
		System.out.println("(8) I would like to exit and shutdown the system. !CAUTION, LOSS OF ALL DATA!");
	}

	//--------------------------------------------------------------------------
	// Usecase parts
	//--------------------------------------------------------------------------

	private void orderNewCarRoutine() {
		OrderNewCarUIPart uiPart = new OrderNewCarUIPart(
				this.facade.getNewOrderSessionHandler(),this.helper);
		uiPart.run();
	}

	private void checkOrderRoutine() {
		CheckOrderDetailsUIPart uiPart = new CheckOrderDetailsUIPart(
				this.facade.getCheckOrderDetailsHandler(),this.helper);
		uiPart.run();
	}

	private void performAssemblyTaskRoutine() {
		PerformAssemblyTasksUIPart uiPart = new PerformAssemblyTasksUIPart(
				this.facade.getPerformAssemblyTaskHandler(),this.helper);
		uiPart.run();
	}

	private void checkAssemblyLineStatusRoutine() {
		CheckAssemblyLineStatusUIPart uiPart = new CheckAssemblyLineStatusUIPart(
				this.facade.getAssemblyLineStatusHandler(),this.helper);
		uiPart.run();
	}

	private void checkProductionStatisticsRoutine() {
		CheckProductionStatisticsUIPart uiPart = new CheckProductionStatisticsUIPart(
				this.facade.getCheckProductionStatisticsHandler(),this.helper);
		uiPart.run();
	}

	private void adaptSchedulingAgorithmRoutine() {
		AdaptSchedulingAlgorithmUIPart uiPart = new AdaptSchedulingAlgorithmUIPart(
				this.facade.getAdaptSchedulingAlgorithmHandler(),this.helper);
		uiPart.run();
	}

	private void orderSingleTaskRoutine() {
		OrderSingleTaskUIPart uiPart = new OrderSingleTaskUIPart(
				this.facade.getOrderSingleTaskHandler(),this.helper);
		uiPart.run();
	}

	private void changeOperationalStatusRoutine() {
		ChangeOperationelStatusUIPart uiPart = new ChangeOperationelStatusUIPart(
				this.facade.getChangeOperationalStatusHandler(),this.helper);
		uiPart.run();
	}

}
