package domain.productionSchedule.strategy;

import java.util.ArrayList;
import java.util.List;

import domain.Specification;

/**
 * The AlgorithmStrategyFactory methods provides methods for getting each specific
 * SchedulingStrategy, as well as a list of their respective views.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class AlgorithmStrategyFactory {
	/** 
	 * Get a list of all SchedulingStrategyViews that can be constructed with this
	 * AlgorithmStrategyFactory
	 * 
	 * @return a list of all SchedulingStrategyViews that can be constructed with this
	 * 		   AlgorithmStrategyFactory.
	 */
	public List<SchedulingStrategyView> getAlgorithmViews() {
		List<SchedulingStrategyView> result = new ArrayList<>() ;
		result.add(this.getFifoStrategy());
		result.add(this.getBatchStrategy());
	}

	/**
	 * Get a First in First Out (FIFO) SchedulingStrategy for orders. 
	 * 
	 * @return a FIFO SchedulingStrategy
	 */
	public SchedulingStrategy getFifoStrategy() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Get a Batch SchedulingStrategy that orders Orders that match the specified
	 * Specification first until all are done. 
	 * 
	 * @return A BatchSchedulingStrategy.
	 */
	public SchedulingStrategy getBatchStrategy(Specification spec) {
		throw new UnsupportedOperationException();
	}
	//TODO create an prototype BatchStrategy field.

}