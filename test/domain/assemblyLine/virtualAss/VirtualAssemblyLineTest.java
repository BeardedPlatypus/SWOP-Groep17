package domain.assemblyLine.virtualAss;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import domain.DateTime;
import domain.assemblyLine.TaskType;
import domain.car.Model;
import domain.car.Specification;
import domain.order.Order;
import domain.order.StandardOrder;

public class VirtualAssemblyLineTest {
	@Rule public ExpectedException exception = ExpectedException.none();
	TaskType[] seq = {TaskType.BODY, 
					  TaskType.CARGO, 
					  TaskType.DRIVETRAIN, 
					  TaskType.ACCESSORIES, 
					  TaskType.CERTIFICATION};
	
	TaskType[] seq3 = {TaskType.BODY, TaskType.DRIVETRAIN, TaskType.ACCESSORIES}; 
	
	@Mock Order order1;
	@Mock Order order2;
	@Mock Order order3;
	@Mock Order order4;
	@Mock Order order5;
	@Mock Order order6;
	
	@Mock Model model;
	@Mock Specification specs;
	
	List<Optional<Order>> listOrders3;
	List<Optional<Order>> nonMockedOrderList3;
	List<Optional<Order>> entry;
	List<Order> potatoList;
	
	List<Optional<VirtualAssProc>> emptyVirtAss5;
	List<Optional<VirtualAssProc>> nonEmptyVirtAss5;
	List<Optional<VirtualAssProc>> nonEmptyVirtAss52;
	VirtualAssemblyLine virtAssLine;

	@Mock DateTime dt1;
	@Mock DateTime dt2;
	@Mock DateTime dt3;
	@Mock DateTime dt4;
	@Mock DateTime dt5;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		listOrders3 = new ArrayList<>();
		
		listOrders3.add(Optional.of(order1));
		listOrders3.add(Optional.of(order2));
		listOrders3.add(Optional.of(order3));
		
		emptyVirtAss5 = new ArrayList<>();
		emptyVirtAss5.add(Optional.<VirtualAssProc> absent());
		emptyVirtAss5.add(Optional.<VirtualAssProc> absent());
		emptyVirtAss5.add(Optional.<VirtualAssProc> absent());
		emptyVirtAss5.add(Optional.<VirtualAssProc> absent());
		emptyVirtAss5.add(Optional.<VirtualAssProc> absent());

		nonEmptyVirtAss5 = new ArrayList<>();
		nonEmptyVirtAss5.add(Optional.of(new VirtualAssProc(order1)));
		nonEmptyVirtAss5.add(Optional.of(new VirtualAssProc(order2)));
		nonEmptyVirtAss5.add(Optional.of(new VirtualAssProc(order3)));
		nonEmptyVirtAss5.add(Optional.of(new VirtualAssProc(order4)));
		nonEmptyVirtAss5.add(Optional.of(new VirtualAssProc(order5)));

		Mockito.when(order1.getMinutesOnPostOfType(TaskType.BODY)).thenReturn(60);
		Mockito.when(order1.getMinutesOnPostOfType(TaskType.CARGO)).thenReturn(0);
		Mockito.when(order1.getMinutesOnPostOfType(TaskType.DRIVETRAIN)).thenReturn(60);
		Mockito.when(order1.getMinutesOnPostOfType(TaskType.ACCESSORIES)).thenReturn(60);
		Mockito.when(order1.getMinutesOnPostOfType(TaskType.CERTIFICATION)).thenReturn(0);

		Mockito.when(order2.getMinutesOnPostOfType(TaskType.BODY)).thenReturn(60);
		Mockito.when(order2.getMinutesOnPostOfType(TaskType.CARGO)).thenReturn(0);
		Mockito.when(order2.getMinutesOnPostOfType(TaskType.DRIVETRAIN)).thenReturn(60);
		Mockito.when(order2.getMinutesOnPostOfType(TaskType.ACCESSORIES)).thenReturn(60);
		Mockito.when(order2.getMinutesOnPostOfType(TaskType.CERTIFICATION)).thenReturn(0);

		Mockito.when(order3.getMinutesOnPostOfType(TaskType.BODY)).thenReturn(60);
		Mockito.when(order3.getMinutesOnPostOfType(TaskType.CARGO)).thenReturn(0);
		Mockito.when(order3.getMinutesOnPostOfType(TaskType.DRIVETRAIN)).thenReturn(60);
		Mockito.when(order3.getMinutesOnPostOfType(TaskType.ACCESSORIES)).thenReturn(60);
		Mockito.when(order3.getMinutesOnPostOfType(TaskType.CERTIFICATION)).thenReturn(0);

