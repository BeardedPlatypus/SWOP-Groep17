package domain.assembly_line;

import java.util.ArrayList;
import java.util.List;

import domain.DateTime;
import domain.order.OrderView;
import domain.statistics.StatisticsLogger;

/**
 * A class representing a workfloor containing a number of assemblylines.
 * This Floor oversees those lines, and can unidle or idle a line if necessary.
 * A floor can be empty and lines can be added to a floor.
 * 
 * @author Frederik Goovaerts
 */
public class AssemblyFloor {

	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Construct a new and empty assemblyFloor, containing no AssemblyLines.
	 */
	public AssemblyFloor(StatisticsLogger logger){
		if (logger == null)
			throw new IllegalArgumentException("logger can not be null!");
		this.lines = new ArrayList<>();
		this.logger = logger;
	}
	
	/**
	 * Construct a new AssemblyFloor with given AssemblyLines.
	 * 
	 * @param lines
	 * 		The lines that are on this new AssemblyFloor
	 * @throws IllegalArgumentException
	 */
	public AssemblyFloor(List<AssemblyLineFacade> lines, StatisticsLogger logger)
			throws IllegalArgumentException{
		if (lines == null)
			throw new IllegalArgumentException("lines can not be null!");
		if (lines.contains(null))
			throw new IllegalArgumentException("lines can not contain null!");
		if (logger == null)
			throw new IllegalArgumentException("logger can not be null!");
		this.lines = new ArrayList<>(lines);
		this.logger = logger;
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	/**
	 * Get a view of the assemblyLine with given index. A view is immutable.
	 * 
	 * @param lineNb
	 * 		The index of wanted assemblyLine
	 * @return assemblyline with given index
	 *
	 * @throws IllegalArgumentException
	 * 		When given index is not valid
	 *
	 */
	public AssemblyLineView getLineView(int lineNb) throws IllegalArgumentException{
		if(lineNb<0 || lineNb >= getLineViews().size())
			throw new IllegalArgumentException("Not a valid index for an assemblyline.");
		return lines.get(lineNb);
	}
	
	/**
	 * Get a list of views of all assemblylines. They are all immutable.
	 * 
	 * @return a list of all assemblylines, as views.
	 * 
	 */
	public List<AssemblyLineView> getLineViews(){
		return new ArrayList<AssemblyLineView>(this.lines);
	}

	/**
	 * Get the lines of this Floor for internal use.
	 * 
	 * @return the assembly lines of this floor
	 */
	private List<AssemblyLineFacade> getLines() {
		return this.lines;
	}
	
	/**
	 * Add the specified AssemblyLineFacade to this AssemblyFloor
	 * 
	 * @param line
	 * 		The line to add
	 * @throws IllegalArgumentException
	 * 		line is null
	 * @throws IllegalArgumentException
	 * 		line was previously added to floor
	 */
	public void addAssemblyLine(AssemblyLineFacade line) throws IllegalArgumentException {
		if (line == null) {
			throw new IllegalArgumentException("Cannot add null AssemblyLine to AssemblyFloor");
		}
		if (this.lines.contains(line)) {
			throw new IllegalArgumentException("Cannot add line more than once to AssemblyFloor");
		}
		this.lines.add(line);
	}
	
	/** a list with all assemblylines of this floor */
	private List<AssemblyLineFacade> lines;
	
	//--------------------------------------------------------------------------
	// AssemblyFloor and AssemblyLine methods
	//--------------------------------------------------------------------------

	/**
	 * Get the StatisticsLogger of this class for internal use
	 * 
	 * @return
	 */
	private StatisticsLogger getStatisticsLogger(){
		return this.logger;
	}
	
	/** StatisticsLogger for all assemblyLines */
	private final StatisticsLogger logger; 

	/**
	 * Get a report on the statistics of this assemblyFloor.
	 * 
	 * @return 
	 * 		A report that takes the form of a String object. The client is responsible
	 * 		for deriving meaning from that report.
	 */
	public String getStatisticsReport() {
		return this.getStatisticsLogger().getReport();
	}

	/**
	 * Get a list with all orders that are currently active on the assembly lines.
	 * 
	 * The orders are immutable
	 * 
	 * @return a list with all orders on the assembly lines
	 */
	public List<OrderView> getActiveOrderViews() {
		List<OrderView> result = new ArrayList<>();
		for(AssemblyLineFacade fac : this.getLines()){
			result.addAll(fac.getActiveOrderContainers());
		}
		return result;
	}

	/**
	 * Check whether or not an assembly line on this floor contains given order.
	 * 
	 * @param order
	 * 		The order to check for
	 * @return whether this order is present on one of the assemblyLines
	 */
	public boolean contains(OrderView order) {
		for(AssemblyLineFacade line : this.getLines()){
			if(line.contains(order))
				return true;
		}
		return false;
	}

	/**
	 * Return the estimated remaining time of production for an order on this
	 * assemblyFloor
	 * 
	 * @param order
	 *		The order to calculate for
	 * @return
	 * 		The calculated time
	 * @throws IllegalStateException
	 * 		If the order is not present on this floor
	 */
	public DateTime getEstimatedCompletionTime(OrderView order)
			throws IllegalStateException{
		for(AssemblyLineFacade line : this.getLines()){
			if(line.contains(order))
				return line.getEstimatedCompletionTime(order);
		}
		throw new IllegalStateException("Order not present on assemblyLine.");
	}

	//--------- Perform Assembly Tasks methods ---------//
	
	/**
	 * Get the workposts on the assemblyLine with given index.
	 * 
	 * The workposts are immutable.
	 * 
	 * @param lineNb
	 * 		The index for the desired assemblyLine
	 * 
	 * @return a list with the desired workposts
	 */
	public List<WorkPostView> getWorkPostViewsAt(int lineNb) throws IllegalArgumentException{
		if(lineNb<0 || lineNb >= getLineViews().size())
			throw new IllegalArgumentException("Not a valid index for an assemblyline.");
		return this.getLines().get(lineNb).getWorkPostViews();
	}
	
	/**
	 * Get the assembly tasks on given workpost on given assembly line.
	 * 
	 * The tasks are immutable.
	 * 
	 * @param lineNb
	 * 		The index of the desired assembly line
	 * @param postNb
	 * 		The index of the desired workpost on the desired assembly line
	 *
	 * @return the list of tasks on the desired workpost
	 * 
	 * @throws IllegalArgumentException
	 * 		When the index of the assembly line or workpost is not a legal value
	 */
	public List<AssemblyTaskView> getAssemblyTasksAtPost(int lineNb, int postNb) 
		throws IllegalArgumentException{
		if(lineNb<0 || lineNb >= getLineViews().size())
			throw new IllegalArgumentException("Not a valid index for an assemblyline.");
		return this.getLines().get(lineNb).getAssemblyTasksAtPost(postNb);
	}
	

	/**
	 * Set the task with given index on workpost with given index of assemblyline
	 * with given index as complete.
	 * 
	 * @param lineNb
	 * 		The index of desired assemblyLine
	 * @param postNb
	 * 		The index of the desired workpost
	 * @param taskNb
	 * 		The index of the desired task
	 * @param minutes
	 * 		The amount of minutes spent on the task
	 * 
	 * @throws IllegalArgumentException
	 * 		if one of the indices is not legal
	 */
	public void completeWorkpostTask(int lineNb, int postNb, int taskNb,
			int minutes) throws IllegalArgumentException{
		if(lineNb<0 || lineNb >= getLineViews().size())
			throw new IllegalArgumentException("Not a valid index for an assemblyline.");
		this.getLines().get(lineNb).completeWorkpostTask(postNb, taskNb, minutes);;
	}


	//----- end of Perform Assembly Tasks methods -----//



}
