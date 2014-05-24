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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.Manufacturer;
import domain.car.Model;
import domain.clock.EventActor;
import domain.clock.EventConsumer;
import domain.clock.TimeEvent;
import domain.order.Order;
import domain.production_schedule.OrderRequest;
import domain.production_schedule.SchedulerContext;

public class AssemblyLineControllerTest {
	
	@Rule public ExpectedException expected = ExpectedException.none();
	
	@Mock AssemblyLine line;
	@Mock Manufacturer manufacturer;
	@Mock SchedulerContext schedCon;
	@Mock AssemblyLineState state;
	@Mock EventConsumer consumer;
	@Mock DateTime argTime;
	@Mock Order order;
	@Mock Model model;
	@Mock TaskType type;
	
	AssemblyLineController controller;
	Optional<DateTime> deadLine;
	Optional<DateTime> deadLineFar;
	List<Model> models;
	List<TaskType> types;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		//FIXME
		controller = new AssemblyLineController();
		deadLine = Optional.of(new DateTime(1, 9, 0));
		deadLineFar = Optional.of(new DateTime(10, 6, 0));
		models = new ArrayList<Model>();
		models.add(model);
		types = new ArrayList<TaskType>();
		types.add(type);
	}

	@Test
	public void timeExceedsToday_true() {
		controller.setOverTime(360);
		assertTrue(controller.timeExceedsToday(new DateTime(0, 20, 0)));
	}
	
	@Test
	public void timeExceedsToday_false() {
		controller.setOverTime(0);
		assertFalse(controller.timeExceedsToday(new DateTime(0, 1, 0)));
	}
	
	@Test
	public void mustScheduleDeadline_true() {
		Mockito.when(argTime.getDays()).thenReturn(0);
		Mockito.when(order.getDeadline()).thenReturn(deadLine);
		
		assertTrue(controller.mustScheduleDeadline(argTime, order));
	}
	
	@Test
	public void mustScheduleDeadline_false() {
		Mockito.when(argTime.getDays()).thenReturn(0);
		Mockito.when(order.getDeadline()).thenReturn(deadLineFar);
		
		assertFalse(controller.mustScheduleDeadline(argTime, order));
	}
	
	@Test
	public void requestOrderTest() {
		Mockito.when(line.getAcceptedModels()).thenReturn(models);
		controller.requestStandardOrder();
		Mockito.verify(schedCon).getOrder(Mockito.any(OrderRequest.class));
	}
	
	@Test
	public void requestDeadlineOrder() {
		Mockito.when(line.getTaskTypes()).thenReturn(types);
		controller.requestDeadlineOrder();
		Mockito.verify(schedCon).getOrder(Mockito.any(OrderRequest.class));
	}
	
	@Test
	public void requestSingleTaskOrder_invalid() {
		Mockito.when(line.getTaskTypes()).thenReturn(new ArrayList<TaskType>(Arrays.asList(TaskType.BODY)));
		expected.expect(IllegalArgumentException.class);
		controller.requestSingleTaskOrder(TaskType.DRIVETRAIN);
	}
	
	@Test
	public void requestSingleTaskOrder_null() {
		expected.expect(IllegalArgumentException.class);
		controller.requestSingleTaskOrder(null);
	}
	
	@Test
	public void requestSingleTaskOrder_valid() {
		Mockito.when(line.getTaskTypes()).thenReturn(new ArrayList<TaskType>(Arrays.asList(TaskType.BODY)));
		controller.requestSingleTaskOrder(TaskType.BODY);
		Mockito.verify(schedCon).getOrder(Mockito.any(OrderRequest.class));
	}
	
	@Test
	public void scheduleEndDay_test() {
		AssemblyLineController spiedController = Mockito.spy(controller);
		spiedController.setOverTime(360);
		// new overtime is 0 minutes when calling this
		spiedController.scheduleEndDay(new DateTime(0, 16, 0));
		Mockito.verify(spiedController).setOverTime(0);
		DateTime timeTillNextDay = new DateTime(0, 14, 0);
		ArgumentCaptor<DateTime> captorTime = ArgumentCaptor.forClass(DateTime.class);
		ArgumentCaptor<EventActor> captorActor = ArgumentCaptor.forClass(EventActor.class);
		Mockito.verify(consumer).constructEvent(captorTime.capture(), captorActor.capture());
		assertEquals(timeTillNextDay, captorTime.getValue());
		assertEquals(spiedController, captorActor.getValue());	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void goToActive_test() {
		controller.goToActive(order);
		Mockito.verify(schedCon).detachOrderObserver(controller);
		Mockito.verify(consumer).register(controller);
		Mockito.verify(line).advance(Mockito.anyList());
	}
	
	@Test
	public void goToIdle_test() {
		controller.goToIdle();
		Mockito.verify(schedCon).attachOrderObserver(controller);
		Mockito.verify(consumer).unregister(controller);
	}

}
