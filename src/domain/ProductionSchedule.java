package domain;

import java.util.ArrayList;
import java.util.List;

public class ProductionSchedule {
	public ProductionSchedule(Manufacturer manufacturer, AssemblyLine assemblyLine) {
		this.manufacturer = manufacturer;
		this.assemblyLine = assemblyLine;
	}
	
	private Manufacturer manufacturer;
	private List<AssemblyProcedure> pendingAssemblies = new ArrayList<AssemblyProcedure>();
	private AssemblyLine assemblyLine;

	public List<OrderContainer> getPendingOrderContainers() {
		throw new UnsupportedOperationException();
	}

	/**
	 * create from the specified model and specs and internal time a new
	 * order object and add this to the pendingOrders of this ProductionSchedule.
	 * 
	 * @param model
	 * 		The model of this new Order
	 * @param specs
	 * 		The specification of this new Order. 
	 * 
	 * @postcondition
	 * 		| Eo o == order(model, specs) && o in (new this).getPendingOrderContainers().   
	 * 
	 * @throws NullPointerException 
	 * 		| model == null || specs == null
	 * @throws IllegalArgumentException
	 * 		| !model.isValidSpecifications(specs)
	 */
	public void addNewOrder(Model model, Specification specs) throws NullPointerException, IllegalArgumentException{
		
	}

	/*
	public DateTime getEstimatedCompletionTime(Object order) {
		throw new UnsupportedOperationException();
	}
	*/

	public AssemblyProcedure getNextScheduledAssembly() {
		throw new UnsupportedOperationException();
	}
}