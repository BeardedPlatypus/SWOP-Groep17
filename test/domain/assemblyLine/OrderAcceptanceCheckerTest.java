package domain.assemblyLine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import domain.DateTime;
import domain.car.CarModel;
import domain.car.Model;
import domain.car.OptionCategory;
import domain.car.Specification;
import domain.car.TruckModel;
import domain.order.StandardOrder;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
public class OrderAcceptanceCheckerTest {
	
	OrderAcceptanceChecker selector;
	
	CarModel carModel1;
	CarModel carModel2;
	TruckModel truckModel;
	
	@Mock Specification spec;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		carModel1 = new CarModel("Kneel", new ArrayList<OptionCategory>(), 60);
		carModel2 = new CarModel("before", new ArrayList<OptionCategory>(), 70);
		truckModel = new TruckModel("Zod", new ArrayList<OptionCategory>(), 60, 90, 45);
		
		selector = new OrderAcceptanceChecker(carModel1, truckModel);
	}

	@Test
	public void constructor_test() {
		OrderAcceptanceChecker selector = new OrderAcceptanceChecker(carModel1, truckModel);
		List<Model> models = Whitebox.getInternalState(selector, "allowedModels");
		assertTrue(models.contains(carModel1));
		assertTrue(models.contains(truckModel));
	}
	
	@Test
	public void accepts_true() {
		StandardOrder order = new StandardOrder(carModel1, spec, 0, new DateTime(0, 0, 0));
		assertTrue(selector.accepts(order));
		
		order = new StandardOrder(truckModel, spec, 0, new DateTime(0, 0, 0));
		assertTrue(selector.accepts(order));
	}
	
	@Test
	public void accepts_false() {
		StandardOrder order = new StandardOrder(carModel2, spec, 0, new DateTime(0, 0, 0));
		assertFalse(selector.accepts(order));
	}

}
