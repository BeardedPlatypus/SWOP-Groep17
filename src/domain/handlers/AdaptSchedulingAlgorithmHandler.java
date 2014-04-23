package domain.handlers;

import java.util.List;

import domain.AlgorithmView;
import domain.Manufacturer;
import domain.Specification;
import domain.productionSchedule.strategy.SchedulingStrategyView;

/**
 * Class responsible for viewing the current scheduling algorithm and changing
 * that algorithm whenever the user desires.
 * 
 * @author Thomas Vochten
 *
 */
public class AdaptSchedulingAlgorithmHandler {	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Construct a new AdaptSchedulingAlgorithmHandler with the specified 
	 * Manufacturer. 
	 * 
	 * @param manufacturer
	 * 		The manufacturer of this AdaptSchedulingAlgorithmHandler. 
	 * 
	 * @postcondition | (new this).getManufacturer() == manufacturer
	 * 
	 * @throws IllegalArgumentException
	 * 		| manufacturer == null
	 */
	public AdaptSchedulingAlgorithmHandler(Manufacturer manufacturer) throws IllegalArgumentException{
		if (manufacturer == null) {
			throw new IllegalArgumentException("Manufacturer cannot be null.");
		}
		
		this.manufacturer = manufacturer;
	}
	
	//--------------------------------------------------------------------------
	// Interaction methods.
	//--------------------------------------------------------------------------
	// All flows
	/**
	 * Get a list of SchedulingStrategyViews of all available SchedulingAlgorithms
	 * 
	 * @return The SchedulingStrategyViews
	 */
	public List<SchedulingStrategyView> getAlgorithms() {
		return this.getManufacturer().getAlgorithms();
	}
	
	/**
	 * Get a view of the currently used SchedulingAlgorithm
	 * 
	 * @return The SchedulingAlgorithm
	 */
	public SchedulingStrategyView getCurrentAlgorithm() {
		return this.getManufacturer().getCurrentAlgorithm();
	}
	
	//--------------------------------------------------------------------------
	// Fifo flow
	/**
	 * Set the currently used SchedulingAlgorithm to the FIFO algorithm
	 */
	public void setFifoAlgorithm() {
		this.getManufacturer().setFifoAlgorithm();
	}
	
	//--------------------------------------------------------------------------
	// Batch flow
	/**
	 * Get the batches that are currently eligible for use in batch strategies.
	 * 
	 * @return The batches
	 */
	public List<Specification> getCurrentBatches() {
		return this.getManufacturer().getCurrentBatches();
	}
	
	/**
	 * Set the currently used SchedulingAlgorithm to a batch strategy that
	 * uses the specified Specification.
	 * 
	 * @param batch
	 * 		The batch used to compare Orders
	 * @throws IllegalArgumentException
	 * 		batch is null
	 */
	public void setBatchAlgorithm(Specification batch) throws IllegalArgumentException {
		this.getManufacturer().setBatchAlgorithm(batch);
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	/**
	 * Get the Manufacturer of this AdaptSchedulingAlgorithmHandler
	 * 
	 * @return the Manufacturer of this AdaptSchedulingAlgorithmHandler
	 */
	protected Manufacturer getManufacturer() {
		return this.manufacturer;
	}
	
	/** The Manufacturer of this AdaptSchedulingAlgorithmHandler. */
	private final Manufacturer manufacturer;
	
}

