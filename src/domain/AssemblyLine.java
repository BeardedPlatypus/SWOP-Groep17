package domain;

import java.util.ArrayList;
import java.util.List;

import domain.WorkPost;

/**
 * A class depicting an AssemblyLine in the system. An AssemblyLine is composed 
 * of a number of {@link WorkPost}s.
 * It is associated with a {@link Manufacturer} that it uses to communicate with the rest
 * of the system.
 * It provides methods for advancing the assembly line and requesting new {@link Order}s.
 * 
 * @author Thomas Vochten, Frederik Goovaerts, Martinus Wilhelmus Tegelaers
 */
public class AssemblyLine {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	//TODO: Raw method, add annotation
	//TODO: Add additional throws of exceptions. 	
	/**
	 * Instantiate a new AssemblyLine with the specified {@link Manufacturer}.
	 * 
	 * @param manufacturer
	 * 		manufacturer that owns this AssemblyLine
	 * @throws IllegalArgumentException
	 * 		manufacturer == null
	 */
	public AssemblyLine(Manufacturer manufacturer) throws IllegalArgumentException {
		if (manufacturer == null) {
			throw new IllegalArgumentException();
		}
		
		this.manufacturer = manufacturer;
		manufacturer.setAssemblyLine(this);
		
		int workPostNum = 0;
		for (TaskType type : TaskType.values()) {
			WorkPost newPost = new WorkPost(type, workPostNum);
			workPostNum++;
			this.workPosts.add(newPost);
		}
	}	
	
	//--------------------------------------------------------------------------
	// Manufacturer.
	//--------------------------------------------------------------------------
	/** 
	 * Get the manufacturer that owns this AssemblyLine. 
	 * 
	 * @return this manufacturer that owns this AssemblyLine.
	 */
	private Manufacturer getManufacturer() {
		return this.manufacturer;
	}
	
	/** The manufacturer that owns this AssemblyLIne. */
	private final Manufacturer manufacturer;

	//--------------------------------------------------------------------------
	// WorkPost related methods and variables.
	//--------------------------------------------------------------------------
	/**
	 * Asks the given {@link WorkPost} to complete {@link AssemblyTask} with given number on its current {@link AssemblyProcedure}
	 * 
	 * @param workPostNumber
	 * 		The number of the {@link WorkPost} we want to address
	 * @param taskNumber
	 * 		The number of the {@link AssemblyTask} that should be marked as completed at given {@link WorkPost}
	 * @throws IllegalArgumentException
	 * 		When the workPostNumber is not a valid index
	 */
	public void completeWorkpostTask(int workPostNumber, int taskNumber) throws IllegalArgumentException {
		if(!this.isValidWorkPost(workPostNumber))
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		this.getWorkPost(workPostNumber).completeTask(taskNumber);
	}
	
	//--------------------------------------------------------------------------
	// OrderContainer methods.
	
	/**
	 * Get the matching assemblyTasks at the specified {@link WorkPost}.
	 * 
	 * @param workPostNumber
	 * 		The number of the WorkPost for which we want to retrieve the tasks
	 * 
	 * @return AssemblyTasks at the given WorkPost matching the workpost's type
	 * 
	 * @throws IllegalArgumentException
	 * 		workPostNumber refers to a WorkPost that does not exist.
	 */
	public List<AssemblyTaskContainer> getMatchingTaskContainersAtWorkPost(int workPostNumber) throws IllegalArgumentException {
		if(!this.isValidWorkPost(workPostNumber))
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		return this.getWorkPost(workPostNumber).getMatchingAssemblyTasks();
	}

	/**
	 * Get a list of pending {@link OrderContainer}s on the assembly line. 
	 * 
	 * @return List of pending order containers on the assembly line.
	 */
	public List<OrderContainer> getActiveOrderContainers() {
		ArrayList<OrderContainer> activeOrders = new ArrayList<>();
		
		for (WorkPost post : this.getWorkPosts()) {
			if (!post.isEmpty()) {
				activeOrders.add(post.getOrderContainer());
			}
		}
		return activeOrders;
	}

	//--------------------------------------------------------------------------
	/**
	 * Get the WorkPosts composing the assembly line, as immutable {@link WorkPostContainer}s
	 * 
	 * @return A list of immutable containers for all respective WorkPosts in their order.
	 */
	public List<WorkPostContainer> getWorkPostContainers() {
		return new ArrayList<WorkPostContainer>(workPosts);
	}

	/** 
	 * Get the size of this AssemblyLine.
	 * 
	 * @return the size of this AssemblyLine.
	 */
	public int getAssemblyLineSize() {
		return this.workPosts.size();
	}
	

