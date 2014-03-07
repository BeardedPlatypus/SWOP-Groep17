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

public class PerformAssemblyTaskHandlerTest {

	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock AssemblyLine assemblyLine;
	@Mock WorkPost workPost1;
	@Mock WorkPost workPost2;
	
	PerformAssemblyTaskHandler handler;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.handler = new PerformAssemblyTaskHandler(this.assemblyLine);
	}
	
	@Test
	public void constructor_NullAssemblyLineTest() {
		this.exception.expect(IllegalArgumentException.class);
		new PerformAssemblyTaskHandler(null);
	}
	
	@Test
	public void getWorkPostsTest() {
		List<WorkPostContainer> array = new ArrayList<WorkPostContainer>();
		array.add(this.workPost1);
		Mockito.when(this.assemblyLine.getWorkPosts()).thenReturn(array);
		
		List<WorkPostContainer> returnVal = handler.getWorkPosts();
		assertTrue(returnVal.contains(workPost1));
		assertFalse(returnVal.contains(workPost2));
	}

}
