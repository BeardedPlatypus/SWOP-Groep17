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
	@Mock AssemblyTaskInfo assemblyTaskInfo1;
	@Mock AssemblyTaskInfo assemblyTaskInfo2;
	@Mock AssemblyTaskInfo assemblyTaskInfo3;
	
	TaskType workPostType = TaskType.BODY;
	TaskType wrongWorkPostType = TaskType.DRIVETRAIN;
	
	WorkPost workPost;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		List<AssemblyTaskInfo> tasks = new ArrayList<AssemblyTaskInfo>();
		tasks.add(assemblyTaskInfo1);
		tasks.add(assemblyTaskInfo2);
		
		Mockito.when(assemblyProcedure.getAssemblyTasks(workPostType)).thenReturn(tasks);
		
		Mockito.when(assemblyTaskInfo1.getTaskType()).thenReturn(workPostType);
		Mockito.when(assemblyTaskInfo2.getTaskType()).thenReturn(workPostType);
		Mockito.when(assemblyTaskInfo3.getTaskType()).thenReturn(wrongWorkPostType);
		
		this.workPost = new WorkPost(this.workPostType);
	}

	@Test
	public void constructor_NullTaskTypeTest() {
		exception.expect(IllegalArgumentException.class);
		new WorkPost(null);
	}
	
	@Test
	public void constructor_ValidTaskTypeTest() {
		WorkPost workPost = new WorkPost(TaskType.BODY);
		assertEquals(workPost.getTaskType(), TaskType.BODY);
	}
	
	@Test
	public void getAssemblyTaskInfosTest_noAssemblyAssigned() {
		List<AssemblyTaskInfo> infos = workPost.getAssemblyTasks();
		assertTrue(infos.isEmpty());
	}
	
	@Test
	public void getAssemblyTaskInfosTest_assemblyAssigned() {
		workPost.setAssemblyProcedure(assemblyProcedure);
		
		List<AssemblyTaskInfo> infos = workPost.getAssemblyTasks();
		assertTrue(infos.contains(assemblyTaskInfo1));
		assertTrue(infos.contains(assemblyTaskInfo2));
		assertFalse(infos.contains(assemblyTaskInfo3));
	}
	
	

}
