package domain.assemblyLine;

public class ActiveState extends OperationalState {

	/**
	 * Initialise a new ActiveState with the specified AssemblyLine.
	 * 
	 * @param line
	 * 		The AssemblyLine of interest
	 * @throws IllegalArgumentException
	 */
	public ActiveState(AssemblyLine line) throws IllegalArgumentException {
		super(line);
	}

}
