package interfaceLayer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import domain.AssemblyTaskContainer;
import domain.PerformAssemblyTaskHandler;
import domain.WorkPostContainer;

public class CarMechanicTerminal {

	public static void login(PerformAssemblyTaskHandler assHandler) {
		
		int workPostNumber = selectWorkPost(assHandler);
		List<AssemblyTaskContainer> assTaskList = assHandler.getAssemblyTasksAtPost(workPostNumber);
		
	out:while(true){
			
			int assTaskNumber = selectAssemblyTask(assTaskList);
			if(assTaskNumber == -1) return;
			
			listAssemblyTaskDetails(assTaskList.get(assTaskNumber));
			
			assHandler.completeWorkpostTask(workPostNumber, assTaskNumber);
			
			if(!userWantsToContinue()){
				break out;
			}
			
		}
		
	}

	private static boolean userWantsToContinue() {
		
		System.out.println();
		System.out.println("Do you want to...");
		System.out.println("1. [C]ontinue working on this workpost.");
		System.out.println("2. [E]xit.");
		
		try {
			while(true){
				// We'll only read in the first character.
				String choice = String.valueOf((char) System.in.read());
				// Don't forget to clear the input buffer.
				System.in.skip(System.in.available());
				if(choice.equalsIgnoreCase("C")){
					System.out.println();
					return true;
				}else if(choice.equalsIgnoreCase("E")){
					System.out.println();
					return false;
				}else{
					System.out.println();
					System.out.println("Sorry, but \"" + choice + "\" is not a valid option. Please try again.");
					System.out.println();
				}
			}
		} catch (IOException e) {
			System.err.println("Critical error: Unable to read user input.");
			e.printStackTrace();
		}
		
		return false;
	}

	private static void listAssemblyTaskDetails(
			AssemblyTaskContainer assemblyTaskContainer) {
		
		System.out.println("Perform the following actions to complete the assembly task:");
		
		System.out.println(assemblyTaskContainer.getActionInfo());
		
		System.out.println();
		System.out.println("Press enter when you're finished...");
		
		try {
			//Wait for the user to press enter.
			System.in.read();
			// Don't forget to clear the rest of the input buffer.
			System.in.skip(System.in.available());
			
			System.out.println();
			System.out.println("Assembly task finished.");
			System.out.println();
		} catch (IOException e) {
			System.err.println("Critical error: Unable to read user input.");
			e.printStackTrace();
		}
		
	}

	private static int selectAssemblyTask(
			List<AssemblyTaskContainer> assTaskList) {
		
		LinkedList<AssemblyTaskContainer> pendingTasks = new LinkedList<AssemblyTaskContainer>();
		LinkedList<Integer> taskMapper = new LinkedList<Integer>();
		
		// First we gather the pending assembly tasks.
		for(int i = 0; i < assTaskList.size(); i++){
			AssemblyTaskContainer assTask = assTaskList.get(i);
			if(!assTask.isCompleted()){
				pendingTasks.add(assTask);
				taskMapper.add(i);
			}
		}
		
		if(pendingTasks.size() == 0){
			System.out.println("There are no pending assembly tasks on this workpost.");
			return -1;
		}
		
		System.out.println("Please select one of the pending assembly tasks...");
		for(int i = 0; i < pendingTasks.size(); i++){
			AssemblyTaskContainer assTask = pendingTasks.get(i);
			System.out.println("[" + (i+1) + "] " + assTask.getName());
		}
		
		System.out.println();
		
		try {
			while(true){
				// We'll only read in the first character.
				String choiceStr = String.valueOf((char) System.in.read());
				int choice = 0;
				try{
					choice = Integer.parseInt(choiceStr);
				}catch(Exception e){}
				// Don't forget to clear the input buffer.
				System.in.skip(System.in.available());
				if(choice < 1 || choice > pendingTasks.size()){
					System.out.println();
					System.out.println("Sorry, but \"" + choice + "\" is not a valid assembly task choice. Please try again.");
					System.out.println();
				}else{
					System.out.println();
					return taskMapper.get(choice-1);
				}
			}
		} catch (IOException e) {
			System.err.println("Critical error: Unable to read user input.");
			e.printStackTrace();
		}
		
		// unreachable
		return -1;
	}

	private static int selectWorkPost(
			PerformAssemblyTaskHandler assHandler) {
		
		List<WorkPostContainer> workpostList = assHandler.getWorkPosts();
		
		System.out.println("Please select the workpost you're currently residing at...");
		
		for(int i = 0; i < workpostList.size(); i++){
			System.out.println("[" + (i+1) + "] " + workpostList.get(i).getName());
		}
		System.out.println();
		
		try {
			while(true){
				// We'll only read in the first character.
				String choiceStr = String.valueOf((char) System.in.read());
				int choice = 0;
				try{
					choice = Integer.parseInt(choiceStr);
				}catch(Exception e){}
				// Don't forget to clear the input buffer.
				System.in.skip(System.in.available());
				if(choice < 1 || choice > workpostList.size()){
					System.out.println();
					System.out.println("Sorry, but \"" + choice + "\" is not a valid workpost choice. Please try again.");
					System.out.println();
				}else{
					System.out.println();
					return workpostList.get(choice-1).getWorkPostNumber();
				}
			}
		} catch (IOException e) {
			System.err.println("Critical error: Unable to read user input.");
			e.printStackTrace();
		}
		
		//unreachable code.
		return -1;
	}

}
