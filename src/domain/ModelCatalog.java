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
	 * Initialises a model catalog.
	 * 
	 * @post getModels() is not null
	 * @post getModels() is not empty
	 */
	public ModelCatalog(List<Model> inputModels) {
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
}