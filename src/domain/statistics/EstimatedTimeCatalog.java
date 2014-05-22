package domain.statistics;

import domain.DateTime;
import domain.order.OrderView;

/**
 * A class that keeps an EstimatedProductionTimeRegistrar and can query it for
 * estimates, based on given orders.
 * 
 * @author Frederik Goovaerts
 */
public class EstimatedTimeCatalog {
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	
	/**
	 * Create an EstimatedTimeCatalog with given registrar as registrar.
	 * 
	 * @param registrar
	 * 		The registrar this Catalog queries for the estimations
	 */
	public EstimatedTimeCatalog(EstimatedProductionTimeRegistrar registrar){
		this.estRegistrar = registrar;
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	/** registrar of this Catalog */
	private final EstimatedProductionTimeRegistrar estRegistrar;
	
	/**
	 * Get the registrar for internal use.
	 * 
	 * @return the registrar
	 */
	private EstimatedProductionTimeRegistrar getRegistrar(){
		return this.estRegistrar;
	}
	
	//--------------------------------------------------------------------------
	// Catalog Methods
	//--------------------------------------------------------------------------
	
	/**
	 * For given order, get the estimated date and time of completion.
	 * 
	 * @param order
	 * 		Order to get the estimated completion time of
	 * 
	 * @return the estimated completion time of given order
	 */
	public DateTime getEstimatedCompletionTime(OrderView order){
		return this.getRegistrar().getCompletionTimeOfModel(order.getModel());
	}
}
