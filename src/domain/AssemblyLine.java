package domain;

import java.util.ArrayList;
import java.util.List;

public class AssemblyLine {
	private ProductionSchedule productionSchedule;
	private ArrayList<WorkPost> workPosts = new ArrayList<WorkPost>();
	private AssemblyProcedure finishedAssemblyProcedure = null;

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

	public AssemblyProcedure getCompleteAssembly() {
		throw new UnsupportedOperationException();
	}

	public List<WorkPostContainer> getWorkPosts() {
		return new ArrayList<WorkPostContainer>(workPosts);
	}

	public List<AssemblyTaskContainer> getAssemblyTasksAtPost(int workPostNumber) throws IllegalArgumentException {
		return this.workPosts.get(workPostNumber).getMatchingAssemblyTasks();
	}

	public void completeWorkpostTask(int workPostNumber, int taskNumber) throws IllegalArgumentException {
		this.workPosts.get(workPostNumber).completeTask(taskNumber);
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

	public void tryAdvance(int time) throws IllegalStateException{
		ArrayList<AssemblyTaskContainer> tasks = new ArrayList<>();
		for(WorkPost post : workPosts){
			tasks.addAll(post.getMatchingAssemblyTasks());
		}
		for(AssemblyTaskContainer task : tasks){
			if(!task.isCompleted())
				throw new IllegalStateException("One or more of the tasks on the current state of the assemblyline are still to be executed.");
		}
		// TODO advance time of prod sched
		shiftWorkPosts();
		putNextOrderOnAssemblyLine();
	}
	
	private void shiftWorkPosts() throws IllegalStateException{
		if(this.finishedAssemblyProcedure != null)
			throw new IllegalStateException("The last finished assembly is still not recovered from the end of the Assembly line.");
		this.finishedAssemblyProcedure = workPosts.get(workPosts.size()-1).getAssemblyProcedure();
		for(int i = workPosts.size()-1; i > 0 ; i++){
			AssemblyProcedure shiftedProcedure = workPosts.get(i-1).getAssemblyProcedure();
			workPosts.get(i).setAssemblyProcedure(shiftedProcedure);
		}
		workPosts.get(0).setAssemblyProcedure(null);;
	}
	
	protected AssemblyProcedure removeFinishedAssemblyFromEndOfTheAssemblyLine() {
		AssemblyProcedure finished = this.finishedAssemblyProcedure;
		this.finishedAssemblyProcedure = null;
		return finished;
	}
	
	public int getSize() {
		return workPosts.size();
	}
	
	private AssemblyProcedure makeProcedure(Order order){
		return new AssemblyProcedure(order);
	}
}