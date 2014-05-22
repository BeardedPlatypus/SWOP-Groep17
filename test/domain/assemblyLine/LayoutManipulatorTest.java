package domain.assemblyLine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.car.CarModel;
import domain.car.Model;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.car.Specification;
import domain.order.Order;
import domain.order.SingleTaskOrder;
import domain.order.StandardOrder;

public class LayoutManipulatorTest {
	
	@Rule public ExpectedException expected = ExpectedException.none();
	
	LayoutManipulator man;
	
	@Mock AssemblyLine assemblyLine;
	@Mock Order procOrder;
	AssemblyLineState state;
	List<WorkPost> workPosts;
	
	Option bodyOption;
	Option cargoOption;
	Option driveOption;
	Option accOption;
	Option certOption;
	
	AssemblyTask bodyTask;
	AssemblyTask cargoTask;
	AssemblyTask driveTask;
	AssemblyTask accTask;
	AssemblyTask certTask;
	
	Model model = new CarModel("Batmobile", new ArrayList<OptionCategory>(), 60);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(assemblyLine.getElapsedTime()).thenReturn(new DateTime(0, 0, 60));
		
		bodyOption = new Option(TaskType.BODY, "Body", "Fuck the Kingsguard");
		cargoOption = new Option(TaskType.CARGO, "Cargo", "Fuck the city");
		driveOption = new Option(TaskType.DRIVETRAIN, "Drivetrain", "Fuck the king");
		accOption = new Option(TaskType.ACCESSORIES, "Accessories", "What's in the box?!");
		certOption = new Option(TaskType.CERTIFICATION, "Certification", "My name is Inigo Montoya");
		
		bodyTask = new AssemblyTask(bodyOption, 0);
		cargoTask = new AssemblyTask(cargoOption, 1);
		driveTask = new AssemblyTask(driveOption, 2);
		accTask = new AssemblyTask(accOption, 3);
		certTask = new AssemblyTask(certOption, 4);
		
		workPosts = new ArrayList<WorkPost>();
		workPosts.add(new WorkPost(TaskType.BODY, 0));
		workPosts.add(new WorkPost(TaskType.CARGO, 1));
		workPosts.add(new WorkPost(TaskType.DRIVETRAIN, 2));
		workPosts.add(new WorkPost(TaskType.ACCESSORIES, 3));
		workPosts.add(new WorkPost(TaskType.CERTIFICATION, 4));
		
		Mockito.when(assemblyLine.getWorkPost(0)).thenReturn(workPosts.get(0));
		Mockito.when(assemblyLine.getWorkPost(1)).thenReturn(workPosts.get(1));
		Mockito.when(assemblyLine.getWorkPost(2)).thenReturn(workPosts.get(2));
		Mockito.when(assemblyLine.getWorkPost(3)).thenReturn(workPosts.get(3));
		Mockito.when(assemblyLine.getWorkPost(4)).thenReturn(workPosts.get(4));
		
		Mockito.when(assemblyLine.getFirstWorkPost()).thenReturn(workPosts.get(0));
		Mockito.when(assemblyLine.getLastWorkPost()).thenReturn(workPosts.get(4));
		
		Mockito.when(assemblyLine.getAssemblyLineSize()).thenReturn(workPosts.size());
		
