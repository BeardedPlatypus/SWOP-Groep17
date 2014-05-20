package domain.assemblyLine;

import java.util.ArrayList;
import java.util.List;

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
	public AssemblyLineView getLineView(int lineNb){
		if(lineNb<0 || lineNb >= getLineViews().size())
			throw new IllegalArgumentException("Not a valid index for an assemblyline.");
		return null;
	}
	
	public List<AssemblyLineView> getLineViews(){
		return new ArrayList<AssemblyLineView>(this.lines);
	}
	
	private List<AssemblyLineFacade> lines;
}