	/**
	 * Get the amount of WorkPosts who currently have an AssemblyProcedure, also
	 * counting those with all tasks finished.
	 * 
	 * @return the amount of WorkPosts who currently have an AssemblyProcedure
	 */
	private int getNbOfActiveWorkPosts() {
		int count = 0;
		for(WorkPost p : this.getWorkPosts()) {
			if (!p.isEmpty()) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Get the elapsed time since the last advancement of the AssemblyLine. The
	 * elapsed time of this production stage is the maximum of the elapsed time
	 * at each workpost this stage. This means the workpost that spent the longest
	 * time working this stage indicates the elapsed time.
	 * 
	 * @return the elapsed time since the last advancement of the AssemblyLine
	 */
	private DateTime getElapsedTime() throws IllegalStateException{
		DateTime elapsed = null;
		
		for(WorkPost p : this.getWorkPosts()){
			if(elapsed == null){
				elapsed = p.getElapsedTime();
			} else if (elapsed < p.getElapsedTime()){
				elapsed = p.getElapsedTime();
			}
		}
		
		if(elapsed == null)
			throw new IllegalStateException("No workposts found or no elapsed time registered!");
		
		return elapsed;
	}

	/**
	 * Return if this AssemblyLine is currently empty. 
	 * 
	 * @return True if empty, otherwise false.
	 */
	public boolean isEmpty() {
		for(WorkPost p : this.getWorkPosts()) {
			if (!p.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Check if the specified workPostNumber correpsonds to a valid WorkPost 
	 * in this AssemblyLine.
	 * 
	 * @param workPostNumber 
	 * 		The workPostNumber of the workPost to be checked. 
	 * @return workPostNumber corresponds with a valid workPost. 
	 */
	public boolean isValidWorkPost(int workPostNumber) {
		return workPostNumber >= 0 && workPostNumber < this.getAssemblyLineSize(); 
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
		if(!isValidWorkPost(workPostNumber))
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		return this.getWorkPosts().get(workPostNumber);
	}
	
	/**
	 * Get the {@link WorkPost}s of this AssemblyLine.
	 * 
	 * @return The WorkPosts of this AssLine
	 */
	private List<WorkPost> getWorkPosts() {
		return new ArrayList<WorkPost>(this.workPosts);
	}
		
	/** The {@link WorkPost}s of this assembly line, ordered by the assembly line's layout */
	private final List<WorkPost> workPosts = new ArrayList<WorkPost>();

	/**
	 * Increment the assemblyCounter, indicating an additional workPost has
	 * finished the AssemblyTasks it can perform on its current AssemblyProcedure.
	 * Notify the assemblyLine itself of the fact that a workPost has finished,
	 * so it can check if it has to advance the Line.
	 */
	void incrementFinishedAssemblyCounter() {
		this.finishedAssemblyCounter++;
		this.checkAdvance();
	}

	/**
	 * Counter that keeps track of how many WorkPosts have finished all their
	 * respective tasks at this stage of production
	 */
	private int finishedAssemblyCounter = 0;
	
	//--------------------------------------------------------------------------
	// AssemblyLine Advancement methods. 
	//--------------------------------------------------------------------------
	
	/**
	 * Check whether all WorkPosts with an AssemblyProcedure have finished their
	 * work, and if this is the case, start advancing the AssemblyLine.
	 */
	private void checkAdvance() {
		if((this.finishedAssemblyCounter >= this.getNbOfActiveWorkPosts()) &&
				!this.isEmpty()){
			DateTime elapsedTime = this.getElapsedTime();
			this.tryAdvance(elapsedTime);
		}
		
	}

	//TODO: update this code. 
	/**
	 * Advance this AssemblyLine by one {@link WorkPost} and update the {@link DateTime}
	 * with the elapsed time since the previous advancement. 
	 * Request a new order from the {@link Manufacturer} for the first WorkPost. 
	 * 
	 * @param elapsedTime
	 * 		The time that has elapsed since the last advancement, in minutes. 
	 * @throws IllegalStateException
	 * 		When there is still a finished assembly ready to be collected before
	 * 		the shifting of the WorkPosts.
	 */
	public void tryAdvance(DateTime elapsedTime) throws IllegalStateException{
		ArrayList<AssemblyTaskContainer> tasks = new ArrayList<>();

		this.shiftWorkPosts();
		
		this.getProductionSchedule().advanceTime(elapsedTime);
		
		putNextOrderOnAssemblyLine();
		
		if(this.hasFinishedAssemblyProcedure()){
			AssemblyProcedure finishedAssembly = this.removeFinishedAssemblyFromFinishedAssemblyProcedureCollectionSpace();
			Order finishedOrder = finishedAssembly.getOrder();
			finishedOrder.setAsCompleted();
			this.getManufacturer().completeOrder(finishedOrder);
		}
	}
	
	public void tryAdvance(int time) throws IllegalStateException{
		this.tryAdvance(new DateTime(0, 0, time));
	}

	//TODO:
	/**
	 * Advance this AssemblyLine by one WorkPost. If the final WorkPost is not 
	 * empty its AssemblyProcedure will be put in the completedOrder field.
	 * All other AssemblyProcedures are shifted forward by one WorkPost.
	 * 
	 * @throws IllegalStateException
	 * 		If the completedOrder field is not empty, the line cannot be advanced.
	 */
	private void shiftWorkPosts() throws IllegalStateException{
		if(this.finishedAssemblyProcedure != null)
			throw new IllegalStateException("The last finished assembly is still not recovered from the end of the Assembly line.");
		this.finishedAssemblyProcedure = getWorkPost(getAmountOfWorkPosts()-1).getAssemblyProcedure();
		for(int i = workPosts.size()-1; i > 0 ; i--){
			AssemblyProcedure shiftedProcedure = getWorkPost(i-1).getAssemblyProcedure();
			getWorkPost(i).setAssemblyProcedure(shiftedProcedure);
		}
		getWorkPost(0).setAssemblyProcedure(null);
	}

	//TODO
	/**
	 * Request a new Order from the Manufacturer, convert this to an AssemblyProcedure
	 * and add this AssemblyProcedure to the first WorkPost. 
	 * 
	 * @throws IllegalStateException
	 * 		| !this.getWorkPost(0).isEmpty()
	 */
	private void putNextOrderOnAssemblyLine() throws IllegalStateException{
		if(this.getWorkPost(0).isEmpty())
			throw new IllegalStateException("First workpost is not empty yet, cannot add a new assembly to the assemblyline.");
		
		try{
			Order nextOrder = this.getManufacturer().popNextOrderFromSchedule();
			this.getWorkPost(0).setAssemblyProcedure(this.createNewAssemblyProcedure(nextOrder));
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			//FIXME VERY BAD
			this.getWorkPost(0).setAssemblyProcedure(null);
		}
	}

	//--------------------------------------------------------------------------
	// AssemblyProcedure Factory Methods. 
	//--------------------------------------------------------------------------
	/**
	 * Generate a list of AssemblyTasks from the specified order.
	 * 
	 * @param order
	 * 		The order from which the list of AssemblyTasks is created.
	 * 
	 * @precondition
	 * 		order != null
	 *   
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
	
	/**
	 * Construct a new AssemblyTask with the specified parameters. 
	 * 
	 * @see AssemblyTask
	 */
	protected AssemblyTask makeAssemblyTask(String taskName, 
											String actionDescription,
											TaskType taskType, 
											int index) {
			return new AssemblyTask(taskName, actionDescription, taskType, index);
	}

	/**
	 * Check if this AssemblyLine has a collectedFinshedAssemblyProcedureRobot9000. 
	 * 
	 * @return if has collectedFinishedAssemblyProcedureRobot9000
	 */
	protected boolean hasFinishedAssemblyProcedure() {
		return this.finishedAssemblyProcedure != null;
	}
	
	/**
	 * Remove and return the finished AssemblyProcedure from this AssemblyLine's 
	 * lastFinishedAssemblyProcedure.
	 * 
	 * @return
	 * 		The finished assembly or null if there isn't one
	 * 
	 * @throws IllegalStateException
	 * 		| !this.hasFinishedAssemblyProcedure()
	 */
	private AssemblyProcedure removeFinishedAssemblyFromFinishedAssemblyProcedureCollectionSpace() {
		if (!this.hasFinishedAssemblyProcedure())
			throw new IllegalStateException("I ate fish for breakfast.");
		
		AssemblyProcedure finished = this.finishedAssemblyProcedure;
		this.finishedAssemblyProcedure = null;
		return finished;
	}

	/** The last unprocessed finishedAssemblyProcedure of this AssemblyLine. */
	private AssemblyProcedure finishedAssemblyProcedure = null;
	
	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	// Statistics logger and related methods
	//--------------------------------------------------------------------------
	/** The statistics logger of this assembly line. */
	private final StatisticsLogger statisticsLogger;
	
	/**
	 * Get this AssemblyLine's StatisticsLogger
	 * 
	 * @return The StatisticsLogger
	 */
	private StatisticsLogger getStatisticsLogger() {
		return this.statisticsLogger;
	}
	
	/**
	 * Get a report on the statistical variables watched by this AssemblyLine
	 * 
	 * @return A report in the form of a String.
	 */
	public String getStatisticsReport() {
		return this.getStatisticsLogger().getReport();
	}
}
