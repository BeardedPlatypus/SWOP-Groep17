package domain;

import java.util.ArrayList;
import java.util.List;

public class InitialisationHandler {
	private final AdvanceAssemblyLineHandler advanceHandler;
	private final NewOrderSessionHandler newOrderHandler;
	private final PerformAssemblyTaskHandler taskHandler;
	
	public InitialisationHandler(){
		//Make Options
		List<Option> options = new ArrayList<Option>();
		options.add(new Option("Body", "Mount the car body on the chassis. Fasten everyhting tightly.", TaskType.BODY, "Sedan", "Break"));
		options.add(new Option("Color", "Paint car in designated color.", TaskType.BODY, "Red", "Blue", "Black", "White"));
		options.add(new Option("Engine", "Insert engine in the hood.", TaskType.DRIVETRAIN, "Standard 2l 4 cilinders", "Performance 2.5l 6 cilinders"));
		options.add(new Option("Gearbox", "Insert gearbox in the car.", TaskType.DRIVETRAIN, "6 speed manual", "5 speed automatic"));
		options.add(new Option("Seats", "Install seats with specified color.", TaskType.ACCESSORIES, "Leather black", "Leather white", "Vinyl grey"));
		options.add(new Option("Airco", "Install airco system in the car.", TaskType.ACCESSORIES, "Manual", "Automatic climate control"));
		options.add(new Option("Wheels", "Mount wheels on the chassis.", TaskType.ACCESSORIES, "Comfort", "Sports (low profile)"));
		//Make Model
		Model defaultModel = new Model("CSWorks Ultra Mega Special Deluxe", options);
		//Make ModelCatalog
		List<Model> models = new ArrayList<Model>();
		models.add(defaultModel);
		ModelCatalog catalog = new ModelCatalog(models);
		//Make Manufacturer
		Manufacturer manufacturer = new Manufacturer(catalog);
		//Make ProdSched
		ProductionSchedule productionSchedule = new ProductionSchedule(manufacturer);
		//Make AssemblyLine
		AssemblyLine assemblyLine = new AssemblyLine(productionSchedule);
		
		advanceHandler = new AdvanceAssemblyLineHandler(assemblyLine);
		newOrderHandler = new NewOrderSessionHandler(manufacturer);
		taskHandler = new PerformAssemblyTaskHandler(assemblyLine);
	}
	
	public AdvanceAssemblyLineHandler getAdvanceHandler() {
		return advanceHandler;
	}

	public NewOrderSessionHandler getNewOrderHandler() {
		return newOrderHandler;
	}

	public PerformAssemblyTaskHandler getTaskHandler() {
		return taskHandler;
	}

}