		Mockito.when(order4.getMinutesOnPostOfType(TaskType.BODY)).thenReturn(60);
		Mockito.when(order4.getMinutesOnPostOfType(TaskType.CARGO)).thenReturn(0);
		Mockito.when(order4.getMinutesOnPostOfType(TaskType.DRIVETRAIN)).thenReturn(60);
		Mockito.when(order4.getMinutesOnPostOfType(TaskType.ACCESSORIES)).thenReturn(60);
		Mockito.when(order4.getMinutesOnPostOfType(TaskType.CERTIFICATION)).thenReturn(0);

		Mockito.when(order5.getMinutesOnPostOfType(TaskType.BODY)).thenReturn(60);
		Mockito.when(order5.getMinutesOnPostOfType(TaskType.CARGO)).thenReturn(0);
		Mockito.when(order5.getMinutesOnPostOfType(TaskType.DRIVETRAIN)).thenReturn(60);
		Mockito.when(order5.getMinutesOnPostOfType(TaskType.ACCESSORIES)).thenReturn(60);
		Mockito.when(order5.getMinutesOnPostOfType(TaskType.CERTIFICATION)).thenReturn(0);

		Mockito.when(order6.getMinutesOnPostOfType(TaskType.BODY)).thenReturn(60);
		Mockito.when(order6.getMinutesOnPostOfType(TaskType.CARGO)).thenReturn(0);
		Mockito.when(order6.getMinutesOnPostOfType(TaskType.DRIVETRAIN)).thenReturn(60);
		Mockito.when(order6.getMinutesOnPostOfType(TaskType.ACCESSORIES)).thenReturn(60);
		Mockito.when(order6.getMinutesOnPostOfType(TaskType.CERTIFICATION)).thenReturn(0);
		
		nonMockedOrderList3 = new ArrayList<>();
		 
		Mockito.when(model.getMinsOnWorkPostOfType(TaskType.BODY)).thenReturn(40);
		Mockito.when(model.getMinsOnWorkPostOfType(TaskType.CARGO)).thenReturn(0);
		Mockito.when(model.getMinsOnWorkPostOfType(TaskType.DRIVETRAIN)).thenReturn(40);
		Mockito.when(model.getMinsOnWorkPostOfType(TaskType.ACCESSORIES)).thenReturn(40);
		Mockito.when(model.getMinsOnWorkPostOfType(TaskType.CERTIFICATION)).thenReturn(0);
		
		Order o1 = new StandardOrder(model, specs, 1, dt1);
		Order o2 = new StandardOrder(model, specs, 2, dt2);
		Order o3 = new StandardOrder(model, specs, 3, dt3);
		nonMockedOrderList3.add(Optional.of(o1));
		nonMockedOrderList3.add(Optional.of(o2));
		nonMockedOrderList3.add(Optional.of(o3));
		
		virtAssLine = new VirtualAssemblyLine(seq3, nonMockedOrderList3);
		
		nonEmptyVirtAss52 = new ArrayList<>();
		nonEmptyVirtAss52.add(Optional.of(new VirtualAssProc(order1)));
		nonEmptyVirtAss52.add(Optional.<VirtualAssProc> absent());
		nonEmptyVirtAss52.add(Optional.of(new VirtualAssProc(order3)));
		nonEmptyVirtAss52.add(Optional.<VirtualAssProc> absent());
		nonEmptyVirtAss52.add(Optional.of(new VirtualAssProc(order6)));

		entry = new ArrayList<>();
		entry.add(Optional.of(order3));
		entry.add(Optional.of(order4));
		entry.add(Optional.of(order5));
		
