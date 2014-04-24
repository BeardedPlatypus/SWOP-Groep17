package domain.handlers;

import java.util.ArrayList;
import java.util.List;

import domain.DateTime;
import domain.Manufacturer;
import domain.Specification;
import domain.order.OrderContainer;
import exceptions.OrderDoesNotExistException;

/**
 * A handler class taking care of querying the system for information about
 * orders, by the user.
 * 
 * @author Frederik Goovaerts
 */
public class CheckOrderDetailsHandler {
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Create a new {@link CheckOrderDetailsHandler} with given {@link Manufacturer}
	 * as its Manufacturer.
	 * 
	 * @param man
	 * 		The manufacturer for the new handler object
	 * 
	 * @throws IllegalArgumentException
	 * 		When the given manufacturer is null
	 */
	public CheckOrderDetailsHandler(Manufacturer man) throws IllegalArgumentException{
		if (man == null)
			throw new IllegalArgumentException("Manufacturer should not be null");
		this.manufacturer = man;
		snapshotPendingOrders = new ArrayList<>();
		snapshotCompletedOrders = new ArrayList<>();
		snapshotWasUsed = true;
		currentObservedOrder = null;
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	/**
	 * Get this handler's manufacturer for internal use.
	 * 
	 * @return this handler's manufacturer
	 */
	private Manufacturer getManufacturer(){
		return this.manufacturer;
	}
	
	/** manufacturer this handler interfaces with */
	private final Manufacturer manufacturer;
	
	//--------------------------------------------------------------------------
	// Snapshot methods

	/**
	 * Update the snapshot lists so they represent the state of the orders in the
	 * system at the time this method was last called.
	 * Also set accompanying boolean that the snapshots haven't been used yet.
	 * 
	 * @post this.snapshotPendingOrders.equals(this.manufacturer.getPendingOrderContainers)
	 * @post this.snapshotCompletedOrders.equals(this.manufacturer.getCompletedOrderContainers)
	 * @post this.snapshotWasUsed == false
	 */
	private void updateSnapshots(){
		snapshotPendingOrders = this.getManufacturer().getPendingOrderContainers();
		snapshotCompletedOrders = this.getManufacturer().getCompletedOrderContainers();
		this.setDeprecatedState(false);
	}
	
	/**
	 * Get a copy of the completed orders snapshot list for the user.
	 * If the snapshots are deprecated, these are refreshed first.
	 * 
	 * @return a copy of the completed orders snapshot list.
	 */
	public List<OrderContainer> getCompletedOrdersContainers(){
		if(this.snapshotsAreDeprecated())
			updateSnapshots();
		return new ArrayList<>(this.snapshotCompletedOrders);
	}
	
	/**
	 * Get a copy of the pending orders snapshot list for the user.
	 * If the snapshots are deprecated, these are refreshed first.
	 * 
	 * @return a copy of the pending orders snapshot list.
	 */
	public List<OrderContainer> getPendingOrdersContainers(){
		if(this.snapshotsAreDeprecated())
			updateSnapshots();
		return new ArrayList<>(this.snapshotPendingOrders);
	}
	
	/**
	 * The pending orders snapshot for internal use.
	 * This snapshot may be deprecated.
	 * 
	 * @return the pending orders snapshot
	 */
	private List<OrderContainer> getPendingSnapshot(){
		return this.snapshotPendingOrders;
	}
	
	/**
	 * The completed orders snapshot for internal use.
	 * This snapshot may be deprecated.
	 * 
	 * @return the completed orders snapshot
	 */
	private List<OrderContainer> getCompletedSnapshot(){
		return this.snapshotCompletedOrders;
	}
	
	/** List which keeps a snapshot of the pending orders at one point in the system */
	private List<OrderContainer> snapshotPendingOrders;

	/** List which keeps a snapshot of the completed orders at one point in the system */
	private List<OrderContainer> snapshotCompletedOrders;
	
	/**
	 * Set the state of snapshot deprecation to given boolean
	 * 
	 * @param state
	 * 		The new state of deprecation
	 */
	private void setDeprecatedState(boolean state){
		
	}
	
	/** boolean which records if the current snapshot lists have been used */
	private boolean snapshotWasUsed;
	
	/**
	 * Check whether or not the snapshots have been used.
	 * 
	 * @return whether or not the snapshots have been used
	 */
	private boolean snapshotsAreDeprecated(){
		return snapshotWasUsed;
	}
	

	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// Current Order methods
	
	/**
	 * Check whether an order is present in the handler
	 * 
	 * @return whether an order is present in the handler
	 */
	private boolean orderIsSet(){
		return !(currentObservedOrder == null);
	}
	
	/**
	 * Get the current Observed order for internal use.
	 * 
	 * @return the current observed order
	 */
	private OrderContainer getCurrentObservedOrder(){
		return this.currentObservedOrder;
	}

	/** current queried Order of this handler */
	private OrderContainer currentObservedOrder;
	
	
	/**
	 * Select order with given index from the Completed Orders snapshot.
	 * Both snapshots are then deprecated and cannot be used anymore.
	 * This method cannot be used until the snapshots are refreshed.
	 * 
	 * @param orderIndex
	 * 		The index of the wanted order in the list of completed orders
	 * 
	 * @throws IllegalArgumentException
	 * 		When the given index is out of the bounds of the list
	 * @throws IllegalStateException
	 * 		When this method is called with deprecated snapshots
	 */
	public void selectCompletedOrder(int orderIndex)
			throws IllegalArgumentException,
			IllegalStateException
	{
		if(this.snapshotsAreDeprecated())
			throw new IllegalStateException("Snapshots are deprecated.");
		if(orderIndex < 0 || orderIndex >= this.getCompletedSnapshot().size())
			throw new IllegalArgumentException("Index is not a correct index.");
		this.currentObservedOrder = this.getCompletedSnapshot().get(orderIndex);
		this.setDeprecatedState(true);
	}
	
	/**
	 * Select order with given index from the Pending Orders snapshot.
	 * Both snapshots are then deprecated and can not be used anymore.
	 * This method cannot be used until the snapshots are refreshed.
	 * 
	 * @param orderIndex
	 * 		The index of the wanted order in the list of completed orders
	 * 
	 * @throws IllegalArgumentException
	 * 		When the given index is out of the bounds of the list
	 * @throws IllegalStateException
	 * 		When this method is called with deprecated snapshots
	 */
	public void selectPendingOrder(int orderIndex)
			throws IllegalArgumentException,
			IllegalStateException
	{
		if(this.snapshotsAreDeprecated())
			throw new IllegalStateException("Snapshots are deprecated.");
		if(orderIndex < 0 || orderIndex >= this.getPendingSnapshot().size())
			throw new IllegalArgumentException("Index is not a correct index.");
		this.currentObservedOrder = this.getPendingSnapshot().get(orderIndex);
		this.setDeprecatedState(true);
	}

	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// Class methods
	//--------------------------------------------------------------------------
	
	/**
	 * Get the Specification of the currently observed Order, if there is one.
	 * 
	 * @return the specification of the order
	 * 
	 * @throws IllegalStateException
	 * 		If this method is called when no order is set
	 */
	public Specification getCurrentOrderSpecification(){
		if(!orderIsSet())
			throw new IllegalStateException("No order has been set!");
		return this.getCurrentObservedOrder().getSpecifications();
	}
	
	/**
	 * Get the submission time of the currently observed Order, if there is one.
	 * 
	 * @return the submission time of the order
	 * 
	 * @throws IllegalStateException
	 * 		If this method is called when no order is set
	 */
	public DateTime getCurrentOrderSubmissionTime(){
		if(!orderIsSet())
			throw new IllegalStateException("No order has been set!");
		return this.getCurrentObservedOrder().getSubmissionTime();
	}
	
	/**
	 * Get the completion state of the currently observed Order, if there is one.
	 * 
	 * @return the completion state of the order
	 * 
	 * @throws IllegalStateException
	 * 		If this method is called when no order is set
	 */
	public boolean currentOrderIsComplete(){
		if(!orderIsSet())
			throw new IllegalStateException("No order has been set!");
		return this.getCurrentObservedOrder().isCompleted();
	}
	
	/**
	 * Get the Completion Time of the currently observed Order, if there is one.
	 * 
	 * @return the Completion Time of the order
	 * 
	 * @throws IllegalStateException
	 * 		If this method is called when no order is set
	 * @throws IllegalStateException
	 * 		If the order has not been completed yet
	 */
	public DateTime getCurrentOrderCompletionTime(){
		if(!orderIsSet())
			throw new IllegalStateException("No order has been set!");
		if(!currentOrderIsComplete())
			throw new IllegalStateException("The order is not completed yet!");
		return this.getCurrentObservedOrder().getCompletionTime();
	}
	
	/**
	 * Get the estimated Completion Time of the currently observed Order, if there is one.
	 * 
	 * @return the estimated Completion Time of the order
	 * 
	 * @throws OrderDoesNotExistException 
	 * 		If the order is not recognised by the system
	 * @throws IllegalStateException
	 * 		If this method is called when no order is set
	 * @throws IllegalStateException
	 * 		If the order is already completed
	 */
	public DateTime getCurrentOrderEstimatedCompletionTime() throws OrderDoesNotExistException{
		if(!orderIsSet())
			throw new IllegalStateException("No order has been set!");
		if(currentOrderIsComplete())
			throw new IllegalStateException("The order already completed!");
		return this.getManufacturer().getEstimatedCompletionTime(this.getCurrentObservedOrder());
	}


	/**
	 * Get estimated completion time of given orderContainer
	 * 
	 * @param order
	 * 		The order to check for
	 * 
	 * @return the estimated completion time of the order
	 * 
	 * @throws IllegalArgumentException
	 * 		If the order is null
	 * @throws OrderDoesNotExistException
	 * 		If the order is not an order of the system
	 */
	public DateTime getEstimatedCompletionTime(OrderContainer order) 
			throws IllegalArgumentException,
			OrderDoesNotExistException
	{
		if(order == null)
			throw new IllegalArgumentException("Order cannot be null.");
		return this.getManufacturer().getEstimatedCompletionTime(order);
	}
}
