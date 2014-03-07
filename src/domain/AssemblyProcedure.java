package domain;

import java.util.ArrayList;
import java.util.List;

public class AssemblyProcedure implements AssemblyProcedureContainer {
	
	private final List<AssemblyTask> tasks;
	private final Order assemblyOrder;
	
	public AssemblyProcedure(Order order) throws IllegalArgumentException {
		if (order == null) {
			throw new IllegalArgumentException("Cannot initialise an assembly procedure with non-existent order.");
		}
		this.assemblyOrder = order;
		this.tasks = this.generateTasksFrom(order);
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
	
	public Order getOrder() {
		return this.assemblyOrder;
	}

	public List<AssemblyTaskContainer> getAssemblyTasks(){
		return new ArrayList<AssemblyTaskContainer>(tasks);
	}
	
	public List<AssemblyTaskContainer> getAssemblyTasks(TaskType taskType) {
		ArrayList<AssemblyTaskContainer> typeTasks = new ArrayList<AssemblyTaskContainer>();
		for(AssemblyTaskContainer task : this.getAssemblyTasks()){
			if(task.getTaskType().equals(taskType))
				typeTasks.add(task);
		}
		return typeTasks;
	}

	/**
	 * Complete the task specified with intTask of taskType within this
	 * assembly procedure.
	 * @param intTask
	 * 		Specifies the task of taskType that is completed.
	 * @param taskType
	 * 		The type of the task that is completed.
	 * @post let task = this.getAssemblyTasks(taskType).get(intTask) in
	 * 			! task.isCompleted() => (new task).isCompleted() == true
	 * @throws IllegalArgumentException
	 * 		An invalid task number is supplied | 0 > intTask || intTask > getAssemblyTasks.size()
	 * @throws IllegalArgumentException
	 * 		A task of a type different from taskType was selected
	 */
	public void completeTask(int intTask, TaskType taskType) throws IllegalArgumentException {
		if (intTask < 0 || intTask >= this.getAssemblyTasks().size()) {
			throw new IllegalArgumentException("Task number is not a correct value for this procedure.");
		}
		if(tasks.get(intTask).getTaskType()!=taskType)
			throw new IllegalArgumentException("Chosen task is not a task of the correct type.");
		tasks.get(intTask).setCompleted(true);
	}

	public OrderContainer getOrderContainer() {
		return this.getOrder();
	}
}