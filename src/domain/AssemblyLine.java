package domain;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

/**
 * 
 * A class depicting an assembly line in the system. An assembly line is composed of a number of workposts.
 * It is linked to a production schedule, which supplies it with new orders when it needs a new order to fill
 * the first workpost when it becomes vacant.
 * It also has a space to store one completed assembly which just came of the assembly line completed, until
 * it is collected. If no assembly is waiting to be collected, this contains a null object.
 * 
 * @author Thomas Vochten, Frederik Goovaerts, Maarten Tegelaers
 *
 */
public class AssemblyLine {
	
	/**
	 * Instantiates a new assembly line with the given production schedule
	 * @param productionSchedule
	 * 		Production schedule from which the new assembly line will accept orders.
	 * @throws IllegalArgumentException
	 * 		productionSchedule == null
	 */
	public AssemblyLine(ProductionSchedule productionSchedule) throws IllegalArgumentException {
		if (productionSchedule == null) {
			throw new IllegalArgumentException();
		}
		this.productionSchedule = productionSchedule;
		productionSchedule.setAssemblyLine(this);
		
		this.workPosts = new ArrayList<WorkPost>();
		int workPostNum = 0;
		
		for (TaskType type : TaskType.values()) {
			WorkPost newPost = new WorkPost(type, workPostNum);
			workPostNum++;
			this.workPosts.add(newPost);
		}
	}
	
	/**
	 * The production schedule this assembly line works for
	 */
	private ProductionSchedule productionSchedule;
	
	/**
	 * Getter for the production schedule for internal use
	 * 
	 * @return
	 * 		The production schedule
	 */
	private ProductionSchedule getProductionSchedule() {
		return productionSchedule;
	}

	/**
	 * The workposts which compose this assembly line, in their respective orders in the list as they are ordered in the assembly line's layout
	 */
	private List<WorkPost> workPosts;
	/**
	 * An extra location at the end of the assembly line where a finished assembly that just came from the last workpost resides until it is collected 
	 */
	private AssemblyProcedure finishedAssemblyProcedure = null;

	/**
	 * Gets a list of all assemblies currently assigned to a workstation.
	 * Empty workposts will return null, which will be included in the list.
	 * The list will thusly have the same number of elements as there are workposts.
	 * The list of assemblies will have a corresponding order with the queried workstations.
	 * 
	 * @return
	 * 		A list of the assemblies on the workstations of the assembly line
	 */
	public List<AssemblyProcedureContainer> getAssemblyOnEachWorkStation() {
		List<AssemblyProcedureContainer> toReturn = new ArrayList<AssemblyProcedureContainer>();
		for (WorkPost workPost : this.getWorkPosts()) {
			toReturn.add(workPost.getAssemblyProcedure());
		}
		return toReturn;
	}

	/**
	 * Getter for the workposts variable for internal use
	 * 
	 * @return
	 * 		the workposts variable
	 */
	private List<WorkPost> getWorkPosts() {
		return workPosts;
	}

	/**
	 * Gets the workposts composing the assembly line, as immutable objects
	 * 
	 * @return
	 * 		A list of immutable containers for all respective workposts in their order
	 */
	public List<WorkPostContainer> getWorkPostContainers() {
		return new ArrayList<WorkPostContainer>(workPosts);
	}

