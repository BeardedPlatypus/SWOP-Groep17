package ui;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.javatuples.Pair;

import domain.AdvanceAssemblyLineHandler;
import domain.AssemblyProcedureContainer;
import domain.AssemblyTaskContainer;
import domain.InitialisationHandler;
import domain.Model;
import domain.NewOrderSessionHandler;
import domain.Option;
import domain.PerformAssemblyTaskHandler;
import domain.Specification;
import domain.WorkPostContainer;
import domain.order.OrderContainer;

public class UI {
	private final AdvanceAssemblyLineHandler advanceHandler;
	private final NewOrderSessionHandler newOrderHandler;
	private final PerformAssemblyTaskHandler performTaskHandler;
	private Scanner input;
	public static final String SEPERATOR = "-------------------------";
	public static final String CRLF = "\r\n";
	
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
		UI ui = new UI(initHandler.getAdvanceHandler(), initHandler.getNewOrderHandler(), initHandler.getTaskHandler());
		ui.run();
	}
	
	
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
	public UI(AdvanceAssemblyLineHandler advanceHandler, NewOrderSessionHandler newOrderHandler,
			PerformAssemblyTaskHandler performTaskHandler){
		if(advanceHandler == null)
			throw new IllegalArgumentException("AdvanceAssemblyLineHandler should not be null.");
		if(newOrderHandler == null)
			throw new IllegalArgumentException("NewOrderSessionHandler should not be null.");
		if(performTaskHandler == null)
			throw new IllegalArgumentException("PerformAssemblyTaskHandler should not be null.");
		this.advanceHandler = advanceHandler;
		this.newOrderHandler = newOrderHandler;
		this.performTaskHandler = performTaskHandler;
		input = new Scanner(System.in);
	}
	
	/**
	 * Runs the UI, displaying the main menu and giving the user the choice to log in or exit.
	 * This is repeated when exiting a submenu, until the exit option is chosen.
	 * After exiting the main menu the system quits, and all data is lost.
	 */
	public void run(){
		boolean exitRequest = false;
		while(!exitRequest){
			showMainMenu();
			int choice = getIntFromUser(1, 4);
			switch(choice){
			case 1:
				orderNewCarRoutine();
				break;
			case 2:
				performAssemblyTaskRoutine();
				break;
			case 3:
				advanceAssemblyLineRoutine();
				break;
			case 4:
				exitRequest = true;
				break;
			default:
				System.out.println("You entered a faulty choice.");
			}
		}
		System.out.println("Shutting down the system, goodbye.");
			
	}

	/**
	 * Runs the routine for ordering a new car.
	 * Until the exit choice is taken on the ordermenu the possible models are printed,
	 * and after choosing one, the specs are collected and a new model is made if the user
	 * hasn't canceled during the specs.
	 * If both model and specs are chosen, the order is placed and the submenu runs again,
	 * with an updated view of the pending and completed orders.
	 */
	private void orderNewCarRoutine() {
		System.out.println(SEPERATOR);
		boolean exitMenu = false;
		while(!exitMenu){
			List<OrderContainer> completedOrders = getNewOrderHandler().getCompletedOrders();
			List<OrderContainer> pendingOrders = getNewOrderHandler().getIncompleteOrders();
			visualiseCompletedAndPendingOrders(completedOrders,pendingOrders);
			System.out.println("What do you want to do?:" + CRLF + "1) Order a new car" + CRLF + "2) Exit this menu");
			int choice = getIntFromUser(1, 2);
			if(choice == 2){
				exitMenu = true;
			} else {
				List<Model> orderModels = getNewOrderHandler().getNewOrderModels();
				int modelChoice = getModelChoice(orderModels);
				Specification orderSpecification = getSpecs(orderModels.get(modelChoice));
				if(orderSpecification != null)
					getNewOrderHandler().chooseModelAndSpecifications(orderModels.get(modelChoice), orderSpecification);
			}
		}
		
	}

	/**
	 * Constructs a Specification from user input based on the options in the model.
	 * Each option is showed with its choices and the user selects a choice for each one.
	 * At each point, the user can also cancel the order, and is returned to the order submenu.
	 * 
	 * A null object is returned on canceling, which is handled by the OrderRoutine.
	 * 
	 * @param model
	 * 		The model for which the user wants to set specifications
	 * @return
	 * 		The Specifications object created if all options are succesfully evaluated,
	 * 		or null if the order is canceled.
	 */
	private Specification getSpecs(Model model) {
		int optionsUpperLimit = model.getAmountOfOptions();
		int[] choices = new int[optionsUpperLimit];
		int optionCounter = 0;
		boolean cancelOrder = false;
		System.out.println("Which options would you like?");
		while(optionCounter<optionsUpperLimit && !cancelOrder){
			Option currentOption = model.getModelOption(optionCounter);
			System.out.println("Option: " + currentOption.getOptionName());
			for(int i = 0; i<currentOption.getAmountOfChoices();i++){
				System.out.println((i+1) + ") " + currentOption.getChoiceName(i));
			}
			System.out.println((currentOption.getAmountOfChoices() + 1 ) + ") Cancel placing an order");
			int choice = getIntFromUser(1, currentOption.getAmountOfChoices() + 1);
			if(choice == currentOption.getAmountOfChoices()+1){
				cancelOrder = true;
			} else {
				choices[optionCounter] = choice-1;
				optionCounter++;
			}
		}
		if(cancelOrder)
			return null;
		return new Specification(choices);
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
		System.out.println(SEPERATOR);
		int modelCounter = 1;
		for(Model model: orderModels){
			System.out.println( modelCounter + ") " + model.getModelName());
			modelCounter++;
		}
		return getIntFromUser(1, amountOfChoices)-1;
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
		System.out.println("Current time: " + getNewOrderHandler().currentTime().toString());
		System.out.println(SEPERATOR);
		System.out.println("Completed orders to date:");
		System.out.println(SEPERATOR);
		for(OrderContainer order: completedOrders){
			Model currentOrderModel = order.getModel();
			int optionAmount = currentOrderModel.getAmountOfOptions();
			
			System.out.println("Model Name:" + currentOrderModel.getModelName());
			for(int i = 0; i < optionAmount; i++){
				int currentSpec = order.getSpecifications().getSpec(i);
				Option currentOption = currentOrderModel.getModelOption(i);
				System.out.println("\tOption: " + currentOption.getOptionName() + ", with specification: " + currentOption.getChoiceName(currentSpec));
			}
			System.out.println(SEPERATOR);
		}
		
		System.out.println("Pending orders at this moment:");
		System.out.println(SEPERATOR);
		for(OrderContainer order: pendingOrders){
			Model currentOrderModel = order.getModel();
			int optionAmount = currentOrderModel.getAmountOfOptions();
			
			System.out.println("Model Name:" + currentOrderModel.getModelName());
			for(int i = 0; i < optionAmount; i++){
				int currentSpec = order.getSpecifications().getSpec(i);
				Option currentOption = currentOrderModel.getModelOption(i);
				System.out.println("\tOption: " + currentOption.getOptionName() + ", with specification: " + currentOption.getChoiceName(currentSpec));
			}
			System.out.println("\twith estimated completion time: " + getNewOrderHandler().getEstimatedCompletionTime(order).toString());
			System.out.println(SEPERATOR);
		}
		
	}

	/**
	 * Runs the submenu for performing an assembly task.
	 * The user is asked to select his workpost and is presented with a list of tasks to complete.
	 * The user can't exit this menu without choosing a workpost.
	 */
	private void performAssemblyTaskRoutine() {
		System.out.println(SEPERATOR);
		int postNumber = selectPost();
		boolean exit = false;
		while(!exit){
			AssemblyTaskContainer task = selectTask(postNumber);
			if(task == null){
				exit = true;
			}else{
				showAndCompleteTask(task,postNumber);
			}
		}
	}

	/**
	 * The user is shown a description of given task, along with post number of the task, for completion purposes.
	 * The user indicates when he is finished, and the task is marked as completed.
	 * 
	 * @param task
	 * 		The task the user has to execute
	 * @param postNumber
	 * 		The post number the task originates from
	 */
	private void showAndCompleteTask(AssemblyTaskContainer task, int postNumber) {
		System.out.println(SEPERATOR);
		System.out.println("Short overview of task " + task.getName() + ":");
		System.out.println("\t" + task.getActionInfo());
		System.out.println("Please complete the task, press enter when task is completed.");
		input.nextLine();
		input.nextLine();
		performTaskHandler.completeWorkpostTask(postNumber, task.getTaskNumber());
	}

	/**
	 * Shows a menu of assembly tasks based on the given workpost number.
	 * The tasks match the workpost's type.
	 * The user chooses one of the tasks, which is then returned.
	 * The user can also choose to stop performing tasks.
	 * 
	 * @param postNumber
	 * 		The post number the user currently resides at
	 * @return
	 * 		The chosen task as a container
	 */
	private AssemblyTaskContainer selectTask(int postNumber) {
		System.out.println("Please select an incomplete task at your workpost from the ones below:");
		List<AssemblyTaskContainer> postTasks = getPerformTaskHandler().getAssemblyTasksAtPost(postNumber);
		List<AssemblyTaskContainer> incompletePostTasks = new ArrayList<>();
		for(AssemblyTaskContainer task : postTasks){
			if(!task.isCompleted())
				incompletePostTasks.add(task);
		}
		for(int i = 0; i<incompletePostTasks.size(); i++){
			AssemblyTaskContainer currentTask = incompletePostTasks.get(i);
			System.out.println((i+1) + ") " + currentTask.getName());
		}
		System.out.println((incompletePostTasks.size()+1) + ") Stop performing assembly tasks");
		int choice = getIntFromUser(1, incompletePostTasks.size()+1);
		if(choice == incompletePostTasks.size()+1){
			return null;
		} else {
			return incompletePostTasks.get(choice-1);
		}
	}

	/**
	 * The user is shown a list of all workposts in the system, and has to choose one.
	 * The choice is made based on indices, and the returned int symbolising the workpost
	 * is based on the order of the workposts, the first being 0.
	 * 
	 * @return
	 * 		The number of the workpost, taken from the order they appear on the assembly line,
	 * 		with the first workpost being 0
	 */
	private int selectPost() {
		System.out.println("Welcome, please select your workpost:");
		List<WorkPostContainer> posts= getPerformTaskHandler().getWorkPosts();
		for(int i = 0; i<posts.size(); i++){
			WorkPostContainer currentPost = posts.get(i);
			System.out.println((i+1) + ") " + currentPost.getName());
		}
		return (getIntFromUser(1, posts.size())-1);
	}

	/**
	 * The user logs in as manager and is asked whether or not he wants to advance the assemblyline.
	 * The user either immediately exits and doens't advance the assembly line, or gets a view
	 * on the current and estimated future status, without checking the elapsed time.
	 * If the user then wants to advance, he puts in the elapsed time, and the system checks whether
	 * the line can actually be advanced.
	 * If the line can be advanced, it is, and the system time is updated with the elapsed time.
	 * If the line cannot be advanced an error is shown and the manager is logged out.
	 */
	private void advanceAssemblyLineRoutine() {
		System.out.println(SEPERATOR);
		System.out.println("Hello manager. Would you like to advance the assembly line?");
		System.out.println("1) Yes");
		System.out.println("2) No");
		int choice = getIntFromUser(1, 2);
		if(choice == 2){
			System.out.println("Logging you out.");
		} else {
			System.out.println(SEPERATOR);
			presentCurrentLineStatus();
			presentFutureLineStatus();
			boolean continueAdvance = confirmAdvance();
			if(!continueAdvance){
				System.out.println("Logging you out.");
			} else {
				System.out.println("How many minutes have elapsed since last advancing the assembly line?");
				int minutes = getIntFromUser(0, Integer.MAX_VALUE);
				try{
					getAdvanceHandler().tryAdvance(minutes);
					System.out.println("Assembly line succesfully advanced.");
					presentCurrentLineStatus();
				} catch (IllegalStateException e) {
					System.out.println("Assembly line could not be moved forward because of unfinished tasks.");
					List<WorkPostContainer> unfinishedPosts = getAdvanceHandler().getUnfinishedWorkPosts();
					System.out.println("Unfinished posts are:");
					for(WorkPostContainer post : unfinishedPosts){
						System.out.println(" " + (post.getWorkPostNumber()+1) + " - " + post.getName());
					}
				} finally {
					System.out.println("Press enter to log out.");
					input.nextLine();
					input.nextLine();
				}
			}
		}
	}

	/**
	 * Asks whether or not the manager wants to advance the assembly line, returning a boolean
	 * signaling the decision.
	 * 
	 * @return
	 * 		The choice the user made, whether or not he wants to advance the line
	 */
	private boolean confirmAdvance() {
		System.out.println("Would you like to try and advance the assemblyline?");
		System.out.println("1) Yes");
		System.out.println("2) No");
		int choice = getIntFromUser(1, 2);
		if(choice == 1)
			return true;
		return false;
	}

	/**
	 * Prints a view of the future assembly line status, in which all current assemblies are shifted forwards
	 * one position, and a new order is scheduled on the first post if the system has time for its assembly.
	 * Each assembly shows its tasks and whether or not those are completed.
	 */
	private void presentFutureLineStatus() {
		List<Pair<AssemblyProcedureContainer, WorkPostContainer>> pairs = getAdvanceHandler().getFutureWorkpostsAndActiveAssemblies();
		System.out.println("Future assembly line status (after advancing):"+CRLF+"----");
		for(Pair<AssemblyProcedureContainer, WorkPostContainer> pair : pairs){
			WorkPostContainer currentWorkPost = pair.getValue1();
			AssemblyProcedureContainer currentProcedure = pair.getValue0();
			System.out.println("Post " + (currentWorkPost.getWorkPostNumber()+1) + ": " + currentWorkPost.getName());
			System.out.println("currently contains:");
			if(currentProcedure != null){
				System.out.println("Order number:" + currentProcedure.getOrder().getOrderNumber());
				System.out.println("with assembly tasks:");
				List<AssemblyTaskContainer> currentProcedureTasks = currentProcedure.getAssemblyTasks();
				for(AssemblyTaskContainer task : currentProcedureTasks){
					if(task.isCompleted()){
						System.out.println(task.getName() + " (" + task.getTaskType().getName() + ") : Completed");
					} else {
						System.out.println(task.getName() + " (" + task.getTaskType().getName() + ") : Incomplete");
					}
				}
			} else {
				System.out.println("No active assembly at this post.");
			}
			System.out.println("----");
		}
	}

	/**
	 * Prints a view of the current assembly line status with its assemblies.
	 * Each assembly shows its tasks and whether or not those are completed.
	 */
	private void presentCurrentLineStatus() {
		List<Pair<AssemblyProcedureContainer, WorkPostContainer>> pairs = getAdvanceHandler().getCurrentWorkpostsAndActiveAssemblies();
		System.out.println("Current assembly line status:"+CRLF+"----");
		for(Pair<AssemblyProcedureContainer, WorkPostContainer> pair : pairs){
			WorkPostContainer currentWorkPost = pair.getValue1();
			AssemblyProcedureContainer currentProcedure = pair.getValue0();
			System.out.println("Post " + (currentWorkPost.getWorkPostNumber()+1) + ": " + currentWorkPost.getName());
			System.out.println("currently contains:");
			if(currentProcedure != null){
				System.out.println("Order number:" + currentProcedure.getOrder().getOrderNumber());
				System.out.println("with assembly tasks:");
				List<AssemblyTaskContainer> currentProcedureTasks = currentProcedure.getAssemblyTasks();
				for(AssemblyTaskContainer task : currentProcedureTasks){
					if(task.isCompleted()){
						System.out.println(task.getName() + " (" + task.getTaskType().getName() + ") : Completed");
					} else {
						System.out.println(task.getName() + " (" + task.getTaskType().getName() + ") : Incomplete");
					}
				}
			} else {
				System.out.println("No active assembly at this post.");
			}
			System.out.println("----");
		}
	}

	/**
	 * Prints a basic main menu with all options.
	 */
	private void showMainMenu() {
		System.out.println(SEPERATOR);
		System.out.println("Welcome to the assembly system.");
		System.out.println(SEPERATOR);
		System.out.println("Choose one of the options below:");
		System.out.println("(1) I would like to log in as Garage Holder to order a new car");
		System.out.println("(2) I would like to log in as Mechanic to perform a task at my workpost");
		System.out.println("(3) I would like to log in as Manager to advance the assembly line");
		System.out.println("(4) I would like to exit and shutdown the system. !CAUTION, LOSS OF ALL DATA!");
	}

	/**
	 * Getter for internal use of the advanceHandler
	 * 
	 * @return
	 * 		the advanceHandler
	 */
	private AdvanceAssemblyLineHandler getAdvanceHandler() {
		return advanceHandler;
	}

	/**
	 * Getter for internal use of the newOrderHandler
	 * 
	 * @return
	 * 		the newOrderHandler
	 */
	private NewOrderSessionHandler getNewOrderHandler() {
		return newOrderHandler;
	}

	/**
	 * Getter for internal use of the performTaskHandler
	 * 
	 * @return
	 * 		the performTaskHandler
	 */
	private PerformAssemblyTaskHandler getPerformTaskHandler() {
		return performTaskHandler;
	}
	
	/**
	 * Method to get integer input from a user.
	 * A lower and upper bound are supplied, between which the choice has to be, both bounds inclusive.
	 * If an integer outside of the bounds is supplied, or wrong input is given, an error is displayed and the user
	 * is asked for new input.
	 * @param lowerBound
	 * 		The lower bound for allowed input, inclusive
	 * @param upperBound
	 * 		The upper bound for allowed input, inclusive
	 * @return
	 * 		The legal input between the bounds
	 */
	private int getIntFromUser(int lowerBound, int upperBound){
		int choice = 0;
		boolean decided = false;
		while(!decided){
			try{
				choice = Integer.parseInt(input.nextLine());
				if(choice >= lowerBound && choice <= upperBound){
					decided = true;
				} else{
					System.out.println("That is not a valid choice.\r\nTry again:");
				}
			} catch (NumberFormatException e){
				System.out.println("That is not a valid choice.\r\nTry again:");
			}
		}
		return choice;
	}
}
