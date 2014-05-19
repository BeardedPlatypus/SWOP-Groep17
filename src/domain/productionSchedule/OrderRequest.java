package domain.productionSchedule;

import domain.assemblyLine.TaskType;
import domain.car.Model;

/** 
 * OrderRequest provides an interface for requesting orders from the 
 * ProductionSchedule of this project.
 * It specifies the type of order requested, currently either StandardOrder or
 * SingleTaskOrder. And if a standard order is requested the model, if the 
 * SingleTaskOrder is requested the OptionCategory.
 * 
 * @author Martinus Wilhelmus Tegelaers
 */
public class OrderRequest {
	//--------------------------------------------------------------------------
	// Shared
	//--------------------------------------------------------------------------
	/**
	 * Get the OrderType of this OrderRequest. 
	 * 
	 * @return the OrderType of this OrderRequest
	 */
	public Type getOrderType() {
		return this.orderType;
	}
	
	/** The type of the order that is requested. */
	private final Type orderType;
	
	/** The possible types of orders that can be specified in this OrderRequest.*/
	public enum Type {STANDARD, SINGLETASK};
	
	//--------------------------------------------------------------------------
	// StandardOrder Request
	//--------------------------------------------------------------------------
	/**
	 * Construct a new OrderRequest of a StandardOrder with the specified Model.
	 * 
	 * @param model
	 * 		The Model of this new OrderRequest.
	 * 
	 * @postcondition | (new this).getOrderType == STANDARD
	 * @postcondition | (new this).getModel == model
	 * 
	 * @throws IllegalArgumentException | model == null
	 */
	public OrderRequest(Model model) throws IllegalArgumentException {
		if (model == null) {
			throw new IllegalArgumentException("model cannot be null.");
		}
		
		this.orderType = Type.STANDARD;
		this.model = model;
		this.taskType = null;
	}
	
	/**
	 * If the orderType of this equals Standard it specifies the model of this
	 * OrderRequest. 
	 * 
	 * @return | this.getOrderType == STANDARD -> (the model of this OrderRequest)
	 * @throws IllegalStateArgument | this.getOrderType != STANDARD
	 */
	public Model getModel() throws IllegalStateException {
		switch (this.getOrderType()) {
		case STANDARD:
			return this.model;
		default:
			throw new IllegalStateException("Cannot access model of a non-StandardOrder.");
		}
	}
	
	/** The model of the StandardOrder request. */
	private final Model model;
	
	//--------------------------------------------------------------------------
	// SingleTaskOrder
	//--------------------------------------------------------------------------
	/**
	 * Construct a new OrderRequest of a SingleTaskOrder with the specified task.
	 * 
	 * 
	 * @param task
	 * 		The TaskType of this new OrderRequest
	 * 
	 * @postcondition | (new this).getOrderType == SINGLETASK
	 * @postcondition | (new this).getTaskType == task
	 * 
	 * @throws IllegalArgumentException | task == null
	 */
	public OrderRequest(TaskType task) throws IllegalArgumentException {
		if (task == null) {
			throw new IllegalArgumentException("task cannot be null.");
		}
		
		this.orderType = Type.SINGLETASK;
		this.taskType = task;
		this.model = null;
	}
	
	/**
	 * If the orderType of this equals SingleTask, it specifies the taskType of
	 * this OrderRequest
	 * 
	 * @return | this.getOrderType == SINGLETASK -> (the tasktype of this OrderRequest).
	 * @throws IllegalStateArgument | this.getOrderType != SINGLETASK.
	 */
	public TaskType getTaskType() throws IllegalStateException {
		switch (this.getOrderType()) {
		case SINGLETASK:
			return this.taskType;
		default:
			throw new IllegalStateException("Cannot access TaskType of a non-SingleTaskOrder.");
		}
	}
	
	/** The taskType of the SingleTaskOrder request. */
	private final TaskType taskType;
}

