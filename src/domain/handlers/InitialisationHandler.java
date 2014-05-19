package domain.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domain.assemblyLine.AssemblyLine;
import domain.assemblyLine.SchedulerIntermediate;
import domain.Manufacturer;
import domain.assemblyLine.TaskType;
import domain.car.Model;
import domain.car.ModelCatalog;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.car.Model;
import domain.order.CompletedOrderCatalog;
import domain.order.OrderFactory;
import domain.order.SingleTaskCatalog;
import domain.order.StandardOrder;
import domain.productionSchedule.ClockManager;
import domain.productionSchedule.ProductionScheduleFacade;
import domain.productionSchedule.SchedulerContext;
import domain.productionSchedule.strategy.AlgorithmStrategyFactory;
import domain.productionSchedule.strategy.FifoStrategy;
import domain.restrictions.OptionProhibitsOtherSetRestriction;
import domain.restrictions.OptionRequiresOtherSetRestriction;
import domain.restrictions.OptionRestrictionManager;
import domain.restrictions.RequiredOptionSetRestriction;
import domain.restrictions.Restriction;
import domain.statistics.CarsProducedRegistrar;
import domain.statistics.DelayRegistrar;
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
		
		// -- Body Options
		Option bodySedanOption = new Option(TaskType.BODY, "Sedan Body",
				"Mount the Sedan body on the car.");
		Option bodyBreakOption = new Option(TaskType.BODY, "Break Body",
				"Mount the Break body on the car.");
		Option bodySportOption = new Option(TaskType.BODY, "Sport Body",
				"Mount the Sport body on the car.");
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
		// -- Gearbox Options
		Option gearbox6ManOption = new Option(TaskType.DRIVETRAIN, "6 speed manual",
				"Mount the 6 speed manual gearbox.");
		Option gearbox5ManOption = new Option(TaskType.DRIVETRAIN, "5 speed manual",
				"Mount the 5 speed manual gearbox.");
		Option gearbox5AutoOption = new Option(TaskType.DRIVETRAIN, "5 speed automatic",
				"Mount the 5 speed automatic gearbox.");
		// -- Seat Options
		Option seatLeatherWhiteOption = new Option(TaskType.ACCESSORIES,
				"White leather seats", "Mount white leather seats in the car.");
		Option seatLeatherBlackOption = new Option(TaskType.ACCESSORIES,
				"Black leather seats", "Mount black leather seats in the car.");
		Option seatVinylGrayOption = new Option(TaskType.ACCESSORIES,
				"Gray Vinyl seats", "Mount grey Vinyl seats in the car.");
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
		// -- Spoiler Options
		Option spoilerHighOption = new Option(TaskType.ACCESSORIES, "High Spoiler",
				"Mount high spoiler on the car.");
		Option spoilerLowOption = new Option(TaskType.ACCESSORIES, "Low Spoiler",
				"Mount low spoiler on the car.");
		Option spoilerNoneOption = new Option(TaskType.ACCESSORIES, "No Spoiler",
				"Mount no spoiler on the car.");
		spoilerNoneOption.setNeedsAssemblyTask(false);
		//----------------------------------------------------------------------
		
		//----------------------------------------------------------------------
		// Initialise OptionCategories
		
		List<Option> modelABodyList = new ArrayList<>();
		List<Option> modelBBodyList = new ArrayList<>();
		List<Option> modelCBodyList = new ArrayList<>();
		modelABodyList.add(bodySedanOption);
		modelABodyList.add(bodyBreakOption);
		modelBBodyList.add(bodySedanOption);
		modelBBodyList.add(bodyBreakOption);
		modelBBodyList.add(bodySportOption);
		modelCBodyList.add(bodySportOption);
		OptionCategory modelABodyCategory = new OptionCategory(modelABodyList, "Car Body");
		OptionCategory modelBBodyCategory = new OptionCategory(modelBBodyList, "Car Body");
		OptionCategory modelCBodyCategory = new OptionCategory(modelCBodyList, "Car Body");

		List<Option> modelAPaintList = new ArrayList<>();
		List<Option> modelBPaintList = new ArrayList<>();
		List<Option> modelCPaintList = new ArrayList<>();
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
		OptionCategory modelAPaintCategory = new OptionCategory(modelAPaintList, "Car Paint");
		OptionCategory modelBPaintCategory = new OptionCategory(modelBPaintList, "Car Paint");
		OptionCategory modelCPaintCategory = new OptionCategory(modelCPaintList, "Car Paint");
		
		List<Option> modelAEngineList = new ArrayList<>();
		List<Option> modelBEngineList = new ArrayList<>();
		List<Option> modelCEngineList = new ArrayList<>();
		modelAEngineList.add(engineStandardOption);
		modelAEngineList.add(enginePerformanceOption);
		modelBEngineList.add(engineStandardOption);
		modelBEngineList.add(enginePerformanceOption);
		modelBEngineList.add(engineUltraOption);
		modelCEngineList.add(enginePerformanceOption);
		modelCEngineList.add(engineUltraOption);
		OptionCategory modelAEngineCategory = new OptionCategory(modelAEngineList, "Engine");
		OptionCategory modelBEngineCategory = new OptionCategory(modelBEngineList, "Engine");
		OptionCategory modelCEngineCategory = new OptionCategory(modelCEngineList, "Engine");
		
		List<Option> modelAGearboxList = new ArrayList<>();
		List<Option> modelBGearboxList = new ArrayList<>();
		List<Option> modelCGearboxList = new ArrayList<>();
		modelAGearboxList.add(gearbox6ManOption);
		modelAGearboxList.add(gearbox5ManOption);
		modelAGearboxList.add(gearbox5AutoOption);
		modelBGearboxList.add(gearbox6ManOption);
		modelBGearboxList.add(gearbox5AutoOption);
		modelCGearboxList.add(gearbox6ManOption);
		OptionCategory modelAGearboxCategory = new OptionCategory(modelAGearboxList, "Gearbox");
		OptionCategory modelBGearboxCategory = new OptionCategory(modelBGearboxList, "Gearbox");
		OptionCategory modelCGearboxCategory = new OptionCategory(modelCGearboxList, "Gearbox");

		List<Option> modelASeatsList = new ArrayList<>();
		List<Option> modelBSeatsList = new ArrayList<>();
		List<Option> modelCSeatsList = new ArrayList<>();
		modelASeatsList.add(seatVinylGrayOption);
		modelASeatsList.add(seatLeatherBlackOption);
		modelASeatsList.add(seatLeatherWhiteOption);
		modelBSeatsList.add(seatVinylGrayOption);
		modelBSeatsList.add(seatLeatherBlackOption);
		modelBSeatsList.add(seatLeatherWhiteOption);
		modelCSeatsList.add(seatLeatherBlackOption);
		modelCSeatsList.add(seatLeatherWhiteOption);
		OptionCategory modelASeatsCategory = new OptionCategory(modelASeatsList, "Seats");
		OptionCategory modelBSeatsCategory = new OptionCategory(modelBSeatsList, "Seats");
		OptionCategory modelCSeatsCategory = new OptionCategory(modelCSeatsList, "Seats");

		List<Option> modelAAircoList = new ArrayList<>();
		List<Option> modelBAircoList = new ArrayList<>();
		List<Option> modelCAircoList = new ArrayList<>();
		modelAAircoList.add(aircoAutoOption);
		modelAAircoList.add(aircoManOption);
		modelAAircoList.add(aircoNoneOption);
		modelBAircoList.add(aircoAutoOption);
		modelBAircoList.add(aircoManOption);
		modelBAircoList.add(aircoNoneOption);
		modelCAircoList.add(aircoAutoOption);
		modelCAircoList.add(aircoManOption);
		modelCAircoList.add(aircoNoneOption);
		OptionCategory modelAAircoCategory = new OptionCategory(modelAAircoList, "Airco");
		OptionCategory modelBAircoCategory = new OptionCategory(modelBAircoList, "Airco");
		OptionCategory modelCAircoCategory = new OptionCategory(modelCAircoList, "Airco");

		List<Option> modelAWheelsList = new ArrayList<>();
		List<Option> modelBWheelsList = new ArrayList<>();
		List<Option> modelCWheelsList = new ArrayList<>();
		modelAWheelsList.add(wheelsSportOption);
		modelAWheelsList.add(wheelsWinterOption);
		modelAWheelsList.add(wheelsComfortOption);
		modelBWheelsList.add(wheelsSportOption);
		modelBWheelsList.add(wheelsWinterOption);
		modelBWheelsList.add(wheelsComfortOption);
		modelCWheelsList.add(wheelsSportOption);
		modelCWheelsList.add(wheelsWinterOption);
		OptionCategory modelAWheelsCategory = new OptionCategory(modelAWheelsList, "Wheels");
		OptionCategory modelBWheelsCategory = new OptionCategory(modelBWheelsList, "Wheels");
		OptionCategory modelCWheelsCategory = new OptionCategory(modelCWheelsList, "Wheels");

		List<Option> modelASpoilerList = new ArrayList<>();
		List<Option> modelBSpoilerList = new ArrayList<>();
		List<Option> modelCSpoilerList = new ArrayList<>();
		modelASpoilerList.add(spoilerNoneOption);
		modelBSpoilerList.add(spoilerNoneOption);
		modelBSpoilerList.add(spoilerLowOption);
		modelCSpoilerList.add(spoilerNoneOption);
		modelCSpoilerList.add(spoilerLowOption);
		modelCSpoilerList.add(spoilerHighOption);
		OptionCategory modelASpoilerCategory = new OptionCategory(modelASpoilerList, "Spoiler");
		OptionCategory modelBSpoilerCategory = new OptionCategory(modelBSpoilerList, "Spoiler");
		OptionCategory modelCSpoilerCategory = new OptionCategory(modelCSpoilerList, "Spoiler");
		//----------------------------------------------------------------------
		
		//--------------------------------------------------------------------------
		// Initialise Models
		List<OptionCategory> modelACategories = new ArrayList<>();
		modelACategories.add(modelAAircoCategory);
		modelACategories.add(modelABodyCategory);
		modelACategories.add(modelAEngineCategory);
		modelACategories.add(modelAGearboxCategory);
		modelACategories.add(modelAPaintCategory);
		modelACategories.add(modelASeatsCategory);
		modelACategories.add(modelASpoilerCategory);
		modelACategories.add(modelAWheelsCategory);
		Model modelA = new Model("Model A", modelACategories, 50);

		List<OptionCategory> modelBCategories = new ArrayList<>();
		modelBCategories.add(modelBAircoCategory);
		modelBCategories.add(modelBBodyCategory);
		modelBCategories.add(modelBEngineCategory);
		modelBCategories.add(modelBGearboxCategory);
		modelBCategories.add(modelBPaintCategory);
		modelBCategories.add(modelBSeatsCategory);
		modelBCategories.add(modelBSpoilerCategory);
		modelBCategories.add(modelBWheelsCategory);
		Model modelB = new Model("Model B", modelBCategories, 70);
		
		List<OptionCategory> modelCCategories = new ArrayList<>();
		modelCCategories.add(modelCAircoCategory);
		modelCCategories.add(modelCBodyCategory);
		modelCCategories.add(modelCEngineCategory);
		modelCCategories.add(modelCGearboxCategory);
		modelCCategories.add(modelCPaintCategory);
		modelCCategories.add(modelCSeatsCategory);
		modelCCategories.add(modelCSpoilerCategory);
		modelCCategories.add(modelCWheelsCategory);
		Model modelC = new Model("Model C", modelCCategories, 60);

		Model singleTaskModel = new Model("Single Task Order",
				new ArrayList<OptionCategory>(), 60);
		//----------------------------------------------------------------------
		
		//----------------------------------------------------------------------
		// Initialise ModelCatalog

		List<Model> normalOrderSessionModels = new ArrayList<>();
		normalOrderSessionModels.add(modelA);
		normalOrderSessionModels.add(modelB);
		normalOrderSessionModels.add(modelC);
		ModelCatalog modelCatalog = new ModelCatalog(normalOrderSessionModels,
				singleTaskModel);
		//----------------------------------------------------------------------

		//--------------------------------------------------------------------------
		// Initialise Restrictions

		List<Restriction> restrictions = new ArrayList<Restriction>();
		
		// Body is required
		//Reusing old list, not good, but it makes for less lines
		Set<Option> allBodyOptionsSet = new HashSet<Option>(modelBBodyList);
		restrictions.add(new RequiredOptionSetRestriction(allBodyOptionsSet));
		
		//Paint is required
		Set<Option> allPaintOptionsSet = new HashSet<Option>(modelAPaintList);
		allPaintOptionsSet.addAll(modelBPaintList);
		restrictions.add(new RequiredOptionSetRestriction(allPaintOptionsSet));
		
		//Engine is required
		Set<Option> allEngineOptionsSet = new HashSet<Option>(modelBEngineList);
		restrictions.add(new RequiredOptionSetRestriction(allEngineOptionsSet));
		
		//Gearbox is required
		Set<Option> allGearboxOptionsSet = new HashSet<Option>(modelAGearboxList);
		restrictions.add(new RequiredOptionSetRestriction(allGearboxOptionsSet));
		
		//Seats are required
		Set<Option> allSeatsOptionsSet = new HashSet<Option>(modelBSeatsList);
		restrictions.add(new RequiredOptionSetRestriction(allSeatsOptionsSet));
		
		//Wheels are required
		Set<Option> allWheelsOptionsSet = new HashSet<Option>(modelBWheelsList);
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
		//--------------------------------------------------------------------------
		
		//--------------------------------------------------------------------------
		// Initialise RestrictionManager
		OptionRestrictionManager restrictionsMan = new OptionRestrictionManager(restrictions);
		//--------------------------------------------------------------------------
		
		//--------------------------------------------------------------------------
		// Initialise Single Task Catalog
		
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
		//--------------------------------------------------------------------------

		////--------------------------------------------------------------------------
		// Initialise AlgorithmStrategyFactory
		
		AlgorithmStrategyFactory stratFact = new AlgorithmStrategyFactory();

		//--------------------------------------------------------------------------
		
		//--------------------------------------------------------------------------
		// Initialise CompletedOrderCatalog
		
		CompletedOrderCatalog  complCat = new CompletedOrderCatalog();

		//--------------------------------------------------------------------------
		// Initialise the ProductionSchedule
		ClockManager clockMan = new ClockManager();
		List<TaskType> types = new ArrayList<>();
		types.add(TaskType.BODY);
		types.add(TaskType.ACCESSORIES);
		SchedulerContext schedCont = new SchedulerContext(new FifoStrategy<StandardOrder>(), types);
		ProductionScheduleFacade schedFacade = new ProductionScheduleFacade(clockMan, schedCont);

		//--------------------------------------------------------------------------
		
		//--------------------------------------------------------------------------
		// Initialise the orderfactory
		
		OrderFactory orderFact = new OrderFactory();
		
		//--------------------------------------------------------------------------
		// Initialise Manufacturer

		Manufacturer manufacturer = new Manufacturer(
				stratFact,
				singleCatalog,
				complCat,
				modelCatalog,
				restrictionsMan,
				schedFacade,
				orderFact);
		
		//--------------------------------------------------------------------------
		// Initialise AssemblyLine
		
		AssemblyLine line = new AssemblyLine(manufacturer);
		StatisticsLogger logger = new StatisticsLogger();
		CarsProducedRegistrar prodRegistrar = new CarsProducedRegistrar();
		logger.addRegistrar(prodRegistrar);
		DelayRegistrar delayRegistrar = new DelayRegistrar();
		logger.addRegistrar(delayRegistrar);
		line.setStatisticsLogger(logger);
		
		SchedulerIntermediate inter = new SchedulerIntermediate(line);
		manufacturer.setSchedulerIntermediate(inter);
		inter.setManufacturer(manufacturer);
		
		clockMan.attachTimeObserver(logger);
		clockMan.attachTimeObserver(orderFact);
		clockMan.attachTimeObserver(complCat);
		clockMan.attachTimeObserver(inter);

		
		//--------------------------------------------------------------------------
		// Setters afterwards
		
		orderFact.setManufacturer(manufacturer);
		
		

		
		//--------------------------------------------------------------------------
		// Initialise Handlers
		
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
		//--------------------------------------------------------------------------
		
		//--------------------------------------------------------------------------
		// Initialise DomainFacade
		
		this.domainFacade = new DomainFacade(
				algorithmHandler,
				assemblyLineStatusHandler,
				orderDetailsHandler,
				prodStatHandler,
				newOrderHandler,
				singleTaskHandler,
				performHandler);
		//--------------------------------------------------------------------------

	}
	
	/** 
	 * Get the DomainFacade of the system initialised 
	 * by this InitialisationHandler.
	 * 
	 * @return The DomainFacade of the system initialised by this InitialisationHandler
	 */
	public DomainFacade getDomainFacade() {
		return this.domainFacade;
	}
	
	/** The domain facade that is accessible by the UI. */
	private final DomainFacade domainFacade;

}
