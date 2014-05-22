package domain.assemblyLine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.car.CarModel;
import domain.car.OptionCategory;
import domain.car.TruckModel;

public class LayoutFactoryTest {
	
	LayoutFactory layoutFactory;
	
	CarModel carModel1;
	CarModel carModel2;
	TruckModel truckModel;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		layoutFactory = new LayoutFactory();
		
		carModel1 = new CarModel("Fuck the Kingsguard", new ArrayList<OptionCategory>(), 60);
		carModel2 = new CarModel("Fuck the city", new ArrayList<OptionCategory>(), 70);
		truckModel = new TruckModel("Fuck the king", new ArrayList<OptionCategory>(), 60, 90, 45);
	}

	@Test
	public void carLayoutTest() {
		List<WorkPost> workPosts = layoutFactory.makeLayout(carModel1);
		assertEquals(3, workPosts.size());
		assertEquals(TaskType.BODY, workPosts.get(0).getTaskType());
		assertEquals(TaskType.DRIVETRAIN, workPosts.get(1).getTaskType());
		assertEquals(TaskType.ACCESSORIES, workPosts.get(2).getTaskType());
		assertEquals(0, workPosts.get(0).getWorkPostNum());
		assertEquals(1, workPosts.get(1).getWorkPostNum());
		assertEquals(2, workPosts.get(2).getWorkPostNum());
	}
	
	@Test
	public void truckLayoutTest() {
		List<WorkPost> workPosts = layoutFactory.makeLayout(truckModel);
		assertEquals(5, workPosts.size());
		assertEquals(TaskType.BODY, workPosts.get(0).getTaskType());
		assertEquals(TaskType.CARGO, workPosts.get(1).getTaskType());
		assertEquals(TaskType.DRIVETRAIN, workPosts.get(2).getTaskType());
		assertEquals(TaskType.ACCESSORIES, workPosts.get(3).getTaskType());
		assertEquals(TaskType.CERTIFICATION, workPosts.get(4).getTaskType());
		assertEquals(0, workPosts.get(0).getWorkPostNum());
		assertEquals(1, workPosts.get(1).getWorkPostNum());
		assertEquals(2, workPosts.get(2).getWorkPostNum());
		assertEquals(3, workPosts.get(3).getWorkPostNum());
		assertEquals(4, workPosts.get(4).getWorkPostNum());
	}
	
	@Test
	public void mixedModelsTest() {
		List<WorkPost> workPosts = layoutFactory.makeLayout(carModel1, carModel2, truckModel);
		assertEquals(5, workPosts.size());
		assertEquals(TaskType.BODY, workPosts.get(0).getTaskType());
		assertEquals(TaskType.CARGO, workPosts.get(1).getTaskType());
		assertEquals(TaskType.DRIVETRAIN, workPosts.get(2).getTaskType());
		assertEquals(TaskType.ACCESSORIES, workPosts.get(3).getTaskType());
		assertEquals(TaskType.CERTIFICATION, workPosts.get(4).getTaskType());
		assertEquals(0, workPosts.get(0).getWorkPostNum());
		assertEquals(1, workPosts.get(1).getWorkPostNum());
		assertEquals(2, workPosts.get(2).getWorkPostNum());
		assertEquals(3, workPosts.get(3).getWorkPostNum());
		assertEquals(4, workPosts.get(4).getWorkPostNum());
	}

}
