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
	
	/**
	 * Generate a list of AssemblyTasks from the specified order.
	 * 
	 * @param order
	 * 		The order from which the list of AssemblyTasks is created.
	 * 
	 * @precondition
	 * 		order != null  
	 * @return A list of assembly tasks based on the specified order.
	 * @throws IllegalStateException
	 * 		! order.getModel().isValidSpecification(order.getSpecifications())
	 */
	protected List<AssemblyTask> generateTasksFrom(OrderContainer order) throws IllegalStateException {
		Specification orderSpecs = order.getSpecifications();
		Model orderModel = order.getModel();
		
		if(!orderModel.isValidSpecification(orderSpecs))
			throw new IllegalStateException("This order does not have matching specifications and model.");
		
		List<AssemblyTask> toReturn = new ArrayList<AssemblyTask>();
		
		for(int i = 0; i < orderSpecs.getAmountofSpecs(); i++){
			Option currentOption = orderModel.getModelOption(i);
			String choiceName = currentOption.getChoiceName(orderSpecs.getSpec(i));
			String taskName = currentOption.getOptionName() + " - " + choiceName;
			
			toReturn.add(makeAssemblyTask(taskName,
										  currentOption.getOptionActionDescription(),
										  currentOption.getType(), i));
		}
		return toReturn;
	}
	
	protected AssemblyTask makeAssemblyTask(String taskName, String actionDescription,
			TaskType taskType, int index) {
			return new AssemblyTask(taskName, actionDescription, taskType, index);
	}
}