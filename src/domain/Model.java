package domain;

import java.util.ArrayList;
import domain.Option;
import domain.OptionCategory;

/**
 * A model class is a class representing a car model. Has a set of possible options chosen when a car
 * of this model is ordered made.
 * 
 * When an order is to be placed, choices based on the options are stored in specifications.
 * 
 * This class is instantiated by the ModelCatalog and instantiates Specification objects.
 * 
 * @author Frederik Goovaerts
 */
public class Model {
	private ArrayList<Option> options = new ArrayList<Option>();
	public ArrayList<OptionCategory> optionCategories = new ArrayList<OptionCategory>();

	public void getOptionCategories() {
		throw new UnsupportedOperationException();
	}
}