package domain;

import java.util.ArrayList;
import java.util.List;

public class AssemblyLine {
	/**
	 * The production schedule this assembly line works for
	 */
	private ProductionSchedule productionSchedule;
	/**
	 * The workposts which compose this assemblyline, in their respective orders in the list as they are ordered in the assembly line's layout
	 */
	private ArrayList<WorkPost> workPosts = new ArrayList<WorkPost>();
	/**
	 * An extra location at the end of the assembly line where a finished assembly that just came from the last workpost resides until it is collected 
	 */
	private AssemblyProcedure finishedAssemblyProcedure = null;

	/**
	 * Gets all assemblies currently residing on the assemblyline.
	 * Empty workposts will not return a assembly, so the amount of active assemblies might be lower than the amount of workposts.
	 * 
	 * @return
	 * 		A list of the assemblies on the assemblyline
	 */
	public List<AssemblyProcedureContainer> getActiveAssemblies() {
		List<AssemblyProcedureContainer> toReturn = new ArrayList<AssemblyProcedureContainer>();
		for (WorkPost workPost : this.workPosts) {
			AssemblyProcedureContainer activeAssembly = workPost.getAssemblyProcedure();
			if (activeAssembly != null) {
				toReturn.add(activeAssembly);
			}
		}
		return toReturn;
	}

	/**
	 * Gets the workposts composing the assemblyline, as immutable objects
	 * 
	 * @return
	 * 		A list of immutable containers for all respective workposts in their order
	 */
	public List<WorkPostContainer> getWorkPosts() {
		return new ArrayList<WorkPostContainer>(workPosts);
	}

	/**
	 * @param workPostNumber
	 * @return
	 * @throws IllegalArgumentException
	 */
	public List<AssemblyTaskContainer> getAssemblyTasksAtPost(int workPostNumber) throws IllegalArgumentException {
		if(workPostNumber < 0 || workPostNumber >= getAmountOfWorkPosts())
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		return this.getWorkPost(workPostNumber).getMatchingAssemblyTasks();
	}

	public void completeWorkpostTask(int workPostNumber, int taskNumber) throws IllegalArgumentException {
		if(workPostNumber < 0 || workPostNumber >= getAmountOfWorkPosts())
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		this.getWorkPost(workPostNumber).completeTask(taskNumber);
	}

	public List<OrderContainer> getActiveOrderContainers() {
		throw new UnsupportedOperationException();
	}

	public List<AssemblyProcedureContainer> getCurrentActiveAssemblies() {
		return getActiveAssemblies();
	}

	public List<AssemblyProcedureContainer> getFutureActiveAssemblies() {
		ArrayList<AssemblyProcedureContainer> activeAssemblies = new ArrayList<>(getActiveAssemblies());
		if(activeAssemblies.size()>0)
			activeAssemblies.remove(activeAssemblies.size()-1);
		try{
			Order nextOrder = productionSchedule.getNextOrderToSchedule();
			AssemblyProcedureContainer nextAssembly = new AssemblyProcedure(nextOrder); 
			activeAssemblies.add(0, nextAssembly);
		} catch (IndexOutOfBoundsException e){
			activeAssemblies.add(0, null);
		}
		return activeAssemblies;
		
	}
	
	public int getAmountOfWorkPosts(){
		return workPosts.size();
	}

	public void tryAdvance(int time) throws IllegalStateException{
		ArrayList<AssemblyTaskContainer> tasks = new ArrayList<>();
		for(WorkPostContainer post : getWorkPosts()){
			tasks.addAll(post.getMatchingAssemblyTasks());
		}
		for(AssemblyTaskContainer task : tasks){
			if(!task.isCompleted())
				throw new IllegalStateException("One or more of the tasks on the current state of the assemblyline are still to be executed.");
		}
		shiftWorkPosts();
		putNextOrderOnAssemblyLine();
		// TODO advance time of prod sched
	}
	
	private void putNextOrderOnAssemblyLine() {
		if(getWorkPost(0).getAssemblyProcedure() != null)
			throw new IllegalStateException("First workpost is not empty yet, cannot add a new assembly to the assemblyline.");
		try{
			Order nextOrder = productionSchedule.getNextOrderToSchedule();
			getWorkPost(0).setAssemblyProcedure(new AssemblyProcedure(nextOrder));
		} catch (IndexOutOfBoundsException e) {
			getWorkPost(0).setAssemblyProcedure(null);
		}
	}
	
	private WorkPost getWorkPost(int workPostNumber){
		if(workPostNumber < 0 || workPostNumber >= workPosts.size())
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		return workPosts.get(workPostNumber);
	}

	private void shiftWorkPosts() throws IllegalStateException{
		if(this.finishedAssemblyProcedure != null)
			throw new IllegalStateException("The last finished assembly is still not recovered from the end of the Assembly line.");
		this.finishedAssemblyProcedure = getWorkPost(getAmountOfWorkPosts()-1).getAssemblyProcedure();
		for(int i = workPosts.size()-1; i > 0 ; i++){
			AssemblyProcedure shiftedProcedure = getWorkPost(i-1).getAssemblyProcedure();
			getWorkPost(i).setAssemblyProcedure(shiftedProcedure);
		}
		getWorkPost(0).setAssemblyProcedure(null);
	}
	
	AssemblyProcedure removeFinishedAssemblyFromEndOfTheAssemblyLine() {
		AssemblyProcedure finished = this.finishedAssemblyProcedure;
		this.finishedAssemblyProcedure = null;
		return finished;
	}
	
	public int getSize() {
		return workPosts.size();
	}
}