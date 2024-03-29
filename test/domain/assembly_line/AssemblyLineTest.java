package domain.assembly_line;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.Manufacturer;
import domain.assembly_line.AssemblyLine;
import domain.assembly_line.AssemblyProcedure;
import domain.assembly_line.AssemblyTask;
import domain.assembly_line.AssemblyTaskView;
import domain.assembly_line.IdleState;
import domain.assembly_line.OperationalState;
import domain.assembly_line.TaskType;
import domain.assembly_line.WorkPost;
import domain.assembly_line.WorkPostObserver;
import domain.assembly_line.WorkPostView;
import domain.car.Model;
import domain.car.Option;
import domain.car.Specification;
import domain.clock.Clock;
import domain.order.CompletedOrderCatalog;
import domain.order.CompletedOrderEvent;
import domain.order.Order;
import domain.order.OrderView;
import domain.statistics.StatisticsLogger;
import exceptions.OrdersNotEmptyWhenAdvanceException;

@RunWith(PowerMockRunner.class)
public class AssemblyLineTest {
	
	@Rule public ExpectedException expected = ExpectedException.none();
	
	@Mock Manufacturer manufacturer;
	//@Mock SchedulerIntermediate sched;
	
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
	@Mock CompletedOrderCatalog cat;
	
	@Mock Model model;
	List<Model> modelList;
	@Mock Clock mockDeKlokMock;
	
	List<WorkPost> workPosts;
	
	AssemblyLine assemblyLine;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		modelList = new ArrayList<Model>();
		modelList.add(model);
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
		procedures.add(null);
		procedures.add(procedure2);
		procedures.add(procedure3);
		procedures.add(null);
		
		workPosts = new ArrayList<WorkPost>();
		workPosts.add(new WorkPost(TaskType.BODY, 0));
		workPosts.add(new WorkPost(TaskType.CARGO, 1));
		workPosts.add(new WorkPost(TaskType.DRIVETRAIN, 2));
		workPosts.add(new WorkPost(TaskType.ACCESSORIES, 3));
		workPosts.add(new WorkPost(TaskType.CERTIFICATION, 4));
		
		//Optional<Order> absentOrder = Optional.absent();
		//Mockito.when(sched.popNextOrderFromSchedule()).thenReturn(absentOrder);
		
		assemblyLine = new AssemblyLine(workPosts, modelList, mockDeKlokMock);
		assemblyLine.attachObserver(cat);
		assemblyLine.attachObserver(logger);
		
		for (int i = 0; i < assemblyLine.getAssemblyLineSize(); i++)
		{
			WorkPost wp = (WorkPost) assemblyLine.getWorkPostViews().get(i);
			Whitebox.invokeMethod(wp, "setAssemblyProcedure", Optional.fromNullable(procedures.get(i)));
		}
		
		assemblyLine.setCurrentState(new OperationalState());
	}
	
	@Test
	public void constructor_nullWorkPosts() {
		expected.expect(IllegalArgumentException.class);
		new AssemblyLine(null, modelList, mockDeKlokMock);
	}
	
	@Test
	public void constructor_emptyWorkPosts() {
		expected.expect(IllegalArgumentException.class);
		new AssemblyLine(new ArrayList<WorkPost>(), modelList, mockDeKlokMock);
	}
	
	@Test
	public void constructor_nullModels() {
		expected.expect(IllegalArgumentException.class);
		new AssemblyLine(workPosts, null, mockDeKlokMock);
	}
	
	@Test
	public void constructor_emptyModels() {
		expected.expect(IllegalArgumentException.class);
		new AssemblyLine(workPosts, new ArrayList<Model>(), mockDeKlokMock);
	}
	
