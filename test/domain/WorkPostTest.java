package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WorkPost.class)
public class WorkPostTest {
	@Rule public ExpectedException exception = ExpectedException.none();

	@Mock AssemblyProcedure assemblyProcedure;
	@Mock AssemblyTask assemblyTaskInfo1;
	@Mock AssemblyTask assemblyTaskInfo2;
	@Mock AssemblyTask assemblyTaskInfo3;
	@Mock WorkPostObserver observer;
	@Mock Order order;
	
	TaskType workPostType = TaskType.BODY;
	TaskType wrongWorkPostType = TaskType.DRIVETRAIN;
	
	WorkPost workPost;
	WorkPost emptyWorkPost;
	WorkPost thirdWorkPost;
	
	AssemblyProcedure realProcedure;
	
	AssemblyTask realTask1;
	AssemblyTask realTask2;
	AssemblyTask realTask3;
	
	Option option1;
	Option option2;
	Option option3;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		List<AssemblyTaskContainer> tasks = new ArrayList<AssemblyTaskContainer>();
		tasks.add(assemblyTaskInfo1);
		tasks.add(assemblyTaskInfo2);
		tasks.add(assemblyTaskInfo3);
		
		Mockito.when(assemblyProcedure.getAssemblyTasks(workPostType)).thenReturn(tasks);
		
		Mockito.when(assemblyTaskInfo1.getTaskType()).thenReturn(workPostType);
		Mockito.when(assemblyTaskInfo2.getTaskType()).thenReturn(workPostType);
		Mockito.when(assemblyTaskInfo3.getTaskType()).thenReturn(wrongWorkPostType);
		
		option1 = new Option(TaskType.BODY, "blah", "blah");
		option2 = new Option(TaskType.BODY, "blah", "blah");
		option3 = new Option(TaskType.DRIVETRAIN, "blah", "blah");
		
		realTask1 = new AssemblyTask(option1, 0);
		realTask2 = new AssemblyTask(option2, 1);
		realTask3 = new AssemblyTask(option3, 2);
		
		realProcedure = new AssemblyProcedure(order, Arrays.asList(realTask1, realTask2, realTask3), 180);
		
		this.workPost = new WorkPost(this.workPostType, 0);
		this.emptyWorkPost = new WorkPost(this.wrongWorkPostType, 1);
		this.thirdWorkPost = new WorkPost(TaskType.ACCESSORIES, 2);
		
