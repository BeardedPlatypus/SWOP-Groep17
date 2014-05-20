package domain.assemblyLine.virtualAss;

import domain.assemblyLine.TaskType;
import domain.order.Order;

/**
 * Helper class for the VirtualAssemblyLine. Stores the order and if it is
 * finished. 
 * 
 * @author Martinus Wilhelmus Tegelaers.
 *
 */
public class VirtualAssProc {
	/**
	 * Construct a new VirtualAssProc with the specified Order.
	 * 
	 * @param order
	 * 		The Order of this VirtualAssProc.
	 * 
	 * @throws IllegalArgumentException | order == null.
	 */
	public VirtualAssProc(Order order) throws IllegalArgumentException {
		if (this.order == null)
			throw new IllegalArgumentException("Order cannot be null");
			
		this.order = order;
	}
	
	//--------------------------------------------------------------------------
	// order related methods.
	//--------------------------------------------------------------------------
	/**
	 * get the number of minutes that this VirtualAssProc spends on the 
	 * specified TaskType
	 * 
	 * @return the number of minutes that this VirtualAssProc spends on the 
	 * 		   specified TaskType.
	 */
	public int getMinutesOnPostOfType(TaskType task) {
		return this.getOrder().getMinutesOnPostOfType(task);
	}
	
	/** 
	 * Get the Order of this VirtualAssProc. 
	 * 
	 * @return the Order of this VirtualAssProc.
	 */
	private Order getOrder() {
		return this.order;
	}
	
	/** The order of this VirtualAssProc. */
	private final Order order;
	
	//--------------------------------------------------------------------------
	// isFinished.
	//--------------------------------------------------------------------------
	/** 
	 * Check if this VirtualAssProc is finished. 
	 * 
	 * @return if this VirtualAssProc is finished.
	 */
	public boolean hasFinished() {
		return this.isFinished;
	}
	
	/**
	 * Set if this VirtualAssProc is finished.
	 * 
	 * @param isFinished
	 * 		The new finished state of this VirtualAssProc
	 * 
	 * @postcondition | (new this).isFinished == isFinished;
	 */
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	/** Finished state of this VirtualAssProc. */
	private boolean isFinished = false;
}
