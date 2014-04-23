package domain.handlers;

import java.util.List;

import domain.AlgorithmView;
import domain.Manufacturer;
import domain.Specification;

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
	public List<SchedulingStrategyView> getAlgorithms() {
		throw new UnsupportedOperationException();
	}
	
	public SchedulingStrategyView getCurrentAlgorithm() {
		throw new UnsupportedOperationException();
	}
	
	//--------------------------------------------------------------------------
	// Fifo flow
	public void setFifoAlgorithm() {
		throw new UnsupportedOperationException();
	}
	
	//--------------------------------------------------------------------------
	// Batch flow
	//FIXME check which datatype batch should be. 
	public List<Specification> getCurrentBatches() {
		throw new UnsupportedOperationException();
	}
	
	public void setBatchAlgorithm(Specification batch) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
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

