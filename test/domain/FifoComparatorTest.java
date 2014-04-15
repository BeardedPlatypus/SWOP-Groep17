package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class FifoComparatorTest {
	
	@Mock Order order1;
	@Mock Order order2;
	@Mock Order order3;
	@Mock Order order4;
	
	DateTime time1;
	DateTime time2;
	DateTime time3;
	DateTime time4;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		time1 = new DateTime(1, 0, 0);
		time2 = new DateTime(2, 0, 0);
		time3 = new DateTime(3, 0, 0);
		time4 = new DateTime(4, 0, 0);
		
		Mockito.when(order1.getSubmissionTime()).thenReturn(time1);
		Mockito.when(order2.getSubmissionTime()).thenReturn(time2);
		Mockito.when(order3.getSubmissionTime()).thenReturn(time3);
		Mockito.when(order4.getSubmissionTime()).thenReturn(time4);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
