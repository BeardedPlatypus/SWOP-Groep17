package domain.order;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import domain.DateTime;

public class CompletedOrderCatalogTest {
	@Rule public ExpectedException exception = ExpectedException.none();
	
	CompletedOrderCatalog cat;
	@Mock Order orderToComplete;
	@Mock Order completedOrder;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.when(orderToComplete.isCompleted()).thenReturn(false);
		Mockito.when(completedOrder.isCompleted()).thenReturn(true);
		cat = new CompletedOrderCatalog();
	}

	@Test
	public void testConstructor() {
		assertTrue(cat.getCompletedOrderContainers().size()==0);
	}

	@Test
	public void testAddCompletedOrder() {
		cat.addCompletedOrder(orderToComplete);
		assertTrue(cat.contains(orderToComplete));
	}

	@Test
	public void testContains() {
		assertFalse(cat.contains(orderToComplete));
		cat.addCompletedOrder(orderToComplete);
		assertTrue(cat.contains(orderToComplete));
		assertFalse(cat.contains(completedOrder));
	}

	@Test
	public void testGetCompletionTime() {
		Mockito.when(orderToComplete.isCompleted()).thenReturn(false);
		cat.addCompletedOrder(orderToComplete);
		Mockito.when(orderToComplete.getCompletionTime()).thenReturn(new DateTime(1, 2, 3));
		Mockito.when(orderToComplete.isCompleted()).thenReturn(true);
		assertTrue(cat.getCompletionTime(orderToComplete).getDays() == 1);
		assertTrue(cat.getCompletionTime(orderToComplete).getHours() == 2);
		assertTrue(cat.getCompletionTime(orderToComplete).getMinutes() == 3);
	}
	
	@Test
	public void testGetCompletionTimeOrderNotPresent() {
		exception.expect(IllegalStateException.class);
		cat.getCompletionTime(completedOrder);
	}
	
	
	@Test
	public void testGetCompletionTimeOrderNotCompleted() {
		exception.expect(IllegalStateException.class);
		cat.getCompletionTime(orderToComplete);
	}
	
	
	@Test
	public void testGetCompletionTimeNull() {
		exception.expect(IllegalArgumentException.class);
		cat.getCompletionTime(null);
	}


	@Test
	public void testUpdate() {
		DateTime time = new DateTime(1, 2, 4);
		cat.update(time);
		assertTrue(time.equals(Whitebox.getInternalState(cat, DateTime.class)));
	}
	
	@Test
	public void testUpdateNull() {
		exception.expect(IllegalArgumentException.class);
		cat.update(null);
	}

}