		potatoList = new ArrayList<>();
		potatoList.add(order1);
		potatoList.add(order2);

	}

	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	@Test
	public void testConstructorNullCurState() {
		exception.expect(IllegalArgumentException.class);
		new VirtualAssemblyLine(seq, null);
	}

	@Test
	public void testConstructorCurStateContainsNull() {
		List<Optional<Order>> curState = new ArrayList<>();
		curState.add(null);
		TaskType[] custSeq = {TaskType.CERTIFICATION};
		
		exception.expect(IllegalArgumentException.class);
		new VirtualAssemblyLine(custSeq, curState);
	}
	
	@Test
	public void testConstructorNullTaskTypeSeq() {
		exception.expect(IllegalArgumentException.class);
		new VirtualAssemblyLine(null, listOrders3);
	}

	@Test
	public void testConstructorNullTaskTypeContainsNull() {
		TaskType[] t = {null};
		List<Optional<Order>> l = new ArrayList<>();
		l.add(Optional.of(order1));
		
		exception.expect(IllegalArgumentException.class);
		new VirtualAssemblyLine(t, l);
	}

	@Test
	public void testConstructorNullTaskTypeContainsNoElements() {
		TaskType[] t = {};
		List<Optional<Order>> l = new ArrayList<>();
		
		exception.expect(IllegalArgumentException.class);
		new VirtualAssemblyLine(t, l);
	}
	
	@Test
	public void testConstructorNotEqualStateTasks() {
		exception.expect(IllegalArgumentException.class);
		new VirtualAssemblyLine(seq, listOrders3);
	}
	
	//--------------------------------------------------------------------------
	// advance methods.
	//--------------------------------------------------------------------------
	@Test
	public void testOrderSeqIsFinishedTrue() {
		VirtualAssemblyLine v = new VirtualAssemblyLine(seq3, nonMockedOrderList3);
		
		assertEquals(true, v.orderSeqIsFinished(emptyVirtAss5));
	}

	@Test
	public void testOrderSeqIsFinishedFalse() {
		VirtualAssemblyLine v = new VirtualAssemblyLine(seq3, nonMockedOrderList3);
		
		assertEquals(false, v.orderSeqIsFinished(nonEmptyVirtAss5));
	}

	@Test
	public void testOrderMinutesToFinishCurStepEmpty() {
		VirtualAssemblyLine v = new VirtualAssemblyLine(seq3, nonMockedOrderList3);
		
		assertEquals(0, v.minutesToFinishCurStep(emptyVirtAss5, 2, seq3));
	}

	@Test
	public void testOrderMinutesToFinishCurStepFullSame() {
		VirtualAssemblyLine v = new VirtualAssemblyLine(seq3, nonMockedOrderList3);
		
		assertEquals(60, v.minutesToFinishCurStep(nonEmptyVirtAss5, 2, seq3));
	}
	
	@Test
	public void testOrderMinutesToFinishCurStepDifPotato() {
		VirtualAssemblyLine v = new VirtualAssemblyLine(seq3, nonMockedOrderList3);
		assertEquals(60, v.minutesToFinishCurStep(nonEmptyVirtAss52, 2, seq3));
	}
	
	//--------------------------------------------------------------------------
	// AdvanceOne Step
	//--------------------------------------------------------------------------
	@Test
	public void testAdvanceOrderSeqFull() {
		VirtualAssemblyLine v = new VirtualAssemblyLine(seq3, nonMockedOrderList3);
		v.advanceOrderSeq(nonEmptyVirtAss5, 2, seq3);
		assertEquals(5, nonEmptyVirtAss5.size());
		assertEquals(Optional.<VirtualAssProc> absent(), nonEmptyVirtAss5.get(0));
		assertEquals(order1, nonEmptyVirtAss5.get(1).get().getOrder());
		assertEquals(order2, nonEmptyVirtAss5.get(2).get().getOrder());
		assertEquals(order3, nonEmptyVirtAss5.get(3).get().getOrder());
		assertEquals(order4, nonEmptyVirtAss5.get(4).get().getOrder());
	}

	@Test
	public void testAdvanceOrderSeqEmpty() {
		VirtualAssemblyLine v = new VirtualAssemblyLine(seq3, nonMockedOrderList3);
		v.advanceOrderSeq(emptyVirtAss5, 2, seq3);
		assertEquals(5, emptyVirtAss5.size());
		assertEquals(Optional.<VirtualAssProc> absent(), emptyVirtAss5.get(0));
		assertEquals(Optional.<VirtualAssProc> absent(), emptyVirtAss5.get(1));
		assertEquals(Optional.<VirtualAssProc> absent(), emptyVirtAss5.get(2));
		assertEquals(Optional.<VirtualAssProc> absent(), emptyVirtAss5.get(3));
		assertEquals(Optional.<VirtualAssProc> absent(), emptyVirtAss5.get(4));
	}
	
	//--------------------------------------------------------------------------
	// TimeToFinish.
	//--------------------------------------------------------------------------
	@Test
	public void testTimeToFinishfull() {
		VirtualAssemblyLine v = new VirtualAssemblyLine(seq3, entry);
		assertEquals(new DateTime(0, 5, 0), v.timeToFinish(potatoList));
	}

	@Test
	public void testTimeToFinishfullNoExtra() {
		VirtualAssemblyLine v = new VirtualAssemblyLine(seq3, entry);
		assertEquals(new DateTime(0, 3, 0), v.timeToFinish(new ArrayList<Order>()));
	}

}
