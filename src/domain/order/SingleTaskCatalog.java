package domain.order;

import java.util.ArrayList;
import java.util.List;

import util.annotations.Immutable;
import domain.car.Option;
import domain.car.OptionCategory;

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

	/**
	 * Check whether or not this catalog contains given option
	 * 
	 * @param option
	 * 		the option to check for
	 * 
	 * @return whether given option is present in this catalog
	 */
	public boolean contains(Option option) {
		for(OptionCategory cat : getPossibleTasks()){
			if(cat.containsOption(option)){
				return true;
			}
		}
		return false;
	}
}