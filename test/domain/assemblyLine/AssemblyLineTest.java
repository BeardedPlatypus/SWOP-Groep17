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
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import domain.DateTime;
import domain.Manufacturer;
import domain.assemblyLine.AssemblyLine;
import domain.assemblyLine.AssemblyProcedure;
import domain.assemblyLine.AssemblyProcedureContainer;
import domain.assemblyLine.AssemblyTask;
import domain.assemblyLine.AssemblyTaskContainer;
import domain.assemblyLine.TaskType;
import domain.assemblyLine.WorkPost;
import domain.assemblyLine.WorkPostContainer;
import domain.assemblyLine.WorkPostObserver;
import domain.car.Option;
import domain.car.Specification;
import domain.order.Order;
import domain.order.OrderContainer;
import domain.statistics.ProcedureStatistics;
import domain.statistics.StatisticsLogger;

@RunWith(PowerMockRunner.class)
public class AssemblyLineTest {
	
	@Rule public ExpectedException expected = ExpectedException.none();
	
	@Mock Manufacturer manufacturer;
	
	AssemblyProcedure procedure1;
	AssemblyProcedure procedure2;
	AssemblyProcedure procedure3;
	
	AssemblyTask task1;
	AssemblyTask task12;
	AssemblyTask task2;
	AssemblyTask task3;
	
	Option option1;
	Option option12;
	Option option2;
	Option option3;
	
	Specification specGenProcedure;
	
	@Mock Order order;
	@Mock Order order2;
	@Mock Order order3;
	@Mock Order notOnAssemblyLine;
	
	@Mock Order newOrder;
	@Mock StatisticsLogger logger;
	
	List<WorkPost> workPosts;
	
	AssemblyLine assemblyLine;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		List<AssemblyProcedure> procedures = new ArrayList<AssemblyProcedure>();
		
		option1 = new Option(TaskType.BODY, "kaworu", "jesus");
		option12 = new Option(TaskType.BODY, "yui", "sama");
		option2 = new Option(TaskType.DRIVETRAIN, "whiny", "shinji");
		option3 = new Option(TaskType.ACCESSORIES, "moe", "rei");
		
		task1 = new AssemblyTask(option1, 0);
		task12 = new AssemblyTask(option12, 0);
		task2 = new AssemblyTask(option2, 0);
		task3 = new AssemblyTask(option3, 0);
		
		specGenProcedure = new Specification(new ArrayList<Option>(Arrays.asList(option1, option12, option2, option3)));
		
		Mockito.when(notOnAssemblyLine.getSpecifications()).thenReturn(specGenProcedure);
		
		procedure1 = new AssemblyProcedure(order, new ArrayList<AssemblyTask>(Arrays.asList(task1)), 180);
		procedure2 = new AssemblyProcedure(order2, new ArrayList<AssemblyTask>(Arrays.asList(task2)), 180);
		procedure3 = new AssemblyProcedure(order3, new ArrayList<AssemblyTask>(Arrays.asList(task3)), 180);
		
		procedures.add(procedure1);
		procedures.add(procedure2);
		procedures.add(procedure3);
		
		assemblyLine = new AssemblyLine(manufacturer);
		assemblyLine.setStatisticsLogger(logger);
		
		for (int i = 0; i < assemblyLine.getAssemblyLineSize(); i++)
		{
			assemblyLine.getWorkPost(i).setAssemblyProcedure(procedures.get(i));
		}
		
		workPosts = new ArrayList<WorkPost>();
		
