package domain;

import java.util.ArrayList;
import java.util.List;

public class AssemblyProcedure implements AssemblyProcedureContainer {
	
	private ArrayList<AssemblyTask> tasks = new ArrayList<AssemblyTask>();
	private Order assemblyOrder;
	
	public AssemblyProcedure(Order order) throws IllegalArgumentException {
		if (order == null) {
			throw new IllegalArgumentException("Cannot initialise an assembly procedure with non-existent order.");
		}
		this.assemblyOrder = order;
		this.generateTasks();
	}

	private void generateTasks() {
		Specification orderSpecs = assemblyOrder.getSpecifications();
		Model orderModel = assemblyOrder.getModel();
		if(!orderModel.isValidSpecification(orderSpecs))
			throw new IllegalStateException("This order does not have matching specifications and model.");
		for(int i=0; i<orderSpecs.getAmountofSpecs();i++){
			Option currentOption = orderModel.getModelOption(i);
			String choiceName = currentOption.getChoiceName(orderSpecs.getSpec(i));
			String taskName = currentOption.getOptionName() + " - " + choiceName;
			tasks.add(new AssemblyTask( taskName, currentOption.getOptionActionDescription(),
					currentOption.getType(), i));
		}
	}

	public Order getOrder() {
		return this.assemblyOrder;
	}

	public List<AssemblyTaskContainer> getAssemblyTasks(){
		return new ArrayList<AssemblyTaskContainer>(tasks);
	}
	
	public List<AssemblyTaskContainer> getAssemblyTasks(TaskType taskType) {
		ArrayList<AssemblyTaskContainer> typeTasks = new ArrayList<AssemblyTaskContainer>();
		for(AssemblyTaskContainer task : tasks){
			if(task.getTaskType().equals(taskType))
				typeTasks.add(task);
		}
		return typeTasks;
	}

	public void completeTask(int intTask, TaskType taskType) throws IllegalArgumentException {
		if (intTask < 0 || intTask >= tasks.size()) {
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