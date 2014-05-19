package domain.assemblyLine;

/**
 * When an AssemblyLine is operational, it is capable of completing tasks
 * and putting Orders on the line.
 * 
 * @author Thomas Vochten
 *
 */
public class OperationalState extends AssemblyLineState {

	/**
	 * Initialise a new OperationalState with the specified AssemblyLine.
	 * 
	 * @param line
	 * 		The AssemblyLine of interest.
	 * @throws IllegalArgumentException
	 * 		line is null
	 */
	public OperationalState(AssemblyLine line) throws IllegalArgumentException {
		super(line);
	}

	@Override
	protected void ensureStateConsistency() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void finaliseSetState() {
		// TODO Auto-generated method stub

	}

}
