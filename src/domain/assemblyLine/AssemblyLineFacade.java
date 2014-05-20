package domain.assemblyLine;

import java.util.List;

import domain.order.Order;
import domain.order.OrderContainer;
import domain.statistics.StatisticsLogger;

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
	public boolean contains(OrderContainer order) {
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
	 * Get a list of pending {@link OrderContainer}s on the assembly line. 
	 * 
	 * @return List of pending order containers on the assembly line.
	 */
	public List<OrderContainer> getActiveOrderContainers() {
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
	public List<AssemblyTaskContainer> getAssemblyTasksAtPost(int workPostNum)
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
	public List<AssemblyProcedure> getAssemblyProcedures() {
		return this.getLine().getAssemblyProcedures();
	}

	//--------------------------------------------------------------------------
	// WorkPost-related variables and methods
	//--------------------------------------------------------------------------
	/**
	 * Get the WorkPosts composing the assembly line, as immutable {@link WorkPostContainer}s
	 * 
	 * @return A list of immutable containers for all respective WorkPosts in their order.
	 */
	public List<WorkPostContainer> getWorkPostContainers() {
		return this.getLine().getWorkPostContainers();
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
	 * Explicitly tell this AssemblyLine to advance, thereby putting the specified
	 * Order on the AssemblyLine
	 * 
	 * @param order
	 * 		The order to schedule
	 */
	public void advance(Order order) {
		this.getLine().advance(order);
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
	public AssemblyProcedure makeAssemblyProcedure(Order order) {
		return this.getLine().makeAssemblyProcedure(order);
	}

	/**
	 * Get this AssemblyLine's OrderSelector,
	 * which determines which Orders this AssemblyLine can handle
	 * 
	 * @return The order selector.
	 */
	public OrderAcceptanceChecker getOrderSelector() {
		return this.getLine().getOrderSelector();
	}

	
	/**
	 * Set this AssemblyLine's StatisticsLogger to the specified StatisticsLogger
	 * 
	 * @param logger
	 * 		The StatisticsLogger of interest
	 * @throws IllegalArgumentException
	 * 		logger is null
	 */
	public void setStatisticsLogger(StatisticsLogger logger) throws IllegalArgumentException {
		this.getLine().setStatisticsLogger(logger);
	}
	
	/**
	 * Get a report on the statistical variables watched by this AssemblyLine
	 * 
	 * @return A report in the form of a String.
	 */
	public String getStatisticsReport() {
		return this.getLine().getStatisticsReport();
	}

	//----- end of AssemblyLine methods -----//
	
	
	//--------- Intermediate methods ---------//

	

	//----- end of Intermediate methods -----//

	
}
