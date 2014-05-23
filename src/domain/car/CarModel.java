package domain.car;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import util.annotations.Immutable;
import domain.assembly_line.TaskType;
import domain.car.OptionCategory;

/**
 * CarModel is a subclass of Model that represents models of cars.
 * 
 * @author Thomas Vochten
 *
 */
public class CarModel extends Model {

	/**
	 * Initialise a CarModel with the specified name, option categories
	 * and default minutes per work post.
	 * 
	 * @param modelName
	 * 		The name of the new model.
	 * @param optionCategories
	 * 		The option categories of the new model.
	 * @param minsPerWorkPost
	 * 		The default minutes per work post of the new model.
	 */
	public CarModel(String modelName, List<OptionCategory> optionCategories,
			int minsPerWorkPost) {
		super(modelName, optionCategories, minsPerWorkPost);
	}

	@Override
	public int getMinsOnWorkPostOfType(TaskType workPostType) {
		switch(workPostType) {
			case CARGO:
				return 0;
			case CERTIFICATION:
				return 0;
			default:
				return super.getMinsPerWorkPost();
		}
	}
}