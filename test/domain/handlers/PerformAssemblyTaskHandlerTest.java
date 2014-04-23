package domain.handlers;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import org.powermock.modules.junit4.PowerMockRunner;

import domain.Manufacturer;
import domain.handlers.PerformAssemblyTaskHandler;

@RunWith(PowerMockRunner.class)
public class PerformAssemblyTaskHandlerTest {

	@Rule public ExpectedException expected = ExpectedException.none();
	@Mock Manufacturer manufacturer;
	
	PerformAssemblyTaskHandler handler;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		handler = new PerformAssemblyTaskHandler(manufacturer);
	}
	
	@Test
	public void constructor_nullManufacturer() {
		expected.expect(IllegalArgumentException.class);
		new PerformAssemblyTaskHandler(null);
	}
	
	@Test
	public void getWorkPostsTest() {
		handler.getWorkPosts();
		Mockito.verify(manufacturer).getWorkPostContainers();
	}
	
	@Test
	public void getAssemblyTasksAtWorkPostTest() {
		handler.getAssemblyTasksAtWorkPost(0);
		Mockito.verify(manufacturer).getAssemblyTasksAtPost(0);
	}
	
	@Test
	public void completeWorkpostTask() {
		handler.completeWorkpostTask(0, 0, 60);
		Mockito.verify(manufacturer).completeWorkpostTask(0, 0, 60);
	}
	

}
