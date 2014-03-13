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
import domain.OrderContainer;
import domain.PerformAssemblyTaskHandler;
import domain.Specification;
import domain.WorkPostContainer;

public class UI {
	private final AdvanceAssemblyLineHandler advanceHandler;
	private final NewOrderSessionHandler newOrderHandler;
	private final PerformAssemblyTaskHandler performTaskHandler;
	private Scanner input;
	public static final String SEPERATOR = "-------------------------";
	public static final String CRLF = "\r\n";
	
	public static void main(String[] args){
		InitialisationHandler initHandler = new InitialisationHandler();
		UI ui = new UI(initHandler.getAdvanceHandler(), initHandler.getNewOrderHandler(), initHandler.getTaskHandler());
		ui.run();
	}
	
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

	private void orderNewCarRoutine() {
		System.out.println(SEPERATOR);
		boolean exitMenu = false;
		while(!exitMenu){
			List<OrderContainer> completedOrders = getNewOrderHandler().getCompletedOrders();
			List<OrderContainer> pendingOrders = getNewOrderHandler().getPendingOrders();
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

	private void visualiseCompletedAndPendingOrders(List<OrderContainer> completedOrders,
			List<OrderContainer> pendingOrders) {
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
			System.out.println(SEPERATOR);
		}
		
	}

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

	private void showAndCompleteTask(AssemblyTaskContainer task, int postNumber) {
		System.out.println(SEPERATOR);
		System.out.println("Short overview of task " + task.getName() + ":");
		System.out.println("\t" + task.getActionInfo());
		System.out.println("Please complete the task, press enter when task is completed.");
		input.nextLine();
		performTaskHandler.completeWorkpostTask(postNumber, task.getTaskNumber());
	}

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

	private int selectPost() {
		System.out.println("Welcome, please select your workpost:");
		List<WorkPostContainer> posts= getPerformTaskHandler().getWorkPosts();
		for(int i = 0; i<posts.size(); i++){
			WorkPostContainer currentPost = posts.get(i);
			System.out.println((i+1) + ") " + currentPost.getName());
		}
		return (getIntFromUser(1, posts.size())-1);
	}

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
						System.out.println(" " + post.getWorkPostNumber() + " - " + post.getName());
					}
				} finally {
					System.out.println("Press enter to log out.");
					input.nextLine();
				}
			}
		}
	}

	private boolean confirmAdvance() {
		System.out.println("Would you like to try and advance the assemblyline?");
		System.out.println("1) Yes");
		System.out.println("2) No");
		int choice = getIntFromUser(1, 2);
		if(choice == 1)
			return true;
		return false;
	}

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

	public AdvanceAssemblyLineHandler getAdvanceHandler() {
		return advanceHandler;
	}

	private NewOrderSessionHandler getNewOrderHandler() {
		return newOrderHandler;
	}

	private PerformAssemblyTaskHandler getPerformTaskHandler() {
		return performTaskHandler;
	}
	
	private int getIntFromUser(int lowerBound, int upperBound){
		int choice = 0;
		boolean decided = false;
		while(!decided){
			try{
				choice = input.nextInt();
				if(choice >= lowerBound && choice <= upperBound){
					decided = true;
				} else{
					System.out.println("That is not a valid choice.\r\nTry again:");
				}
			} catch (InputMismatchException e){
				System.out.println("That is not a valid choice.\r\nTry again:");
			}
		}
		return choice;
	}
}
