package domain.car;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.assembly_line.TaskType;
import domain.car.CarModel;
import domain.car.OptionCategory;

public class CarModelTest {
	
	CarModel model;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		model = new CarModel("John Doe", new ArrayList<OptionCategory>(), 60);
	}

	@Test
	public void getMinsOnWorkPostOfTypeTest() {
		assertEquals(60, model.getMinsOnWorkPostOfType(TaskType.BODY));
		assertEquals(60, model.getMinsOnWorkPostOfType(TaskType.DRIVETRAIN));
		assertEquals(60, model.getMinsOnWorkPostOfType(TaskType.ACCESSORIES));
		assertEquals(0, model.getMinsOnWorkPostOfType(TaskType.CARGO));
		assertEquals(0, model.getMinsOnWorkPostOfType(TaskType.CERTIFICATION));
	}

}