		state = new ActiveState();
		state.setAssemblyLine(assemblyLine);
		man = new LayoutManipulator(state);
	}

	@Test
	public void fullAssemblyLineAdvanceTest() {
		List<AssemblyTask> proc0Tasks = new ArrayList<AssemblyTask>();
		proc0Tasks.add(this.cloneTask(bodyTask));
		proc0Tasks.add(this.cloneTask(cargoTask));
		proc0Tasks.add(this.cloneTask(driveTask));
		proc0Tasks.add(this.cloneTask(accTask));
		proc0Tasks.add(this.cloneTask(certTask));
		proc0Tasks.get(0).setCompleted(true);

		List<AssemblyTask> proc1Tasks = new ArrayList<AssemblyTask>();
		proc1Tasks.add(this.cloneTask(cargoTask));
		proc1Tasks.add(this.cloneTask(driveTask));
		proc1Tasks.add(this.cloneTask(accTask));
		proc1Tasks.add(this.cloneTask(certTask));
		proc1Tasks.get(0).setCompleted(true);
		
		List<AssemblyTask> proc2Tasks = new ArrayList<AssemblyTask>();
		proc2Tasks.add(this.cloneTask(driveTask));
		proc2Tasks.add(this.cloneTask(accTask));
		proc2Tasks.add(this.cloneTask(certTask));
		proc2Tasks.get(0).setCompleted(true);
		
		List<AssemblyTask> proc3Tasks = new ArrayList<AssemblyTask>();
		proc3Tasks.add(this.cloneTask(accTask));
		proc3Tasks.add(this.cloneTask(certTask));
		proc3Tasks.get(0).setCompleted(true);
		
		List<AssemblyTask> proc4Tasks = new ArrayList<AssemblyTask>();
		proc4Tasks.add(this.cloneTask(certTask));
		proc4Tasks.get(0).setCompleted(true);
		
		
		AssemblyProcedure proc0 = new AssemblyProcedure(procOrder, proc0Tasks, 60);
		AssemblyProcedure proc1 = new AssemblyProcedure(procOrder, proc1Tasks, 60);
		AssemblyProcedure proc2 = new AssemblyProcedure(procOrder, proc2Tasks, 60);
		AssemblyProcedure proc3 = new AssemblyProcedure(procOrder, proc3Tasks, 60);
		AssemblyProcedure proc4 = new AssemblyProcedure(procOrder, proc4Tasks, 60);
		
		workPosts.get(0).setAssemblyProcedure(Optional.fromNullable(proc0));
		workPosts.get(1).setAssemblyProcedure(Optional.fromNullable(proc1));
		workPosts.get(2).setAssemblyProcedure(Optional.fromNullable(proc2));
		workPosts.get(3).setAssemblyProcedure(Optional.fromNullable(proc3));
		workPosts.get(4).setAssemblyProcedure(Optional.fromNullable(proc4));
		
		man.advanceAssemblyLine(new ArrayList<Order>());
		assertEquals(Optional.absent(), workPosts.get(0).getAssemblyProcedure());
		assertEquals(proc0, workPosts.get(1).getAssemblyProcedure().get());
		assertEquals(proc1, workPosts.get(2).getAssemblyProcedure().get());
		assertEquals(proc2, workPosts.get(3).getAssemblyProcedure().get());
		assertEquals(proc3, workPosts.get(4).getAssemblyProcedure().get());
	}
	
	@Test
	public void AssemblyLineAdvanceWithNewOrder() {
		Order order = new StandardOrder(model, new Specification(bodyOption), 0, new DateTime(0, 6, 0));
		AssemblyProcedure newProc = new AssemblyProcedure(order, new ArrayList<AssemblyTask>(Arrays.asList(new AssemblyTask(bodyOption, 0))), 60);
		
		AssemblyTask task0 = new AssemblyTask(bodyOption, 0);
		AssemblyTask task1 = new AssemblyTask(cargoOption, 1);
		AssemblyProcedure proc0 = new AssemblyProcedure(procOrder, new ArrayList<AssemblyTask>(Arrays.asList(task0, task1)), 60);
		workPosts.get(0).setAssemblyProcedure(Optional.fromNullable(proc0));
		task0.setCompleted(true);
		
//		Mockito.when(assemblyLine.popNextOrderFromSchedule()).thenReturn(Optional.fromNullable(order));
		Mockito.when(assemblyLine.makeAssemblyProcedure(Optional.fromNullable(order))).thenReturn(newProc);
		
		man.advanceAssemblyLine(new ArrayList<Order>(Arrays.asList(order)));
		assertEquals(newProc, workPosts.get(0).getAssemblyProcedure().get());
		assertEquals(proc0, workPosts.get(1).getAssemblyProcedure().get());
	}
	
	@Test
	public void AssemblyLineAdvanceWithSkips() {
		AssemblyTask proc0Task0 = this.cloneTask(bodyTask);
		AssemblyTask proc0Task1 = this.cloneTask(accTask);
		proc0Task0.setCompleted(true);
		AssemblyProcedure proc0 = new AssemblyProcedure(procOrder, new ArrayList<AssemblyTask>(Arrays.asList(proc0Task0, proc0Task1)), 60);
		
		AssemblyTask proc1Task0 = this.cloneTask(accTask);
		AssemblyTask proc1Task1 = this.cloneTask(certTask);
		proc1Task0.setCompleted(true);
		AssemblyProcedure proc1 = new AssemblyProcedure(procOrder, new ArrayList<AssemblyTask>(Arrays.asList(proc1Task1, proc1Task1)), 60);
		
		workPosts.get(0).setAssemblyProcedure(Optional.fromNullable(proc0));
		workPosts.get(3).setAssemblyProcedure(Optional.fromNullable(proc1));
		
		man.advanceAssemblyLine(new ArrayList<Order>());
		
		assertEquals(proc0, workPosts.get(3).getAssemblyProcedure().get());
		assertEquals(proc1, workPosts.get(4).getAssemblyProcedure().get());
	}
	
	@Test
	public void AssemblyLineAdvance_rollProcedureOffLine() {
		AssemblyTask proc0Task0 = this.cloneTask(accTask);
		proc0Task0.setCompleted(true);
		AssemblyProcedure proc0 = new AssemblyProcedure(procOrder, new ArrayList<AssemblyTask>(Arrays.asList(proc0Task0)), 60);
		
		workPosts.get(3).setAssemblyProcedure(Optional.fromNullable(proc0));
		
		man.advanceAssemblyLine(new ArrayList<Order>());
		
		assertEquals(Optional.absent(), workPosts.get(3).getAssemblyProcedure());
		assertEquals(Optional.absent(), workPosts.get(4).getAssemblyProcedure());
		Mockito.verify(assemblyLine).handleFinishedAssemblyProcedure(Optional.fromNullable(proc0));
	}

	@Test
	public void AssemblyLineAdvance_singleTaskOrder() {
		Order order = new SingleTaskOrder(model, new Specification(accOption), 0, new DateTime(0, 6, 0), new DateTime(1, 6, 0));
//		Mockito.when(assemblyLine.popNextOrderFromSchedule()).thenReturn(Optional.fromNullable(order));
		
		AssemblyProcedure proc = new AssemblyProcedure(order, new ArrayList<AssemblyTask>(Arrays.asList(new AssemblyTask(accOption, 0))), 60);
		Mockito.when(assemblyLine.makeAssemblyProcedure(Optional.fromNullable(order))).thenReturn(proc);
		
		man.advanceAssemblyLine(new ArrayList<Order>(Arrays.asList(order)));
		assertEquals(proc, workPosts.get(3).getAssemblyProcedure().get());
	}
	
	@Test
	public void advanceAssemblyLine_nullList() {
		expected.expect(IllegalArgumentException.class);
		man.advanceAssemblyLine(null);
	}
	
	@Test
	public void advanceAssemblyLine_nullInList() {
		expected.expect(IllegalArgumentException.class);
		Order order = null;
		man.advanceAssemblyLine(new ArrayList<Order>(Arrays.asList(order)));
	}
	
	public AssemblyTask cloneTask(AssemblyTask task) {
		return new AssemblyTask(task.getOption(), task.getTaskNumber());
	}
}
