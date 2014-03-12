package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides an overview of the models that a manufacturer can produce.
 *
 */
public class ModelCatalog {
	
	/**
	 * Initialises a model catalog.
	 * 
	 * @post getModels() is not null
	 * @post getModels() is not empty
	 */
	public ModelCatalog(List<Model> inputModels) {
		models = new ArrayList<>(inputModels);
	}
	
	/**
	 * Gets the list of all available models.
	 */
	public List<Model> getModels() {
		return new ArrayList<Model>(this.models);
	}

	/** A list of all models of this ModelCatalog */
	private final List<Model> models;
}