package domain.productionSchedule.strategy;

import util.annotations.Immutable;

/** 
 * The AlgorithmView interface provides a description of an Algorithm, which
 * can be used by the UI to print information.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
@Immutable
public interface SchedulingStrategyView {
	/**
	 * Get the name of this AlgorithmView
	 * 
	 * @return The name of this AlgorithmView.
	 */
	public String getName();
}
