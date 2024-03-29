package domain.production_schedule.strategy;

import java.util.ArrayList;
import java.util.List;

import domain.car.Option;
import domain.car.Specification;
import domain.order.StandardOrder;

/**
 * The AlgorithmStrategyFactory methods provides methods for getting each specific
 * SchedulingStrategy, as well as a list of their respective views.
 * 
 * @author Martinus Wilhelmus Tegelaers, Thomas Vochten
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
		List<SchedulingStrategyView> result = new ArrayList<>();
		result.add(this.getFifoStrategy());
		
		List<Option> emptyOptions = new ArrayList<Option>();
		Specification emptySpec = new Specification(emptyOptions);
		
		result.add(new BatchStrategy<StandardOrder>(emptySpec));
		
		return result;
	}

	/**
	 * Get a First in First Out (FIFO) SchedulingStrategy for orders. 
	 * 
	 * @return a FIFO SchedulingStrategy
	 */
	public SchedulingStrategy<StandardOrder> getFifoStrategy() {
		return new FifoStrategy<StandardOrder>();
	}
	
	/**
	 * Get a Batch SchedulingStrategy that orders Orders that match the specified
	 * Specification first until all are done. 
	 * 
	 * @return A BatchSchedulingStrategy.
	 * @throws IllegalArgumentException
	 * 		spec is null
	 */
	public SchedulingStrategy<StandardOrder> getBatchStrategy(Specification spec) throws IllegalArgumentException {
		return new BatchStrategy<StandardOrder>(spec);
	}

}