package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.assembly_line.TaskType;
import domain.car.OptionCategory;
import domain.car.TruckModel;

public class TruckModelTest {

	TruckModel model;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		model = new TruckModel("Jane Doe", new ArrayList<OptionCategory>(), 60, 90,
				45);
	}

	@Test
	public void getMinsOnWorkPostOfType() {
		assertEquals(90, model.getMinsOnWorkPostOfType(TaskType.BODY));
		assertEquals(60, model.getMinsOnWorkPostOfType(TaskType.CARGO));
		assertEquals(60, model.getMinsOnWorkPostOfType(TaskType.DRIVETRAIN));
		assertEquals(60, model.getMinsOnWorkPostOfType(TaskType.ACCESSORIES));
		assertEquals(45, model.getMinsOnWorkPostOfType(TaskType.CERTIFICATION));
	}

}