		workPost.addActiveAssemblyProcedure(assemblyProcedure);
		workPost.register(observer);
	}
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void test_constructorNullTaskTypeTest() {
		new WorkPost(null, 0);
	}
	
	@Test
	public void test_constructorValid() {
		WorkPost wp = new WorkPost(TaskType.BODY, 0);
		
		assertEquals(TaskType.BODY.toString(), wp.getName());
		assertEquals(0, wp.getWorkPostNum());
		assertEquals(TaskType.BODY, wp.getTaskType());
		assertEquals(true, wp.isEmpty());
		assertEquals(null, wp.getAssemblyProcedure());
	}
	
	//--------------------------------------------------------------------------
	// Active AssemblyProcedure methods.
	//--------------------------------------------------------------------------
	@Test
	public void test_isEmpty() {
		assertEquals(true, emptyWorkPost.isEmpty());
	}
	
	@Test
	public void test_isNotEmpty() {
		workPost.setAssemblyProcedure(assemblyProcedure);
		
		assertEquals(false, workPost.isEmpty());
	}
	
	@Test
	public void test_addActiveAssemblyProcedureValid() {
		emptyWorkPost.addActiveAssemblyProcedure(assemblyProcedure);
		assertEquals(assemblyProcedure, emptyWorkPost.getAssemblyProcedure());
		assertEquals(false, emptyWorkPost.isEmpty());
	}
	
	@Test
	public void test_addActiveAssemblyProcedureNull() {
		exception.expect(IllegalArgumentException.class);
		
		emptyWorkPost.addActiveAssemblyProcedure(null);
	}
	
	@Test
	public void test_addActiveAssemblyProcedureNotEmpty() {
		exception.expect(IllegalStateException.class);
		workPost.addActiveAssemblyProcedure(assemblyProcedure);
	}
		
	@Test 
	public void test_getMatchinAssemblyTasks_empty() {
		exception.expect(IllegalStateException.class);
		emptyWorkPost.getMatchingAssemblyTasks();
	}

	@Test
	public void getAssemblyTaskInfosTest_assemblyAssigned() {
		workPost.getMatchingAssemblyTasks();

		Mockito.verify(assemblyProcedure).getAssemblyTasks(workPost.getTaskType());
	}
	
	@Test
	public void isFinished_false() {
		assertEquals(false, workPost.isFinished());
	}
	
	@Test
	public void isFinished_emptyWorkPost() {
		assertTrue(emptyWorkPost.isFinished());
	}
	
	@Test
	public void isFinished_nonEmptyWorkPost() {
		Mockito.when(assemblyProcedure.isFinished(workPostType)).thenReturn(true);
		assertTrue(workPost.isFinished());
	}
	
	//--------------------------------------------------------------------------
	// completing a task
	//--------------------------------------------------------------------------
	@Test
	public void completeTask_wrongType() {
		Mockito.doThrow(new IllegalArgumentException()).when(assemblyProcedure)
			.completeTask(2, workPostType);
		exception.expect(IllegalArgumentException.class);
		workPost.completeTask(2, 1337);
	}
	
	@Test
	public void completeTask_invalidTaskNum() {
		Mockito.doThrow(new IllegalArgumentException()).when(assemblyProcedure)
			.completeTask(Integer.MAX_VALUE, workPostType);
		exception.expect(IllegalArgumentException.class);
		workPost.completeTask(Integer.MAX_VALUE, 1337);
	}
	
	@Test
	public void completeTask_emptyWorkPost() {
		exception.expect(IllegalStateException.class);
		emptyWorkPost.completeTask(0, 1337);
	}
	
	@Test
	public void completeTask_validAndNotFinished() {
		workPost.completeTask(0, 60);
		Mockito.verify(assemblyProcedure).completeTask(0, workPostType);
	}
	
	@Test
	public void completeTask_validAndTryTwice() {
		workPost.completeTask(0, 60);
		Mockito.when(assemblyProcedure.taskIsFinished(0)).thenReturn(true);
		
		workPost.completeTask(0, 1337);
		
		Mockito.verify(assemblyProcedure, Mockito.times(1)).completeTask(0, workPostType);
	}
	
	@Test
	public void completeTask_negativeMins() {
		exception.expect(IllegalArgumentException.class);
		workPost.completeTask(0, -1);
	}
	
	@Test
	public void completeTask_lastTask() {
		WorkPost workPost = PowerMockito.spy(this.workPost);
		workPost.setAssemblyProcedure(realProcedure);
		workPost.completeTask(0, 60);
		workPost.completeTask(1, 60);
		try {
			PowerMockito.verifyPrivate(workPost, Mockito.times(2)).invoke("incrementTime", 60);
			PowerMockito.verifyPrivate(workPost).invoke("notifyWorkComplete");
			Mockito.verify(observer).notifyWorkComplete(120);
			assertTrue((int) Whitebox.getInternalState(workPost, "minutesOfWork") == 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void takeProcedureFrom_notAdjacent() {
		exception.expect(IllegalArgumentException.class);
		thirdWorkPost.takeAssemblyProcedureFrom(workPost);
	}
	
	@Test
	public void takeProcedureFrom_valid() {
		emptyWorkPost.takeAssemblyProcedureFrom(workPost);
		assertEquals(emptyWorkPost.getAssemblyProcedure(), assemblyProcedure);
		assertEquals(null, workPost.getAssemblyProcedure());
		assertEquals(0, (int) Whitebox.getInternalState(workPost, "minutesOfWork"));
	}

}