//	@Test
//	public void constructor_nullSchedulerIntermediate() {
//		expected.expect(IllegalArgumentException.class);
//		new AssemblyLine(workPosts, orderSelector);
//	}
	
	@Test
	public void constructor_idleState() {
		List<WorkPost> workPosts = new ArrayList<WorkPost>();
		workPosts.add(new WorkPost(TaskType.BODY, 0));
		AssemblyLine line = new AssemblyLine(workPosts, modelList, mockDeKlokMock);
		assertEquals(IdleState.class, line.getCurrentState().getClass());
	}
	
	@Test
	public void constructor_CheckWorkpostsInitialised() {
		AssemblyLine assemblyLine = new AssemblyLine(workPosts, modelList, mockDeKlokMock);
		AssemblyLine spiedAssemblyLine = PowerMockito.spy(assemblyLine);
		
		List<WorkPost> workPosts = new ArrayList<WorkPost>();
		for (int i = 0; i < spiedAssemblyLine.getAssemblyLineSize(); i++) {
			workPosts.add((WorkPost) PowerMockito.spy(spiedAssemblyLine.getWorkPostViews().get(i)));
		}
		
		@SuppressWarnings("unchecked")
		List<WorkPostObserver> observers = (ArrayList<WorkPostObserver>) Whitebox.getInternalState(workPosts.get(0), "observers");
		assertEquals(assemblyLine, observers.get(1));
		assertTrue(assemblyLine.getAcceptedModels().contains(model));
	}
	
	@Test
	public void makeAssemblyProcedureTest() {
		AssemblyProcedure generated = assemblyLine.makeAssemblyProcedure(Optional.fromNullable(notOnAssemblyLine));
		assertEquals(specGenProcedure, generated.getOrder().getSpecifications());
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
		List<OrderView> orders = assemblyLine.getActiveOrderContainers();
		assertTrue(orders.contains(order));
		assertTrue(orders.contains(order2));
		assertTrue(orders.contains(order3));
	}
	
	@Test
	public void getAssemblyTasksAtPost_valid() {
		List<AssemblyTaskView> containers = assemblyLine.getAssemblyTasksAtPost(0);
		assertTrue(containers.get(0) == task1);
	}
	
	@Test
	public void getWorkPostContainersTest() {
		List<WorkPostView> containers = assemblyLine.getWorkPostViews();
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
		assertEquals(5, assemblyLine.getAssemblyLineSize());
	}
	
	@Test
	public void isEmpty_false() {
		assertFalse(assemblyLine.isEmpty());
	}
	
	@Test
	public void isEmpty_true() {
		List<WorkPost> workPosts = new ArrayList<WorkPost>();
		workPosts.add(new WorkPost(TaskType.BODY, 0));
		assertTrue(new AssemblyLine(workPosts, modelList, mockDeKlokMock).isEmpty());
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
//		Mockito.when(newOrder.getMinutesPerPost()).thenReturn(60);
		Option newOption = new Option(TaskType.DRIVETRAIN, "har", "dar");
		Specification newSpec = new Specification(Arrays.asList(newOption));
		Mockito.when(newOrder.getSpecifications()).thenReturn(newSpec);
		
		assemblyLine.completeWorkpostTask(0, 0, 20);
		assemblyLine.completeWorkpostTask(2, 0, 60);
		assemblyLine.completeWorkpostTask(3, 0, 40);
		
		assertTrue(assemblyLine.canAdvance());
		assemblyLine.advance(new ArrayList<Order>(Arrays.asList(newOrder)));
		
		try {
			assertEquals(60, Whitebox.getInternalState(procedure1, "elapsedMinutes"));
			assertEquals(60, Whitebox.getInternalState(procedure2, "elapsedMinutes"));
			assertEquals(60, Whitebox.getInternalState(procedure3, "elapsedMinutes"));
			assertEquals(new DateTime(0, 0, 0), Whitebox.getInternalState(assemblyLine, "elapsedTime"));
			assertEquals(0, Whitebox.getInternalState(workPosts.get(0), "minutesOfWork"));
			assertEquals(0, Whitebox.getInternalState(workPosts.get(1), "minutesOfWork"));
			assertEquals(0, Whitebox.getInternalState(workPosts.get(2), "minutesOfWork"));
	
			assertTrue(workPosts.get(2).getAssemblyProcedure().isPresent());
			assertEquals(Optional.absent(), workPosts.get(0).getAssemblyProcedure());
			assertEquals(Optional.absent(), workPosts.get(1).getAssemblyProcedure());
			assertEquals(Optional.absent(), workPosts.get(3).getAssemblyProcedure());
			assertEquals(Optional.absent(), workPosts.get(4).getAssemblyProcedure());
			
			assertEquals(newOrder, workPosts.get(2).getAssemblyProcedure().get().getOrder());
			assertEquals(-120, procedure3.makeStatisticsEvent().getDelay());
			assertEquals(0, Whitebox.getInternalState(assemblyLine, "finishedAssemblyCounter"));
			Mockito.verify(logger, Mockito.times(3)).updateCompletedOrder(Matchers.isA(CompletedOrderEvent.class));
			Mockito.verify(cat, Mockito.times(3)).updateCompletedOrder(Matchers.isA(CompletedOrderEvent.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void completeTask_ordersNotEmptyWhenAdvance() {
		Option newOption = new Option(TaskType.BODY, "har", "dar");
		Specification newSpec = new Specification(Arrays.asList(newOption));
		Mockito.when(newOrder.getSpecifications()).thenReturn(newSpec);
		
		assemblyLine.completeWorkpostTask(0, 0, 20);
		assemblyLine.completeWorkpostTask(2, 0, 60);
		assemblyLine.completeWorkpostTask(3, 0, 40);
		
		assertTrue(assemblyLine.canAdvance());
		expected.expect(OrdersNotEmptyWhenAdvanceException.class);
		assemblyLine.advance(new ArrayList<Order>(Arrays.asList(newOrder, newOrder)));
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
	public void handleFinishedAssemblyProcedureTest() {
		assemblyLine.getCurrentState().completeWorkpostTask(0, 0, 0);
		procedure1.addToElapsedMinutes(190);
		assemblyLine.handleFinishedAssemblyProcedure(Optional.fromNullable(procedure1));
		ArgumentCaptor<CompletedOrderEvent> arg = ArgumentCaptor.forClass(CompletedOrderEvent.class);
		Mockito.verify(cat).updateCompletedOrder(arg.capture());
		assertEquals(order, arg.getValue().getCompletedOrder());
		assertEquals(10, arg.getValue().getProcedureStatistics().getDelay());
	}
	

}
