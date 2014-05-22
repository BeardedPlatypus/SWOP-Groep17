package domain.assembly_line;

/**
 * Presents an unmodifiable view of an AssemblyLineState
 * 
 * @author Thomas Vochten
 *
 */
public interface AssemblyLineStateView {
	
	/**
	 * Get the AssemblyLineState's name.
	 * 
	 * @return
	 */
	public String getName();

}
