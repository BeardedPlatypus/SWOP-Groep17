package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class WorkPostTest {
	@Rule public ExpectedException exception = ExpectedException.none();

	@Mock AssemblyProcedure assemblyProcedure;
	@Mock AssemblyTaskContainer assemblyTaskInfo1;
	@Mock AssemblyTaskContainer assemblyTaskInfo2;
	@Mock AssemblyTaskContainer assemblyTaskInfo3;
	
	TaskType workPostType = TaskType.BODY;
	TaskType wrongWorkPostType = TaskType.DRIVETRAIN;
	
	WorkPost workPost;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		List<AssemblyTaskContainer> tasks = new ArrayList<AssemblyTaskContainer>();
		tasks.add(assemblyTaskInfo1);
		tasks.add(assemblyTaskInfo2);
		
		Mockito.when(assemblyProcedure.getAssemblyTasks(workPostType)).thenReturn(tasks);
		
		Mockito.when(assemblyTaskInfo1.getTaskType()).thenReturn(workPostType);
		Mockito.when(assemblyTaskInfo2.getTaskType()).thenReturn(workPostType);
		Mockito.when(assemblyTaskInfo3.getTaskType()).thenReturn(wrongWorkPostType);
		
		this.workPost = new WorkPost(this.workPostType, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor_NullTaskTypeTest() {
		new WorkPost(null, 0);
	}
	
	@Test
	public void constructor_ValidTaskTypeTest() {
		WorkPost workPost = new WorkPost(TaskType.BODY, 0);
		assertEquals(workPost.getTaskType(), TaskType.BODY);
	}
	
	@Test
	public void getAssemblyTaskInfosTest_noAssemblyAssigned() {
		List<AssemblyTaskContainer> infos = workPost.getMatchingAssemblyTasks();
		assertTrue(infos.isEmpty());
	}
	
	@Test
	public void getAssemblyTaskInfosTest_assemblyAssigned() {
		workPost.setAssemblyProcedure(assemblyProcedure);
		
		List<AssemblyTaskContainer> infos = workPost.getMatchingAssemblyTasks();
		assertTrue(infos.contains(assemblyTaskInfo1));
		assertTrue(infos.contains(assemblyTaskInfo2));
		assertFalse(infos.contains(assemblyTaskInfo3));
	}
	
	

}
