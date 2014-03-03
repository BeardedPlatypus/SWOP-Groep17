import java.util.ArrayList;

public class ProductionSchedule {
	public Manufacturer manufacturer;
	public ArrayList<AssemblyProcedure> pendingAssemblies = new ArrayList<AssemblyProcedure>();
	public AssemblyLine assemblyLine;

	public OrderContainer[] getPendingOrderContainers() {
		throw new UnsupportedOperationException();
	}

	public void addNewOrder(Object order) {
		throw new UnsupportedOperationException();
	}

	public DateTime getEstimatedCompletionTime(Object order) {
		throw new UnsupportedOperationException();
	}

	public AssemblyProcedure getNextScheduledAssembly() {
		throw new UnsupportedOperationException();
	}
}