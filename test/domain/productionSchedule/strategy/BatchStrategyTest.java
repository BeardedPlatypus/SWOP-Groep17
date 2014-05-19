package domain.productionSchedule.strategy;

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
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import domain.DateTime;
import domain.assemblyLine.TaskType;
import domain.car.Option;
import domain.car.Specification;
import domain.car.Model;
import domain.order.Order;
import domain.order.StandardOrder;

public class BatchStrategyTest {
	
	@Rule public ExpectedException expected = ExpectedException.none();
	
	BatchStrategy<Order> strat;
	Specification spec;
	Specification otherSpec;
	
	Order order1;
	Order order2;
	Order order3;
	Order order4;
	
	Order newOrder1;
	Order newOrder2;
	
	List<Order> orderQueue;
	
	@Mock Model model;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Option option1 = new Option(TaskType.BODY, "john", "doe");
		Option option2 = new Option(TaskType.DRIVETRAIN, "jane", "doe");
		Option option3 = new Option(TaskType.ACCESSORIES, "adam", "smith");
		
		List<Option> options = new ArrayList<Option>(Arrays.asList(option1, option2, option3));
		List<Option> otherOptions = new ArrayList<Option>(Arrays.asList(option1, option3));
		spec = new Specification(options);
		otherSpec = new Specification(otherOptions);
		
		order1 = new StandardOrder(model, spec, 0, new DateTime(1, 0, 0));
		order2 = new StandardOrder(model, spec, 1, new DateTime(2, 0, 0));
		order3 = new StandardOrder(model, otherSpec, 2, new DateTime(3, 0, 0));
		order4 = new StandardOrder(model, otherSpec, 3, new DateTime(4, 0, 0));
		
		newOrder1 = new StandardOrder(model, spec, 4, new DateTime(0, 0, 0));
		newOrder2 = new StandardOrder(model, otherSpec, 5, new DateTime(5, 0, 0));
		
		orderQueue = new ArrayList<Order>(Arrays.asList(order1, order2, order3, order4));
		
		strat = new BatchStrategy<Order>(spec);
	}
	
	@Test
	public void constructor_nullSpec() {
		expected.expect(IllegalArgumentException.class);
		new BatchStrategy<Order>(null);
	}
	
	@Test
	public void constructor_valid() {
		BatchStrategy<Order> strat = new BatchStrategy<Order>(spec);
		BatchComparator comp = Whitebox.getInternalState(strat, "comparator");
		BatchComparator otherComp = new BatchComparator(spec);
		assertEquals(Whitebox.getInternalState(comp, Specification.class),
				Whitebox.getInternalState(otherComp, Specification.class));
	}
	
	@Test
	public void sortTest() {
		List<Order> orderQueue = new ArrayList<Order>(Arrays.asList(order3, order2, order1, order4));
		strat.sort(orderQueue);
		assertEquals(orderQueue, this.orderQueue);
	}
	
	@Test
	public void addTo_beginning() {
		strat.addTo(newOrder1, orderQueue);
		assertEquals(newOrder1, orderQueue.get(0));
		assertEquals(order1, orderQueue.get(1));
	}
	
	@Test
	public void addTo_middle() {
		Order order = new StandardOrder(model, spec, 6, new DateTime(1, 12, 0));
		strat.addTo(order, orderQueue);
		assertEquals(orderQueue.get(1), order);
		assertEquals(orderQueue.get(2), order2);
	}
	
	@Test
	public void addTo_end() {
		strat.addTo(newOrder2, orderQueue);
		assertEquals(orderQueue.get(orderQueue.size() - 1), newOrder2);
	}

	@Test
	public void isDoneTest_true() {
		List<Order> orderQueue = new ArrayList<Order>(Arrays.asList(order3, order3));
		assertTrue(strat.isDone(orderQueue));
	}
	
	@Test
	public void isDoneTest_false() {
		List<Order> orderQueue = new ArrayList<Order>(Arrays.asList(order1, order1));
		assertFalse(strat.isDone(orderQueue));
	}

}
