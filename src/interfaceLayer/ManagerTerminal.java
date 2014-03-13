package interfaceLayer;

import java.io.IOException;
import java.util.List;

import org.javatuples.Pair;

import domain.AdvanceAssemblyLineHandler;
import domain.AssemblyProcedureContainer;
import domain.AssemblyTaskContainer;
import domain.WorkPostContainer;

public class ManagerTerminal {

	public static void login() {
		
		//TODO: decent constructor
		AdvanceAssemblyLineHandler advanceHandler = null;
		
		if(!wantsToAdvance()){
			return;
		}
		
		if(!confirmsCurrentSituation(advanceHandler)){
			return;
		}
		
		int timeSpent = getTimeSpent();
		
		try {
			advanceHandler.tryAdvance(timeSpent);
			System.out.println("Assembly line has been advanced.");
			System.out.println();
			
			List<Pair<AssemblyProcedureContainer,WorkPostContainer>> procedureWorkpostList 
			= advanceHandler.getCurrentWorkpostsAndActiveAssemblies();
			
			printAssemblyLineStatus(procedureWorkpostList);
			
			
		} catch (IllegalStateException e){
			System.out.println("I'm sorry, but the assembly line can not be advanced. Unfinished workposts:");
			List<WorkPostContainer> unfinishedWps = advanceHandler.getUnfinishedWorkPosts();
			
			for(WorkPostContainer wp : unfinishedWps){
				System.out.println("- " + wp.getName());
			}
			
			System.out.println();
		}
		
		pollReadyToProceed();
		
	}

	private static void pollReadyToProceed() {
		
		System.out.println("Press enter when you're finished...");
		
		try {
			//Wait for the user to press enter.
			System.in.read();
			// Don't forget to clear the rest of the input buffer.
			System.in.skip(System.in.available());
			
			System.out.println();
		} catch (IOException e) {
			System.err.println("Critical error: Unable to read user input.");
			e.printStackTrace();
		}
		
	}

	private static int getTimeSpent() {
		
		System.out.println("How much time was spent during the current phase? (In minutes)");
		System.out.println();
		
		while(true){
			
			try {
				String input = "";
				char nextChar = (char) System.in.read();
				
				while(nextChar != '\n'){
					input += nextChar;
					nextChar = (char) System.in.read();
				}
				
				// Don't forget to clear the input buffer.
				System.in.skip(System.in.available());
				
				System.out.println();
				
				return Integer.parseInt(input);
				
				
			} catch (IOException e) {
				System.err.println("Critical error: Unable to read user input.");
				e.printStackTrace();
			} catch (NumberFormatException e) {
				System.out.println("Not a valid integer, please try again.");
				System.out.println();
			}
			
		}
		
	}

	private static boolean confirmsCurrentSituation(
			AdvanceAssemblyLineHandler advanceHandler) {
		
		System.out.println("ASSEMBLY LINE STATUS");
		System.out.println("---------------");
		System.out.println();
		System.out.println("|- CURRENT");
		System.out.println();
		
		List<Pair<AssemblyProcedureContainer,WorkPostContainer>> procedureWorkpostList 
		= advanceHandler.getCurrentWorkpostsAndActiveAssemblies();
		
		printAssemblyLineStatus(procedureWorkpostList);
		
		System.out.println("---------------");
		System.out.println();
		System.out.println("|- COMPLETED");
		System.out.println();
		
		procedureWorkpostList = advanceHandler.getFutureWorkpostsAndActiveAssemblies();
		
		printAssemblyLineStatus(procedureWorkpostList);
		
		System.out.print("Please confirm. ");
		return wantsToAdvance();
		
	}
	
	private static void printAssemblyLineStatus(
			List<Pair<AssemblyProcedureContainer,WorkPostContainer>> procedureWorkpostList) {
		
		for(Pair<AssemblyProcedureContainer,WorkPostContainer> procedureWorkpostPair : procedureWorkpostList){
			AssemblyProcedureContainer assProc = procedureWorkpostPair.getValue0();
			WorkPostContainer workPost = procedureWorkpostPair.getValue1();
			
			System.out.println("Workpost: " + workPost.getName());
			List<AssemblyTaskContainer> assTasks = assProc.getAssemblyTasks();
			
			for(AssemblyTaskContainer assTask : assTasks){
				System.out.print("Task: " + assTask.getName() + " ");
				if(assTask.isCompleted()){
					System.out.println("[COMPLETED]");
				}else{
					System.out.println("[PENDING]");
				}
			}
			System.out.println();
		}
		
	}

	private static boolean wantsToAdvance() {
		
		System.out.println("Do you want to advance the assembly line?");
		System.out.println("1. [Y]es");
		System.out.println("2. [N]o");
		System.out.println();
		
		try {
			while(true){
				// We'll only read in the first character.
				String choice = String.valueOf((char) System.in.read());
				// Don't forget to clear the input buffer.
				System.in.skip(System.in.available());
				if(choice.equalsIgnoreCase("Y")){
					System.out.println();
					return true;
				}else if(choice.equalsIgnoreCase("N")){
					System.out.println();
					return false;
				}else{
					System.out.println();
					System.out.println("Sorry, but \"" + choice + "\" is not a valid workpost choice. Please try again.");
					System.out.println();
				}
			}
		} catch (IOException e) {
			System.err.println("Critical error: Unable to read user input.");
			e.printStackTrace();
		}
		
		return false;
	}

}
