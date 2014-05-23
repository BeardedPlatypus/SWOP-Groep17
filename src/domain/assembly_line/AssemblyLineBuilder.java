package domain.assembly_line;

import java.util.ArrayList;
import java.util.List;

import domain.car.Model;
import domain.clock.EventConsumer;

/**
 * The AssemblyLineBuilder is responsible for assuring that new
 * AssemblyLines are capable of handling the models they are expected to handle.
 * 
 * @author Thomas Vochten
 *
 */
public class AssemblyLineBuilder {

	/**
	 * Initialise a new AssemblyLineBuilder.
	 * 
	 * @param manufacturer
	 * 		The manufacturer of the new AssemblyLine
	 * @throws IllegalArgumentException
	 * 		manufacturer is null
	 */
	public AssemblyLineBuilder() throws IllegalArgumentException {
		

		this.desiredModels = new ArrayList<Model>();
	}
	
	/** List of Models that the new AssemblyLine is expected to handle */
	private List<Model> desiredModels;
	
	/**
	 * Get the list of Models that the new AssemblyLine is expected to handle.
	 * 
	 * @return
	 * 		The list of Models.
	 */
	private List<Model> getDesiredModels() {
		return this.desiredModels;
	}
	
	/**
	 * Add the specified model to the list of models the new AssemblyLine
	 * is expected to handle.
	 * 
	 * @param model
	 * 		The model of interest.
	 * @throws IllegalArgumentException
	 * 		model is null
	 * @throws IllegalArgumentException
	 * 		model has already been added.
	 */
	public void addToDesiredModels(Model model) throws IllegalArgumentException {
		if (model == null) {
			throw new IllegalArgumentException("AssemblyLines cannot handle"
					+ "null models");
		}
		if (this.getDesiredModels().contains(model)) {
			throw new IllegalArgumentException("Cannot add a model"
					+ "to an AssemblyLineBuilder more than once.");
		}
		this.getDesiredModels().add(model);
	}
	
	/**
	 * Remove all previously added models.
	 */
	public void clearModels() {
		this.getDesiredModels().clear();
	}
	
	/**
	 * Determine whether this AssemblyLineBuilder is ready to build a new
	 * AssemblyLine.
	 * 
	 * @return This AssemblyLineBuilder is ready to build a new AssemblyLine.
	 * 	This depends on whether any models have been added previously.
	 */
	public boolean canBuildAssemblyLine() {
		return ! this.getDesiredModels().isEmpty();
	}
	
	/**
	 * Get the end product of this AssemblyLineBuilder
	 * 
	 * @return A new AssemblyLine that can handle the earlier added models
	 * @throws IllegalStateException
	 * 		canBuildAssemblyLine() is false
	 * @throws IllegalArgumentException
	 * 		See {@link AssemblyLine#AssemblyLine(List<WorkPost), OrderAcceptanceChecker, SchedulerIntermediate) AssemblyLine(List<WorkPost), OrderAcceptanceChecker, SchedulerIntermediate)}
	 */
	public AssemblyLine buildAssemblyLine(EventConsumer eventConsumer)
			throws IllegalStateException {
		if (! this.canBuildAssemblyLine()) {
			throw new IllegalStateException("The AssemblyLineBuilder could"
					+ "not yet build a new AssemblyLine");
		}
		
		LayoutFactory layoutFactory = new LayoutFactory();
		
		List<WorkPost> workPosts = layoutFactory.makeLayout(this.getDesiredModels());
		
		return new AssemblyLine(workPosts, this.getDesiredModels(), eventConsumer);
	}
}