		for (int i = 0; i < assemblyLine.getAssemblyLineSize(); i++) {
			workPosts.add(assemblyLine.getWorkPost(i));
		}
	}

	@Test
	public void constructor_NullManufacturer() {
		expected.expect(IllegalArgumentException.class);
		new AssemblyLine(null);
	}
	
	@Test
	public void constructor_CheckWorkpostsInitialised() {
		AssemblyLine assemblyLine = new AssemblyLine(manufacturer);
		AssemblyLine spiedAssemblyLine = PowerMockito.spy(assemblyLine);
		
		List<WorkPost> workPosts = new ArrayList<WorkPost>();
		for (int i = 0; i < spiedAssemblyLine.getAssemblyLineSize(); i++) {
			workPosts.add(PowerMockito.spy(spiedAssemblyLine.getWorkPost(i)));
		}
		
		int counter = 0;
		for (TaskType type : TaskType.values()) {
			assertEquals(workPosts.get(counter).getTaskType(), type);
			counter++;
		}
		assertEquals(manufacturer, Whitebox.getInternalState(spiedAssemblyLine, Manufacturer.class));
		List<WorkPostObserver> observers = (ArrayList<WorkPostObserver>) Whitebox.getInternalState(workPosts.get(0), "observers");
		assertEquals(assemblyLine, observers.get(0));
	}
	
	@Test
	public void makeAssemblyProcedureTest() {
		AssemblyProcedure generated = assemblyLine.makeAssemblyProcedure(notOnAssemblyLine);
		assertEquals(specGenProcedure, generated.getOrder().getSpecifications());
	}
	
	@Test
	public void getAssemblyOnEachWorkStation_test() {
		List<AssemblyProcedureContainer> assemblyLineProcedures = assemblyLine.getAssemblyOnEachWorkPost();
		assertTrue(assemblyLineProcedures.get(0) == procedure1);
		assertTrue(assemblyLineProcedures.get(1) == procedure2);
		assertTrue(assemblyLineProcedures.get(2) == procedure3);
	}
	
	@Test
	public void getAssemblyTasksAtPost_negativeWorkPostNumber() {
		expected.expect(IllegalArgumentException.class);
		assemblyLine.getAssemblyTasksAtPost(-1);
	}
	
	@Test
	public void getAssemblyTasksAtPost_tooBigWorkPostNumber() {
		expected.expect(IllegalArgumentException.class);
		assemblyLine.getAssemblyTasksAtPost(Integer.MAX_VALUE);
	}
	
	@Test
	public void getActiveOrderContainersTest() {
		List<OrderContainer> orders = assemblyLine.getActiveOrderContainers();
		assertTrue(orders.contains(order));
		assertTrue(orders.contains(order2));
		assertTrue(orders.contains(order3));
	}
	
	@Test
	public void getAssemblyTasksAtPost_valid() {
		List<AssemblyTaskContainer> containers = assemblyLine.getAssemblyTasksAtPost(0);
		assertTrue(containers.get(0) == task1);
	}
	
	@Test
	public void getWorkPostContainersTest() {
		List<WorkPostContainer> containers = assemblyLine.getWorkPostContainers();
		assertTrue(containers.contains(workPosts.get(0)));
		assertTrue(containers.contains(workPosts.get(1)));
		assertTrue(containers.contains(workPosts.get(2)));
	}
	
	@Test
	public void completeWorkPostTask_negativeWorkPostNum() {
		expected.expect(IllegalArgumentException.class);
		assemblyLine.completeWorkpostTask(-1, 0, 60);
	}
	
	@Test
	public void completeWorkPostTask_tooBigWorkPostNum() {
		expected.expect(IllegalArgumentException.class);
		assemblyLine.completeWorkpostTask(Integer.MAX_VALUE, 0, 60);
	}
	
	@Test
	public void getAssemblyLineSizeTest() {
		assertEquals(3, assemblyLine.getAssemblyLineSize());
	}
	
	@Test
	public void isEmpty_false() {
		assertFalse(assemblyLine.isEmpty());
	}
	
	@Test
	public void isEmpty_true() {
		assertTrue(new AssemblyLine(manufacturer).isEmpty());
	}
	
	@Test
	public void isValidWorkPost_tooBig() {
		assertFalse(assemblyLine.isValidWorkPost(Integer.MAX_VALUE));
	}
	
	@Test
	public void isValidWorkPost_negative() {
		assertFalse(assemblyLine.isValidWorkPost(-1));
	}
	
	@Test
	public void isValidWorkPost_valid() {
		assertTrue(assemblyLine.isValidWorkPost(0));
	}
	
	@Test
	public void getNbActiveWorkPostTest() {
		try {
			int workPosts = Whitebox.invokeMethod(assemblyLine, "getNbOfActiveWorkPosts");
			assertEquals(3, workPosts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void notifyWorkComplete_smallerCompletionTime() {
		Whitebox.setInternalState(assemblyLine, "elapsedTime", new DateTime(0, 0, 40));
		assemblyLine.notifyWorkComplete(20);
		assertEquals(new DateTime(0, 0, 40), Whitebox.getInternalState(assemblyLine, "elapsedTime"));
		assertEquals(1, Whitebox.getInternalState(assemblyLine, "finishedAssemblyCounter"));
	}
	
	@Test
	public void notifyWorkComplete_greaterCompletionTime() {
		Whitebox.setInternalState(assemblyLine, "elapsedTime", new DateTime(0, 0, 40));
		assemblyLine.notifyWorkComplete(60);
		assertEquals(new DateTime(0, 0, 60), Whitebox.getInternalState(assemblyLine, "elapsedTime"));
		assertEquals(1, Whitebox.getInternalState(assemblyLine, "finishedAssemblyCounter"));
	}
	
	@Test
	public void completeWorkPostTask_negativeTaskNum() {
		expected.expect(IllegalArgumentException.class);
		assemblyLine.completeWorkpostTask(0, -1, 60);
	}
	
	@Test
	public void completeWorkPostTask_tooBigTaskNum() {
		expected.expect(IllegalArgumentException.class);
		assemblyLine.completeWorkpostTask(0, Integer.MAX_VALUE, 60);
	}
	
	@Test
	public void completeWorkPostTask_negativeTimeElapsed() {
		expected.expect(IllegalArgumentException.class);
		assemblyLine.completeWorkpostTask(0, 0, -1);
	}
	
	@Test
	public void completeWorkPostTask_valid() {
		assemblyLine.completeWorkpostTask(0, 0, 60);
		assertTrue(task1.isCompleted());
		assertEquals(60, Whitebox.getInternalState(workPosts.get(0), "minutesOfWork"));
	}
	
	@Test
	public void completeWorkPostTask_observerMessage() {
		assemblyLine.completeWorkpostTask(0, 0, 60);
		assertTrue(workPosts.get(0).isFinished());
		assertEquals(new DateTime(0, 0, 60), Whitebox.getInternalState(assemblyLine, "elapsedTime"));
	}
	
	@Test
	public void completeWorkPostTask_simulateAdvance() {
		Mockito.when(manufacturer.popNextOrderFromSchedule()).thenReturn(newOrder);
		Mockito.when(newOrder.getMinutesPerPost()).thenReturn(60);
		Option newOption = new Option(TaskType.BODY, "har", "dar");
		Specification newSpec = new Specification(Arrays.asList(newOption));
		Mockito.when(newOrder.getSpecifications()).thenReturn(newSpec);
		
		assemblyLine.completeWorkpostTask(0, 0, 20);
		assemblyLine.completeWorkpostTask(1, 0, 60);
		assemblyLine.completeWorkpostTask(2, 0, 40);
		
		try {
			assertEquals(60, Whitebox.getInternalState(procedure1, "elapsedMinutes"));
			assertEquals(60, Whitebox.getInternalState(procedure2, "elapsedMinutes"));
			assertEquals(60, Whitebox.getInternalState(procedure3, "elapsedMinutes"));
			assertEquals(new DateTime(0, 0, 0), Whitebox.getInternalState(assemblyLine, "elapsedTime"));
			assertEquals(0, Whitebox.getInternalState(workPosts.get(0), "minutesOfWork"));
			assertEquals(0, Whitebox.getInternalState(workPosts.get(1), "minutesOfWork"));
			assertEquals(0, Whitebox.getInternalState(workPosts.get(2), "minutesOfWork"));
			assertEquals(procedure1, workPosts.get(1).getAssemblyProcedure());
			assertEquals(procedure2, workPosts.get(2).getAssemblyProcedure());
			assertEquals(newOrder, workPosts.get(0).getAssemblyProcedure().getOrder());
			assertEquals(-120, procedure3.makeStatisticsEvent().getDelay());
			assertEquals(2, Whitebox.getInternalState(assemblyLine, "finishedAssemblyCounter"));
			Mockito.verify(logger).addStatistics(Matchers.isA(ProcedureStatistics.class));
			Mockito.verify(manufacturer).addToCompleteOrders(order3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void containsTest() {
		assertTrue(assemblyLine.contains(order3));
	}
	
	@Test
	public void containsTest_false() {
		assertFalse(assemblyLine.contains(notOnAssemblyLine));
	}
	
	@Test
	public void setLogger_null() {
		AssemblyLine line = new AssemblyLine(manufacturer);
		expected.expect(IllegalArgumentException.class);
		line.setStatisticsLogger(null);
	}
	
	@Test
	public void setLogger_valid() {
		AssemblyLine line = new AssemblyLine(manufacturer);
		line.setStatisticsLogger(logger);
		assertEquals(logger, Whitebox.getInternalState(line, StatisticsLogger.class));
	}

}
