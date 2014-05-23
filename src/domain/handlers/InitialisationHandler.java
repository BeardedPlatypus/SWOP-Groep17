package domain.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domain.DateTime;
import domain.Manufacturer;
import domain.assembly_line.AssemblyFloor;
import domain.assembly_line.AssemblyLine;
import domain.assembly_line.AssemblyLineBuilder;
import domain.assembly_line.AssemblyLineController;
import domain.assembly_line.AssemblyLineFacade;
import domain.assembly_line.BrokenState;
import domain.assembly_line.MaintenanceState;
import domain.assembly_line.OperationalState;
import domain.assembly_line.StateCatalog;
import domain.assembly_line.TaskType;
import domain.car.CarModel;
import domain.car.Model;
import domain.car.ModelCatalog;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.car.TruckModel;
import domain.clock.Clock;
import domain.clock.ClockManipulator;
import domain.initialdata.InitialDataLoader;
import domain.order.CompletedOrderCatalog;
import domain.order.OrderFactory;
import domain.order.SingleTaskCatalog;
import domain.order.StandardOrder;
import domain.production_schedule.SchedulerContext;
import domain.production_schedule.strategy.AlgorithmStrategyFactory;
import domain.production_schedule.strategy.FifoStrategy;
import domain.restrictions.OptionProhibitsOtherSetRestriction;
import domain.restrictions.OptionRequiresOtherSetRestriction;
import domain.restrictions.OptionRestrictionManager;
import domain.restrictions.RequiredOptionSetRestriction;
import domain.restrictions.Restriction;
import domain.statistics.CarsProducedRegistrar;
import domain.statistics.DelayRegistrar;
import domain.statistics.EstimatedProductionTimeRegistrar;
import domain.statistics.EstimatedTimeCatalog;
import domain.statistics.StatisticsLogger;

/**
 * The InitialisationHandler is responsible for initialising the system. This
 * includes making all the appropriate objects and setting their associations. 
 * It also provides a way for the interface to ask for the appropriate handlers. 
 * 
 */
