package ui;

import java.util.ArrayList;
import java.util.List;

import domain.AssemblyTaskContainer;
import domain.WorkPostContainer;
import domain.handlers.PerformAssemblyTaskHandler;

public class PerformAssemblyTasksUIPart {

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
	public PerformAssemblyTasksUIPart(PerformAssemblyTaskHandler handler, UIHelper helper)
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
	private PerformAssemblyTaskHandler getHandler(){
		return this.partHandler;
	}
	
	/** Handler for this part of the UI */
	private PerformAssemblyTaskHandler partHandler;
	
	/** UIhelper of this class */
	private final UIHelper helper;
	
	//--------------------------------------------------------------------------
	// Usecase methods
	//--------------------------------------------------------------------------
	

	/**
	 * Runs the submenu for performing an assembly task.
	 * The user is asked to select his workpost and is presented with a list of tasks to complete.
	 * The user can't exit this menu without choosing a workpost.
	 */
	public void run() {
		System.out.println(helper.SEPERATOR);
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
		System.out.println(helper.SEPERATOR);
		System.out.println("Short overview of task " + task.getOptionName() + ":");
		System.out.println("\t" + task.getOptionDescription());
		System.out.println("Please complete the task, and then fill in how many minutes it took you.");
		int minutes = helper.getIntFromUser(0, Integer.MAX_VALUE);
		getHandler().completeWorkpostTask(postNumber, task.getTaskNumber(), minutes);
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
		List<AssemblyTaskContainer> postTasks = getHandler().getAssemblyTasksAtWorkPost(postNumber);
		List<AssemblyTaskContainer> incompletePostTasks = new ArrayList<>();
		for(AssemblyTaskContainer task : postTasks){
			if(!task.isCompleted())
				incompletePostTasks.add(task);
		}
		for(int i = 0; i<incompletePostTasks.size(); i++){
			AssemblyTaskContainer currentTask = incompletePostTasks.get(i);
			System.out.println((i+1) + ") " + currentTask.getOptionName());
		}
		System.out.println((incompletePostTasks.size()+1) + ") Stop performing assembly tasks");
		int choice = helper.getIntFromUser(1, incompletePostTasks.size()+1);
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
		List<WorkPostContainer> posts= getHandler().getWorkPosts();
		for(int i = 0; i<posts.size(); i++){
			WorkPostContainer currentPost = posts.get(i);
			System.out.println((i+1) + ") " + currentPost.getName());
		}
		return (helper.getIntFromUser(1, posts.size())-1);
	}
}
