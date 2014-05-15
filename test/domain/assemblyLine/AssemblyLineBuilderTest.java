package domain.assemblyLine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.powermock.modules.junit4.PowerMockRunner;

import domain.Manufacturer;
import domain.car.CarModel;
import domain.car.Model;
import domain.car.OptionCategory;
import domain.car.TruckModel;

@RunWith(PowerMockRunner.class)
public class AssemblyLineBuilderTest {
	
	@Rule ExpectedException expected = ExpectedException.none();
	
	@Mock Manufacturer manufacturer;
	AssemblyLineBuilder builder;
	
	CarModel carModel1;
	CarModel carModel2;
	TruckModel truckModel;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		carModel1 = new CarModel("What do we say", new ArrayList<OptionCategory>(), 60);
		carModel2 = new CarModel("to the God of Death?", new ArrayList<OptionCategory>(), 70);
		truckModel = new TruckModel("Not today!", new ArrayList<OptionCategory>(), 60, 90, 45);
		
		builder = new AssemblyLineBuilder(manufacturer);
	}

	@Test
	public void constructor_nullManufacturer() {
		expected.expect(IllegalArgumentException.class);
		AssemblyLineBuilder builder = new AssemblyLineBuilder(null);
	}
	
	@Test
	public void constructor_valid() {
		AssemblyLineBuilder builder = new AssemblyLineBuilder(manufacturer);
		assertEquals(manufacturer, Whitebox.getInternalState(builder, Manufacturer.class));
	}
	
	@Test
	public void addModelTest() {
		builder.addToDesiredModels(carModel1);
		assertTrue(((ArrayList<Model>) Whitebox.getInternalState(builder, "desiredModels")).contains(carModel1));
	}
	
	@Test
	public void addModelTest_null() {
		expected.expect(IllegalArgumentException.class);
		builder.addToDesiredModels(null);
	}
	
	@Test
	public void addModelTest_duplicate() {
		builder.addToDesiredModels(carModel1);
		expected.expect(IllegalArgumentException.class);
		builder.addToDesiredModels(carModel1);
	}
	
	@Test
	public void buildAssemblyLineTest_noModels() {
		expected.expect(IllegalStateException.class);
		builder.buildAssemblyLine();
	}

	@Test
	public void buildAssemblyLineTest() {
		builder.addToDesiredModels(carModel1);
		builder.addToDesiredModels(truckModel);
		
		AssemblyLine line = builder.buildAssemblyLine();
		assertEquals(manufacturer, Whitebox.getInternalState(line, Manufacturer.class));
		
		OrderSelector selector = line.getOrderSelector();
		List<Model> allowedModels = Whitebox.getInternalState(selector, "allowedModels");
		assertTrue(allowedModels.contains(carModel1));
		assertTrue(allowedModels.contains(truckModel));
		assertFalse(allowedModels.contains(carModel2));
		
		List<WorkPost> workPosts = Whitebox.getInternalState(line, "workPosts");
		List<TaskType> types = new ArrayList<TaskType>();
		for (WorkPost workPost : workPosts) {
			types.add(workPost.getTaskType());
		}
		for (TaskType type : TaskType.values()) {
			assertTrue(types.contains(type));
		}
	}
}