	/**
	 * Gets the assembly tasks at a given workpost, only those who match the workpost's type, so they can actually be executed there.
	 * 
	 * @param workPostNumber
	 * 		The number of the workpost for which we want to retrieve the tasks
	 * @return
	 * 		Assembly tasks at the given workpost matching the workpost's type
	 * @throws IllegalArgumentException
	 */
	public List<AssemblyTaskContainer> getAssemblyTasksAtPost(int workPostNumber) throws IllegalArgumentException {
		if(workPostNumber < 0 || workPostNumber >= getAmountOfWorkPosts())
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		return this.getWorkPost(workPostNumber).getMatchingAssemblyTasks();
	}

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
		if(workPostNumber < 0 || workPostNumber >= getAmountOfWorkPosts())
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		this.getWorkPost(workPostNumber).completeTask(taskNumber);
	}

	/**
	 * Returns the current active assemblies on the {@link AssemblyLine}, coupled with the {@link WorkPost}s they currently belong to, as containers.
	 * {@link WorkPost}s without an active assembly are coupled with null.
	 * 
	 * @return
	 * 		A list all of {@link AssemblyProcedureContainer}s of the active assemblies on the {@link AssemblyLine}, coupled with their respective {@link WorkPostContainer}s.
	 */
	public List<Pair<AssemblyProcedureContainer, WorkPostContainer>> getCurrentWorkPostsAndActiveAssemblies() {
		List<Pair<AssemblyProcedureContainer, WorkPostContainer>> toReturn = new ArrayList<Pair<AssemblyProcedureContainer, WorkPostContainer>>();
		List<AssemblyProcedureContainer> currentAssemblies = getAssemblyOnEachWorkStation();
		for(int i = 0; i < getWorkPostContainers().size(); i++){
			toReturn.add(new Pair<AssemblyProcedureContainer, WorkPostContainer>(currentAssemblies.get(i), getWorkPost(i)));
			// Due to lack of zipwith, this is necessary, feel free to improve.
			// If you are frustrated to death by this method, please don't look at the one below this one.
		}
		return toReturn;
	}

	/**
	 * Takes a list of the {@link AssemblyProcedure}s on the {@link AssemblyLine}, in the hypothetical situation 
	 * where the {@link AssemblyLine} would advance one post, thus shifting all assemblies one post forwards,
	 * removing the current last assembly, and scheduling a new assembly on the first post if the {@link ProductionSchedule}
	 * has a new order that can still be fully assembled on time according to the schedule. This list is then
	 * coupled in order with their respective {@link WorkPostContainer}s in the hypothetical situation. This resulting
	 * list of pairs is then returned.
	 * {@link WorkPost}s without a future active assembly are coupled with null.
	 * 
	 * @return
	 * 		The list of active {@link AssemblyProcedure}s with their {@link WorkPostContainer}s in the hypothetical situation
	 */
	public List<Pair<AssemblyProcedureContainer, WorkPostContainer>> getFutureWorkPostsAndActiveAssemblies() {
		List<Pair<AssemblyProcedureContainer, WorkPostContainer>> toReturn = new ArrayList<Pair<AssemblyProcedureContainer, WorkPostContainer>>();
		ArrayList<AssemblyProcedureContainer> futureAssemblies = new ArrayList<>(getAssemblyOnEachWorkStation());
		if(futureAssemblies.size()>0)
			futureAssemblies.remove(futureAssemblies.size()-1);
		try{
			Order nextOrder = productionSchedule.getNextOrderToSchedule();
			if(nextOrder != null){
				AssemblyProcedureContainer nextAssembly = createNewAssemblyProcedure(nextOrder); 
				futureAssemblies.add(0, nextAssembly);
			} else {
				futureAssemblies.add(0,null);
			}
		} catch (IndexOutOfBoundsException e){
			futureAssemblies.add(0, null);
		}
		for(int i = 0; i < getWorkPostContainers().size(); i++){
			toReturn.add(new Pair<AssemblyProcedureContainer, WorkPostContainer>(futureAssemblies.get(i), getWorkPost(i)));
		}
		return toReturn;
	}
	
	/**
	 * Creates an assemblyProcedure from a given order.
	 * 
	 * @param nextOrder
	 * 		The order for which a procedure has to be generated
	 * @return
	 * 		An assembly procedure based on the given order
	 */
	private AssemblyProcedure createNewAssemblyProcedure(Order nextOrder) {
		List<AssemblyTask> tasks = this.generateTasksFrom(nextOrder);
		return new AssemblyProcedure(nextOrder, tasks);
	}

	/**
	 * Returns the amount of workposts that compose this {@link AssemblyLine}
	 * 
	 * @return
	 * 		the amount of workposts
	 */
	public int getAmountOfWorkPosts(){
		return workPosts.size();
	}

	/**
	 * This method tries advancing the assembly line by one workpost and tells the production schedule how much time has elapsed since
	 * the last time the assembly line was advanced. The production schedule also possibly gives the assembly line a new order for
	 * the first workpost, depending on the schedule time and elapsed time.
	 * 
	 * @param time
	 * 		Amount of time that has passed since last time advancing the schedule, in minutes
	 * @throws IllegalStateException
	 * 		When trying to advance the assembly line while there are still workposts with pending and matching assembly tasks
	 */
	public void tryAdvance(int time) throws IllegalStateException{
		ArrayList<AssemblyTaskContainer> tasks = new ArrayList<>();
		for(WorkPostContainer post : getWorkPostContainers()){
			tasks.addAll(post.getMatchingAssemblyTasks());
		}
		for(AssemblyTaskContainer task : tasks){
			if(!task.isCompleted())
				throw new IllegalStateException("One or more of the tasks on the current state of the assemblyline are still to be executed.");
		}
		shiftWorkPosts();
		getProductionSchedule().advanceTime(time);
		putNextOrderOnAssemblyLine();
		AssemblyProcedure finishedAssembly = removeFinishedAssemblyFromFinishedAssemblyProcedureCollectionSpace();
		
		if (finishedAssembly != null) {
			Order finishedOrder = finishedAssembly.getOrder();
			finishedOrder.setAsCompleted();
		
			getProductionSchedule().completeOrder(finishedOrder);
		}
	}
	
	
	/**
	 * Fills the empty first workpost with an order from the schedule if it has one ready for production.
	 * An assembly procedure will be generated for the order and it will be bound to the first workpost of the assembly line.
	 * 
	 * @throws IllegalStateException
	 * 		If the first workpost is not empty
	 */
	private void putNextOrderOnAssemblyLine() throws IllegalStateException{
		if(getWorkPost(0).getAssemblyProcedure() != null)
			throw new IllegalStateException("First workpost is not empty yet, cannot add a new assembly to the assemblyline.");
		try{
			Order nextOrder = productionSchedule.popNextOrderFromSchedule();
			getWorkPost(0).setAssemblyProcedure(createNewAssemblyProcedure(nextOrder));
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			getWorkPost(0).setAssemblyProcedure(null);
		}
	}
	
	/**
	 * Returns the workpost at a given position in the assembly line
	 * 
	 * @param workPostNumber
	 * 		The index of the wanted workpost
	 * @return
	 * 		The wanted workpost
	 */
	protected WorkPost getWorkPost(int workPostNumber){
		if(workPostNumber < 0 || workPostNumber >= workPosts.size())
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		return workPosts.get(workPostNumber);
	}

	/**
	 * Advances the assembly line by one workpost. The last, and completed assembly is put in the collection space.
	 * All other assembly procedures are shifted forwards one workstation.
	 * This frees up the first workstation for a new order.
	 * 
	 * @throws IllegalStateException
	 * 		If the collection space for the completed assembly is not empty, the line can not be advanced
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
	
	/**
	 * Removes and returns the finished assembly from the collection space if there is one, else, returns null.
	 * 
	 * @return
	 * 		The finished assembly or null if there isn't one
	 */
	private AssemblyProcedure removeFinishedAssemblyFromFinishedAssemblyProcedureCollectionSpace() {
		AssemblyProcedure finished = this.finishedAssemblyProcedure;
		this.finishedAssemblyProcedure = null;
		return finished;
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

	/**
	 * Makes a list of all workposts which still have matching tasks on their procedure that are unfinished.
	 * 
	 * @return
	 * 		The list of workposts with unfinished tasks.
	 */
	public List<WorkPostContainer> getUnfinishedWorkPosts() {
		List<WorkPostContainer> unfinishedWorkPosts = new ArrayList<>();
		for( WorkPost post : getWorkPosts()){
			if(!post.allMatchingTasksFinished())
				unfinishedWorkPosts.add(post);
		}
		return unfinishedWorkPosts;
	}
	
	/**
	 * Returns the position of the order's assembly on the assemblyline, relative form the end of the line.
	 * The last post returns position 0, subsequent posts count up from there.
	 * If the order is not being assembled right now, an IllegalArgumentException is thrown.
	 * 
	 * @param order
	 * 		The order we want to get the position of.
	 * @return
	 * 		The position of the order's assembly on the assembly line, counted up from 0 from the last post on the assembly line.
	 * @throws IllegalArgumentException
	 * 		When the order is not being assembled right now.
	 */
	int getOrderPositionOnAssemblyLine(OrderContainer order) throws IllegalArgumentException{
		for(WorkPost post: getWorkPosts()){
			if(post.isWorkingOnOrder(order))
				return ((getAmountOfWorkPosts() - post.getWorkPostNumber()) - 1);
		}
		throw new IllegalArgumentException("Order is not on assemblyLine.");
	}
	
	/**
	 * Return if this AssemblyLine is currently empty. 
	 * 
	 * @return True if empty, otherwise false.
	 */
	boolean isEmpty() {
		for(WorkPost p : this.getWorkPosts()) {
			if (!p.isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
