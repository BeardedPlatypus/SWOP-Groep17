package domain.assemblyLine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.Manufacturer;
import domain.car.CarModel;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.car.Specification;
import domain.order.Order;
import domain.order.StandardOrder;
import domain.car.Model;

public class OperationalStateTest {

	AssemblyLine line;
	@Mock Order mockOrder;
	@Mock Manufacturer manufacturer;
	@Mock SchedulerIntermediate sched;
	List<WorkPost> workPosts;
	
	AssemblyTask task0;
	AssemblyTask task1;
	AssemblyTask task2;
	
	AssemblyProcedure proc0;
	AssemblyProcedure proc1;
	AssemblyProcedure proc2;
	Model model = new CarModel("Batmobile", new ArrayList<OptionCategory>(), 60);
	Option bodyOption = new Option(TaskType.BODY, "john", "doe");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		workPosts = new ArrayList<WorkPost>();
		workPosts.add(new WorkPost(TaskType.BODY, 0));
		workPosts.add(new WorkPost(TaskType.DRIVETRAIN, 1));
		workPosts.add(new WorkPost(TaskType.ACCESSORIES, 2));
		
		line = new AssemblyLine(workPosts, new OrderAcceptanceChecker(new ArrayList<Model>()), sched);
		
		task0 = new AssemblyTask(new Option(TaskType.BODY, "john", "doe"), 0);
		task1 = new AssemblyTask(new Option(TaskType.DRIVETRAIN, "jane", "doe"), 0);
		task2 = new AssemblyTask(new Option(TaskType.ACCESSORIES, "adam", "smith"), 0);
		
		proc0 = new AssemblyProcedure(mockOrder, new ArrayList<AssemblyTask>(Arrays.asList(task0)), 60);
		proc1 = new AssemblyProcedure(mockOrder, new ArrayList<AssemblyTask>(Arrays.asList(task1)), 60);
		proc2 = new AssemblyProcedure(mockOrder, new ArrayList<AssemblyTask>(Arrays.asList(task2)), 60);
	}

	@Test
	public void setState_idle() {
		line.setCurrentState(new OperationalState());
		assertEquals(new IdleState(), line.getCurrentState());
	}
	
	@Test
	public void setState_active() {
		workPosts.get(0).setAssemblyProcedure(Optional.fromNullable(proc0));
		line.setCurrentState(new OperationalState());
		assertEquals(new ActiveState(), line.getCurrentState());
	}

}
