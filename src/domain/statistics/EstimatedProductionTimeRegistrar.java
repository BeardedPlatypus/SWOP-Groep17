package domain.statistics;

import java.util.HashMap;

import domain.DateTime;
import domain.car.Model;
import domain.clock.Clock;

/**
 * Registrar that keeps a list of models that have been made and the amount of
 * time it took. Keeps an estimate of the production time and updates this when
 * another order of that model is processed.
 * 
 * @author Frederik Goovaerts, Thomas Vochten
 */
public class EstimatedProductionTimeRegistrar implements Registrar {
	
	//--------- Constants ---------//

	private final double OLD_ESTIMATE_WEIGHT = 0.75;
	private final double NEW_ESTIMATE_WEIGHT = 1-OLD_ESTIMATE_WEIGHT;

	//----- end of Constants -----//


	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Create a new registrar with given clock as field. The clock is used
	 * to query the current time.
	 * 
	 * @param clock
	 * 		The clock of the system
	 */
	public EstimatedProductionTimeRegistrar(Clock clock) {
		if (clock == null)
			throw new IllegalArgumentException("clock can not be null!");
		this.clock = clock;
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	/** clock of the system */
	private final Clock clock;
	
	/**
	 * Get the clock for internal use
	 * 
	 * @return the clock of the system
	 */
	private Clock getClock(){
		return this.clock;
	}
	
	//--------------------------------------------------------------------------
	// Statistics methods
	//--------------------------------------------------------------------------
		
	/* (non-Javadoc)
	 * @see domain.statistics.Registrar#addStatistics(domain.statistics.ProcedureStatistics)
	 */
	@Override
	public void addStatistics(ProcedureStatistics statistics) {
		Model completedModel = statistics.getCompletedOrder().getModel();
		DateTime submissionTime = statistics.getCompletedOrder().getSubmissionTime();
		DateTime productionTime = this.getCurrentTime().subtractTime(submissionTime);
		if(!this.hasCompletionEstimate(completedModel)){
			this.setCompletionEstimate(completedModel, productionTime);
		} else {
			DateTime earlierProductionTime = this.getCompletionTimeOfModel(completedModel);
			DateTime newEstimate =  earlierProductionTime.multiplyWithScalar(OLD_ESTIMATE_WEIGHT)
					.addTime(productionTime.multiplyWithScalar(NEW_ESTIMATE_WEIGHT));
			this.setCompletionEstimate(completedModel, newEstimate);
		}
	}

	/* (non-Javadoc)
	 * @see domain.statistics.Registrar#getStatistics()
	 */
	@Override
	public String getStatistics() {
		return "";
	}
	
	//--------------------------------------------------------------------------
	// Time Methods
	//--------------------------------------------------------------------------
	
	/**
	 * Get the current system time form the clock
	 * 
	 * @return the current time of the system
	 */
	private DateTime getCurrentTime(){
		return this.getClock().getCurrentTime();
	}
	
	//--------------------------------------------------------------------------
	// Catalog of completion times for models
	//--------------------------------------------------------------------------
	
	/** Map which records an estimated production time for each model */
	private HashMap<Model, DateTime> completionTimes;
	
	/**
	 * Get an estimated production duration based on given model.
	 * If no previous estimates are saved, the default estimate of a day is
	 * returned. This happens when the system is being set up and no orders have
	 * been produced yet.
	 * 
	 * @param model
	 * 		The model to get an estimation for
	 * 
	 * @return the estimated duration of production
	 */
	public DateTime getCompletionTimeOfModel(Model model){
		if (model == null)
			throw new IllegalArgumentException("model can not be null!");
		if(!hasCompletionEstimate(model))
			return this.getCurrentTime().addTime(new DateTime(1, 0, 0));
		return this.getCurrentTime().addTime(completionTimes.get(model));
	}
	
	/**
	 * Check whether or not this registrar already has an estimate for given
	 * model.
	 * 
	 * @param model
	 * 		The model to check for.
	 * 
	 * @return whether or not this model has an estimate in the registrar
	 */
	private boolean hasCompletionEstimate(Model model){
		if (model == null)
			throw new IllegalArgumentException("model can not be null!");
		return completionTimes.containsKey(model);
	}
	
	/**
	 * Internal method to set the estimate for given model to given value.
	 * 
	 * @param model
	 * 		The model to update the estimate for
	 * @param time
	 * 		The new estimate
	 */
	private void setCompletionEstimate(Model model, DateTime time){
		this.completionTimes. put(model, time);
	}

	/**
	 * Takes a relative completion time and makes it into an absolute time.
	 * 
	 * @param relativeCompletionTime
	 * 		The given relative completion time
	 * @return The resulting absolute completion time
	 */
	public DateTime calculateAbsoluteTime(DateTime relativeCompletionTime) {
		return this.getCurrentTime().addTime(relativeCompletionTime);
	}
	
}
