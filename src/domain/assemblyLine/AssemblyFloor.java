package domain.assemblyLine;

import java.util.ArrayList;
import java.util.List;

import domain.order.Order;
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
	public AssemblyFloor(){
		this.lines = new ArrayList<>();
	}
	
	/**
	 * Construct a new AssemblyFloor with given AssemblyLines.
	 * 
	 * @param lines
	 * 		The lines that are on this new AssemblyFloor
	 * @throws IllegalArgumentException
	 */
	public AssemblyFloor(List<AssemblyLineFacade> lines)
			throws IllegalArgumentException{
		if (lines == null)
			throw new IllegalArgumentException("lines can not be null!");
		if (lines.contains(null))
			throw new IllegalArgumentException("lines can not contain null!");
		this.lines = new ArrayList<>(lines);
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
	
	/** a list with all assemblylines of this floor */
	private List<AssemblyLineFacade> lines;
	
	//--------------------------------------------------------------------------
	// AssemblyFloor and AssemblyLine methods
	//--------------------------------------------------------------------------
	
	/**
	 * If possible, unidle an assemblyline that is currently idle and can fully
	 * process given order. If no such assemblyline exists, nothing happens.
	 * Maximum one assemblyline is unidled.
	 * 
	 * @param order
	 * 		The order the unidled assemblyline has to be able to process
	 */
	public void unidleLineFor(Order order){
		//TODO
	}

	public List<AssemblyLineView> getAssemblyLineViews() {
		//FIXME
		return null;
	}

	public void setStatisticsLogger(StatisticsLogger logger) {
		//FIXME
	}

	public String getStatisticsReport() {
		// TODO Auto-generated method stub
		return null;
	}
}
