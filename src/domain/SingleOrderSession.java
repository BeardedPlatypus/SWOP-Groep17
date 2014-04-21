package domain;

import java.util.List;

/**
 * Used to compose and submit a single task order.
 * 
 * @author Simon Slangen
 */
public class SingleOrderSession {
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Initialize this SingleOrderSession with the specified Manufacturer and SingleTaskCatalog.
	 * 
	 * @param 	manufacturer
	 * 			The manufacturer where this session composes a new single task order.
	 * @param 	singleTaskCatalog
	 * 			A SingleTaskCatalog listing the tasks and options that can be selected for a single task order.
	 */
	public SingleOrderSession(Manufacturer manufacturer, SingleTaskCatalog singleTaskCatalog){
		this.manufacturer = manufacturer;
		this.singleTaskCatalog = singleTaskCatalog;
	}

	//--------------------------------------------------------------------------
	// Getters and setters of private attributes.
	//--------------------------------------------------------------------------
	private Manufacturer manufacturer;
	private SingleTaskCatalog singleTaskCatalog;
	private Option singleOption = null;
	private DateTime deadline = null;
	
	/**
	 * Returns the manufacturer where this session composes a new single task order.
	 * 
	 * @return The manufacturer.
	 */
	private Manufacturer getManufacturer(){
		return manufacturer;
	}
	
	/**
	 * Returns the SingleTaskCatalog listing the tasks and options that can be selected for a single task order.
	 * 
	 * @return The SingleTaskCatalog.
	 */
	private SingleTaskCatalog getSingleTaskCatalog(){
		return singleTaskCatalog;
	}
	
	/**
	 * Sets the single task option in this order session to the supplied value.
	 * 
	 * @param	option
	 * 			A single task option.
	 */
	private void setOption(Option option) {
		singleOption = option;
	}
	
	/**
	 * Returns the option that's been selected in this single task order session, if it exists. Null otherwise.
	 * 
	 * @return	This order session's selected option.
	 * 			Null if none has been selected.
	 */
	private Option getOption(){
		return singleOption;
	}
	
	/**
	 * Sets the deadline in this order session to be within the given number of days hours and minutes.
	 * 
	 * @param	days
	 * 			The number of days before the deadline.
	 * @param	hours
	 * 			The number of hours before the deadline modulo days.
	 * @param	minutes
	 * 			The number of minutes before the deadline modulo hours.
	 */
	private void setDeadline(int days, int hours, int minutes) {
		deadline = new DateTime(days, hours, minutes);
	}
	
	/**
	 * Returns the deadline that's been specified in this single task order session, if it exists. Null otherwise.
	 * 
	 * @return	This order's specified deadline.
	 * 			Null if none has been specified.
	 */
	private DateTime getDeadline(){
		return deadline;
	}
	
	//--------------------------------------------------------------------------
	// Order composition.
	//--------------------------------------------------------------------------
	/**
	 * Returns a list of OptionCategory objects specifying the tasks and options available for a single task order.
	 * 
	 * @return	List of OptionCategory objects.
	 */
	public List<OptionCategory> getPossibleTasks() {
		return getSingleTaskCatalog().getPossibleTasks();
	}

	/**
	 * Selects the given option for this single task order session.
	 * This will fail if another option was selected previously.
	 * 
	 * @param 	option
	 * 			The option to be ordered in this single task order session.
	 * @throws 	IllegalStateException
	 * 			If another option had previously been specified.
	 */
	public void selectOption(Option option) throws IllegalStateException{
		if(getOption() == null){
			setOption(option);
		}else{
			throw new IllegalStateException("Another option has already been selected.");
		}
	}

	/**
	 * Imposes a deadline on the single task order composed in this order session,
	 * within the given amount of days, hours and minutes.
	 * 
	 * @param	days
	 * 			The number of days before the deadline.
	 * @param	hours
	 * 			The number of hours before the deadline modulo days.
	 * @param	minutes
	 * 			The number of minutes before the deadline modulo hours.
	 * @throws 	IllegalStateException
	 * 			If another deadline had previously been specified.
	 */
	public void specifyDeadline(int days, int hours, int minutes) throws IllegalStateException{
		if(getDeadline() == null){
			setDeadline(days, hours, minutes);
		}else{
			throw new IllegalStateException("Another deadline has already been specified.");
		}
	}
	
	/**
	 * Checks whether the order details have been specified. This returns true if an option has been
	 * selected and a deadline has been specified, false otherwise.
	 * 
	 * @return	true if an option and deadline are specified
	 * 			false otherwise
	 */
	public boolean orderDetailsSpecified(){
		if(getOption() == null || getDeadline() == null){
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Submits the single task order composed in this session and schedules it for completion.
	 * Returns the order that's been scheduled.
	 * 
	 * @return	The order that's been scheduled.
	 * @throws 	IllegalStateException
	 * 			If the option or deadline were not yet specified upon submission.
	 */
	public Order submitSingleTaskOrder() throws IllegalStateException{
		if(orderDetailsSpecified()){
			return getManufacturer().submitSingleTaskOrder(getOption(), getDeadline());
		} else {
			throw new IllegalStateException("The option or deadline have not yet been specified.");
		}
	}
}