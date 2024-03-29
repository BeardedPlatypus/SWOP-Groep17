package ui;

import java.util.List;

import domain.assembly_line.AssemblyLineView;
import domain.assembly_line.AssemblyTaskView;
import domain.assembly_line.WorkPostView;
import domain.handlers.AssemblyLineStatusHandler;

public class CheckAssemblyLineStatusUIPart {

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
	public CheckAssemblyLineStatusUIPart(AssemblyLineStatusHandler handler, UIHelper helper)
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
	private AssemblyLineStatusHandler getHandler(){
		return this.partHandler;
	}
	
	/** Handler for this part of the UI */
	private AssemblyLineStatusHandler partHandler;

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
		System.out.println("Current overview of the Assembly Lines:");
		System.out.println(helper.SEPERATOR);
		List<AssemblyLineView> lines = getHandler().getLineViews();
		for(int i = 0; i<lines.size(); i++){
			AssemblyLineView line = lines.get(i);
			System.out.println("Assembly line " + (i+1));
			List<WorkPostView> posts = line.getWorkPostViews();
			for(WorkPostView post : posts){
				System.out.println("Workpost " + post.getName() + " with tasks:");
				try{
					List<AssemblyTaskView> tasks = post.getAssemblyProcedureView().getAssemblyTasks();
					for(AssemblyTaskView task : tasks){
						System.out.println("\t" + task.getOptionName() + "(" + task.isCompleted() + ")");
					}
				} catch(IllegalStateException e) {
					System.out.println("\tNone");
				}
				System.out.println(helper.SEPERATOR);
			}
		}
		helper.getEnter();
	}
}
