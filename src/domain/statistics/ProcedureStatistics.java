package domain.statistics;

import domain.order.Order;
import domain.order.OrderView;
import util.annotations.Immutable;

/**
 * Class to store statistical information of interest about finished
 * AssemblyProcedure objects.
 * 
 * @author Thomas Vochten
 *
 */
@Immutable
public class ProcedureStatistics {

	/**
	 * Initialise a new ProcedureStatistics with the specified delay.
	 * 
	 * @param delay
	 * 		The delay of an order between rolling on the AssemblyLine
	 * 		and rolling off the AssemblyLine, in minutes.
	 */
	public ProcedureStatistics(int delay, Order completedOrder) {
		this.delay = delay;
		this.order = completedOrder;
	}
	
	
	//--------- Delay property ---------//

	/** The delay of an order between rolling on the AssemblyLine
	 * and rolling off the AssemblyLine, in minutes. */
	private int delay;
	
	/**
	 * Get the delay of an order between rolling on the AssemblyLine
	 * and rolling off the AssemblyLine, in minutes.
	 * @return
	 * 		The delay in minutes.
	 */
	public int getDelay() {
		return this.delay;
	}

	//----- end of Delay property -----//


	//--------- Completed order ---------//

	private final Order order;
	
	public OrderView getCompletedOrder(){
		return this.order;
	}

	//----- end of Completed order -----//

	
	

}