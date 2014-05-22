package domain.productionSchedule;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import domain.assembly_line.TaskType;
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
	 * @param models
	 * 		A set of Models of this new OrderRequest, should contain atleast one model.
	 * 
	 * @postcondition | (new this).getOrderType == STANDARD
	 * @postcondition | (new this).getModel == model
	 * @throws IllegalArgumentException (models == null || models.size() < 1)
	 */
	public OrderRequest(Model[] models) throws IllegalArgumentException {
		if (models == null) {
			throw new IllegalArgumentException("models cannot be null.");
		}
		if (models.length < 1) {
			throw new IllegalArgumentException("models should contain atleast one model.");
		}
		
		this.orderType = Type.STANDARD;
		this.models = Sets.newHashSet(models);
		this.taskTypes = null;
	}
	
	/**
	 * If the orderType of this equals Standard it specifies the model of this
	 * OrderRequest. 
	 * 
	 * @return | this.getOrderType == STANDARD -> (the model of this OrderRequest)
	 * @throws IllegalStateArgument | this.getOrderType != STANDARD
	 */
	public Set<Model> getModels() throws IllegalStateException {
		switch (this.getOrderType()) {
		case STANDARD:
			return new HashSet<>(this.models);
		default:
			throw new IllegalStateException("Cannot access model of a non-StandardOrder.");
		}
	}
	
	/** The model of the StandardOrder request. */
	private final Set<Model> models;
	
	//--------------------------------------------------------------------------
	// SingleTaskOrder
	//--------------------------------------------------------------------------
	/**
	 * Construct a new OrderRequest of a SingleTaskOrder with the specified task.
	 * 
	 * @param task
	 * 		The TaskType of this new OrderRequest
	 * 
	 * @postcondition | (new this).getOrderType == SINGLETASK
	 * @postcondition | (new this).getTaskType == task
	 * 
	 * @throws IllegalArgumentException | task == null
	 */
	public OrderRequest(TaskType[] tasks) throws IllegalArgumentException {
		if (tasks == null) {
			throw new IllegalArgumentException("tasks cannot be null.");
		} 
		if (tasks.length < 1) {
			throw new IllegalArgumentException("tasks has to contain atleast one task.");
		}
		
		this.orderType = Type.SINGLETASK;
		this.taskTypes = Sets.newHashSet(tasks);
		this.models = null;
	}
	
	/**
	 * If the orderType of this equals SingleTask, it specifies the taskType of
	 * this OrderRequest
	 * 
	 * @return | this.getOrderType == SINGLETASK -> (the tasktype of this OrderRequest).
	 * @throws IllegalStateArgument | this.getOrderType != SINGLETASK.
	 */
	public Set<TaskType> getTaskTypes() throws IllegalStateException {
		switch (this.getOrderType()) {
		case SINGLETASK:
			return new HashSet<>(this.taskTypes);
		default:
			throw new IllegalStateException("Cannot access TaskType of a non-SingleTaskOrder.");
		}
	}
	
	/** The taskType of the SingleTaskOrder request. */
	private final Set<TaskType> taskTypes;
}

