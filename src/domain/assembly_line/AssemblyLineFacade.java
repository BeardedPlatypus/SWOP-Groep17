package domain.assembly_line;

import java.util.List;

import com.google.common.base.Optional;

import domain.order.Order;
import domain.order.OrderView;
import domain.statistics.StatisticsLogger;

/**
 * A facade which hold together an assemblyLine and its controller.
 * This class offers all necessary methods the system should see of both objects.
 * 
 * @author Frederik Goovaerts
 */
public class AssemblyLineFacade implements AssemblyLineView{
	
	//--------------------------------------------------------------------------
	// Constuctor
	//--------------------------------------------------------------------------
	
	/**
	 * Create a new AssemblyLineFacade with given Line and Intermediate as
	 * components
	 * 
	 * @param line
	 * 		The new AssemblyLine for this facade
	 * @param inter
	 * 		The new AssemblyLineController for this facade
	 * 
	 * @throws IllegalArgumentException
	 * 		If either of the parameters is null
	 */
	public AssemblyLineFacade(AssemblyLine line, SchedulerIntermediate inter)
	throws IllegalArgumentException{
		if (line == null)
			throw new IllegalArgumentException("line can not be null!");
		if (inter == null)
			throw new IllegalArgumentException("intermediate can not be null!");
		this.line = line;
		this.inter = inter;
	}

	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	/**
	 * Get the {@link AssemblyLine} of this Facade, for internal use
	 * 
	 * @return the assemblyline
	 */
	private AssemblyLine getLine(){
		return this.line;
	}
	
	private final AssemblyLine line;

	/**
	 * Get the {@link AssemblyLine} of this Facade, for internal use
	 * 
	 * @return the assemblyline
	 */
	private SchedulerIntermediate getIntermediate(){
		return this.inter;
	}
	
	private final SchedulerIntermediate inter;
	
	//--------------------------------------------------------------------------
	// Facade methods
	//--------------------------------------------------------------------------
	
	//--------- AssemblyLine methods ---------//

	/**
	 * Indicate whether the given Order has been put on this AssemblyLine
	 * 
	 * @param order
	 * 		The order to look for
	 * @return This AssemblyLine has the given Order
	 */
	public boolean contains(OrderView order) {
		return this.getLine().contains(order);
	}
	
	/**
	 * Ask the given {@link WorkPost} to complete {@link AssemblyTask} with given number on its current {@link AssemblyProcedure}
	 * 
	 * @param workPostNumber
	 * 		The number of the {@link WorkPost} we want to address
	 * @param taskNumber
	 * 		The number of the {@link AssemblyTask} that should be marked as completed at given {@link WorkPost}
	 * @throws IllegalArgumentException
	 * 		When the workPostNumber is not a valid index
	 * @throws IllegalArgumentException
	 * 		See {@link WorkPost#completeTask(int, int) completeTask(int, int)}
	 * @throws IllegalStateException
	 * 		See {@link WorkPost#completeTask(int, int) completeTask(int, int)}
	 */
	public void completeWorkpostTask(int workPostNumber, int taskNumber, int minutes) throws IllegalArgumentException,
			IllegalStateException{
		this.getLine().completeWorkpostTask(workPostNumber, taskNumber, minutes);
	}

	/**
	 * Get a list of pending {@link OrderView}s on the assembly line. 
	 * 
	 * @return List of pending order containers on the assembly line.
	 */
	public List<OrderView> getActiveOrderContainers() {
		return this.getLine().getActiveOrderContainers();
	}
	
	/**
	 * Get views those AssemblyTasks that are of the specified WorkPost's type.
	 * 
	 * @param workPostNum
	 * 		The WorkPost of interest
	 * @return Views of the AssemblyTasks that are of the specified WorkPost's type
	 * @throws IllegalArgumentException
	 * 		workPostNum refers to a WorkPost that does not exist
	 */
	public List<AssemblyTaskView> getAssemblyTasksAtPost(int workPostNum)
		throws IllegalArgumentException {
		return this.getLine().getAssemblyTasksAtPost(workPostNum);
	}
	
	/**
	 * Get a list of the AssemblyProcedures that are on this AssemblyLine's
	 * WorkPosts. It is padded with nulls for those WorkPosts that are not working
	 * on an AssemblyProcedure
	 * 
	 * @return
	 */
	public List<Optional<AssemblyProcedure>> getAssemblyProcedures() {
		return this.getLine().getAssemblyProcedures();
	}

	//--------------------------------------------------------------------------
	// WorkPost-related variables and methods
	//--------------------------------------------------------------------------
	/**
	 * Get the WorkPosts composing the assembly line, as immutable {@link WorkPostView}s
	 * 
	 * @return A list of immutable containers for all respective WorkPosts in their order.
	 */
	public List<WorkPostView> getWorkPostViews() {
		return this.getLine().getWorkPostViews();
	}

	/**
	 * Get the {@link WorkPost} at the specified position in the assembly line.
	 * 
	 * @param workPostNumber
	 * 		The index of the wanted WorkPost. 
	 * 
	 * @return The requested WorkPost
	 * 
	 * @throws IllegalArgumentException 
	 * 		| workPostNumber < 0 || workPostNumber >= this.getAssemblyLineSize() 
	 */
	protected WorkPost getWorkPost(int workPostNumber) throws IllegalArgumentException{
		return this.getLine().getWorkPost(workPostNumber);
	}

	/** 
	 * Get the size of this AssemblyLine.
	 * 
	 * @return the size of this AssemblyLine.
	 */
	public int getAssemblyLineSize() {
		return this.getLine().getAssemblyLineSize();
	}
	
	/**
	 * Calculate the time the specified AssemblyProcedure would spend on the specified
	 * 
	 * @param procedure
	 * @param workPost
	 * @throws IllegalArgumentException
	 * 		workPostNum is not a valid WorkPost number
	 * @return
	 */
	public int getTimeOnWorkPost(AssemblyProcedure procedure, int workPostNum)
		throws IllegalArgumentException{
		return this.getLine().getTimeOnWorkPost(procedure, workPostNum);
	}

	/**
	 * Return if this AssemblyLine is currently empty. 
	 * 
	 * @return True if empty, otherwise false.
	 */
	public boolean isEmpty() {
		return this.getLine().isEmpty();
	}

	
	//--------------------------------------------------------------------------
	// AssemblyLine Advancement methods. 
	//--------------------------------------------------------------------------
	
	/**
	 * Explicitly tell this AssemblyLine to advance, putting the specified
	 * Orders on the line.
	 * 
	 * @param order
	 * 		Orders to put on the line
	 * @throws IllegalArgumentException
	 * 		orders is null or contains null
	 */
	public void advance(List<Order> orders) {
		this.getLine().advance(orders);
	}

	//--------------------------------------------------------------------------
	// AssemblyProcedure Factory Methods. 
	//--------------------------------------------------------------------------
	/**
	 * Make an AssemblyProcedure out of the specified Order. The AssemblyProcedure
	 * is built out of tasks 
	 * 
	 * @param order
	 * @return
	 */
	public AssemblyProcedure makeAssemblyProcedure(Optional<Order> order) {
		return this.getLine().makeAssemblyProcedure(order);
	}

	//----- end of AssemblyLine methods -----//
	
	
	//--------- Intermediate methods ---------//

	

	//----- end of Intermediate methods -----//

	
}
