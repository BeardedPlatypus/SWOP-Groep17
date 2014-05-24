package domain.production_schedule;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import domain.assembly_line.TaskType;
import domain.car.Model;
import domain.production_schedule.OrderRequest;

/**
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class OrderRequestTest {
	//--------------------------------------------------------------------------
	// Variables
	//--------------------------------------------------------------------------
	@Rule public ExpectedException exception = ExpectedException.none();
	@Mock Model mockModel;
	TaskType mockTask;
	
	///--------------------------------------------------------------------------
	// SetUp Methods
	//--------------------------------------------------------------------------
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockTask = TaskType.BODY;
	}

	//--------------------------------------------------------------------------
	// Tests
	//--------------------------------------------------------------------------
	@Test
	public void testStandardOrderModelNull() {
		exception.expect(IllegalArgumentException.class);
		Model[] m = null;
		new OrderRequest(m);
	}
	
	@Test 
	public void testStandardOrderValid() {
		Model[] m = {mockModel};
		OrderRequest standardOrder = new OrderRequest(m);
		assertEquals(OrderRequest.Type.STANDARD, standardOrder.getOrderType());
		
		Set<Model> res = standardOrder.getModels();
		assertEquals(1, res.size());
		assertTrue(res.contains(mockModel));
	}
	//--------------------------------------------------------------------------
	@Test
	public void testSingleTaskOrderTaskNull() {
		exception.expect(IllegalArgumentException.class);
		TaskType[] t = null;
		new OrderRequest(t);
	}
	
	@Test
	public void testSingleTaskOrderValid() {
		TaskType[] t = {mockTask};
		OrderRequest singleTaskOrder = new OrderRequest(t);
		assertEquals(OrderRequest.Type.SINGLETASK, singleTaskOrder.getOrderType());
		
		Set<TaskType> res = singleTaskOrder.getTaskTypes();
		assertEquals(1, res.size());
		assertTrue(res.contains(mockTask));
	}
	
	//--------------------------------------------------------------------------
	@Test
	public void testStandardOrderTaskException() {
		Model[] m = {mockModel};
		exception.expect(IllegalStateException.class);
		new OrderRequest(m).getTaskTypes();
	}

	@Test
	public void testSingleTaskOrderModelException() {
		exception.expect(IllegalStateException.class);
		TaskType[] t = {mockTask};
		new OrderRequest(t).getModels();
	}
}
