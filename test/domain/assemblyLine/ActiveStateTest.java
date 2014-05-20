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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.powermock.modules.junit4.*;

import domain.DateTime;
import domain.car.Option;
import domain.order.Order;

@RunWith(PowerMockRunner.class)
public class ActiveStateTest {
	
	@Rule public ExpectedException expected = ExpectedException.none();
	
	@Mock AssemblyLine line;
	@Mock Order mockOrder;
	ActiveState state;
	LayoutManipulator man;
	
	AssemblyTask task0;
	AssemblyTask task1;
	AssemblyTask task2;
	
	AssemblyProcedure proc0;
	AssemblyProcedure proc1;
	AssemblyProcedure proc2;
	
	List<WorkPost> workPosts;

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
		
		task0 = new AssemblyTask(new Option(TaskType.BODY, "john", "doe"), 0);
		task1 = new AssemblyTask(new Option(TaskType.DRIVETRAIN, "jane", "doe"), 0);
		task2 = new AssemblyTask(new Option(TaskType.ACCESSORIES, "adam", "smith"), 0);
		
		proc0 = new AssemblyProcedure(mockOrder, new ArrayList<AssemblyTask>(Arrays.asList(task0)), 60);
		proc1 = new AssemblyProcedure(mockOrder, new ArrayList<AssemblyTask>(Arrays.asList(task1)), 60);
		proc2 = new AssemblyProcedure(mockOrder, new ArrayList<AssemblyTask>(Arrays.asList(task2)), 60);
		
		Mockito.when(line.getWorkPost(0)).thenReturn(workPosts.get(0));
		Mockito.when(line.getWorkPost(1)).thenReturn(workPosts.get(1));
		Mockito.when(line.getWorkPost(2)).thenReturn(workPosts.get(2));
		Mockito.when(line.getFirstWorkPost()).thenReturn(workPosts.get(0));
		Mockito.when(line.getLastWorkPost()).thenReturn(workPosts.get(2));
		Mockito.when(line.getElapsedTime()).thenReturn(new DateTime(0, 0, 60));
		Mockito.when(line.getAssemblyLineSize()).thenReturn(3);
		
		Mockito.when(line.isValidWorkPost(Mockito.anyInt())).thenReturn(true);
		
		workPosts.get(0).setAssemblyProcedure(proc0);
		workPosts.get(1).setAssemblyProcedure(proc1);
		workPosts.get(2).setAssemblyProcedure(proc2);
		
		state = new ActiveState();
		state.setAssemblyLine(line);
	}

	@Test
	public void completeWorkPostTaskTest() {
		state.completeWorkpostTask(0, 0, 60);
		assertTrue(workPosts.get(0).getAssemblyProcedure().getTask(0).isCompleted());
		assertEquals(60, Whitebox.getInternalState(workPosts.get(0), "minutesOfWork"));
	}
	
	@Test
	public void completeWorkPostTask_invalidWorkPostNum() {
		Mockito.when(line.isValidWorkPost(-1)).thenReturn(false);
		expected.expect(IllegalArgumentException.class);
		state.completeWorkpostTask(-1, 0, 60);
	}
	
	@Test
	public void completeWorkPostTask_invalidTaskNum() {
		expected.expect(IllegalArgumentException.class);
		state.completeWorkpostTask(0, -1, 60);
	}
	
	@Test
	public void ensureConsistentState_test() {
		task0.setCompleted(true);
		task1.setCompleted(true);
		task2.setCompleted(true);
		state.advanceAssemblyLine();
		assertEquals(null, workPosts.get(0).getAssemblyProcedure());
		assertEquals(null, workPosts.get(1).getAssemblyProcedure());
		assertEquals(null, workPosts.get(2).getAssemblyProcedure());
		Mockito.when(line.isEmpty()).thenReturn(true);
		state.advanceAssemblyLine();
		try {
			PowerMockito.verifyPrivate(line).invoke("setCurrentState", new IdleState());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void ensureConsistentState_stillActive() {
		state.advanceAssemblyLine();
		try {
			PowerMockito.verifyPrivate(line, Mockito.never()).invoke("setCurrentState", new IdleState());
			PowerMockito.verifyPrivate(line, Mockito.never()).invoke("setCurrentState", new ActiveState());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