public class InitialisationHandler {

	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Construct the initialisationhandler, which in turn sets up a domainfacade,
	 * by constructing all necessary components and putting them together.
	 */
	public InitialisationHandler(){

		//----------------------------------------------------------------------
		// Initialise Options
		//----------------------------------------------------------------------

		// -- Body Options
		Option bodySedanOption = new Option(TaskType.BODY, "Sedan Body",
				"Mount the Sedan body on the car.");
		Option bodyBreakOption = new Option(TaskType.BODY, "Break Body",
				"Mount the Break body on the car.");
		Option bodySportOption = new Option(TaskType.BODY, "Sport Body",
				"Mount the Sport body on the car.");
		Option bodyPlatformOption = new Option(TaskType.BODY, "Platform Body",
				"Mount the Platform body on the truck.");
		Option bodyClosedOption = new Option(TaskType.BODY, "Closed Body",
				"Mount the Closed body on the truck.");
		// -- Paint
		Option paintRedOption = new Option(TaskType.BODY, "Red Paint", "Paint the car red.");
		Option paintBlueOption = new Option(TaskType.BODY, "Blue Paint", "Paint the car blue.");
		Option paintYellowOption = new Option(TaskType.BODY, "Yellow Paint", "Paint the car yellow.");
		Option paintGreenOption = new Option(TaskType.BODY, "Green Paint", "Paint the car green.");
		Option paintWhiteOption = new Option(TaskType.BODY, "White Paint", "Paint the car white.");
		Option paintBlackOption = new Option(TaskType.BODY, "Black Paint", "Paint the car black.");
		// -- Engine Options
		Option engineStandardOption = new Option(TaskType.DRIVETRAIN,
				"standard 2l v4", "Mount the standard engine in the car.");
		Option enginePerformanceOption = new Option(TaskType.DRIVETRAIN,
				"performance 2.5l v6", "Mount the performance engine in the car.");
		Option engineUltraOption = new Option(TaskType.DRIVETRAIN,
				"ultra 3l v8", "Mount the Ultra engine in the car.");
		Option engineStandardTruckOption = new Option(TaskType.DRIVETRAIN,
				"standard engine", "Mount the standard engine in the truck.");
		Option engineHybridTruckOption = new Option(TaskType.DRIVETRAIN,
				"hybrid engine", "Mount the hybrid engine in the truck.");
		// -- Gearbox Options
		Option gearbox6ManOption = new Option(TaskType.DRIVETRAIN, "6 speed manual",
				"Mount the 6 speed manual gearbox.");
		Option gearbox5ManOption = new Option(TaskType.DRIVETRAIN, "5 speed manual",
				"Mount the 5 speed manual gearbox.");
		Option gearbox5AutoOption = new Option(TaskType.DRIVETRAIN, "5 speed automatic",
				"Mount the 5 speed automatic gearbox.");
		Option gearbox8ManOption = new Option(TaskType.DRIVETRAIN, "8 speed manual",
				"Mount the 8 speed manual gearbox.");
		Option gearboxFullAutomaticOption = new Option(TaskType.DRIVETRAIN, "full automatic",
				"Mount the full automatic gearbox.");
		// -- Seat Options
		Option seatLeatherWhiteOption = new Option(TaskType.ACCESSORIES,
				"White leather seats", "Mount white leather seats in the car.");
		Option seatLeatherBlackOption = new Option(TaskType.ACCESSORIES,
				"Black leather seats", "Mount black leather seats in the car.");
		Option seatVinylGrayOption = new Option(TaskType.ACCESSORIES,
				"Gray Vinyl seats", "Mount grey Vinyl seats in the vehicle.");
		Option seatVinylBlackOption = new Option(TaskType.ACCESSORIES,
				"Black Vinyl seats", "Mount black Vinyl seats in the truck.");
		// -- Airco Options
		Option aircoManOption = new Option(TaskType.ACCESSORIES, "Manual Airco",
				"Put manual airco in the car");
		Option aircoAutoOption = new Option(TaskType.ACCESSORIES, "Automatic Airco",
				"Put automatic airco in the car");
		Option aircoNoneOption = new Option(TaskType.ACCESSORIES, "No Airco",
				"Put no airco in the car");
		aircoNoneOption.setNeedsAssemblyTask(false);
		// -- Wheels Options
		Option wheelsWinterOption = new Option(TaskType.ACCESSORIES, "Winter tires",
				"Put winter tires on the car.");
		Option wheelsComfortOption = new Option(TaskType.ACCESSORIES, "Comfort tires",
				"Put comfort tires on the car.");
		Option wheelsSportOption = new Option(TaskType.ACCESSORIES, "Sport tires",
				"Put sport tires on the car.");
		Option wheelsStandardOption = new Option(TaskType.ACCESSORIES, "Standard wheels",
				"Put standard wheels on the truck.");
		Option wheelsHeavyOption = new Option(TaskType.ACCESSORIES, "Heavy Duty wheels",
				"Put Heavy Duty wheels on the truck.");
		// -- Spoiler Options
		Option spoilerHighOption = new Option(TaskType.ACCESSORIES, "High Spoiler",
				"Mount high spoiler on the car.");
		Option spoilerLowOption = new Option(TaskType.ACCESSORIES, "Low Spoiler",
				"Mount low spoiler on the car.");
		Option spoilerNoneOption = new Option(TaskType.ACCESSORIES, "No Spoiler",
				"Mount no spoiler on the car.");
		spoilerNoneOption.setNeedsAssemblyTask(false);
		
		// -- Extra truck options
		Option certificationOption = new Option(TaskType.CERTIFICATION, "Certification",
				"Certify maximum cargo load.");
		Option cargoToolOption = new Option(TaskType.CARGO, "Tool Storage",
				"Install tool storage.");
		Option cargoProtectionOption = new Option(TaskType.CARGO, "Cargo Protection",
				"Add cargo protection.");

		//----------------------------------------------------------------------
		// Initialise OptionCategories
		//----------------------------------------------------------------------

		List<Option> modelABodyList = new ArrayList<>();
		List<Option> modelBBodyList = new ArrayList<>();
		List<Option> modelCBodyList = new ArrayList<>();
		List<Option> modelXBodyList = new ArrayList<>();
		List<Option> modelYBodyList = new ArrayList<>();
		modelABodyList.add(bodySedanOption);
		modelABodyList.add(bodyBreakOption);
		modelBBodyList.add(bodySedanOption);
		modelBBodyList.add(bodyBreakOption);
		modelBBodyList.add(bodySportOption);
		modelCBodyList.add(bodySportOption);
		modelXBodyList.add(bodyPlatformOption);
		modelXBodyList.add(bodyClosedOption);
		modelYBodyList.add(bodyPlatformOption);
		OptionCategory modelABodyCategory = new OptionCategory(modelABodyList, "Car Body");
		OptionCategory modelBBodyCategory = new OptionCategory(modelBBodyList, "Car Body");
		OptionCategory modelCBodyCategory = new OptionCategory(modelCBodyList, "Car Body");
		OptionCategory modelXBodyCategory = new OptionCategory(modelXBodyList, "Car Body");
		OptionCategory modelYBodyCategory = new OptionCategory(modelYBodyList, "Car Body");

		List<Option> modelAPaintList = new ArrayList<>();
		List<Option> modelBPaintList = new ArrayList<>();
		List<Option> modelCPaintList = new ArrayList<>();
		List<Option> modelXPaintList = new ArrayList<>();
		List<Option> modelYPaintList = new ArrayList<>();
		modelAPaintList.add(paintRedOption);
		modelAPaintList.add(paintBlueOption);
		modelAPaintList.add(paintBlackOption);
		modelAPaintList.add(paintWhiteOption);
		modelBPaintList.add(paintRedOption);
		modelBPaintList.add(paintBlueOption);
		modelBPaintList.add(paintGreenOption);
		modelBPaintList.add(paintYellowOption);
		modelCPaintList.add(paintBlackOption);
		modelCPaintList.add(paintWhiteOption);
		modelXPaintList.add(paintGreenOption);
		modelXPaintList.add(paintWhiteOption);
		modelYPaintList.add(paintBlackOption);
		modelYPaintList.add(paintWhiteOption);
		OptionCategory modelAPaintCategory = new OptionCategory(modelAPaintList, "Car Paint");
		OptionCategory modelBPaintCategory = new OptionCategory(modelBPaintList, "Car Paint");
		OptionCategory modelCPaintCategory = new OptionCategory(modelCPaintList, "Car Paint");
		OptionCategory modelXPaintCategory = new OptionCategory(modelXPaintList, "Car Paint");
		OptionCategory modelYPaintCategory = new OptionCategory(modelYPaintList, "Car Paint");

		List<Option> modelAEngineList = new ArrayList<>();
		List<Option> modelBEngineList = new ArrayList<>();
		List<Option> modelCEngineList = new ArrayList<>();
		List<Option> modelXEngineList = new ArrayList<>();
		List<Option> modelYEngineList = new ArrayList<>();
		modelAEngineList.add(engineStandardOption);
		modelAEngineList.add(enginePerformanceOption);
		modelBEngineList.add(engineStandardOption);
		modelBEngineList.add(enginePerformanceOption);
		modelBEngineList.add(engineUltraOption);
		modelCEngineList.add(enginePerformanceOption);
		modelCEngineList.add(engineUltraOption);
		modelXEngineList.add(engineStandardTruckOption);
		modelXEngineList.add(engineHybridTruckOption);
		modelYEngineList.add(engineStandardTruckOption);
		modelYEngineList.add(engineHybridTruckOption);
		OptionCategory modelAEngineCategory = new OptionCategory(modelAEngineList, "Engine");
		OptionCategory modelBEngineCategory = new OptionCategory(modelBEngineList, "Engine");
		OptionCategory modelCEngineCategory = new OptionCategory(modelCEngineList, "Engine");
		OptionCategory modelXEngineCategory = new OptionCategory(modelXEngineList, "Engine");
		OptionCategory modelYEngineCategory = new OptionCategory(modelYEngineList, "Engine");

		List<Option> modelAGearboxList = new ArrayList<>();
		List<Option> modelBGearboxList = new ArrayList<>();
		List<Option> modelCGearboxList = new ArrayList<>();
		List<Option> modelXGearboxList = new ArrayList<>();
		List<Option> modelYGearboxList = new ArrayList<>();
		modelAGearboxList.add(gearbox6ManOption);
		modelAGearboxList.add(gearbox5ManOption);
		modelAGearboxList.add(gearbox5AutoOption);
		modelBGearboxList.add(gearbox6ManOption);
		modelBGearboxList.add(gearbox5AutoOption);
		modelCGearboxList.add(gearbox6ManOption);
		modelXGearboxList.add(gearbox8ManOption);
		modelXGearboxList.add(gearboxFullAutomaticOption);
		modelYGearboxList.add(gearbox8ManOption);
		modelYGearboxList.add(gearboxFullAutomaticOption);
		OptionCategory modelAGearboxCategory = new OptionCategory(modelAGearboxList, "Gearbox");
		OptionCategory modelBGearboxCategory = new OptionCategory(modelBGearboxList, "Gearbox");
		OptionCategory modelCGearboxCategory = new OptionCategory(modelCGearboxList, "Gearbox");
		OptionCategory modelXGearboxCategory = new OptionCategory(modelXGearboxList, "Gearbox");
		OptionCategory modelYGearboxCategory = new OptionCategory(modelYGearboxList, "Gearbox");

		List<Option> modelASeatsList = new ArrayList<>();
		List<Option> modelBSeatsList = new ArrayList<>();
		List<Option> modelCSeatsList = new ArrayList<>();
		List<Option> modelXSeatsList = new ArrayList<>();
		List<Option> modelYSeatsList = new ArrayList<>();
		modelASeatsList.add(seatVinylGrayOption);
		modelASeatsList.add(seatLeatherBlackOption);
		modelASeatsList.add(seatLeatherWhiteOption);
		modelBSeatsList.add(seatVinylGrayOption);
		modelBSeatsList.add(seatLeatherBlackOption);
		modelBSeatsList.add(seatLeatherWhiteOption);
		modelCSeatsList.add(seatLeatherBlackOption);
		modelCSeatsList.add(seatLeatherWhiteOption);
		modelXSeatsList.add(seatVinylGrayOption);
		modelXSeatsList.add(seatVinylBlackOption);
		modelYSeatsList.add(seatVinylGrayOption);
		modelYSeatsList.add(seatVinylBlackOption);
		OptionCategory modelASeatsCategory = new OptionCategory(modelASeatsList, "Seats");
		OptionCategory modelBSeatsCategory = new OptionCategory(modelBSeatsList, "Seats");
		OptionCategory modelCSeatsCategory = new OptionCategory(modelCSeatsList, "Seats");
		OptionCategory modelXSeatsCategory = new OptionCategory(modelXSeatsList, "Seats");
		OptionCategory modelYSeatsCategory = new OptionCategory(modelYSeatsList, "Seats");

		List<Option> modelAAircoList = new ArrayList<>();
		List<Option> modelBAircoList = new ArrayList<>();
		List<Option> modelCAircoList = new ArrayList<>();
		List<Option> modelXAircoList = new ArrayList<>();
		List<Option> modelYAircoList = new ArrayList<>();
		modelAAircoList.add(aircoAutoOption);
		modelAAircoList.add(aircoManOption);
		modelAAircoList.add(aircoNoneOption);
		modelBAircoList.add(aircoAutoOption);
		modelBAircoList.add(aircoManOption);
		modelBAircoList.add(aircoNoneOption);
		modelCAircoList.add(aircoAutoOption);
		modelCAircoList.add(aircoManOption);
		modelCAircoList.add(aircoNoneOption);
		modelXAircoList.add(aircoAutoOption);
		modelXAircoList.add(aircoManOption);
		modelXAircoList.add(aircoNoneOption);
		modelYAircoList.add(aircoAutoOption);
		modelYAircoList.add(aircoManOption);
		modelYAircoList.add(aircoNoneOption);
		OptionCategory modelAAircoCategory = new OptionCategory(modelAAircoList, "Airco");
		OptionCategory modelBAircoCategory = new OptionCategory(modelBAircoList, "Airco");
		OptionCategory modelCAircoCategory = new OptionCategory(modelCAircoList, "Airco");
		OptionCategory modelXAircoCategory = new OptionCategory(modelXAircoList, "Airco");
		OptionCategory modelYAircoCategory = new OptionCategory(modelYAircoList, "Airco");

		List<Option> modelAWheelsList = new ArrayList<>();
		List<Option> modelBWheelsList = new ArrayList<>();
		List<Option> modelCWheelsList = new ArrayList<>();
		List<Option> modelXWheelsList = new ArrayList<>();
		List<Option> modelYWheelsList = new ArrayList<>();
		modelAWheelsList.add(wheelsSportOption);
		modelAWheelsList.add(wheelsWinterOption);
		modelAWheelsList.add(wheelsComfortOption);
		modelBWheelsList.add(wheelsSportOption);
		modelBWheelsList.add(wheelsWinterOption);
		modelBWheelsList.add(wheelsComfortOption);
		modelCWheelsList.add(wheelsSportOption);
		modelCWheelsList.add(wheelsWinterOption);
		modelXWheelsList.add(wheelsStandardOption);
		modelXWheelsList.add(wheelsHeavyOption);
		modelYWheelsList.add(wheelsStandardOption);
		modelYWheelsList.add(wheelsHeavyOption);
		OptionCategory modelAWheelsCategory = new OptionCategory(modelAWheelsList, "Wheels");
		OptionCategory modelBWheelsCategory = new OptionCategory(modelBWheelsList, "Wheels");
		OptionCategory modelCWheelsCategory = new OptionCategory(modelCWheelsList, "Wheels");
		OptionCategory modelXWheelsCategory = new OptionCategory(modelXWheelsList, "Wheels");
		OptionCategory modelYWheelsCategory = new OptionCategory(modelYWheelsList, "Wheels");

		List<Option> modelASpoilerList = new ArrayList<>();
		List<Option> modelBSpoilerList = new ArrayList<>();
		List<Option> modelCSpoilerList = new ArrayList<>();
		List<Option> modelXSpoilerList = new ArrayList<>();
		List<Option> modelYSpoilerList = new ArrayList<>();
		modelASpoilerList.add(spoilerNoneOption);
		modelBSpoilerList.add(spoilerNoneOption);
		modelBSpoilerList.add(spoilerLowOption);
		modelCSpoilerList.add(spoilerNoneOption);
		modelCSpoilerList.add(spoilerLowOption);
		modelCSpoilerList.add(spoilerHighOption);
		modelXSpoilerList.add(spoilerNoneOption);
		modelYSpoilerList.add(spoilerNoneOption);
		OptionCategory modelASpoilerCategory = new OptionCategory(modelASpoilerList, "Spoiler");
		OptionCategory modelBSpoilerCategory = new OptionCategory(modelBSpoilerList, "Spoiler");
		OptionCategory modelCSpoilerCategory = new OptionCategory(modelCSpoilerList, "Spoiler");
		OptionCategory modelXSpoilerCategory = new OptionCategory(modelXSpoilerList, "Spoiler");
		OptionCategory modelYSpoilerCategory = new OptionCategory(modelYSpoilerList, "Spoiler");
		
		//Extra truck optioncategories
		
		OptionCategory certificationCategory = new OptionCategory(
				Arrays.asList(certificationOption), "Certification");
		OptionCategory toolStorageCategory = new OptionCategory(
				Arrays.asList(cargoToolOption), "ToolStorage");
		OptionCategory cargoProtectionCategory = new OptionCategory(
				Arrays.asList(cargoProtectionOption), "CargoProtection");

		//----------------------------------------------------------------------
		// Initialise Models
		//----------------------------------------------------------------------

		List<OptionCategory> modelACategories = new ArrayList<>();
		modelACategories.add(modelAAircoCategory);
		modelACategories.add(modelABodyCategory);
		modelACategories.add(modelAEngineCategory);
		modelACategories.add(modelAGearboxCategory);
		modelACategories.add(modelAPaintCategory);
		modelACategories.add(modelASeatsCategory);
		modelACategories.add(modelASpoilerCategory);
		modelACategories.add(modelAWheelsCategory);
		Model modelA = new CarModel("Model A", modelACategories, 50);

		List<OptionCategory> modelBCategories = new ArrayList<>();
		modelBCategories.add(modelBAircoCategory);
		modelBCategories.add(modelBBodyCategory);
		modelBCategories.add(modelBEngineCategory);
		modelBCategories.add(modelBGearboxCategory);
		modelBCategories.add(modelBPaintCategory);
		modelBCategories.add(modelBSeatsCategory);
		modelBCategories.add(modelBSpoilerCategory);
		modelBCategories.add(modelBWheelsCategory);
		Model modelB = new CarModel("Model B", modelBCategories, 70);

		List<OptionCategory> modelCCategories = new ArrayList<>();
		modelCCategories.add(modelCAircoCategory);
		modelCCategories.add(modelCBodyCategory);
		modelCCategories.add(modelCEngineCategory);
		modelCCategories.add(modelCGearboxCategory);
		modelCCategories.add(modelCPaintCategory);
		modelCCategories.add(modelCSeatsCategory);
		modelCCategories.add(modelCSpoilerCategory);
		modelCCategories.add(modelCWheelsCategory);
		Model modelC = new CarModel("Model C", modelCCategories, 60);
		
		List<OptionCategory> modelXCategories = new ArrayList<>();
		modelXCategories.add(modelXAircoCategory);
		modelXCategories.add(modelXBodyCategory);
		modelXCategories.add(modelXEngineCategory);
		modelXCategories.add(modelXGearboxCategory);
		modelXCategories.add(modelXPaintCategory);
		modelXCategories.add(modelXSeatsCategory);
		modelXCategories.add(modelXSpoilerCategory);
		modelXCategories.add(modelXWheelsCategory);
		modelXCategories.add(certificationCategory);
		modelXCategories.add(toolStorageCategory);
		modelXCategories.add(cargoProtectionCategory);
		Model modelX = new TruckModel("Model X", modelXCategories, 60, 90, 30);

		List<OptionCategory> modelYCategories = new ArrayList<>();
		modelYCategories.add(modelYAircoCategory);
		modelYCategories.add(modelYBodyCategory);
		modelYCategories.add(modelYEngineCategory);
		modelYCategories.add(modelYGearboxCategory);
		modelYCategories.add(modelYPaintCategory);
		modelYCategories.add(modelYSeatsCategory);
		modelYCategories.add(modelYSpoilerCategory);
		modelYCategories.add(modelYWheelsCategory);
		modelYCategories.add(certificationCategory);
		modelYCategories.add(toolStorageCategory);
		modelYCategories.add(cargoProtectionCategory);
		Model modelY = new TruckModel("Model Y", modelYCategories, 60, 120, 45);
		
		Model singleTaskModel = new CarModel("Single Task Order",
				new ArrayList<OptionCategory>(), 60);

		//----------------------------------------------------------------------
		// Initialise ModelCatalog
		//----------------------------------------------------------------------

		List<Model> normalOrderSessionModels = new ArrayList<>();
		normalOrderSessionModels.add(modelA);
		normalOrderSessionModels.add(modelB);
		normalOrderSessionModels.add(modelC);
		normalOrderSessionModels.add(modelX);
		normalOrderSessionModels.add(modelY);
		ModelCatalog modelCatalog = new ModelCatalog(normalOrderSessionModels,
				singleTaskModel);

		//----------------------------------------------------------------------
		// Initialise Restrictions
		//----------------------------------------------------------------------

		List<Restriction> restrictions = new ArrayList<Restriction>();

		// Body is required
		//Reusing old list, not good, but it makes for less lines
		Set<Option> allBodyOptionsSet = new HashSet<Option>(modelBBodyList);
		allBodyOptionsSet.addAll(modelXBodyList);
		restrictions.add(new RequiredOptionSetRestriction(allBodyOptionsSet));

		//Paint is required
		Set<Option> allPaintOptionsSet = new HashSet<Option>(modelAPaintList);
		allPaintOptionsSet.addAll(modelBPaintList);
		restrictions.add(new RequiredOptionSetRestriction(allPaintOptionsSet));

		//Engine is required
		Set<Option> allEngineOptionsSet = new HashSet<Option>(modelBEngineList);
		allEngineOptionsSet.addAll(modelXEngineList);
		restrictions.add(new RequiredOptionSetRestriction(allEngineOptionsSet));

		//Gearbox is required
		Set<Option> allGearboxOptionsSet = new HashSet<Option>(modelAGearboxList);
		allGearboxOptionsSet.addAll(modelXGearboxList);
		restrictions.add(new RequiredOptionSetRestriction(allGearboxOptionsSet));

		//Seats are required
		Set<Option> allSeatsOptionsSet = new HashSet<Option>(modelBSeatsList);
		allSeatsOptionsSet.addAll(modelXSeatsList);
		restrictions.add(new RequiredOptionSetRestriction(allSeatsOptionsSet));

		//Wheels are required
		Set<Option> allWheelsOptionsSet = new HashSet<Option>(modelBWheelsList);
		allWheelsOptionsSet.addAll(modelXWheelsList);
		restrictions.add(new RequiredOptionSetRestriction(allWheelsOptionsSet));

		// If a sport body is selected, a spoiler is mandatory
		Set<Option> reqSpoilers = new HashSet<Option>();
		reqSpoilers.add(spoilerHighOption);
		reqSpoilers.add(spoilerLowOption);
		restrictions.add(new OptionRequiresOtherSetRestriction(bodySportOption, reqSpoilers));

		// If a sport body is selected, a performance or ultra engine is necessary
		Set<Option> reqEngines = new HashSet<Option>();
		reqEngines.add(enginePerformanceOption);
		reqEngines.add(engineUltraOption);
		restrictions.add(new OptionRequiresOtherSetRestriction(bodySportOption, reqEngines));

		// If an ultra engine is selected, this prohibits an automatic airco
		Set<Option> prohAirco = new HashSet<>();
		prohAirco.add(aircoAutoOption);
		restrictions.add(new OptionProhibitsOtherSetRestriction(engineUltraOption, prohAirco));
		
		// If a sport body is selected, a performance or ultra engine is necessary
		Set<Option> reqWheels = new HashSet<Option>();
		reqWheels.add(wheelsHeavyOption);
		restrictions.add(new OptionRequiresOtherSetRestriction(bodyPlatformOption, reqWheels));

		//----------------------------------------------------------------------
		// Initialise RestrictionManager
		//----------------------------------------------------------------------

		OptionRestrictionManager restrictionsMan = new OptionRestrictionManager(restrictions);

		//----------------------------------------------------------------------
		// Initialise Single Task Catalog
		//----------------------------------------------------------------------

		//Necessary OptionCategories:
		// All Paint options
		List<Option> allPaintOptionsList = new ArrayList<Option>(allPaintOptionsSet);
		OptionCategory allPaintCategory = new OptionCategory(allPaintOptionsList, "Custom Paint");
		// All Seats options
		List<Option> allSeatsOptionsList = new ArrayList<>(allSeatsOptionsSet);
		OptionCategory allSeatsCategory = new OptionCategory(allSeatsOptionsList, "Custom Seats");

		List<OptionCategory> singleTaskCategories = new ArrayList<>();
		singleTaskCategories.add(allSeatsCategory);
		singleTaskCategories.add(allPaintCategory);

		SingleTaskCatalog singleCatalog = new SingleTaskCatalog(singleTaskCategories);

		////--------------------------------------------------------------------
		// Initialise AlgorithmStrategyFactory
		//----------------------------------------------------------------------

		AlgorithmStrategyFactory stratFact = new AlgorithmStrategyFactory();


		//----------------------------------------------------------------------
		// Initialise CompletedOrderCatalog
		//----------------------------------------------------------------------

		CompletedOrderCatalog  complCat = new CompletedOrderCatalog();

		//----------------------------------------------------------------------
		// Initialise the Clock
		//----------------------------------------------------------------------
		DateTime start = new DateTime(0, 6, 0);
		Clock clock = new Clock(start);
		
		//----------------------------------------------------------------------
		// Initialise the ProductionSchedule
		//----------------------------------------------------------------------
		SchedulerContext schedule = new SchedulerContext(new FifoStrategy<StandardOrder>());

		//----------------------------------------------------------------------
		// Initialise the orderfactory
		//----------------------------------------------------------------------

		OrderFactory orderFact = new OrderFactory();
		
		//--------------------------------------------------------------------------
		// StateCatalog
		//--------------------------------------------------------------------------
		
		StateCatalog stateCat = new StateCatalog();
		stateCat.addToAvailableStates(new OperationalState());
		stateCat.addToAvailableStates(new BrokenState());
		stateCat.addToAvailableStates(new MaintenanceState());
		
		//----------------------------------------------------------------------
		// Initialise AssemblyLines
		//----------------------------------------------------------------------

		AssemblyLineBuilder proBuilder = new AssemblyLineBuilder();
		List<AssemblyLineFacade> lines = new ArrayList<>();
		
		proBuilder.addToDesiredModels(modelA);
		proBuilder.addToDesiredModels(modelB);
		AssemblyLine line1 = proBuilder.buildAssemblyLine(clock);
		proBuilder.addToDesiredModels(modelC);
		AssemblyLine line2 = proBuilder.buildAssemblyLine(clock);
		proBuilder.addToDesiredModels(modelX);
		proBuilder.addToDesiredModels(modelY);
		AssemblyLine line3 = proBuilder.buildAssemblyLine(clock);
		
		AssemblyLineController line1Controller =  new AssemblyLineController(schedule, clock);
		AssemblyLineController line2Controller =  new AssemblyLineController(schedule, clock);
		AssemblyLineController line3Controller =  new AssemblyLineController(schedule, clock);
		
		lines.add(new AssemblyLineFacade(line1, line1Controller));
		lines.add(new AssemblyLineFacade(line2, line2Controller));
		lines.add(new AssemblyLineFacade(line3, line3Controller));		
		
		StatisticsLogger logger = new StatisticsLogger();
		CarsProducedRegistrar prodRegistrar = new CarsProducedRegistrar();
		logger.addRegistrar(prodRegistrar);
		DelayRegistrar delayRegistrar = new DelayRegistrar();
		logger.addRegistrar(delayRegistrar);
		EstimatedProductionTimeRegistrar estTimeReg =
				new EstimatedProductionTimeRegistrar(clock);
		logger.addRegistrar(estTimeReg);
		AssemblyFloor floor = new AssemblyFloor(lines, logger);
		
		//--------------------------------------------------------------------------
		// Initialise Completion Estimator
		//--------------------------------------------------------------------------
		EstimatedTimeCatalog estTimeCat = new EstimatedTimeCatalog(estTimeReg);

		//----------------------------------------------------------------------
		// Attach Observers
		//----------------------------------------------------------------------
		
		//Timeobservers
		clock.attachTimeObserver(logger);
		clock.attachTimeObserver(orderFact);
		clock.attachTimeObserver(complCat);

		//CompletedOrderObservers
		line1.attachObserver(logger);
		line1.attachObserver(complCat);
		line2.attachObserver(logger);
		line2.attachObserver(complCat);
		line3.attachObserver(logger);
		line3.attachObserver(complCat);
		
//		schedule.attachOrderObserver(line1Controller);
//		schedule.attachOrderObserver(line2Controller);
//		schedule.attachOrderObserver(line3Controller);
		
		clock.register(line1Controller);
		clock.register(line2Controller);
		clock.register(line3Controller);
		
		clock.constructEvent(new DateTime(0, 6, 0), line1Controller);
		clock.constructEvent(new DateTime(0, 6, 0), line2Controller);
		clock.constructEvent(new DateTime(0, 6, 0), line3Controller);
		
		//----------------------------------------------------------------------
		// Initialise Manufacturer
		//----------------------------------------------------------------------

		Manufacturer manufacturer = new Manufacturer(
				stratFact,
				singleCatalog,
				complCat,
				modelCatalog,
				restrictionsMan,
				orderFact,
				floor,
				clock,
				schedule,
				estTimeCat,
				stateCat);

		//----------------------------------------------------------------------
		// Initialise Handlers
		//----------------------------------------------------------------------

		AdaptSchedulingAlgorithmHandler algorithmHandler = 
				new AdaptSchedulingAlgorithmHandler(manufacturer);
		AssemblyLineStatusHandler assemblyLineStatusHandler =
				new AssemblyLineStatusHandler(manufacturer);
		CheckOrderDetailsHandler orderDetailsHandler =
				new CheckOrderDetailsHandler(manufacturer);
		CheckProductionStatisticsHandler prodStatHandler =
				new CheckProductionStatisticsHandler(manufacturer);
		NewOrderSessionHandler newOrderHandler =
				new NewOrderSessionHandler(manufacturer);
		OrderSingleTaskHandler singleTaskHandler =
				new OrderSingleTaskHandler(manufacturer);
		PerformAssemblyTaskHandler performHandler =
				new PerformAssemblyTaskHandler(manufacturer);
		ChangeOperationalStatusHandler changeHandler =
				new ChangeOperationalStatusHandler(manufacturer);

		//----------------------------------------------------------------------
		// Initialise DomainFacade
		//----------------------------------------------------------------------

		this.domainFacade = new DomainFacade(
				algorithmHandler,
				assemblyLineStatusHandler,
				orderDetailsHandler,
				prodStatHandler,
				newOrderHandler,
				singleTaskHandler,
				performHandler,
				changeHandler);
		
		//--------------------------------------------------------------------------
		// Initialdataloader setup
		//--------------------------------------------------------------------------
		ClockManipulator manipulator = new ClockManipulator(clock);
		loader = new InitialDataLoader(domainFacade, manufacturer, manipulator);

	}

	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	/** 
	 * Get the DomainFacade of the system, initialised by this InitialisationHandler.
	 * 
	 * @return The DomainFacade of the system initialised by this InitialisationHandler
	 */
	public DomainFacade getDomainFacade() {
		return this.domainFacade;
	}

	/** The domain facade that is accessible by the UI. */
	private final DomainFacade domainFacade;
	
	/**
	 * Get the initialDataLoader of the set up system
	 * 
	 * @return the initialDataLoader
	 */
	public InitialDataLoader getInitialDataLoader(){
		return this.loader;
	}
	
	private final InitialDataLoader loader;
	
	//--------------------------------------------------------------------------
	// Setup method for iteration 3
	//--------------------------------------------------------------------------
	
	public void setupIteration3(){
		//First ten orders of the system
		this.getInitialDataLoader().placeRandomStandardOrder(5);
		//Complete those
		this.getInitialDataLoader().completeAllOrders();
		//Set the new day
		this.getInitialDataLoader().advanceDay(1);
		//Now we want to get the three lines filled up, we use the identical orders for this
		// since we slightly assume they can populate each assemblyLine
		this.getInitialDataLoader().placeIdenticalStandardOrder(3);
		//Add three singleTaskOrders
		this.getInitialDataLoader().placeSingleTaskOrder(3);
		//Add three batch orders
		this.getInitialDataLoader().placeIdenticalStandardOrder(3);
		//Add three random other orders
		this.getInitialDataLoader().placeRandomStandardOrder(3);
	}

}
