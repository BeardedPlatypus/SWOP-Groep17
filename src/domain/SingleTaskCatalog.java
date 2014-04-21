package domain;

import java.util.ArrayList;
import java.util.List;

import util.annotations.Immutable;
import domain.OptionCategory;

/**
 * Provides an overview of the tasks that can be ordered
 * individually by a customs shop.
 * 
 * @author Simon Slangen
 */
@Immutable
public class SingleTaskCatalog {
	
	private ArrayList<OptionCategory> optionCategories = new ArrayList<OptionCategory>();
	
	/**
	 * Initializes the single task catalog with the given list of option categories.
	 * 
	 * @post getPossibleTasks() is not null
	 */
	public SingleTaskCatalog(List<OptionCategory> inputOptionCategories){
		this.optionCategories = new ArrayList<OptionCategory>(inputOptionCategories);
	}

	/**
	 * Returns a list of option categories in this single task catalog.
	 */
	public ArrayList<OptionCategory> getPossibleTasks() {
		return new ArrayList<OptionCategory>(this.optionCategories);
	}
}