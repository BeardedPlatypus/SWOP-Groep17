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
	/* As the assignment names only one available model, that one model
	 * is hardcoded. The only viable way of programming this more generically is to take
	 * persistent storage, like a database of models, into account. However, the assignment
	 * also says not to worry about persistent storage.
	 */
	public ModelCatalog() {
		List<Option> options = new ArrayList<Option>();
		options.add(new Option("Body", "Assemble car body", TaskType.BODY, "Sedan", "Break"));
		options.add(new Option("Color", "Paint car", TaskType.BODY, "Red", "Blue", "Black", "White"));
		options.add(new Option("Engine", "Insert engine", TaskType.DRIVETRAIN, "Standard 2l 4 cilinders", "Performance 2.5l 6 cilinders"));
		options.add(new Option("Gearbox", "Insert gearbox", TaskType.DRIVETRAIN, "6 speed manual", "5 speed automatic"));
		options.add(new Option("Seats", "Install seats", TaskType.ACCESSORIES, "Leather black", "Leather white", "Vinyl grey"));
		options.add(new Option("Airco", "Install airco", TaskType.ACCESSORIES, "Manual", "Automatic climate control"));
		options.add(new Option("Wheels", "Mount wheels", TaskType.ACCESSORIES, "Comfort", "Sports (low profile)"));
		
		Model model = new Model("CSWorks Ultra Mega Special Deluxe", options);
		this.models.add(model);
	}
	
	/**
	 * Gets the list of all available models.
	 */
	public List<Model> getModels() {
		return new ArrayList<Model>(this.models);
	}

	/** A list of all models of this ModelCatalog */
	private List<Model> models = new ArrayList<Model>();
}