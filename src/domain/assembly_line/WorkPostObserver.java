package domain.assembly_line;

/**
 * Interface for objects that have an interest in knowing when a WorkPost
 * has finished its work.
 * @author Thomas Vochten
 *
 */
public interface WorkPostObserver {
	
	/**
	 * Indicate to this WorkPostObserver that work has been finished, together
	 * with the amount of minutes that it took.
	 * 
	 * @param minutes
	 * 		The amount of time it took to finish work
	 */
	public void notifyWorkComplete(int minutes);

}
