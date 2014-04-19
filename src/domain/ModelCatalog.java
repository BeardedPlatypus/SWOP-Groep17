package domain;

import java.util.ArrayList;
import java.util.List;

import util.annotations.Immutable;
import domain.Model;

/**
 * The ModelCatalog provides an overview of the Models that a Manufacturer 
 * can produce.
 */
@Immutable
public class ModelCatalog {
	/**
	 * Create a new modelCatalog with given list of models as models of the catalog.
	 * 
	 * @throws IllegalArgumentException
	 * 		If either the list of models or one of its elements is null
	 */
	public ModelCatalog(List<Model> inputModels) throws IllegalArgumentException{
		if(inputModels == null)
			throw new IllegalArgumentException("List of system models should not be null");
		if(inputModels.contains(null))
			throw new IllegalArgumentException("List of system models should not contain null");
		models = new ArrayList<>(inputModels);
	}	
	
	/**
	 * Get a list of all available Models in this ModelCatalog.
	 * 
	 * @return a list of all Models in this ModelCatalog
	 */
	public List<Model> getModels() {
		return new ArrayList<Model>(this.models);
	}	
	
	/** A list of all models of this ModelCatalog */
	private final List<Model> models;

	/**
	 * Check whether or not the given model is contained in this catalog.
	 * 
	 * @param model
	 * 		The model to check for.
	 * 
	 * @return whether or not given model is contained in this catalog
	 */
	public boolean contains(Model model) {
		return models.contains(model);
	}
}